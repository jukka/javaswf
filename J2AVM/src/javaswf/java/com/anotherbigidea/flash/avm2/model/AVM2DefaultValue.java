package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.MultiName;
import com.anotherbigidea.flash.avm2.Namespace;
import com.anotherbigidea.flash.avm2.ValueKind;

/**
 * The default value for a slot or an optional parameter
 *
 * @author nickmain
 */
public class AVM2DefaultValue {
    
    /** The kind of value */
    public final ValueKind valueKind;
    
    /** The value - must be compatible with the kind */
    public final Object value;
    
    /**
     * @param valueKind the kind of value
     * @param value the value
     */
    public AVM2DefaultValue( ValueKind valueKind, Object value ) {
        this.valueKind = valueKind;
        
        if( valueKind == ValueKind.CONSTANT_Namespace ) value = new AVM2Namespace((Namespace) value );
        if( valueKind == ValueKind.CONSTANT_Multiname ) value = AVM2Name.from((MultiName) value );
        
        this.value     = value;
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        valueKind.dump( out, value );
    }
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {
        valueKind.initPool( context, value );
    }
}