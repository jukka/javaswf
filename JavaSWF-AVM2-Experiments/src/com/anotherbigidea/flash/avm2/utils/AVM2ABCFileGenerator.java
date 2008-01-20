package com.anotherbigidea.flash.avm2.utils;

import java.io.IOException;

import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.writers.SWFWriter;
import com.anotherbigidea.flash.writers.TagWriter;

/**
 * Utilities for generating ABC SWFs
 * 
 *
 * @author nickmain
 */
public class AVM2ABCFileGenerator {

    /**
     * Write a SWF containing ABC code.  No symbol classes are defined and 
     * the ABC code is NOT lazily initialized.
     *  
     * @param swfFile the file to write
     * @param abc the ABC data to include
     */
    public static void makeScriptSWF( String swfFile, AVM2ABCFile abc ) throws IOException {                
        SWFWriter   swf  = new SWFWriter( swfFile );
        SWFTagTypes tags = new TagWriter( swf );
        swf.header( SWFConstants.FLASH_9, -1, 2000, 2000, 12, 1 );
        tags.tagFileAttributes( SWFConstants.FILE_ATTRIBUTES_ALLOW_AS3 );
        tags.tagSetBackgroundColor( new Color( 0xffffff ) );
        abc.write( tags.tagDoABC( 0, "test" ));        
        tags.tagShowFrame();
        tags.tagEnd();       
    }    
}
