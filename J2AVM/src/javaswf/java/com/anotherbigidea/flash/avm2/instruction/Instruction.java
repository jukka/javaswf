package com.anotherbigidea.flash.avm2.instruction;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

import org.epistem.code.LocalValue;
import org.epistem.io.InStream;
import org.epistem.io.IndentingPrintWriter;
import org.epistem.io.OutStream;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.ArgType;
import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.instruction.AVM2CodeAnalyzer.Frame;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;

/**
 * Base for AVM2 instructions.
 *
 * @author nickmain
 */
public class Instruction {

    /*pkg*/ InstructionList instructionList;
    /*pkg*/ Instruction prevInstruction;
    /*pkg*/ Instruction nextInstruction;
    
    private int offset = -1;
    
    /** The instruction operation */
    public final Operation operation;
    
    /** The instruction arguments */
    public final Object[]  arguments;

    /**
     * Get the previous instruction
     * @return null if this is the first instruction
     */
    public Instruction prev() { return prevInstruction; }
    
    /**
     * Get the next instruction
     * @return null if this is the last instruction
     */
    public Instruction next() { return nextInstruction; }
    
    /**
     * @param operation the instruction operation
     * @param arguments the arguments - may be null
     */
    public Instruction( Operation operation, Object... arguments ) {
        this.operation = operation;
        this.arguments = (arguments != null) ? arguments : new Object[0];
    }

    /**
     * Get the instruction offset (address)
     * @return -1 if the offset is unknown
     */
    public int getOffset() { return offset; }
    
    /**
     * Set the offset
     */
    public void setOffset( int offset ) { this.offset = offset; }

    /** 
     * Get the set of targeted instruction addresses.
     */
    public Set<Integer> getTargetAddresses() {
        if( operation == null ) return Collections.EMPTY_SET;
        Set<Integer> targets = new HashSet<Integer>();
        
        for (int i = 0; i < arguments.length; i++) {
            ArgType argType = operation.argTypes[i];
            Object  arg     = arguments[i];
            
            if( argType.isOffset() ) {
                if( arg instanceof int[] ) {
                    int[] offsets = (int[]) arg;
                    for (int j = 0; j < offsets.length; j++) {
                        targets.add( offsets[j] );
                    }
                } 
                else if( arg instanceof Integer ) {
                    int targetOffset = ((Integer) arg).intValue();
                    
                    targets.add( targetOffset );
                }
            }
        }
        
        return targets;
    }
    
    /**
     * Get the set of instructions that follow this one in normal execution
     * flow
     */
    public Set<Instruction> getFollowOnInstructions() {
        Set<Instruction> ins = new HashSet<Instruction>();
        
        //gather branch targets
        Set<Integer> targets = getTargetAddresses();
        for( Integer targ : targets ) {
            ins.add( instructionList.targets.get( targ ));
        }
        
        //add next
        if( ! operation.isAbruptFlow() ) {
            ins.add( nextInstruction );
        }
        
        return ins;
    }
    
    /**
     * Pretty print the instruction
     */
    public void print( IndentingPrintWriter out ) {
        if( offset >= 0 ) {
            out.writeField( offset + ":", 4, true );
            out.write( " " );
            
            if( operation == null ) out.write( "???" );
            else out.write( operation.mnemonic );
            
            for( int i = 0; i < arguments.length; i++ ) {
                out.write( " " );
                out.print( arguments[i] );
            }
            
            out.println();
        }
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.write( operation.mnemonic );
        
        for( int i = 0; i < arguments.length; i++ ) {
            out.write( " " );
            
            Object arg = arguments[i];
            if( arg == null ) continue;
            
            //call the dump method
            try {
                Method m = arg.getClass().getMethod( "dump", IndentingPrintWriter.class );
                m.invoke( arg, out );
                
            } catch( Exception ex) {
                
                if( arg instanceof int[] ) {
                    int[] ii = (int[]) arg;
                    out.println();
                    out.indent();
                    
                    for( int j = 0; j < ii.length; j++ ) {
                        out.writeField( ""+j, 3, false );
                        out.println( " --> " + ii[j] );
                    }
                    
                    out.unindent();
                    out.print( "}" );
                    continue;
                }
                
                //write the arg directly
                if( arg instanceof String ) out.writeDoubleQuotedString( (String ) arg );
                else out.print( arg );
            }
        }
        
        out.println();        
    }
    
    /**
     * Get the number of scope stack slots popped by this instruction - 0 or 1
     */
    public int getScopePopCount() {
        return operation.scopePopCount;
    }
    
    /**
     * Get the number of scope stack slots pushed by ths instruction - 0 or 1
     */
    public int getScopePushCount() {
        return operation.scopePushCount;
    }
    
    /**
     * Get the number of stack slots popped by this instruction
     */
    public int getPopCount() {
        int count = operation.popCount;
        
        //check the arguments for implication of extra values
        ArgType[] argTypes = operation.argTypes;
        for( int i = 0; i < argTypes.length; i++ ) {
            count += argTypes[i].extraStackPops( arguments[i] );
        }     
        
        return count;
    }
    
    /**
     * Get the number of stack slots pushed by ths instruction
     */
    public int getPushCount() {
        return operation.pushCount;
    }
    
    /**
     * Get the registers referenced/used by this instruction
     * 
     * @return zero-length array if this instruction does not use a register
     */
    public int[] registersUsed() {
        
        int register;
        
        switch( operation ) {
            case OP_setlocal0: register = 0; break;
            case OP_setlocal1: register = 1; break;
            case OP_setlocal2: register = 2; break;
            case OP_setlocal3: register = 3; break;
            case OP_getlocal0: register = 0; break;
            case OP_getlocal1: register = 1; break;
            case OP_getlocal2: register = 2; break;
            case OP_getlocal3: register = 3; break;

            case OP_setlocal:
            case OP_kill:
            case OP_inclocal:
            case OP_inclocal_i:
            case OP_getlocal:
            case OP_declocal:
            case OP_declocal_i:
                return new int[] { (Integer) arguments[0] };

            case OP_hasnext2:
                return new int[] { (Integer) arguments[0], (Integer) arguments[1] };
            
            default: return new int[0];
        }
        
        return new int[] { register };
    }
    
    /**
     * Write the instruction as bytecode data 
     */
    public void write( OutStream out ) throws IOException {
        out.writeUI8( operation.opcode );
        
        ArgType[] argTypes = operation.argTypes;
        for (int i = 0; i < argTypes.length; i++) {
            argTypes[i].write( out, arguments[i] );
        }        
    }
    
    /** cache of Instruction class by opcode */
    private static Map<Operation, Constructor<? extends Instruction>> classCache = 
        new EnumMap<Operation, Constructor<? extends Instruction>>( Operation.class );
    
    /**
     * Parse and return a new Instruction.  An Instruction sub-type is returned
     * appropriate to the given operation, if one exists - otherwise a 
     * generic Instruction is returned.
     * 
     * @param in the bytecode data, positioned at the start of an instruction.
     *           The data stream must consists only of bytecode data, that is,
     *           the first instruction must have been at the head of the stream.
     * @return the new instruction
     * @throws IOException if an IO problem occurs or if the opcode is unknown
     */
    public static Instruction parse( InStream in ) throws IOException {
        int offset = (int) in.getBytesRead();
        int opcode = in.readUI8();
        
        Operation op = Operation.fromOpcode( opcode );
        if( op == null ) {
            throw new IOException( "Unknown opcode: 0x" + Integer.toHexString( opcode ) );
        }
        
        Constructor<? extends Instruction> cons = classCache.get( op );
        if( cons == null ) {
            String className = Instruction.class.getName() + "_" + op.mnemonic;
            
            Class<? extends Instruction> clazz = null;
            try {
                clazz = (Class<? extends Instruction>) Class.forName( className );
            } catch( ClassNotFoundException ignored ) { }
            
            if( clazz == null || ! Instruction.class.isAssignableFrom( clazz ) ) {
                clazz = Instruction.class;
            }
            
            try {
                cons = (Constructor<? extends Instruction>) 
                    clazz.getConstructor( new Class[] { Operation.class, Object[].class } );
            } catch( Exception ex ) {
                throw new RuntimeException( ex );
            }
                
            classCache.put( op, cons );
        }
        
        ArgType[] argTypes = op.argTypes;
        Object[]  args = new Object[ (argTypes != null) ? argTypes.length : 0 ];
        for( int i = 0; i < args.length; i++ ) {
            args[i] = argTypes[i].parse( in );
        }
        
        try {
            Instruction instr = cons.newInstance( new Object[] { op, args } );
            instr.setOffset( offset );         
            instr.convertOffsetsToAddresses();
            return instr;
            
        } catch( Exception ex ) {
            throw new RuntimeException( ex );
        }
    }
    
    /**
     * Convert the target offsets to addresses.
     *
     */
    private void convertOffsetsToAddresses() {
        for (int i = 0; i < arguments.length; i++) {
            ArgType argType = operation.argTypes[i];
            Object  arg     = arguments[i];
            
            if( argType.isOffset() ) {
                if( arg instanceof int[] ) {
                    int[] offsets = (int[]) arg;
                    for (int j = 0; j < offsets.length; j++) {
//                        offsets[j] = argType.offsetAdjustment() + offsets[j];
                        offsets[j] = offset + argType.offsetAdjustment() + offsets[j];
                    }
                } 
                else if( arg instanceof Integer ) {
                    int targetOffset = ((Integer) arg).intValue();
                    
//                    arguments[i] = new Integer( argType.offsetAdjustment() + targetOffset );
                    arguments[i] = new Integer( offset + argType.offsetAdjustment() + targetOffset );
                }
            }
        }
    }
    
    /**
     * Clear the instruction - clear the list and prev/next references and
     * any InstructionTargets
     */
    protected void clear() {
        instructionList = null;
        prevInstruction = null;
        nextInstruction = null;
    }
    
    /**
     * Get the locals referenced by the instruction
     * 
     * @param locals the collection to add the locals to
     */
    public void gatherReferencedLocals( Collection<LocalValue<Instruction>> locals ) {
        for( Object arg : arguments ) {
            if( arg instanceof LocalValue ) {
                
                @SuppressWarnings( "unchecked" )
                LocalValue<Instruction> local = (LocalValue<Instruction>) arg; 

                //--setters
                switch( operation ) {
                    case OP_declocal :  
                    case OP_declocal_i : 
                    case OP_hasnext2 :  
                    case OP_inclocal :  
                    case OP_inclocal_i :  
                    case OP_kill :  
                    case OP_setlocal :  
                    case OP_setlocal0 :  
                    case OP_setlocal1 :  
                    case OP_setlocal2 :  
                    case OP_setlocal3 : 
                        local.setters.add( this );
                        break;
                }                

                //--getters
                switch( operation ) {
                    case OP_declocal :  
                    case OP_declocal_i : 
                    case OP_hasnext2 :  
                    case OP_inclocal :  
                    case OP_inclocal_i :  
                    case OP_getlocal :  
                    case OP_getlocal0 :  
                    case OP_getlocal1 :  
                    case OP_getlocal2 :  
                    case OP_getlocal3 :  
                        local.getters.add( this );
                        break;
                }                
                
                locals.add( local );
            }
        }        
    }   
    
    /**
     * Initialize a write-context
     */
    public void initPool( AVM2ABCFile.WriteContext context ) {
        
        for( int i = 0; i < operation.argTypes.length; i++ ) {
            ArgType argType = operation.argTypes[i];
            Object  arg     = arguments[i];
            argType.initPool( context, arg );
        }
    }
    
    /**
     * Write the instruction by calling the corresponding method of the
     * Instruction interface.  The arguments are first converted into
     * pooled versions.
     */
    public void write( ABC.Instructions instrs, AVM2ABCFile.WriteContext context ) {
        
        Object[] args = new Object[ arguments.length ];
        for( int i = 0; i < args.length; i++ ) {
            args[i] = operation.argTypes[i].index( context, arguments[i] );
        }
        
        write( instrs, args );
    }    
    
    /**
     * Write the instruction by calling the corresponding method of the
     * Instruction interface.  The arguments are assumed to be the raw
     * pooled versions.
     */
    public void write( ABC.Instructions instrs ) {
        write( instrs, arguments );
    }
        
    private void write( ABC.Instructions instrs, Object[] arguments ) {
        
        //Parts of the following code were generated from a template
        
        switch( operation ) {
            case OP_add : instrs.add( ); break;
            case OP_add_i : instrs.add_i( ); break;
            case OP_astype : instrs.astype( (Integer) arguments[0] ); break;
            case OP_astypelate : instrs.astypelate( ); break;
            case OP_bitand : instrs.bitand( ); break;
            case OP_bitnot : instrs.bitnot( ); break;
            case OP_bitor : instrs.bitor( ); break;
            case OP_bitxor : instrs.bitxor( ); break;
            case OP_bkpt : instrs.bkpt( ); break;
            case OP_bkptline : instrs.bkptline( (Integer) arguments[0] ); break;
            case OP_call : instrs.call( (Integer) arguments[0] ); break;
            case OP_callmethod : instrs.callmethod( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_callproperty : instrs.callproperty( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_callproplex : instrs.callproplex( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_callpropvoid : instrs.callpropvoid( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_callstatic : instrs.callstatic( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_callsuper : instrs.callsuper( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_callsupervoid : instrs.callsupervoid( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_checkfilter : instrs.checkfilter( ); break;
            case OP_coerce : instrs.coerce( (Integer) arguments[0] ); break;
            case OP_coerce_a : instrs.coerce_a( ); break;
            case OP_coerce_b : instrs.coerce_b( ); break;
            case OP_coerce_d : instrs.coerce_d( ); break;
            case OP_coerce_i : instrs.coerce_i( ); break;
            case OP_coerce_o : instrs.coerce_o( ); break;
            case OP_coerce_s : instrs.coerce_s( ); break;
            case OP_coerce_u : instrs.coerce_u( ); break;
            case OP_construct : instrs.construct( (Integer) arguments[0] ); break;
            case OP_constructprop : instrs.constructprop( (Integer) arguments[0], (Integer) arguments[1] ); break;
            case OP_constructsuper : instrs.constructsuper( (Integer) arguments[0] ); break;
            case OP_convert_b : instrs.convert_b( ); break;
            case OP_convert_d : instrs.convert_d( ); break;
            case OP_convert_i : instrs.convert_i( ); break;
            case OP_convert_o : instrs.convert_o( ); break;
            case OP_convert_s : instrs.convert_s( ); break;
            case OP_convert_u : instrs.convert_u( ); break;
            case OP_debug : instrs.debug( ); break;
            case OP_debugfile : instrs.debugfile( (Integer) arguments[0] ); break;
            case OP_debugline : instrs.debugline( (Integer) arguments[0] ); break;
            case OP_declocal : instrs.declocal( reg( arguments[0]) ); break;
            case OP_declocal_i : instrs.declocal_i( reg( arguments[0]) ); break;
            case OP_decrement : instrs.decrement( ); break;
            case OP_decrement_i : instrs.decrement_i( ); break;
            case OP_deleteproperty : instrs.deleteproperty( (Integer) arguments[0] ); break;
            case OP_divide : instrs.divide( ); break;
            case OP_dup : instrs.dup( ); break;
            case OP_dxns : instrs.dxns( (Integer) arguments[0] ); break;
            case OP_dxnslate : instrs.dxnslate( ); break;
            case OP_equals : instrs.equals( ); break;
            case OP_esc_xattr : instrs.esc_xattr( ); break;
            case OP_esc_xelem : instrs.esc_xelem( ); break;
         // case OP_finddef : instrs.finddef( (Integer) arguments[0] ); break;
            case OP_findproperty : instrs.findproperty( (Integer) arguments[0] ); break;
            case OP_findpropstrict : instrs.findpropstrict( (Integer) arguments[0] ); break;
            case OP_getdescendants : instrs.getdescendants( (Integer) arguments[0] ); break;
            case OP_getglobalscope : instrs.getglobalscope( ); break;
            case OP_getglobalslot : instrs.getglobalslot( (Integer) arguments[0] ); break;
            case OP_getlex : instrs.getlex( (Integer) arguments[0] ); break;
            case OP_getlocal : instrs.getlocal( reg( arguments[0]) ); break;
            case OP_getlocal0 : instrs.getlocal0( ); break;
            case OP_getlocal1 : instrs.getlocal1( ); break;
            case OP_getlocal2 : instrs.getlocal2( ); break;
            case OP_getlocal3 : instrs.getlocal3( ); break;
            case OP_getproperty : instrs.getproperty( (Integer) arguments[0] ); break;
            case OP_getscopeobject : instrs.getscopeobject( (Integer) arguments[0] ); break;
            case OP_getslot : instrs.getslot( (Integer) arguments[0] ); break;
            case OP_getsuper : instrs.getsuper( (Integer) arguments[0] ); break;
            case OP_greaterequals : instrs.greaterequals( ); break;
            case OP_greaterthan : instrs.greaterthan( ); break;
            case OP_hasnext : instrs.hasnext( ); break;
            case OP_hasnext2 : instrs.hasnext2( reg( arguments[0]), reg( arguments[1]) ); break;
            case OP_ifeq : instrs.ifeq( (Integer) arguments[0] ); break;
            case OP_iffalse : instrs.iffalse( (Integer) arguments[0] ); break;
            case OP_ifge : instrs.ifge( (Integer) arguments[0] ); break;
            case OP_ifgt : instrs.ifgt( (Integer) arguments[0] ); break;
            case OP_ifle : instrs.ifle( (Integer) arguments[0] ); break;
            case OP_iflt : instrs.iflt( (Integer) arguments[0] ); break;
            case OP_ifne : instrs.ifne( (Integer) arguments[0] ); break;
            case OP_ifnge : instrs.ifnge( (Integer) arguments[0] ); break;
            case OP_ifngt : instrs.ifngt( (Integer) arguments[0] ); break;
            case OP_ifnle : instrs.ifnle( (Integer) arguments[0] ); break;
            case OP_ifnlt : instrs.ifnlt( (Integer) arguments[0] ); break;
            case OP_ifstricteq : instrs.ifstricteq( (Integer) arguments[0] ); break;
            case OP_ifstrictne : instrs.ifstrictne( (Integer) arguments[0] ); break;
            case OP_iftrue : instrs.iftrue( (Integer) arguments[0] ); break;
            case OP_in : instrs.in( ); break;
            case OP_inclocal : instrs.inclocal( reg( arguments[0]) ); break;
            case OP_inclocal_i : instrs.inclocal_i( reg( arguments[0]) ); break;
            case OP_increment : instrs.increment( ); break;
            case OP_increment_i : instrs.increment_i( ); break;
            case OP_initproperty : instrs.initproperty( (Integer) arguments[0] ); break;
            case OP_instanceof : instrs.instanceof_( ); break;
            case OP_istype : instrs.istype( (Integer) arguments[0] ); break;
            case OP_istypelate : instrs.istypelate( ); break;
            case OP_jump : instrs.jump( (Integer) arguments[0] ); break;
            case OP_kill : instrs.kill( reg( arguments[0]) ); break;
            case OP_label : instrs.label( ); break;
            case OP_lessequals : instrs.lessequals( ); break;
            case OP_lessthan : instrs.lessthan( ); break;
            case OP_lookupswitch : instrs.lookupswitch( (Integer) arguments[0], (int[]) arguments[1] ); break;
            case OP_lshift : instrs.lshift( ); break;
            case OP_modulo : instrs.modulo( ); break;
            case OP_multiply : instrs.multiply( ); break;
            case OP_multiply_i : instrs.multiply_i( ); break;
            case OP_negate : instrs.negate( ); break;
            case OP_negate_i : instrs.negate_i( ); break;
            case OP_newactivation : instrs.newactivation( ); break;
            case OP_newarray : instrs.newarray( (Integer) arguments[0] ); break;
            case OP_newcatch : instrs.newcatch( (Integer) arguments[0] ); break;
            case OP_newclass : instrs.newclass( (Integer) arguments[0] ); break;
            case OP_newfunction : instrs.newfunction( (Integer) arguments[0] ); break;
            case OP_newobject : instrs.newobject( (Integer) arguments[0] ); break;
            case OP_nextname : instrs.nextname( ); break;
            case OP_nextvalue : instrs.nextvalue( ); break;
            case OP_nop : instrs.nop( ); break;
            case OP_not : instrs.not( ); break;
            case OP_pop : instrs.pop( ); break;
            case OP_popscope : instrs.popscope( ); break;
            case OP_pushbyte : instrs.pushbyte( ((Number) arguments[0]).byteValue() ); break;
            case OP_pushdouble : instrs.pushdouble( (Integer) arguments[0] ); break;
            case OP_pushfalse : instrs.pushfalse( ); break;
            case OP_pushint : instrs.pushint( (Integer) arguments[0] ); break;
            case OP_pushnamespace : instrs.pushnamespace( (Integer) arguments[0] ); break;
            case OP_pushnan : instrs.pushnan( ); break;
            case OP_pushnull : instrs.pushnull( ); break;
            case OP_pushscope : instrs.pushscope( ); break;
            case OP_pushshort : instrs.pushshort( (Integer) arguments[0] ); break;
            case OP_pushstring : instrs.pushstring( (Integer) arguments[0] ); break;
            case OP_pushtrue : instrs.pushtrue( ); break;
            case OP_pushuint : instrs.pushuint( (Integer) arguments[0] ); break;
            case OP_pushundefined : instrs.pushundefined( ); break;
            case OP_pushwith : instrs.pushwith( ); break;
            case OP_returnvalue : instrs.returnvalue( ); break;
            case OP_returnvoid : instrs.returnvoid( ); break;
            case OP_rshift : instrs.rshift( ); break;
            case OP_setglobalslot : instrs.setglobalslot( (Integer) arguments[0] ); break;
            case OP_setlocal : instrs.setlocal( reg( arguments[0]) ); break;
            case OP_setlocal0 : instrs.setlocal0( ); break;
            case OP_setlocal1 : instrs.setlocal1( ); break;
            case OP_setlocal2 : instrs.setlocal2( ); break;
            case OP_setlocal3 : instrs.setlocal3( ); break;
            case OP_setproperty : instrs.setproperty( (Integer) arguments[0] ); break;
            case OP_setslot : instrs.setslot( (Integer) arguments[0] ); break;
            case OP_setsuper : instrs.setsuper( (Integer) arguments[0] ); break;
            case OP_strictequals : instrs.strictequals( ); break;
            case OP_subtract : instrs.subtract( ); break;
            case OP_subtract_i : instrs.subtract_i( ); break;
            case OP_swap : instrs.swap( ); break;
            case OP_throw : instrs.throw_( ); break;
            case OP_timestamp : instrs.timestamp( ); break;
            case OP_typeof : instrs.typeof( ); break;
            case OP_urshift : instrs.urshift( ); break;        
            default: throw new RuntimeException( "Unknown operation " + operation );        
        }
    }
    
    /**
     * Unwrap a register allocation
     */
    private static int reg( Object arg ) {
        if( arg instanceof Number ) return ((Number) arg).intValue();
        if( arg instanceof LocalValue ) return ((LocalValue<Instruction>) arg).allocatedRegister;        
        throw new RuntimeException( "Register arg must be number or allocation" );
    }
}
