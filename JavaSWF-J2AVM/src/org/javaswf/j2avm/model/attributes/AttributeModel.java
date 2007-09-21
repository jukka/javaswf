package org.javaswf.j2avm.model.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;

/**
 * Base for class, field, method and code attributes
 *
 * @author nickmain
 */
public abstract class AttributeModel {

    /** The attribute name */
    public final String name;
    
    /**
     * @param name the attribute name
     */
    public AttributeModel( String name ) {
        this.name = name;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public final boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof AttributeModel )) return false;
        return ((AttributeModel) obj).name.equals( name );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public final int hashCode() {
        return name.hashCode();
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.println( name );
    }
    
    /**
     * Parse an attribute and place it in the map
     */
    public static void parseAttr( Map<AttributeName,AttributeModel> attrMap,
    		                       DataInput in, ConstantPool pool ) 
    	throws IOException {
    	
        int    nameIndex = in.readUnsignedShort();
        int    dataSize  = in.readInt();
        String attrName  = pool.getUTF8Value( nameIndex );
        
        byte[] data = new byte[ dataSize ];
        in.readFully( data );        
        
        try {
            AttributeModel attr;
        	AttributeName name = AttributeName.valueOf( attrName );

        	Class<? extends AttributeModel> attrClass = name.attributeClass;
            DataInputStream dataIn = new DataInputStream( new ByteArrayInputStream( data ));
            
            attr = (AttributeModel) attrClass.getMethod( "parse", ConstantPool.class, DataInput.class )
                                             .invoke( null, pool, dataIn );

            attrMap.put( name, attr );
            
        } catch( InvocationTargetException itex ) {
            if( itex.getCause() instanceof IOException ) throw (IOException) itex.getCause();            
            if( itex.getCause() instanceof RuntimeException ) throw (RuntimeException) itex.getCause();            
            
            throw new RuntimeException( itex );
            
        } catch( Exception ex ) {                   
            //Unknown attribute - ignore for now            
        }
    }
}
