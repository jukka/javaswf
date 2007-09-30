package org.javaswf.j2avm.steps;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.TranslationStep;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.flags.MethodFlag;

/**
 * Translation step that adds a dummy method body to abstract and native
 * methods.
 * 
 * TODO: Handle interfaces before this
 * 
 * @author nickmain
 */
public class NativeAndAbstractMethodsStep implements TranslationStep {

	/** @see org.javaswf.j2avm.TranslationStep#process(org.javaswf.j2avm.model.ClassModel, org.javaswf.j2avm.TranslationContext) */
	public boolean process( ClassModel classModel, TranslationContext context ) {
		
		for( MethodModel methodModel : classModel.methods.values() ) {
			boolean isAbstract = methodModel.flags.contains( MethodFlag.MethodIsAbstract );
			boolean isNative   = methodModel.flags.contains( MethodFlag.MethodIsNative );
			
			if( isAbstract || isNative ) {				

				String nature = isAbstract ? "abstract" : "native";
				context.debug( "Creating dummy implementation for " + nature + " method " + 
						       classModel.type.name + "::" + 
						       methodModel.signature );

				CodeAttribute code = CodeAttribute.dummyFor( methodModel.returnType );
				methodModel.attributes.put( AttributeName.Code, code );
			}
		}
		
		return true;
	}
}
