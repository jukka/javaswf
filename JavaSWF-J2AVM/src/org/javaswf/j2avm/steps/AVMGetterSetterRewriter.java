package org.javaswf.j2avm.steps;

import org.javaswf.j2avm.emitter.EmitterUtils;
import org.javaswf.j2avm.model.FieldDescriptor;
import org.javaswf.j2avm.model.MethodDescriptor;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.types.ValueType;
import org.javaswf.j2avm.runtime.annotations.Getter;
import org.javaswf.j2avm.runtime.annotations.Setter;

/**
 * An instruction rewriter that replaces calls to getter and setter methods in
 * the AVM2 runtime classes with direct field access.  The Java proxies for the 
 * runtime classes use methods since that is the preferred Java form but the
 * AVM2 prefers direct field access (which may actually be a getter or setter
 * method under the hood).
 *
 * @author nickmain
 */
public class AVMGetterSetterRewriter extends RewriteStep {

    /** @see org.javaswf.j2avm.model.code.InstructionListWalker#invokeVirtual(org.javaswf.j2avm.model.MethodDescriptor) */
	@Override
	public void invokeVirtual( MethodDescriptor methodDesc ) {
        
		String methodName = methodDesc.signature.name;
        MethodModel meth = methodFor( methodDesc );

        boolean isGetter = meth.annotation( Getter.class.getName() ) != null;
        boolean isSetter = meth.annotation( Setter.class.getName() ) != null;
        
        //getter
        if( isGetter ) {
            String fieldName = EmitterUtils.nameFromAccessor( methodName );
            FieldDescriptor fieldDesc = 
            	new FieldDescriptor( methodDesc.owner, 
            			             fieldName, 
            			             (ValueType) meth.returnType );
            
            insertAfter().pushField( fieldDesc );
            remove();
            
            context().debug( "Replaced " + classModel().type + "::" + methodName 
                             + " with field get for " + fieldName );
            return;
        }

        //setter
        if( isSetter ) {
            String fieldName = EmitterUtils.nameFromAccessor( methodName );
            FieldDescriptor fieldDesc = 
            	new FieldDescriptor( methodDesc.owner, 
            			             fieldName, 
            			             methodDesc.signature.paramTypes[0] );
            
            insertAfter().storeField( fieldDesc );
            remove();

            context().debug( "Replaced " + classModel().type + "::" + methodName 
                             + " with field set for " + fieldName );
            return;
        }
    }
}
