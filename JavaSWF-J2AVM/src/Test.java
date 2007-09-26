

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
	}
}
