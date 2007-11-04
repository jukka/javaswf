import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.ModelFactory;

public class Test implements Cloneable {

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
		
		dip();
	}
	
	private double dip() {
		return (System.currentTimeMillis() > 1000) ? (System.currentTimeMillis() + 2) : 2.3;
	}
	
	public static void main( String[] args ) {
		
		ModelFactory factory = new ModelFactory( Test.class.getClassLoader() );
		
		ClassModel model = factory.modelForName( "Test" );
		
		IndentingPrintWriter ipw = IndentingPrintWriter.SYSOUT;
		
		model.dump( ipw );
		ipw.flush();
		
		if( args.length > 0 ) System.out.println( args[0] );
	}
}
