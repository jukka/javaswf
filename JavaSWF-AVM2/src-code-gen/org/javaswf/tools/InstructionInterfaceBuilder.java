package org.javaswf.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epistem.template.Template;
import org.epistem.template.TemplateSource;

import com.anotherbigidea.flash.avm2.ArgType;
import com.anotherbigidea.flash.avm2.Operation;

/**
 * Tool to build the ABC.InstructionInterface methods, based on the Operation enum.
 *
 * @author nickmain
 */
public class InstructionInterfaceBuilder {

    private static final Template methodTemplate = loadTemplate( "instruction_method" );
    private static final Template caseTemplate   = loadTemplate( "instruction_case" );
        
    public static void main(String[] args) throws IOException {
     
        generateIFaceMethods();
    }
    
    public static void generateIFaceMethods() {    
        
        //generate interface methods
        for( Operation operation : Operation.values() ) {
            String opName = operation.name().substring(3); //drop the OP_ prefix
            
            List<Object> params = new ArrayList<Object>();
            for( ArgType argType : operation.argTypes) {
                Map<String, String> map = new HashMap<String, String>();
                map.put( "argName" , argType.name().toLowerCase() );
                
                Class<?> c = argType.valueType;
                
                map.put( "typeName", c.getSimpleName() );
                params.add( map );
            }
            
            System.out.println( methodTemplate.instantiate( opName, params ) );
        }
        
        //generate interface method cases
        for( Operation operation : Operation.values() ) {
            String opName = operation.name().substring(3); //drop the OP_ prefix
            int[] argIndices = new int[ operation.argTypes.length ];
            for (int i = 0; i < argIndices.length; i++) {
                argIndices[i] = i;
            }
            
            System.out.println( caseTemplate.instantiate( operation.name(), opName, argIndices ) );
        }
    }
    
    private static Template loadTemplate( String name ) {
        
    	return new Template( InstructionInterfaceBuilder.class, ""name + ".ftl" );
    	
        String filename = "javaswf/tools/templates/" + name + ".template";
        
        try {
            return TemplateSource.forClassLoader().getTemplate( filename );
        } catch( IOException ioe ) {
            throw new RuntimeException( ioe );
        }
    }
}
