package org.epistem.io;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.epistem.util.Hex;


/**
 * Util to dump pipeline interface calls to XML that can later be replayed.
 *
 * @author nickmain
 */
public final class InterfaceCallDumper<IFACE extends PipelineInterface> {
    
    protected final IndentingPrintWriter out;
    protected final Class<IFACE> iface;
    
    //map of method name to param names
    protected final Map<String, String[]> paramNames = new HashMap<String, String[]>();
    
    private boolean hasWrapperElement = false;
    
    private final InvocationHandler handler = new InvocationHandler() {
        /** @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]) */
        public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return InterfaceCallDumper.this.invoke(proxy, method, args);
        }
    };
    
    /**
     * @param iface the interface to proxy
     * @param out the output target
     * @param makeWrapperElement true to wrap all calls in an element denoting the interface name
     */
    public InterfaceCallDumper( Class<IFACE> iface, IndentingPrintWriter out, boolean makeWrapperElement ) {
        this.out   = out;
        this.iface = iface;
        
        loadParamNames();
        
        if( makeWrapperElement ) {
            out.startElement( iface.getName().replace( '.', '_' ));
            hasWrapperElement = true;
        }
    }
    
    /**
     * Create a proxy that implements the required interface
     */
    public final IFACE getInterface() {
        
        @SuppressWarnings( "unchecked" )
        IFACE proxy = (IFACE) Proxy.newProxyInstance(iface.getClassLoader(), new Class[] { iface }, handler );        
        return proxy;
    }
    
    /**
     * To be overidden - called when a method on the interface proxy is invoked.
     */
    protected Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        
        if( nestedDumper != null ) {
            out.endElement();
            nestedDumper = null;
        }
                
        return printCall(method, args);
    }
    
    /*pkg*/ static boolean isArrayParam( Class paramType ) {
        return paramType.isArray()
            || Collection.class.isAssignableFrom( paramType )
            || Map.class.isAssignableFrom( paramType );
    }
    
    /**
     * Emit a method call as an XML element.
     * Any array/collection/map parameters are emitted as nested elements.
     * If the method returns an interface then the calls to that interface
     * are nested elements also.
     * 
     * @param method the invoked method
     * @param args the args
     * @return the result for the method
     */
    @SuppressWarnings( "unchecked" )
    protected final Object printCall( Method method, Object[] args ) {
        
        //intercept the done method
        if( method.getName().equals( "done" ) ) {
            if( hasWrapperElement ) {
                out.endElement(); //the one started in the constructor
            }
            out.flush();
            return null;
        }
        
        //get the param names
        String[] argNames = paramNames.get( method.getName() );
        if( argNames == null ) {            
            argNames = new String[ method.getParameterTypes().length ];
            for (int i = 0; i < argNames.length; i++) {
                argNames[i] = "arg" + (i+1);
            }
        }

        if( args == null ) args = new Object[0];
        
        out.startElement( method.getName() );
        
        Class<?>[] paramTypes = method.getParameterTypes();
        int arrayParamCount = 0; //num of param types that are array/collection/map
        boolean hasArrayParams = false; //true if any array params are actually instantiated
        boolean hasReturnInterface = method.getReturnType().isInterface();
        
        //emit non-arrays as attributes
        for (int i = 0; i < args.length; i++) {
            
            //skip arrays, collections and maps
            Class<?> paramType = paramTypes[ i ]; 
            if( isArrayParam( paramType )) {               
                arrayParamCount++;
                continue;
            }
            
            Object arg = args[i];
            out.attribute( argNames[i], arg );            
        }        
        
        //emit arrays
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            if( arg == null ) continue;
            Class<?> paramType = paramTypes[ i ]; 

            if( Collection.class.isAssignableFrom( paramType ) ) {
                Collection c = (Collection) arg;
                if( c.isEmpty() ) continue;
                
                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.startElement( argNames[i] );
                }
                for (Object object : c) {
                    emitValueElement( object, null );            
                }                
                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.endElement();
                }
                hasArrayParams = true;
                continue;
            }

            if( paramType == byte[].class ) {
                byte[] byteArray = (byte[]) arg;
                if( byteArray.length == 0 ) continue;
                
                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.startElement( argNames[i] );
                }
                
                Hex.dump( out, byteArray, 0L, "", false );
                
                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.endElement();
                }
                hasArrayParams = true;
                continue;
            }
            
            if( paramType.isArray() ) {
                int length = Array.getLength( arg );
                if( length == 0 ) continue;

                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.startElement( argNames[i] );
                }
                for (int j = 0; j < length; j++) {
                    emitValueElement( Array.get( arg, j ), null );
                }                
                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.endElement();
                }
                hasArrayParams = true;
                continue;
            }
            
            if( Map.class.isAssignableFrom( paramType ) ) {
                Map<?,?> m = (Map<?,?>) arg;
                if( m.isEmpty() ) continue;
                
                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.startElement( argNames[i] );
                }

                for ( Map.Entry<?,?> entry : m.entrySet()) {
                    Object key = entry.getKey();
                    Object val = entry.getValue();

                    emitValueElement( val, key.toString() );
                }

                if( arrayParamCount > 1 || hasReturnInterface ) {
                    out.endElement();
                }
                hasArrayParams = true;
                continue;                
            }                
        }        
        
        //return nested interface dumper        
        if( hasReturnInterface ) {               
            nestedDumper =  new InterfaceCallDumper( method.getReturnType(), out, hasArrayParams );            
            return nestedDumper.getInterface();
        }
        
        out.endElement();       
        return null;
    }

    private InterfaceCallDumper nestedDumper; 
        
    /** Load the param names from a property file */
    protected void loadParamNames() {
        loadParamNames( iface, paramNames );
    }
    
    /*pkg*/ static void loadParamNames( Class<?> iface, Map<String, String[]> paramNames ) {
        String filename = iface.getSimpleName() + ".param-names";
        InputStream in = iface.getResourceAsStream( filename );
        if( in == null ) return;
        
        Properties props = new Properties();
        try{
            props.load( in );
        } catch( IOException ioe ) {  
            throw new RuntimeException(ioe);            
        }
        
        for (Enumeration e = props.propertyNames(); e.hasMoreElements();) {
            String key   = (String) e.nextElement();
            String names = props.getProperty( key );
            
            List<String> nameList = new ArrayList<String>();
            StringTokenizer tok = new StringTokenizer( names );
            while( tok.hasMoreTokens() ) {
                nameList.add( tok.nextToken() );
            }
            
            paramNames.put( key, nameList.toArray( new String[ nameList.size() ]));
        }
    }
    
    /**
     * Emit an element containing the given value, with a tag that denotes
     * the value type
     * 
     * @param key optional key
     */
    private void emitValueElement( Object obj, String key ) {

        String tag = "value";
        
        if     ( obj == null              ) tag = "null";
        else if( obj instanceof Byte      ) tag = "byte";
        else if( obj instanceof Character ) tag = "char";            
        else if( obj instanceof Short     ) tag = "short";
        else if( obj instanceof Boolean   ) tag = "boolean";
        else if( obj instanceof Integer   ) tag = "int";
        else if( obj instanceof Long      ) tag = "long";
        else if( obj instanceof Float     ) tag = "float";
        else if( obj instanceof Double    ) tag = "double";
        else if( obj instanceof String    ) tag = "string";
        
        if( key != null ) {
            out.element( tag, obj, "key", key );
        } else {
            out.element( tag, obj );
        }
    }
}
