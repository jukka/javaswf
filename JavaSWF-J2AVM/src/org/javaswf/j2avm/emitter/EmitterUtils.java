package org.javaswf.j2avm.emitter;

import org.javaswf.j2avm.TranslationContext;
import org.javaswf.j2avm.abc.TargetABC;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.MethodModel;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.types.ArrayType;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.Signature;
import org.javaswf.j2avm.model.types.VoidType;

import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.AVM2StandardName;
import com.anotherbigidea.flash.avm2.model.AVM2StandardNamespace;

/**
 * Utility methods for the emitter
 *
 * @author nickmain
 */
public class EmitterUtils {

    /**
     * Get the AVM2 QName corresponding to a Java type
     */
    public static AVM2QName qnameForJavaType( JavaType type,
    		                                  TargetABC abc ) {

    	if( type == VoidType.VOID         ) return AVM2StandardName.TypeVoid.qname; 
        if( type == PrimitiveType.BYTE    ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.BOOLEAN ) return AVM2StandardName.TypeBoolean.qname;
        if( type == PrimitiveType.SHORT   ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.CHAR    ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.INT     ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.FLOAT   ) return AVM2StandardName.TypeNumber.qname;
        if( type == PrimitiveType.LONG    ) return AVM2StandardName.TypeInt.qname;
        if( type == PrimitiveType.DOUBLE  ) return AVM2StandardName.TypeNumber.qname;

        if( type instanceof ArrayType  ) {
        	return AVM2StandardName.TypeArray.qname;
        }
        
    	if( type instanceof ObjectType ) {
    		return abc.nameForJavaClass( type.name );
    	}
 
    	return null;
    }
    
    /**
     * Get a qname for a method name
     */
    public static AVM2QName nameForMethod( String name ) {
        return new AVM2QName( AVM2StandardNamespace.EmptyPackage.namespace, name ); 
    }

    /**
     * Get a qname for a field name
     */
    public static AVM2QName nameForField( String name ) {
        return new AVM2QName( AVM2StandardNamespace.EmptyPackage.namespace, name ); 
    }

    /**
     * Get a qname for a private method name
     */
    public static AVM2QName nameForPrivateMethod( String name ) {
        return new AVM2QName( AVM2Namespace.privateNamespace, name ); 
    }

    /**
     * Get a qname for a private field name
     */
    public static AVM2QName nameForPrivateField( String name ) {
        return new AVM2QName( AVM2Namespace.privateNamespace, name ); 
    }

    public static AVM2QName nameForMethod( MethodModel method ) {
    	
    	if( method.flags.contains( MethodFlag.MethodIsProtected ) ) {
    		
    	}
    	
    }
     
    
    
    /**
     * Determine whether a method is an override.
     * 
     * @param jclass the class of the method
     * @param method the method
     * @param context the translation context
     * @return true if the method overrides a super method
     */
    public static boolean isOverride( ClassModel jclass, MethodModel method,
    		                          TranslationContext context ) {

        Signature sig = method.signature;
        
        ClassModel superclass = context.modelForName( jclass.superclass.name );
        while( superclass != null ) {
            for( Signature superSig : superclass.methods.keySet() ) {
                
                if( superSig.equals( sig )) {
                    return true; //found matching method signature
                }
            }
            
            ObjectType supertype = superclass.superclass;
            if( supertype == null ) break;
            superclass = context.modelForName( supertype.name );
        }
        
        return false;
    }
    
    /**
     * Strip the "get", "is", "has", "set" prefix from an accessor method name and
     * return the target field name.
     * @param accessor the accessor method name
     * @return the field name
     */
    public static String nameFromAccessor( String accessor ) {
        
        String name;
        
        if     ( accessor.startsWith( "get" ) ) name = accessor.substring( 3 );
        else if( accessor.startsWith( "is"  ) ) name = accessor.substring( 2 );
        else if( accessor.startsWith( "has" ) ) name = accessor.substring( 3 );
        else if( accessor.startsWith( "set" ) ) name = accessor.substring( 3 );
        else name = accessor;
        
        name = name.substring( 0, 1 ).toLowerCase() + name.substring( 1 );
        return name;
    }
}
