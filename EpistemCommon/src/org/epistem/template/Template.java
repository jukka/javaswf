package org.epistem.template;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A simple template engine.
 * 
 * The following show some of the directive forms:
 * 
 *  #foo         - substitute property foo
 *  #foo[,]      - substitute property foo with given punctuation
 *  #foo{}#      - a sub-template with foo on the top of the bean stack
 *  #foo[,]{}#   - sub-template with punctuation
 *  #foo?{}#     - if foo is true then insert the given content
 *  #foo!{}#     - if foo is false then insert the given content
 *  #foo.bar.baz - the property name can be compound
 *  #bar:foo     - use the external or internal template foo applied to prop bar
 *  #foo={}#     - define an internal template
 *  ##           - insert the stack top bean toString
 *  #foo#        - terminate the prop name if needed
 *  \#           - escape the pound
 * 
 *  #(foo,bar)\n - at the start of the file, provides param names
 * 
 * @author nickmain
 */
public class Template {

    /**
     * The parameter names when calling via the writeWithParams() method. 
     */
    public  final List<String> paramNames;
    /*pkg*/ final List<String> paramNames_internal = new ArrayList<String>();
    {
        paramNames =  Collections.unmodifiableList( paramNames_internal );
    }
    
    
    /** The template content */
    final List<Segment> segments = new ArrayList<Segment>();
    
    final Map<String, Template> subTemplates = new HashMap<String, Template>();
    
    /**
     * Base for the various segments of the template content
     */
    static abstract class Segment {        
        Template parentTemplate;
        
        public abstract void write( List<Object> beans, Writer out ) throws IOException;
    }
    
    /**
     * A segment that is just static text
     */
    static class StaticSegment extends Segment {        
        private final String content;
        
        StaticSegment( String content ) {
            this.content = content;
        }

        public void write( List<Object> beans, Writer out ) throws IOException {
            out.write( content );
        }
    }
    
    /**
     * A segment that writes the toString of the first bean
     */
    static class BeanValueSegment extends Segment {
        public void write( List<Object> beans, Writer out ) throws IOException {
            
            if( beans.isEmpty() ) return;
            out.write( ""+ beans.get(0) );
        }        
    }
    
    /**
     * A segment that draws its value from a property of the value object
     */
    static class PropertySegment extends Segment {
        private final String[] propName;
        private final String   separator;
        
        PropertySegment( String[] propName, String separator ) {
            this.propName  = propName;
            this.separator = separator;
        }

        public void write( List<Object> beans, Writer out ) throws IOException {

            Object[] values = getProperty( beans, propName );
            if( values == null ) throw new IOException( "Could not find property " + Arrays.toString(propName) );
            
            for (int i = 0; i < values.length; i++) {
                if( i > 0 ) out.write( separator );
                out.write( "" + values[i] );
            }
        }        
    }
    
    /**
     * A segment that draws its value from a property of the value object and
     * emits a sub-template for each
     */
    static class SubtemplateSegment extends Segment {
        private final String[] propName;
        private final String   separator;
        /*pkg*/ Template subtemplate;
        private final int  conditionType; //0=none,1=true,2=false
        /*pkg*/ final String   subTemplateName;
        
        SubtemplateSegment( String[] propName, String separator, 
                            int conditionType, 
                            Template subtemplate, String subTemplateName ) {
            this.propName        = propName;
            this.separator       = separator;
            this.subtemplate     = subtemplate;
            this.conditionType   = conditionType;
            this.subTemplateName = subTemplateName;
        }

        public void write( List<Object> beans, Writer out ) throws IOException {

            Object[] values = getProperty( beans, propName);
            if( values == null ) throw new IOException( "Could not find property " + Arrays.toString(propName) );
            
            for (int i = 0; i < values.length; i++) {
                Object obj = values[i];
                if( conditionType == 1 && ! isTrue( obj ) ) continue;
                if( conditionType == 2 &&   isTrue( obj ) ) continue;
                
                if( i > 0 ) out.write( separator );

                beans.add( 0, obj );
                subtemplate.write( out, beans );
                beans.remove( 0 );
            }
        }        
    }
    
    Template() { } //package-private
    
    /**
     * Write out the template, filled in with the given values.  The supplied
     * list of objects is searched in forward order until any given
     * property is found. The TemplateCommonProperties bean is added to the
     * end of the list before being passed to the template.
     * 
     * @param beans the list of objects that supply the values
     * @param out the output target
     */
    public void write( Writer out, Object...beans ) throws IOException {
        List<Object> beanList = new LinkedList<Object>();
        beanList.addAll( Arrays.asList( beans ) );
        write( out, beanList );
    }

    /**
     * Write out the template, filled in with the given values.  The supplied
     * list of objects is searched in forward order until any given
     * property is found.  The TemplateCommonProperties bean is added to the
     * end of the list before being passed to the template.
     * 
     * @param beans the list of objects that supply the values
     * @param out the output target
     */
    public void write( Writer out, List<Object> beans ) throws IOException {
        List<Object> newBeans = new ArrayList<Object>( beans );
        newBeans.add( new TemplateCommonProperties() );
        for (Segment segment : segments) {
            segment.write( newBeans, out );
        }
    }

    /**
     * Write out the template, using the given arguments to bind against the
     * defined parameter names of the template ( when the #(name,name2)
     * directive is used.  If parameter names have not been defined then there
     * will be no custom properties available to the template.
     * 
     * The TemplateCommonProperties bean is added to the
     * end of the list before being passed to the template.
     *   
     * @param out the output target
     * @param args the values that are bound to the parameter names
     */
    public void writeWithParams( Writer out, Object...args ) throws IOException {
        Map<String, Object> bindings = new HashMap<String, Object>();
        
        for (int i = 0; i < paramNames.size(); i++) {
            if( i < args.length ) {
                bindings.put( paramNames.get(i) , args[i] );
            } else {
                bindings.put( paramNames.get(i) , "" );
            }
        }
        
        write( out, bindings );
    }
    
    /**
     * Instantiate the template and return the result.
     * @param args the arguments as per the writeWithParams() method
     */
    public String instantiate( Object...args ) {
        StringWriter writer = new StringWriter();
        try {
            writeWithParams( writer, args );
        } catch( IOException ioe ) {
            throw new RuntimeException(ioe);
        }
        
        return writer.toString();
    }
    
    /**
     * Determine if an object is "true"
     */
    static boolean isTrue( Object value ) {
        if( value == null ) return false;
        if( value instanceof Boolean    ) return ((Boolean) value).booleanValue();
        if( value instanceof Number     ) return ((Number) value).doubleValue() != 0;
        if( value.getClass().isArray()  ) return Array.getLength( value ) > 0;
        if( value instanceof String     ) return ((String) value).length() > 0;
        if( value instanceof Collection ) return ! ((Collection) value).isEmpty();
        
        return true;
    }
    
    /**
     * Get a property value.  The property is sought on the objects in the
     * list of beans.  
     * 
     * If the property is a Collection or an array then the result is an array
     * of the elements - otherwise it is an array of size 1 containing just the
     * atomic value. 
     * 
     * @param beans the object to search for properties
     * @param propName the multi-part property name to find 
     * @return the values found - null if none.
     */
    static Object[] getProperty( List<Object> beans, String[] propName ) {
        if( propName == null || propName.length < 1 ) return null;
        
        //get first value from the bean list
        Object[] value = valueFromBeans( beans, propName[0] );        

        for( int i = 1; i < propName.length; i++ ) {
            if( value    == null ) return null;            
            if( value[0] == null ) return null;
            
            String name = propName[i];
            
            value = getPropertyAtomic( value[0], name );
        }
        
        if( value == null ) return null;
        
        return explodeValue( value );
    }
        
    /**
     * Get an atomically named property by searching a list of beans.
     */
    static Object[] valueFromBeans( List<Object> beans, String propName  ) {
        if( beans.isEmpty() ) return null;

        //look in the bean list until an object has the property
        for (Object obj : beans ) {
            Object[] value = getPropertyAtomic( obj, propName );
            if( value != null ) return value;
        }
        
        return null;        
    }
    
    /**
     * Explode a collection or array value into elements.
     */
    static Object[] explodeValue( Object[] value ) {
        
        if( value == null )     return null;
        if( value.length != 1 ) return value;
        if( value[0] == null )  return value;
        
        //handle collections and arrays
        if( value[0] instanceof Iterable ) {
            Iterable it = (Iterable) value[0];
            List<Object> values = new ArrayList<Object>();
            for( Object v : it ) {
                values.add( v );
            }
            return values.toArray();
        }
        
        if( value[0].getClass().isArray()) {
            Object[] values = new Object[ Array.getLength( value[0] ) ];
            for (int i = 0; i < values.length; i++) {
                values[i] = Array.get( value[0], i );
            }
            return values;
        }

        return value;
    }
    
    /**
     * Find single-part property name on a single bean
     * 
     * @return null if the property was not found - otherwise Object[1] containing the
     *         property value (which may be null).
     */
    static Object[] getPropertyAtomic( Object bean, String propName ) {

        if( bean == null ) return null;
        Object value = null;

        find_value: {
            
            if( bean instanceof Map ) {
                Map map = (Map) bean;
                if( map.containsKey( propName ) ) {
                    value = map.get( propName );
                    if( value == null ) return new Object[] { null };
                    break find_value;
                }
            }
            
            Class clazz  = bean.getClass();

            //look for a getter method
            String capName = (propName.length() > 1) ?
                               (propName.substring(0,1).toUpperCase() + propName.substring(1)) :
                               propName.toUpperCase();
                               
            for (String prefix : GETTER_PREFIXES) {
                try {
                    Method m = clazz.getMethod( prefix + capName );
                    
                    //only want public instance methods
                    int mods = m.getModifiers();
                    if( Modifier.isStatic( mods ) 
                     || ! Modifier.isPublic( mods ) ) continue;
                    
                    //call the getter method
                    try {
                        value = m.invoke( bean );
                        if( value == null ) return new Object[] { null };
                        
                        break find_value;
                    } catch( Exception ex ) {
                        throw new RuntimeException( ex );
                    }
                    
                } catch( NoSuchMethodException ignored ) {}
            }
            
            //look for a field
            if( value == null ) {
                try {
                    Field f = clazz.getField( propName );

                    //only want public instance fields
                    int mods = f.getModifiers();
                    if( Modifier.isStatic( mods ) 
                     || ! Modifier.isPublic( mods ) ) break find_value;

                    try {
                        value = f.get( bean );
                        if( value == null ) return new Object[] { null };
                        
                    } catch( Exception ex ) {
                        throw new RuntimeException( ex );                        
                    }
                } catch( NoSuchFieldException ignored ) {}
            }
            
        } //end find_value
        
        if( value == null ) return null; //no value found at all

        return new Object[] { value };
    }
    
    //prefixed for property getter methods
    private static final String[] GETTER_PREFIXES = { "get", "is", "has" };

}
