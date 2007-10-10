package org.javaswf.j2avm.abc;

import org.javaswf.j2avm.model.types.Signature;

import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * Represents a translated method and holds information about how it will be, or
 * was translated.
 * 
 * @author nickmain
 */
public class MethodTranslation {

	/**
	 * The class this method belongs to
	 */
	public final ClassTranslation clazz;
	
	/**
	 * The method signature
	 */
	public final Signature sig;
	
	public MethodTranslation( ClassTranslation clazz, Signature sig ) {
		this.sig   = sig;
		this.clazz = clazz;
	}
	
	/**
	 * Get the qualified name for the method
	 */
	public AVM2QName getMethodName() {
		return methodName;
	}
	
	private AVM2QName methodName;
}
