package org.javaswf.j2avm.steps;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.code.InstructionListWalker;

/**
 * A base for translation steps that involve an InstructionListWalker for
 * rewriting instructions 
 * 
 * @author nickmain
 */
public class RewriteStep extends InstructionListWalker implements TranslationStep {

	/** The current context */
	private TranslationContext context;
		
	/** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.model.ClassModel, org.javaswf.j2avm.TranslationContext) */
	public boolean process( ClassModel classModel, TranslationContext context ) {
		this.context = context;

		for( MethodModel methodModel : classModel.methods.values() ) {			
			if( rewriteInstructions( classModel, methodModel ) ) {
				walk( classModel, methodModel );
			}			
		}
		
		return true;
	}

	/**
	 * Get the current translation context
	 */
	protected final TranslationContext context() {
		return context;
	}
	
	/**
	 * Override to indicate whether to walk the instructions of the given 
	 * method. The default is true.
	 */
	protected boolean rewriteInstructions( ClassModel classModel, MethodModel methodModel ) {
		return true;
	}
	
	/**
	 * Get a method from a descriptor
	 * @param desc the method descriptor
	 * 
	 * @return null if the referenced method could not be found
	 */
	protected final MethodModel methodFor( MethodDescriptor desc ) {		
		ClassModel clazz = context.modelForName( desc.owner.name );
		if( clazz == null ) return null;
		
		return clazz.methods.get( desc.signature );
	}
}
