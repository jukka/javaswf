package org.javaswf.j2avm.emitter;

import java.util.List;

import org.javaswf.j2avm.JavaClass;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.AVM2StandardName;

/**
 * Utility methods for the emitter
 *
 * @author nickmain
 */
public class EmitterUtils {

    /**
     * Get the AVM2 QName corresponding to a Java type
     */
    public static AVM2QName qnameForJavaType( Type type ) {

        switch( type.getSort() ) {
            case Type.VOID:    return AVM2StandardName.TypeVoid.qname;
            case Type.BOOLEAN: return AVM2StandardName.TypeBoolean.qname;
            case Type.CHAR:    return AVM2StandardName.TypeInt.qname;
            case Type.BYTE:    return AVM2StandardName.TypeInt.qname;
            case Type.SHORT:   return AVM2StandardName.TypeInt.qname;
            case Type.INT:     return AVM2StandardName.TypeInt.qname;
            case Type.FLOAT:   return AVM2StandardName.TypeNumber.qname;
            case Type.LONG:    return AVM2StandardName.TypeInt.qname;
            case Type.DOUBLE:  return AVM2StandardName.TypeNumber.qname;
            case Type.ARRAY:   return AVM2StandardName.TypeArray.qname;
            case Type.OBJECT:  return AVM2StandardName.TypeObject.qname;
            default:  return null;
        }
    }
    
    /**
     * Determine whether a method is an override.
     * 
     * @param jclass the class of the method
     * @param method the method
     * @return true if the method overrides a super method
     */
    public static boolean isOverride( JavaClass jclass, MethodNode method ) {

        String desc = method.desc;
        String name = method.name;
        
        JavaClass superclass = jclass.superclass;
        while( superclass != null ) {
            @SuppressWarnings("unchecked")
            List<MethodNode> superMethods = (List<MethodNode>) superclass.node.methods;
            for( MethodNode superMethod : superMethods ) {
                
                if( superMethod.name.equals( name )
                 && superMethod.desc.equals( desc )) {
                    return true; //found matching method name and sig
                }
            }
        }
        
        return false;
    }
}
