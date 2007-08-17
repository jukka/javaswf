package org.epistem.io;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Counterpart to the InterfaceCallDumper.  Replays the method calls.
 *
 * @author nickmain
 * @see InterfaceCallDumper
 */
public class InterfaceCallReplayer<IFACE extends PipelineInterface> {

    private final IFACE target;
    private final Class<IFACE> iface;
    
    private final Map<String, Method> methods = new HashMap<String, Method>();    
    
    //map of method name to param names
    private final Map<String, String[]> paramNames = new HashMap<String, String[]>();
    
    //map of method name to count of array/collection/map parameters
    private final Map<String, Integer> arrayParamCounts = new HashMap<String, Integer>();
    
    /**
     * @param iface the interface to proxy
     * @param target the object implementing the interface
     */
    public InterfaceCallReplayer( Class<IFACE> iface, IFACE target ) {
        this.iface  = iface;
        this.target = target;
        
        loadParamNames();        

        //load the method map and determine array param counts
        for( Method m : iface.getMethods()) {
            methods.put( m.getName(), m );
            
            int arrayParamCount = 0;
            for( Class<?> paramType : m.getParameterTypes() ) {
                if( paramType.isArray()
                 || Collection.class.isAssignableFrom( paramType )
                 || Map.class.isAssignableFrom( paramType )) {
                          
                    arrayParamCount++;
                }
            }
            arrayParamCounts.put( m.getName(), arrayParamCount );
        }
    }

    /**
     * Parse an XML document from the given stream and replay the methods calls
     * therein.
     */
    public void replay( InputStream in ) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse( in );
            
            replay( doc.getDocumentElement() );
            
        } catch( RuntimeException rex ) {
            throw rex;
        } catch( Exception ex ) {
            throw new RuntimeException( ex );
        }        
    }
    
    /**
     * Parse an XML document from the given filename and replay the methods calls
     * therein.
     */
    public void replay( String filename ) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder        builder = factory.newDocumentBuilder();
            
            Document doc = builder.parse( new File( filename ) );
            
            replay( doc.getDocumentElement() );
            
        } catch( RuntimeException rex ) {
            throw rex;
        } catch( Exception ex ) {
            throw new RuntimeException( ex );
        }        
    }
    
    /** Load the param names from a property file */
    protected void loadParamNames() {
        InterfaceCallDumper.loadParamNames( iface, paramNames );
    }

    /**
     * Replay the method calls contained within the given element
     */
    public void replay( Element element ) {
        
        NodeList kids = element.getChildNodes();
        
        for (int i = 0; i < kids.getLength(); i++) {
            Node n = kids.item( i );
            if( ! (n instanceof Element)) continue;
            
            Element methodElement = (Element) n;
            replayMethod( methodElement );
        }
        
        target.done();
    }
    
    /**
     * Replay the method call encoded in the given element.
     */
    public void replayMethod( Element methodElement ) {
        
        String methodName = methodElement.getTagName();
        
        Method method = methods.get( methodName );
        if( method == null ) {
            throw new RuntimeException( "Could not find method " + methodName 
                                        + " in interface " + iface.getName() );
        }
        
        Class[]  paramTypes = method.getParameterTypes();
        String[] pNames = paramNames.get( methodName );        
        Object[] args   = new Object[ paramTypes.length ];
        
        int arrayParamCount = arrayParamCounts.get( methodName );
        boolean hasArrayParams = false; //true if any array params are actually instantiated
        boolean hasReturnInterface = method.getReturnType().isInterface();
        
        //gather the argument values
        for (int i = 0; i < args.length; i++) {
            String paramName = pNames[i];
            Class  paramType = paramTypes[i];
            boolean isArrayParam = InterfaceCallDumper.isArrayParam( paramType );
            
            //whether this parameter is represented by directly contained elements
            //rather than being wrapped
            boolean isUnwrapped = isArrayParam 
                               && (! hasReturnInterface ) 
                               && ( arrayParamCount == 1 );
            
            Object val = findArgValue( methodElement, paramName, paramType, isArrayParam, isUnwrapped );
            args[i] = val;
            
            //detect that an array parameter has been instantiated
            if( isArrayParam && val != null && Array.getLength( val ) > 0 ) {
                hasArrayParams = true;
            }
        }

        //make the call
        Object result;
        try {
            result = method.invoke( target, args );
        } catch( Exception ex ) {
            throw new RuntimeException(ex);
        }
        
        //play back nested calls to the returned interface
        if( hasReturnInterface && result != null ) {
            PipelineInterface nestedTarget = (PipelineInterface) result;
            Element containerElement = null;
            
            if( hasArrayParams ) {
                //get the child element that contains the method calls
                String tagName = method.getReturnType().getName().replace( '.', '_' );
                NodeList nl = methodElement.getElementsByTagName( tagName );
                
                if( nl.getLength() == 1 ) {
                    containerElement = (Element) nl.item(0);
                } 
            } else {
                containerElement = methodElement;
            }
            
            //no nested method calls..
            if( containerElement == null ) {
                nestedTarget.done();
                return;
            }
            
            @SuppressWarnings( "unchecked" ) //java generics suck
            InterfaceCallReplayer nestedReplayer = 
                new InterfaceCallReplayer( method.getReturnType(), nestedTarget );
            
            nestedReplayer.replay( containerElement );
        }
    }
 
    //get an argument value
    private Object findArgValue( Element elem, String paramName, 
                                 Class paramType, boolean isArrayParam, 
                                 boolean isUnwrapped ) {
        
        if( ! isArrayParam ) {
            String valueString = elem.getAttribute( paramName );
            
            if     ( paramType == Byte.class      || paramType == byte.class    ) {
                if( valueString.trim().length() == 0 ) valueString = "0";
                return Byte.valueOf( valueString );
            }
            else if( paramType == Character.class || paramType == char.class    ) {
                if( valueString.trim().length() == 0 ) valueString = "\0"; 
                return new Character( valueString.charAt(0));
            }
            else if( paramType == Short.class     || paramType == short.class   ) {
                if( valueString.trim().length() == 0 ) valueString = "0"; 
                return Short.valueOf( valueString );
            }
            else if( paramType == Boolean.class   || paramType == boolean.class ) {
                if( valueString.trim().length() == 0 ) valueString = "false"; 
                return Boolean.valueOf( valueString );
            }
            else if( paramType == Integer.class   || paramType == int.class     ) {
                if( valueString.trim().length() == 0 ) valueString = "0"; 
                return Integer.valueOf( valueString );
            }
            else if( paramType == Long.class      || paramType == long.class    ) {
                if( valueString.trim().length() == 0 ) valueString = "0"; 
                return Long.valueOf( valueString );
            }
            else if( paramType == Float.class     || paramType == float.class   ) {
                if( valueString.trim().length() == 0 ) valueString = "0"; 
                return Float.valueOf( valueString );
            }
            else if( paramType == Double.class    || paramType == double.class  ) {
                if( valueString.trim().length() == 0 ) valueString = "0"; 
                return Double.valueOf( valueString );                
            }
            else if( paramType.isEnum() ) {
                if( valueString.trim().length() == 0 ) return null;
                
                try {
                    Method valueOf = paramType.getMethod( "valueOf", String.class );
                    Object enumObj = valueOf.invoke( null, valueString );
                    return enumObj;
                    
                } catch( Exception ex ) {
                    throw new RuntimeException( ex );
                }
            }
            else if( paramType == String.class ) {
                return valueString;
            }
            
            //unhandled object type
            return null;
        }
        
        //gather the value elements
        Element container = null; //the container of the value elements
        
        //find the child element that contains the values for the param
        if( ! isUnwrapped ) {
            NodeList nodes = elem.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item( i );
                if( node instanceof Element ) {
                    Element e = (Element) node;
                    if( e.getTagName().equals( paramName ) ) {
                        container = e;
                        break;
                    }
                }
            }
        } else {
            container = elem;  //values are direct children of the method elem
        }
        
        List<Element> valueElems = getChildElements( container );
        int length = valueElems.size();
        
        if( Collection.class.isAssignableFrom( paramType ) ) {
            List<Object> list = new ArrayList<Object>();
            for (int i = 0; i < length; i++) {
                list.add( decodeValue( valueElems.get( i )));
            }
            return list;            
        }

        if( paramType.isArray() ) {
            Object array = Array.newInstance( paramType.getComponentType(), length );
            for (int i = 0; i < length; i++) {
                Array.set( array, i, decodeValue( valueElems.get( i )));
            }
            return array;
        }
        
        if( Map.class.isAssignableFrom( paramType ) ) {
            Map<Object, Object> map = new HashMap<Object, Object>();
            for (int i = 0; i < length; i++) {
                String key = valueElems.get( i ).getAttribute( "key" );
                
                map.put( key, decodeValue( valueElems.get( i )));
            }
            return map;            
        }                
        
        return null; 
    }    
    
    //convert an element back to a value
    private Object decodeValue( Element elem ) {
        String tag = elem.getTagName();
        String valueString = elem.getTextContent();
        
        if     ( tag.equals( "null"    )) return null;
        else if( tag.equals( "byte"    )) return Byte   .valueOf( valueString );
        else if( tag.equals( "char"    )) return valueString.charAt(0);      
        else if( tag.equals( "short"   )) return Short  .valueOf( valueString );
        else if( tag.equals( "boolean" )) return Boolean.valueOf( valueString );
        else if( tag.equals( "int"     )) return Integer.valueOf( valueString );
        else if( tag.equals( "long"    )) return Long   .valueOf( valueString );
        else if( tag.equals( "float"   )) return Float  .valueOf( valueString );
        else if( tag.equals( "double"  )) return Double .valueOf( valueString );
        else if( tag.equals( "string"  )) return valueString;
        
        return null;
    }
    
    private List<Element> getChildElements( Element parent ) {
        if( parent == null ) return Collections.emptyList();
            
        List<Element> kids = new ArrayList<Element>();
        NodeList nodes = parent.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item( i );
            if( node instanceof Element ) kids.add( (Element) node );
        }
        return kids;
    }
}
