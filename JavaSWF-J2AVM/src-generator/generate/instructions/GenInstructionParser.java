package generate.instructions;

import java.io.File;
import java.lang.annotation.Annotation;

import org.epistem.template.CodeGenerator;
import org.javaswf.j2avm.model.code.instruction.Operation;

/**
 * Code generator for the instruction parser logic
 *
 * @author nickmain
 */
public class GenInstructionParser extends CodeGenerator {

	private GenInstructionParser( File dir ) {
		super( dir );
	}
	
	public String dumpAnnos( Object obj ) {
		System.err.println( obj );
		StringBuilder buff = new StringBuilder();
		
		for( Annotation anno : obj.getClass().getAnnotations() ) {
			buff.append( anno.annotationType().getName() + "\n" );
		}
		
		return buff.toString();
	}
	
	public static void main( String[] args ) throws Exception {
		File dir = new File( "generated" );
		dir.mkdirs();
		GenInstructionParser gen = new GenInstructionParser( dir );
		
		Operation[] ops = Operation.values();
		gen.addToModel( "ops", ops );
		
		gen.generate( "test.txt", "test" );
	}
}
