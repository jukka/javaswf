package org.javaswf.j2avm.steps;

import java.lang.reflect.Method;

import org.javaswf.j2avm.JavaClass;
import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.asm.ASMUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;

public abstract class CallAndAccessRewriter {

    private CallAndAccessRewriteStep rewriteStep;
    private JavaClass javaClass;
    private TranslationContext context;
    
    //the current instruction and instruction-list
    private AbstractInsnNode instruction;
    private InsnList list; 
    
    /**
     * Initialize with the JavaClass and context to be used.  This will be
     * called for each class being passed down the translation pipeline. 
     */
    /*pkg*/ final void init( CallAndAccessRewriteStep rewriteStep,
                            JavaClass javaClass, TranslationContext context ) {
        this.rewriteStep = rewriteStep;
        this.context     = context;
        this.javaClass   = javaClass;
    }
    
    /**
     * Set the instruction that is about to be visited.
     */
    /*pkg*/ final void setInstruction( InsnList list, AbstractInsnNode insn ) {
        this.instruction = insn;
        this.list        = list;
    }
    
    /**
     * Log a debug message
     */
    protected final void log( String message ) {
        context.debug( message );
    }
    
    /**
     * Get a method - loading it via the current class being translated.
     * 
     * @param ownerClass the name of the class that owns the method
     * @param methodName the method name
     * @param methodDescriptor the method descriptor
     * @return
     */
    protected final Method getMethod( String ownerClass, String methodName,
                                      String methodDescriptor ) {

        return ASMUtils.getMethod( javaClass, ownerClass, methodName, 
                                   methodDescriptor );
    }
    
    /**
     * Get the method relating to the current visit.
     * @return null if the current visit is not for a method
     */
    protected final Method getCurrentMethod() {
        if( instruction instanceof MethodInsnNode ) {
            MethodInsnNode methInsn = (MethodInsnNode) instruction;
            
            return ASMUtils.getMethod( javaClass, 
                                       methInsn.owner, 
                                       methInsn.name, 
                                       methInsn.desc );            
        }
        
        return null;
    }
    
    /**
     * Replace the current instruction with one or more new ones.  The visit
     * to the current instruction is aborted for any succeeding rewriters.
     * 
     * @param instructions the new instructions
     */
    protected final void replaceWith( AbstractInsnNode...instructions ) {
        insertBefore( instructions );
        list.remove( instruction );
        rewriteStep.cancelVisit();
    }
    
    /**
     * Insert new instructions before the current one.  The visit to the current
     * instruction will continue for succeeding rewriters.
     * 
     * @param instructions the new instructions
     */
    protected final void insertBefore( AbstractInsnNode...instructions ) {
        for( AbstractInsnNode newInsn : instructions ) {
            list.insertBefore( instruction, newInsn );
        }
    }    

    /**
     * Insert new instructions after the current one.  The visit to the current
     * instruction will continue for succeeding rewriters.
     * 
     * @param instructions the new instructions
     */
    protected final void insertAfter( AbstractInsnNode...instructions ) {
        AbstractInsnNode location = instruction;
        for( AbstractInsnNode newInsn : instructions ) {
            list.insert( location, newInsn );
            location = newInsn;
        }        
    }    
    
    /**
     * Handle the access of an instance field
     * 
     * @param className the external name of the class that owns the field
     * @param fieldName the field name
     * @param fieldType the field type
     */
    protected void rewriteFieldGet( String className, String fieldName, Type fieldType ) {
        //to be overridden        
    }
    
    /**
     * Handle the setting of an instance field
     * 
     * @param className the external name of the class that owns the field
     * @param fieldName the field name
     * @param fieldType the field type
     */
    protected void rewriteFieldSet( String className, String fieldName, Type fieldType ) {
        //to be overridden        
    }

    /**
     * Handle the access of a static field
     * 
     * @param className the external name of the class that owns the field
     * @param fieldName the field name
     * @param fieldType the field type
     */
    protected void rewriteStaticGet( String className, String fieldName, Type fieldType ) {
        //to be overridden        
    }
    
    /**
     * Handle the setting of a static field
     * 
     * @param className the external name of the class that owns the field
     * @param fieldName the field name
     * @param fieldType the field type
     */
    protected void rewriteStaticSet( String className, String fieldName, Type fieldType ) {
        //to be overridden        
    }

    /**
     * Handle a call to a 
     * 
     * @param className the external name of the class that owns the method
     * @param methodName the method name
     * @param returnType the method return type
     * @param paramTypes the method parameter types
     */
    protected void rewriteVirtualCall( String className, String methodName, 
                                       Type returnType, Type[] paramTypes ) {
        //to be overridden        
    }

    /**
     * Handle a call to a constructor
     * 
     * @param className the external name of the class
     * @param paramTypes the constructor parameter types
     */
    protected void rewriteConstructorCall( String className, Type[] paramTypes) {
        //to be overridden        
    }

    /**
     * Handle a call to a static method
     * 
     * @param className the external name of the class that owns the method
     * @param methodName the method name
     * @param returnType the method return type
     * @param paramTypes the method parameter types
     */
    protected void rewriteStaticCall( String className, String methodName, 
                                      Type returnType, Type[] paramTypes) {
        //to be overridden        
    }

    /**
     * Handle a call to a super instance method
     * 
     * @param className the external name of the class that owns the method
     * @param methodName the method name
     * @param returnType the method return type
     * @param paramTypes the method parameter types
     */
    protected void rewriteSuperCall( String className, String methodName, 
                                     Type returnType, Type[] paramTypes ) {
        //to be overridden        
    }

    /**
     * Handle a call to a private instance method
     * 
     * @param className the external name of the class that owns the method
     * @param methodName the method name
     * @param returnType the method return type
     * @param paramTypes the method parameter types
     */
    protected void rewritePrivateCall( String className, String methodName, 
                                       Type returnType, Type[] paramTypes ) {
        //to be overridden        
    }
    
    /**
     * Rewrite a new-object instruction.
     * @param className the class to be instantiated
     */
    protected void rewriteNew( String className ) {
        //to be overridden
    }
}
