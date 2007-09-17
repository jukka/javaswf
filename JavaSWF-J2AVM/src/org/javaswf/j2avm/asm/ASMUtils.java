package org.javaswf.j2avm.asm;

import java.lang.reflect.Method;

import org.javaswf.j2avm.JavaClass;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

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

    /**
     * Whether the private flag is set
     */
    public static boolean isPrivate( int flags ) {
        return (flags & Opcodes.ACC_PRIVATE) != 0; 
    }
    
    /**
     * Get the number of arguments for a method
     * @param methodDescriptor the method descriptor
     */
    public static int getArgCount( String methodDescriptor ) {        
        return Type.getArgumentTypes( methodDescriptor ).length;
    }
    
    /**
     * Whether a method is void
     * @param methodDescriptor the method descriptor
     */
    public static boolean isVoidMethod( String methodDescriptor )  {
        return Type.getReturnType( methodDescriptor ) == Type.VOID_TYPE;
    }
    
    /**
     * Find a method.
     * 
     * @param currentClass the class to use to load the target class
     * @param owner the class of the method
     * @param name the method name
     * @param desc the method descriptor
     */
    public static Method getMethod( JavaClass currentClass,
                                    String owner, String name, String desc ) {
        
        try {
            Class<?> clazz = currentClass.clazz
                                         .getClassLoader()
                                         .loadClass( externalName( owner ));
            while( clazz != null ) {
                    
                Method[] methods  = clazz.getDeclaredMethods();
                Type[]   argTypes = Type.getArgumentTypes( desc );
                
                for( int i = 0; i < methods.length; i++ ) {
                    Method m = methods[i];
                    if( m.getName().equals( name )) {
                        
                        Class<?>[] params = m.getParameterTypes();
                        
                        if( params.length != argTypes.length ) continue;
                        if( params.length == 0 ) return m;
                        
                        for( int j = 0; j < params.length; j++ ) {
                            String argType = argTypes[j].getClassName();
                            if( argType.equals( params[j].getName() )) return m;                            
                        }
                    }
                }

                clazz = clazz.getSuperclass(); //look up the chain
            }
        } catch( Exception ex ) {
            throw new RuntimeException( ex );
        }
        
        throw new RuntimeException( "Could not find method " + owner + "::" + name + desc );
    }
}
