package org.epistem.util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.sun.mirror.apt.AnnotationProcessor;
import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorFactory;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;

/**
 * Utility to extract the parameter names out of the Java source of an interface.
 * This information is intended to be provided to the InterfaceCallDumper.
 * 
 * Example apt command line:
 * apt -cp <..> -factory org.epistem.util.InterfaceParamNameExtractor -Aiface=com.anotherbigidea.flash.avm2.ABC javaswf/src-core/com/anotherbigidea/flash/avm2/ABC.java
 * 
 * @author nickmain
 */
public class InterfaceParamNameExtractor implements AnnotationProcessorFactory {

    /** @see com.sun.mirror.apt.AnnotationProcessorFactory#getProcessorFor(java.util.Set, com.sun.mirror.apt.AnnotationProcessorEnvironment) */
    public AnnotationProcessor getProcessorFor( Set<AnnotationTypeDeclaration> set, 
                                                AnnotationProcessorEnvironment env ) {
        
        //get the interface name
        String ifaceName = null;
        for ( String key : env.getOptions().keySet()) {
            if( key.startsWith( "-Aiface=" ) ) {
                ifaceName = key.substring( 8 );
                break;
            }
        }
        
        if( ifaceName == null ) {
            System.err.println( "Use -Aiface=<fq-classname> to provide the interface to process" );
            return null;
        }
         
        TypeDeclaration decl = env.getTypeDeclaration( ifaceName );
        processIface( decl );
        
        return null;
    }

    private void processIface( TypeDeclaration decl ) {
        try {
            String filename = decl.getSimpleName();
            int dot = filename.indexOf( "." );
            if( dot >= 0 ) filename = filename.substring( dot + 1 );
            filename += ".param-names";
            
            FileWriter out = new FileWriter( filename );

            for (MethodDeclaration meth : decl.getMethods()) {
                out.write( meth.getSimpleName() + "=" );
                
                for( ParameterDeclaration param : meth.getParameters()) {
                    out.write( " " + param.getSimpleName() );
                }
                out.write( "\n" );
            }
            
            out.close();
            
        } catch( IOException ioe ) {
            throw new RuntimeException( ioe );
        }

        for( TypeDeclaration type : decl.getNestedTypes()) {
            processIface( type );
        }
    }
    
    /** @see com.sun.mirror.apt.AnnotationProcessorFactory#supportedAnnotationTypes() */
    public Collection<String> supportedAnnotationTypes() {
        return Collections.singleton( "*" );
    }

    /** @see com.sun.mirror.apt.AnnotationProcessorFactory#supportedOptions() */
    public Collection<String> supportedOptions() {
        return Collections.singleton( "-Aiface" );
    }
}
