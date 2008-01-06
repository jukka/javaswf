package com.anotherbigidea.flash.avm2.model;

import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.ABC;

/**
 * A const or var.
 *
 * @author nickmain
 */
public class AVM2Slot extends AVM2Trait {

    /** Whether a constant (true) or a var */
    public final boolean isConstant;
    
    /** The slot type - null means Object */
    public final AVM2Name type;
    
    /** The default value - may be null */
    public AVM2DefaultValue value;
    
    /*pkg*/ AVM2Slot(AVM2QName name, boolean isConstant, AVM2Name type, AVM2DefaultValue value ) {
        super(name);
        
        this.isConstant = isConstant;
        this.type       = type;
        this.value      = value;
    }
    
    protected void dumpEx( IndentingPrintWriter out ) {
        if( isConstant ) out.print( "const : " );
        else             out.print( "var : " );
        
        if( type == null ) out.print( "*" );
        else type.dump( out );
        
        if( value != null ) {
            out.print( " = " );
            value.dump( out );
        }
        
        out.println();
    }
    
    protected void initPoolEx( AVM2ABCFile.WriteContext context ) {
        if( type  != null ) type.initPool( context );
        if( value != null ) value.initPool( context );        
    }
    
    /**
     * Write.
     */
    public void write( ABC.Traits ts, AVM2ABCFile.WriteContext context ) {
        
        int nameIndex = name.indexIn( context.pool );
        int slotId    = indexId + 1;
        int typeIndex = (type == null) ? 0 : type.indexIn( context.pool );
                
        int valueIndex = 0;
        int valueKind  = 0;
        
        if( value != null ) {
            valueKind  = value.valueKind.value;
            valueIndex = value.valueKind.poolIndex( context, value.value );
        }
        
        if( isConstant ) {
            ts.constant( nameIndex, slotId, typeIndex, valueIndex, valueKind, metadataIndices( context ) );
        } else {
            ts.slot( nameIndex, slotId, typeIndex, valueIndex, valueKind, metadataIndices( context ) );
        }
    }
}
