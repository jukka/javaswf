import java.io.InputStream;
import java.io.Serializable;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.ClassModel;


public class Test implements Serializable, Cloneable {

	public static void main( String[] args ) {
		
		InputStream in = Test.class.getClassLoader().getResourceAsStream( "Test.class" );
		
		ClassModel model = new ClassModel( in );
		
		IndentingPrintWriter ipw = IndentingPrintWriter.SYSOUT;
		
		model.dump( ipw );
		ipw.flush();
		
		if( args.length > 0 ) System.out.println( args[0] );
	}
	
	public Test() {
		for( int i = 0; i < 10; i++ ) {
			String s;
			try {
				switch(i) {
					case 2: s = "two"; break;
					case 6: s = "six"; break;
					default: s = "other"; break;
				}
			} catch( Exception ex ) {
				s = "oops";
			}
			
			System.out.println( s );
		}
	}
}
