package org.javaswf.j2avm.abc;

import static org.javaswf.j2avm.emitter.EmitterUtils.isOverride;

import java.util.EnumSet;
import java.util.Set;

import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.types.ValueType;

import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.model.AVM2Method;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;
import com.anotherbigidea.flash.avm2.model.AVM2MethodSlot;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.AVM2StandardNamespace;

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
	/*pkg*/ final ClassTranslation clazz;
	
	/*pkg*/ AVM2Method avmMethod;
	
	/**
	 * The method
	 */
	/*pkg*/ final MethodModel method;
	
	public MethodTranslation( ClassTranslation clazz, MethodModel method ) {
		this.method = method;
		this.clazz  = clazz;
		
		generateName();
	}
	
	/**
	 * Get the qualified name for the method
	 */
	private AVM2QName getMethodName() {
		return methodName;
	}
	
	private AVM2QName methodName;
	
	/**
	 * Generate the method name
	 */
	private void generateName() {
		AVM2Namespace namespace;
		
		switch( method.visibility() ) {
			case PUBLIC:
				namespace = AVM2StandardNamespace.EmptyPackage.namespace;
				name      = method.signature.name;
				
			case PRIVATE:
			case PROTECTED:
			case PACKAGE:
		}
		
		new AVM2QName( namespace, name );
	}
    
	private String generateNameMangle() {
		//FIXME:
	}
	
    /**
     * Translate a constructor
     */
    private void translateConstructor( MethodModel constructor ) {
        
        Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );
        
        avmMethod = new AVM2Method( null, flags );
        clazz.avm2Class.constructor = avmMethod;
        
        AVM2MethodBody body = avmMethod.methodBody;
        body.scopeDepth     = clazz.classScopeDepth + 1;
        
        //FIXME: ...
    }
    
    /**
     * Translate a method or constructor
     */
    private void translateMethod( MethodModel method ) {
        
        clazz.abc.context.debug( "*** AVM2ClassEmitter - Visiting method " 
                                 + method.signature.toString() );
        
        String methodName = method.signature.name;
        
        if( methodName.equals( MethodModel.CONSTRUCTOR_NAME )) {
            translateConstructor( method );
        }
        else if( methodName.equals( MethodModel.STATIC_INIT_NAME )) {
            translateStaticInit( method );
        } 
        else if( method.flags.contains( MethodFlag.MethodIsStatic ) )  {
        	//FIXME: static methods
        }
        else {
            translateMemberMethod( method );
        }
        
        //FIXME: Update stack size / scope depth ?
    }

    
    /**
     * Translate the static initializer
     */
    private void translateStaticInit( MethodModel clinit ) {
        //FIXME: translateStaticInit
    }

    /**
     * Translate a member method
     */
    private void translateMemberMethod( MethodModel method ) {
        
        Set<MethodInfoFlags> flags = EnumSet.noneOf( MethodInfoFlags.class );

        //determine the method visibility and get the corresponding namespace
        AVM2Namespace methodNamespace = 
        	method.flags.contains( MethodFlag.MethodIsPrivate ) ?
                AVM2Namespace.privateNamespace :
                AVM2StandardNamespace.EmptyPackage.namespace;
        
        AVM2QName  name       = new AVM2QName( methodNamespace, method.signature.name );                
        AVM2QName  returnType = ClassTranslation.qnameForJavaType( method.returnType );
        AVM2Method amv2method = new AVM2Method( returnType, flags );
        
        //add the parameters
        ValueType[] paramTypes = method.signature.paramTypes;
        for( ValueType paramType : paramTypes ) {            
        	amv2method.addParameter( 
        	    "", ClassTranslation.qnameForJavaType( paramType ), null );
        }
        
        AVM2MethodSlot ms = clazz.avm2Class.traits.addMethod( 
               name, 
               amv2method, 
               method.flags.contains( MethodFlag.MethodIsFinal ),
               isOverride( clazz.javaClass, method, clazz.abc.context ) );
        
        AVM2MethodBody body = amv2method.methodBody;
        body.scopeDepth = clazz.classScopeDepth;
        
        //instance methods have a larger scope depth due to the instance object
        if( ! method.flags.contains( MethodFlag.MethodIsStatic )) body.scopeDepth++;

        translateCode( body );
        
        //FIXME: ???
    }
    
    /**
     * Translate Java bytecode to AVM2 bytecode
     */
    private void translateCode( AVM2MethodBody body ) {
        
        //FIXME: handle abstract methods
        
        AVM2Code code = new AVM2Code( body.instructions );
        code.setupInitialScope();
        
        InstructionVisitor visitor = new InstructionVisitor( this );        
        visitor.walk( clazz.javaClass, method );
                        
        //FIXME: these values need further thought..
        //FIXME: could reduce the stack size if longs or doubles are involved
        CodeAttribute codeAttr = method.code();
        body.maxRegisters = codeAttr.maxLocals;
        body.maxStack     = codeAttr.maxStack;
        body.maxScope     = body.scopeDepth + 1;        
    }
}
