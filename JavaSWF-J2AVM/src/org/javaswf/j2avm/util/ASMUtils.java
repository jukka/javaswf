package org.javaswf.j2avm.util;

import org.objectweb.asm.Opcodes;

/**
 * Utility methods for dealing with ASM Java Class structures
 *
 * @author nickmain
 */
public class ASMUtils {

    /**
     * Convert an internal class name to the normal period form
     */
    public static String externalName( String internalName ) {
        return internalName.replace( '/', '.' );
    }
    
    /**
     * Whether the final flag is set
     */
    public static boolean isFinal( int flags ) {
        return (flags & Opcodes.ACC_FINAL) != 0; 
    }

    /**
     * Whether the interface flag is set
     */
    public static boolean isInterface( int flags ) {
        return (flags & Opcodes.ACC_INTERFACE) != 0; 
    }

    /**
     * Whether the static flag is set
     */
    public static boolean isStatic( int flags ) {
        return (flags & Opcodes.ACC_STATIC) != 0; 
    }

}
