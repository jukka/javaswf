package com.anotherbigidea.flash.avm2.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * A set of traits.
 *
 * @author nickmain
 */
public class AVM2Traits {

    /** The traits. Read-only map, keyed by trait name. */
    public final Collection<AVM2Trait> traits;
    private final Collection<AVM2Trait> traits_internal = new ArrayList<AVM2Trait>();
    { traits = Collections.unmodifiableCollection( traits_internal ); }
    
    /**
     * Add or replace a constant value.
     * 
     * @param name the slot name
     * @param type the type
     * @param value the default value;
     * @return the new slot
     */
    public AVM2Slot addConst( AVM2QName name, AVM2Name type, AVM2DefaultValue value ) {
        AVM2Slot slot = new AVM2Slot( name, true, type, value );
        traits_internal.add( slot );
        return slot;
    }
    
    /**
     * Add or replace a var value.
     * 
     * @param name the slot name
     * @param type the type
     * @param value the default value;
     * @return the new slot
     */
    public AVM2Slot addVar( AVM2QName name, AVM2Name type, AVM2DefaultValue value ) {
        AVM2Slot slot = new AVM2Slot( name, false, type, value );
        traits_internal.add( slot );
        return slot;
    }
 
    /**
     * Add or replace a method
     * @param name the method name
     * @param method the method
     * @param isFinal whether the method is final
     * @param isOverride whether the method is an override
     * @return the new method
     */
    public AVM2MethodSlot addMethod( AVM2QName name, AVM2Method method,
                                     boolean isFinal, boolean isOverride ) {
        
        AVM2MethodSlot m = new AVM2MethodSlot( name, method, isFinal, isOverride );
        traits_internal.add( m );
        return m;
    }

    /**
     * Add or replace a getter
     * @param name the method name
     * @param method the method
     * @param isFinal whether the method is final
     * @param isOverride whether the method is an override
     * @return the new method
     */
    public AVM2Getter addGetter( AVM2QName name, AVM2Method method,
                                 boolean isFinal, boolean isOverride ) {
        
        AVM2Getter m = new AVM2Getter( name, method, isFinal, isOverride );
        traits_internal.add( m );
        return m;
    }

    /**
     * Add or replace a setter
     * @param name the method name
     * @param method the method
     * @param isFinal whether the method is final
     * @param isOverride whether the method is an override
     * @return the new method
     */
    public AVM2Setter addSetter( AVM2QName name, AVM2Method method,
                                 boolean isFinal, boolean isOverride ) {
        
        AVM2Setter m = new AVM2Setter( name, method, isFinal, isOverride );
        traits_internal.add( m );
        return m;
    }

    /**
     * Add or replace a class slot.
     * @param name the slot name
     * @param className the referenced class - must be a class in the ABC file
     * @return the new slot
     */
    public AVM2ClassSlot addClass( AVM2QName name, AVM2QName className ) {
        AVM2ClassSlot cs = new AVM2ClassSlot( name, className );
        traits_internal.add( cs );
        return cs;
    }
    
    /**
     * Add or replace a function
     * @param name the function slot name
     * @param method the function
     * @return the new slot
     */
    public AVM2FunctionSlot addFunction( AVM2QName name, AVM2Method method ) {
        
        AVM2FunctionSlot m = new AVM2FunctionSlot( name, method );
        traits_internal.add( m );
        return m;
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {   
        for( AVM2Trait t : traits ) {
            t.initPool( context );
        }
    }
    
    /**
     * Write.
     */
    public void write( ABC.Traits ts, AVM2ABCFile.WriteContext context ) {
        for( AVM2Trait t : traits ) {
            t.write( ts, context );
        }        
        ts.done();
    }
}
