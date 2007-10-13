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
