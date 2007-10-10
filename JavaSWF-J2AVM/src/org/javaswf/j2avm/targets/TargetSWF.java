package org.javaswf.j2avm.targets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.javaswf.j2avm.TranslationTarget;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.io.AVM2ABCBuilder;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * A target SWF within which to embed the translated ABC data.
 *
 * @author nickmain
 */
public class TargetSWF implements TranslationTarget {

	/** @see org.javaswf.j2avm.TranslationTarget#finish() */
	public void finish() {
		try {		
		    //complete the SWF
		    tagTypes.tagSymbolClass( classes );
		    tagTypes.tagShowFrame();
		    tagTypes.tagEnd();
		} catch( IOException ioe ) {
			throw new RuntimeException(ioe);
		}
   	}

	/** @see org.javaswf.j2avm.TranslationTarget#insertABC(AVM2ABCFile) */
	public void insertABC( AVM2ABCFile abc ) {
		addABC( abc, "j2avm", true );
	}

	/** @see org.javaswf.j2avm.TranslationTarget#setMainClass(com.anotherbigidea.flash.avm2.model.AVM2Class) */
	public void setMainClass( AVM2Class mainClass ) {
		addSymbolClass( 0, mainClass.name.toQualString() );		
	}

	private final SWFTagTypes tagTypes;
    private final Map<Integer, String> classes = new LinkedHashMap<Integer, String>();
    
    /**
     * @param targetSWF the target file
     * @param width the stage width in pixels
     * @param height the stage height in pixels
     * @param frameRate the frames-per-second
     * @param backgroundColor the RGB background color
     */
    public TargetSWF( File targetSWF, 
    		          int width, int height, 
    		          int frameRate, int backgroundColor ){
    	try {        
        	OutputStream out = new FileOutputStream( targetSWF );    		
    		SWFWriter writer = new SWFWriter( out );
            writer.setCompression( true );
            
            tagTypes = new TagWriter( writer );
            
            tagTypes.header( SWFConstants.FLASH_9, 
                             -1,                   //defer calculation
                             width  * SWFConstants.TWIPS, 
                             height * SWFConstants.TWIPS, 
                             frameRate, 
                             -1 );                 //defer calculation
            
            tagTypes.tagFileAttributes( SWFConstants.FILE_ATTRIBUTES_ALLOW_AS3
                                      + SWFConstants.FILE_ATTRIBUTES_USE_NETWORK );
            
            tagTypes.tagScriptLimits( 1000, 60 );
            
            Color color = new Color( backgroundColor );
            tagTypes.tagSetBackgroundColor( color );
            
        } catch( IOException ioe ) {
            throw new RuntimeException( ioe );
        }
    }
    
    /**
     * Add an ABC file to the SWF
     * 
     * @param abcFile the file to add
     * @param name the file name
     * @param lazyInit true to run the scripts lazily
     */
    public void addABC( AVM2ABCFile abcFile, String name, boolean lazyInit ) {
        try {
            int flags = lazyInit ? SWFConstants.DO_ABC_LAZY_INITIALIZE_FLAG : 0;
            
            ABC abc = tagTypes.tagDoABC( flags, name );            
            abcFile.write( abc );
            
        } catch( IOException ioe ) { //this shouldn't happen since target is ByteArrayOutputStream
            throw new RuntimeException( ioe );
        }
    }
    
    /**
     * Add an association between a symbol (sprite, font etc) and an
     * ActionScript class.
     * 
     * @param symbolId the symbol id - zero is the main timeline
     * @param className the fully qualified class name
     */
    public void addSymbolClass( int symbolId, String className ) {
        classes.put( symbolId, className );
    }
}
