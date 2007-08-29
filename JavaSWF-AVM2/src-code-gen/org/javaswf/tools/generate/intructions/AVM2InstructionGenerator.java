package org.javaswf.tools.generate.intructions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epistem.template.CodeGenerator;
import org.epistem.template.Template;

import com.anotherbigidea.flash.avm2.ArgType;
import com.anotherbigidea.flash.avm2.Operation;

import freemarker.template.TemplateException;

public class AVM2InstructionGenerator extends CodeGenerator {

	private final Template interfaceTemplate = template( "interface" );
	private final Template switchTemplate    = template( "switch" );
	
	/**
	 * @param targetDir the destination for generated files
	 */
	public AVM2InstructionGenerator( File targetDir ) {
		super( targetDir );
	}
	
	/**
	 * Generate the code
	 */
	public void generate() throws IOException, TemplateException {
		
		generateIFaceMethods();
		
	}
	
    private void generateIFaceMethods() throws IOException, TemplateException {    
        
    	System.out.println( "  generating interface methods" );
    	
        //generate interface methods
        for( Operation operation : Operation.values() ) {
            String opName = operation.name().substring(3); //drop the OP_ prefix
            
            Map<String,Object> model = new HashMap<String,Object>();
            for( ArgType argType : operation.argTypes) {
            }
            
            generate( "interface_methods.txt", interfaceTemplate, model );
        }

    	System.out.println( "  generating interface switch" );
        
        //generate interface method cases
    	Map<String,Object> model = new HashMap<String,Object>();
        for( Operation operation : Operation.values() ) {
            String opName = operation.name().substring(3); //drop the OP_ prefix
            int[] argIndices = new int[ operation.argTypes.length ];
            for (int i = 0; i < argIndices.length; i++) {
                argIndices[i] = i;
            }
            
            generate( "interface_switch.txt", switchTemplate, model );            
        }
    }
	
	public static void main(String[] args) throws Exception {
		System.out.println( "Generating code...." );
		File targetDir = new File( args[0] );
		AVM2InstructionGenerator gen = new AVM2InstructionGenerator( targetDir );
		gen.generate();		
		System.out.println( "....done." );
	}
}
