package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;

/**
 * Base for parameter annotations
 *
 * @author nickmain
 */
public abstract class ParameterAnnotationAttribute extends AnnotationAttribute {
    
    /** The list of parameter annotations - each is a map keyed by annotation class name */
    public List<Map<String,AnnotationModel>> parameterAnnotations = new ArrayList<Map<String,AnnotationModel>>();

    protected ParameterAnnotationAttribute( AttributeName name, boolean isVisible ) {
        super( name, isVisible );
    }
    
    /** Parse the parameter annotations */
    protected void parseAnnotations( ConstantPool pool, DataInput in ) throws IOException {
        
        int paramCount = in.readUnsignedByte();
        
        for (int i = 0; i < paramCount; i++) {
            Map<String,AnnotationModel> annotations = new HashMap<String, AnnotationModel>();            
            parseAnnotations( annotations, pool, in );
            parameterAnnotations.add( annotations );
        }
    }
    
    /** Dump for debug purposes */
    public final void dump( IndentingPrintWriter out ) {
        out.println( name + " {" );
        out.indent();
        
        for( Map<String,AnnotationModel> annotations : parameterAnnotations ) {
            out.println( "parameter {" );
            out.indent();
            
            for( AnnotationModel anno : annotations.values() ) {
                anno.dump( out );            
            }
            
            out.unindent();
            out.println( "}" );                    
        }
        
        out.unindent();
        out.println( "}" );        
    }
}
