package org.javaswf.j2avm.emitter.concerns;

import org.javaswf.j2avm.emitter.AVM2Code;

/**
 * Collection of all methods relating to support of translating longs.
 * The AVM2 does not have a 64 bit integer type so 
 *
 * @author nickmain
 */
public class ClassLiteralSupport {

    private final AVM2Code code;
    
    /**
     * @param code the code to generate
     */
    public ClassLiteralSupport( AVM2Code code ) {
        this.code = code;
    }
    
}
