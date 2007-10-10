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
				
		String[] files = {
				"/Users/dmain/workspace-os/JavaSWF-J2AVM-TestBed/eclipse-build",
				"/Users/dmain/workspace-os/JavaSWF-FlashRuntime/eclipse-build",
				"/Users/dmain/workspace-os/JavaSWF-J2AVM/eclipse-build"
		};
		
		URL[] urls = new URL[ files.length ];
		for( int i = 0; i < urls.length; i++ ) {
			urls[i] = new File( files[i] ).toURL();
		}
		
		ClassLoader loader = new URLClassLoader( urls );
		
		TargetSWF swf = new TargetSWF( new File( "../JavaSWF-J2AVM-Testbed/generated/test.swf" ), 800, 500, 12, 0xeeeeff );
		J2AVM j2 = new J2AVM( loader, swf ); 
		
		j2.setMainClass( "test.Test" );
		j2.translate();
	}
}
