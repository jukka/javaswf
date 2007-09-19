package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.parser.ConstantPool;


/**
 * Base for annotation containers
 *
 * @author nickmain
 */
public abstract class Annotations extends AnnotationAttribute {

    /** The annotations - keyed by annotation class name */
    public final Map<String,AnnotationModel> annotations = new HashMap<String,AnnotationModel>();
    
    protected Annotations( AttributeName name, boolean isVisible ) {
        super( name, isVisible );
    }
    
    /** Parse the annotations */
    protected void parseAnnotations( ConstantPool pool, DataInput in ) throws IOException {
        parseAnnotations( annotations, pool, in );
    }
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        
        out.println( name + " {" );
        out.indent();
        
        for( AnnotationModel anno : annotations.values() ) {
            anno.dump( out );            
        }
        
        out.unindent();
        out.println( "}" );
    }
}
