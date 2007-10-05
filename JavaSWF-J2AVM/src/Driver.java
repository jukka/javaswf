import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.javaswf.j2avm.J2AVM;
import org.javaswf.j2avm.targets.TargetSWF;

/**
 * Driver for debugging.
 *  
 * @author dmain
 */
public class Driver {

	public static void main( String[] args ) throws Exception {
		
		URL url = new URL( "file:///Users/dmain/workspace-os/JavaSWF-J2AVM-TestBed/eclipse-build" );
		ClassLoader loader = new URLClassLoader( new URL[] { url } );
		TargetSWF swf = new TargetSWF( new File( "../JavaSWF-J2AVM-Testbed/generated/test.swf" ), 800, 500, 12, 0xeeeeff );
		J2AVM j2 = new J2AVM( Driver.class.getClassLoader(), swf ); 
		
		j2.setMainClass( "test.Test" );
		j2.translate();
	}
}
