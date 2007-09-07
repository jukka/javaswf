package org.javaswf.j2avm.emitter;

import com.anotherbigidea.flash.avm2.model.AVM2Class;

/**
 * Internal information about the translation of a class
 *
 * @author nickmain
 */
public class TranslationInfo {

    /* pkg */ int classScopeDepth;  //the scope depth of the class definition
    
    /* pkg */ AVM2Class avm2class;

}
