package org.javaswf.j2avm.steps;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.javaswf.j2avm.emitter.EmitterUtils;
import org.javaswf.j2avm.runtime.annotations.Getter;
import org.javaswf.j2avm.runtime.annotations.Setter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.FieldInsnNode;

/**
 * An instruction rewriter that replaces calls to getter and setter methods in
 * the AVM2 runtime classes with direct field access.  The Java proxies for the 
 * runtime classes use methods since that is the preferred Java form but the
 * AVM2 prefers direct field access (which may actually be a getter or setter
 * method under the hood).
 *
 * @author nickmain
 */
public class AVMGetterSetterRewriter extends CallAndAccessRewriter {

    /** @see org.javaswf.j2avm.steps.CallAndAccessRewriter#rewriteVirtualCall(java.lang.String, java.lang.String, org.objectweb.asm.Type, org.objectweb.asm.Type[]) */
    @Override
    protected void rewriteVirtualCall( String className, String methodName,
                                       Type returnType, Type[] paramTypes) {
        
        Method meth = getCurrentMethod();

        boolean isGetter = false;
        boolean isSetter = false;

        //need to look for the annotations by name since they are loaded
        //via a different classloader and are different Class instances
        for( Annotation anno : meth.getDeclaredAnnotations() ) {
            if( anno.annotationType().getName().equals( Getter.class.getName())) {
                isGetter = true;
            }
            if( anno.annotationType().getName().equals( Setter.class.getName())) {
                isSetter = true;
            }
        }
        
        //getter
        if( isGetter ) {
            String fieldName = EmitterUtils.nameFromAccessor( methodName );
            String fieldDesc = returnType.getDescriptor();
            
            replaceWith( new FieldInsnNode( Opcodes.GETFIELD, 
                                            className, fieldName, fieldDesc ) );
            
            log( "Replaced " + className + "::" + methodName 
                 + " with field get for " + fieldName );
            return;
        }

        //setter
        if( isSetter ) {
            String fieldName = EmitterUtils.nameFromAccessor( methodName );
            String fieldDesc = paramTypes[0].getDescriptor();
            
            replaceWith( new FieldInsnNode( Opcodes.PUTFIELD, 
                                            className, fieldName, fieldDesc ) );

            log( "Replaced " + className + "::" + methodName 
                    + " with field set for " + fieldName );
            return;
        }
    }
}
