package babelswf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * Driver for the AVM translation process for a single file
 *
 * @author nickmain
 */
public class AVMTranslator {

	public static final Logger log = Logger.getLogger( AVMTranslator.class.getName() );
	
	private final File avm1swf;
	private final File avm2swf;
    private final File runtimeSwf;
	
    public static final boolean TRACE = false;
    
	/**
	 * @param avm1swf the avm1 input swf
	 * @param avm2swf the avm2 output swf
	 */
	public AVMTranslator( File avm1swf, File avm2swf, File runtimeSwf ) {
		this.avm1swf    = avm1swf;
		this.avm2swf    = avm2swf;
		this.runtimeSwf = runtimeSwf;		
	}
	
	/**
	 * Perform the translation process
	 */
	public void translate() throws IOException {
		FileOutputStream out = new FileOutputStream( avm2swf );
		SWFWriter writer = new SWFWriter( out );
		TagWriter tagWrt = new TagWriter( writer );
		
		AVM1ActionInterceptor interceptor = new AVM1ActionInterceptor( avm1swf.getName(), tagWrt, runtimeSwf );
		FileInputStream in = new FileInputStream( avm1swf );
		SWFReader reader = new SWFReader( interceptor, in );
		reader.readFile();
		
		in.close();
		out.close();
	}	
}
