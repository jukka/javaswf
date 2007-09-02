package org.javaswf.j2avm.swf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * Represents a SWF file that is an output target.
 *
 * @author nickmain
 */
public class TargetSWF {

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final SWFTagTypes tagTypes;
    private final Map<Integer, String> classes = new LinkedHashMap<Integer, String>();
    
    /**
     * @param width the stage width in pixels
     * @param height the stage height in pixels
     * @param frameRate the frames-per-second
     * @param backgroundColor the RGB background color
     */
    public TargetSWF( int width, int height, int frameRate, int backgroundColor ){
        try {        
            SWFWriter writer = new SWFWriter( baos );
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
            
        } catch( IOException ioe ) { //this shouldn't happen since target is ByteArrayOutputStream
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
    
    /**
     * Write to the given file.
     * 
     * @param targetSWF the file to write to
     */
    public void write( File targetSWF ) throws IOException {
        
        //complete the SWF
        tagTypes.tagSymbolClass( classes );
        tagTypes.tagShowFrame();
        tagTypes.tagEnd();
        
        //write the raw SWF bytes
        OutputStream out = new FileOutputStream( targetSWF );
        try {
            out.write( baos.toByteArray() );
        } finally {
            out.close();
        }
    }
}
