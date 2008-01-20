package com.anotherbigidea.flash.avm2.utils;

import java.io.FileInputStream;
import java.io.IOException;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.io.AVM2ABCBuilder;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.interfaces.SWFTags;
import com.anotherbigidea.flash.readers.SWFReader;
import com.anotherbigidea.flash.readers.TagParser;
import com.anotherbigidea.flash.writers.SWFTagTypesImpl;

/**
 * Utilities for loading ABC files
 *
 * @author nickmain
 */
public class AVM2ABCFileLoader {

    /**
     * Extract the first ABC file from a SWF 
     */
    public static AVM2ABCFile abcFileFromExistingSWF( String swfFile ) throws IOException {
        
        final AVM2ABCBuilder builder = new AVM2ABCBuilder();
        
        SWFTagTypes tags = new SWFTagTypesImpl( null ) {
            private boolean isFirst = true;
            
            /** @see com.anotherbigidea.flash.writers.SWFTagTypesImpl#tagDoABC() */
            @Override
            public ABC tagDoABC( int flags, String name ) throws IOException {
                if( ! isFirst ) return null;
                isFirst = false;
                return builder;
            }
        };
        
        FileInputStream in  = new FileInputStream( swfFile );        
        SWFTags tagparser = new TagParser( tags );        
        SWFReader reader = new SWFReader( tagparser, in );        
        reader.readFile();
        
        return builder.file;
    }
    
}
