/**
 * 
 */
package org.javaswf.j2avm.emitter;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.objectweb.asm.ClassVisitor;

/**
 * The final JVM to AVM2 bytecode translator.
 *
 * @author nickmain
 */
public class AVM2ClassEmitter implements TranslationStep {

    /**
     * @see org.javaswf.j2avm.TranslationStep#prepare(org.javaswf.j2avm.TranslationContext, org.objectweb.asm.ClassVisitor)
     */
    public ClassVisitor prepare( TranslationContext context,
                                 ClassVisitor downstreamVisitor) {
        
        return new EmitterClassVisitor( context, downstreamVisitor );
    }
}
