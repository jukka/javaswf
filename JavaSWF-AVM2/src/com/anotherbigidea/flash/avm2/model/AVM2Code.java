package com.anotherbigidea.flash.avm2.model;

import static com.anotherbigidea.flash.avm2.Operation.*;

import java.util.*;

import org.epistem.code.LocalValue;
import org.epistem.io.IndentingPrintWriter;

import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.instruction.AVM2CodeAnalyzer;
import com.anotherbigidea.flash.avm2.instruction.Instruction;
import com.anotherbigidea.flash.avm2.instruction.InstructionList;


/**
 * Wrapper around an instruction list to manage scope, stack and register
 * counts and capture some common AVM2 idioms.
 *
 * @author nickmain
 */
public final class AVM2Code {

    /**
     * Represents the construction of a try-catch-finally handler
     */
    public class ExceptionHandler {
        private final String tryStart;
        private String tryEnd;
        private String handlerStart;

        /**
         * @param startLabel the start of the try-block
         */
        ExceptionHandler( String startLabel ) {
            this.tryStart = startLabel;
        }
        
        
    }
    
	private final AVM2MethodBody body;
	private final InstructionList instructions;
	private Map<String, Integer> labelInts;
	private int labelIndex = 0;

	private final Set<Integer> reservedRegisters;
	
	/**
	 * The local value representing the "this" value
	 */
	public final LocalValue<Instruction> thisValue = new LocalValue<Instruction>(0);
	
	/**
	 * The activation object for local named vars.
	 */
	public LocalValue<Instruction> activationValue = thisValue;
	
	/**
	 * The local values representing the function parameters
	 */
	public final LocalValue<Instruction>[] paramValues;
	
    /**
     * The local value representing the "arguments" value
     */
    public final LocalValue<Instruction> argumentsValue;

	/**
     * @param body the method/script body to wrap
     * @param reservedRegisters the set of registers to reserve
	 * @param paramCount the number of parameters
	 * @param hasArguments whether there is an "arguments" value
	 */
	public AVM2Code( AVM2MethodBody body, Set<Integer> reservedRegisters, 
	                 int paramCount, boolean hasArguments ) {
		this.body              = body;
		this.instructions      = body.instructions;
		this.reservedRegisters = (reservedRegisters != null) ? 
		                             reservedRegisters :
		                             new HashSet<Integer>();
		this.reservedRegisters.add( 0 );
		
		paramValues = new LocalValue[ paramCount ];
		for( int i = 0; i < paramValues.length; i++ ) {
            paramValues[i] = new LocalValue<Instruction>( i + 1 );
            this.reservedRegisters.add( i + 1 );
        }
		
		if( hasArguments ) {
		    argumentsValue = new LocalValue<Instruction>( paramCount + 1 );
		    this.reservedRegisters.add( paramCount + 1 );
		}
		else {
		    argumentsValue = null;
		}
	}

    /**
     * Reserve register zero, no parameters, no arguments
     * 
     * @param body the method/script body to wrap
     */
    public AVM2Code( AVM2MethodBody body ) {
        this( body, null, 0, false );
    }

    /**
     * Get the base scope depth for the code body
     */
    public int scopeDepth() {
        return body.scopeDepth;
    }
    
    /**
     * Dump the code.
     */
    public void dump( IndentingPrintWriter ipw ) {
        body.dump( ipw );
    }
    
	/**
	 * Calculate and set the max-registers, max-stack, and max-scope for the
	 * method body, given the current instructions
	 */
	public void analyze() {
	    new AVM2CodeAnalyzer( body ).analyze( reservedRegisters );
	}
	
	/**
	 * Start a try-catch-finally construct.  The construct must be terminated
	 * by a call to tryEnd().  There must be at least one call to
	 * catchException() or finallyStart() in the body of the construct.
	 * 
	 * These constructs must be properly nested - a nested construct must
	 * be terminated before any call to catchException() or finallyStart()
	 * that relates to this construct and a construct nested in a catch or
	 * finally block must also be terminated before the end of that block.
	 */
	public ExceptionHandler tryStart() {
	    //FIXME
	    String label = newLabel();
	    label();
	    target( label );
	    
	    return new ExceptionHandler( label );
	}
	
	/**
	 * 
	 */
	public void catchException() {
	    //FIXME
	}
	
	/**
	 * 
	 */
	public void finallyStart() {
	    //FIXME
	}
	
	public void tryEnd(  ) {
	    
	}
	
	/**
	 * Append a branch target to the list
	 */
	public void target( int label ) {
	    instructions.appendTarget( label );
	}
	
	/**
     * Append a branch target to the list
     */
    public void target( String label ) {
        target( stringToIntLabel( label ) );
    }
	
	/**
	 * Add a label instruction that allows the AVM2 verifier to accept the
	 * followning code as non-dead.
	 */
	public void label() {
	    instructions.append( OP_label );
	}
	
	/**
	 * An unconditional branch
	 */
	public void jump( int label ) {
	    instructions.append( OP_jump, label );
	}

	private int stringToIntLabel( String label ) {
        if( labelInts == null ) {
            labelInts = new HashMap<String, Integer>();
        }

        Integer intLabel = labelInts.get( label );
        if( intLabel == null ) {
            intLabel = labelInts.size() + Short.MAX_VALUE;  //offset to avoid collisions with int labels
            labelInts.put( label, intLabel );
        }
        
        return intLabel;	    
	}
	
	/**
	 * Generate a new unique label - does not actually place it in the code.
	 */
	public String newLabel() {	    
	    return "avm2Code::" + (labelIndex++);
	}
	
	/**
	 * An unconditional branch
	 */
	public void jump( String label ) {
	    jump( stringToIntLabel( label ) );
	}
	
	public void ifeq      ( int label ) { instructions.append( OP_ifeq      , label ); }      
	public void iffalse   ( int label ) { instructions.append( OP_iffalse   , label ); }   
	public void ifge      ( int label ) { instructions.append( OP_ifge      , label ); }      
	public void ifgt      ( int label ) { instructions.append( OP_ifgt      , label ); }      
	public void ifle      ( int label ) { instructions.append( OP_ifle      , label ); }      
	public void iflt      ( int label ) { instructions.append( OP_iflt      , label ); }      
	public void ifne      ( int label ) { instructions.append( OP_ifne      , label ); }      
	public void ifnge     ( int label ) { instructions.append( OP_ifnge     , label ); }     
	public void ifngt     ( int label ) { instructions.append( OP_ifngt     , label ); }     
	public void ifnle     ( int label ) { instructions.append( OP_ifnle     , label ); }     
	public void ifnlt     ( int label ) { instructions.append( OP_ifnlt     , label ); }     
	public void ifstricteq( int label ) { instructions.append( OP_ifstricteq, label ); }
	public void ifstrictne( int label ) { instructions.append( OP_ifstrictne, label ); }
	public void iftrue    ( int label ) { instructions.append( OP_iftrue    , label ); } 
	
    public void ifeq      ( String label ) { ifeq      ( stringToIntLabel( label )); }      
    public void iffalse   ( String label ) { iffalse   ( stringToIntLabel( label )); }   
    public void ifge      ( String label ) { ifge      ( stringToIntLabel( label )); }      
    public void ifgt      ( String label ) { ifgt      ( stringToIntLabel( label )); }      
    public void ifle      ( String label ) { ifle      ( stringToIntLabel( label )); }      
    public void iflt      ( String label ) { iflt      ( stringToIntLabel( label )); }      
    public void ifne      ( String label ) { ifne      ( stringToIntLabel( label )); }      
    public void ifnge     ( String label ) { ifnge     ( stringToIntLabel( label )); }     
    public void ifngt     ( String label ) { ifngt     ( stringToIntLabel( label )); }     
    public void ifnle     ( String label ) { ifnle     ( stringToIntLabel( label )); }     
    public void ifnlt     ( String label ) { ifnlt     ( stringToIntLabel( label )); }     
    public void ifstricteq( String label ) { ifstricteq( stringToIntLabel( label )); }
    public void ifstrictne( String label ) { ifstrictne( stringToIntLabel( label )); }
    public void iftrue    ( String label ) { iftrue    ( stringToIntLabel( label )); } 
	
    /**
     * Get a member of an object - if the object is undefined then the
     * value is undefined.  Name then object are on the stack. 
     */
    public void getMember_Safe() {
        
        swap();            // -> object, name
        dup();             // -> object, object, name
        pushUndefined();   // -> undefined, object, object, name
        
        String ifundefined = newLabel();
        ifstricteq( ifundefined );
        
        swap(); // -> name, object
        instructions.append( OP_getproperty, AVM2LateMultiname.EMPTY_PACKAGE );

        String exit = newLabel();
        jump( exit );
        
        target( ifundefined );
        label();
        
        swap(); // -> name, object
        pop();  // -> object (which is undefined)
        
        target( exit );
        label();        
    }
    
    /**
     * Create a new local value instance
     */
    public LocalValue<Instruction> newLocal() {
        return new LocalValue<Instruction>();
    }
    
    /**
     * Set a member of an object - if the object is undefined then the operation
     * does nothing.  Value, name then object are on the stack. 
     */
    public void setMember_Safe() {
        // stack is: value|name|object|..
        
        LocalValue<Instruction> tempReg1 = newLocal();
        LocalValue<Instruction> tempReg2 = newLocal();

        setLocal( tempReg1 );  // -> n|o|..
        setLocal( tempReg2 );  // -> o|..
        dup();                 // -> o|o..
        pushUndefined();       // -> undefined|o|o..
        
        String ifundefined = newLabel();
        ifstricteq( ifundefined );  // -> o|..
        
        getLocal( tempReg2 ); // -> n|o|..
        getLocal( tempReg1 ); // -> v|n|o|..
        
        instructions.append( OP_setproperty, AVM2LateMultiname.EMPTY_PACKAGE );

        String exit = newLabel();
        jump( exit );
        
        target( ifundefined );
        label();        
        pop();  // -> ..
        
        target( exit );
        label();        
    }
    
    /**
     * Get a variable from the current scope stack - name is on the stack, public
     * namespace is assumed
     */
    public void getVariable() {
        
        dup(); //the name
        
        instructions.append( OP_findproperty, AVM2LateMultiname.EMPTY_PACKAGE ); //will push object or global
        swap();   
        instructions.append( OP_getproperty, AVM2LateMultiname.EMPTY_PACKAGE );
    }
    
    /**
     * Set a variable on the activation object - value then name are on the
     * stack, public namespace is assumed.
     */
    public void setLocalVariable() {
        LocalValue<Instruction> tempReg = newLocal();
                
        setLocal( tempReg );   //value,name,.. -> name,...
        getLocal( activationValue ); // -> object,name,...
        swap();                // -> name,object,...
        getLocal( tempReg );   // -> value,name.object,...
        instructions.append( OP_setproperty, AVM2LateMultiname.EMPTY_PACKAGE );
    }
    
    /**
     * Set the "this" variable to point at the item in local register zero
     */
    public void setThis() {
        getLocal( activationValue );
        getLocal( thisValue );
        setProperty( "this" );
    }

    /**
     * Push a "with" object onto the scope stack
     */
    public void pushWith() {
        instructions.append( OP_pushwith );
    }

    /**
     * Kill a local variable
     */
    public void killLocal( LocalValue<Instruction> local ) {
        instructions.append( OP_kill, local );
    }
    
	/**
	 * Trace out a message
	 */
	public void trace( String msg ) {
		callPropStrictVoid( "trace", msg );
	}

	/**
	 * Trace out a message - string is on the stack
	 */
	public void trace() {
        findPropStrict( "trace" );
        swap();
        callPropVoid( "trace", 1 );	
	}

    /**
     * Trace out stack top (dupped), preceded by a message
     */
    public void traceTop( String msg ) {
        dup();
        pushString( msg );
        swap();
        add();        
        findPropStrict( "trace" );
        swap();
        callPropVoid( "trace", 1 ); 
    }	
	
	/**
	 * Increment a local register as an int
	 */
	public void incLocal_i( LocalValue<Instruction> local ) {
	    instructions.append( OP_inclocal_i, local );
	}

    /**
     * Increment a local register as a number
     */
    public void incLocal( LocalValue<Instruction> local ) {
        instructions.append( OP_inclocal, local );
    }

    /**
     * Decrement a local register as an int
     */
    public void decLocal_i( LocalValue<Instruction> local ) {
        instructions.append( OP_declocal_i, local );
    }

    /**
     * Decrement a local register as a number
     */
    public void decLocal( LocalValue<Instruction> local ) {
        instructions.append( OP_declocal, local );
    }

    /**
     * Increment stack top
     */
    public void increment() {
        instructions.append( OP_increment );        
    }

    /**
     * Descrement stack top
     */
    public void decrement() {
        instructions.append( OP_decrement );        
    }
    
	/**
	 * Push the global scope object
	 */
	public void getGlobalScope() {
	    instructions.append( OP_getglobalscope );
	}
	
	/**
	 * Set a property
	 */
	public void setProperty( String name ) {
	    AVM2QName qname = new AVM2QName( name );
	    instructions.append( OP_setproperty, qname );
	}

    /**
     * Set a property
     */
    public void setProperty( AVM2Name name ) {
        instructions.append( OP_setproperty, name );
    }
	
	/**
	 * Get a property
	 */
	public void getProperty( String name ) {
        AVM2QName qname = new AVM2QName( name );
	    instructions.append( OP_getproperty, qname );
	}

	/**
     * Get a property
     */
    public void getProperty( AVM2Name name ) {
        instructions.append( OP_getproperty, name );
    }
	
	/**
	 * Duplicate the top stack value
	 */
	public void dup() {
		instructions.append( OP_dup );
	}	
	
	/**
	 * Swap the top two values on the stack
	 */
	public void swap() {
		instructions.append( OP_swap );
	}
	
	/**
	 * Call a function object.
	 * @param argCount the arg count.
	 */
	public void call( int argCount ) {
	    instructions.append( OP_call, argCount );
	}
	
	/**
	 * Find (strict) the object with the given property and call that property
	 * on it.
	 * 
	 * @param qualifiedName the prop name
	 * @param args the args
	 */
	public void callPropStrictVoid( String qualifiedName, Object...args ) {
        findPropStrict( qualifiedName );
        for( Object arg : args ) push( arg );
        callPropVoid( qualifiedName, args.length );				
	}
	
	/**
	 * Add, with type conversion.
	 */
	public void add() {
	    instructions.append( OP_add );
	}

    /**
     * Add ints, with conversion if required.
     */
    public void addInts() {
        instructions.append( OP_add_i );
    }
	
    /**
     * Subtract, with type conversion.
     */
    public void subtract() {
        instructions.append( OP_subtract );
    }

    /**
     * Subtract ints, with conversion if required.
     */
    public void subtractInts() {
        instructions.append( OP_subtract_i );
    }
    
    /**
     * Multiply, with type conversion.
     */
    public void multiply() {
        instructions.append( OP_multiply );
    }

    /**
     * Multiply ints, with conversion if required.
     */
    public void multiplyInts() {
        instructions.append( OP_multiply_i);
    }
    
    
    /**
     * Divide, with type conversion.
     */
    public void divide() {
        instructions.append( OP_divide );
    }
    
    /**
     * Divide, with type conversion.
     */
    public void modulo() {
        instructions.append( OP_modulo );
    }
    
    /**
     * Compare two values
     */
    public void equals() {
        instructions.append( OP_equals );        
    }

    /**
     * Compare two values, strictly
     */
    public void strictEquals() {
        instructions.append( OP_strictequals );        
    }
    
    /**
     * Compare two values
     */
    public void lessThan() {
        instructions.append( OP_lessthan );        
    }

    /**
     * Compare two values
     */
    public void greaterThan() {
        instructions.append( OP_greaterthan );        
    }
    
    /**
     * Logical AND
     */
    public void and() {
        //AVM1 does not have a logical AND - WTF ?!
        
        //stack is a,b,...
        
        String ifTrue = newLabel();
        iftrue( ifTrue );
        
        //a is false - discard b and push false
        pop();
        pushBoolean( false );
        
        target( ifTrue );
        label();
        convertToBoolean();
        
        //stack now contains either b or false
    }

    /**
     * Logical OR
     */
    public void or() {
        //AVM1 does not have a logical AND - WTF ?!
        
        //stack is a,b,...
        String ifFalse = newLabel();
        iffalse( ifFalse );
        
        //a is true - discard b and push true
        pop();
        pushBoolean( true );
        
        target( ifFalse );
        label();
        convertToBoolean();
        
        //stack now contains either b or true
    }

    /**
     * Logical NOT
     */
    public void not() {
        instructions.append( OP_not );    
    }
    
    /**
     * Check that stack top is not NaN. If it is NaN then trace a message and
     * throw up.
     * 
     * @param message the message to trace.
     */
    public void checkIsNotNan( String message ) {
        String notNan = newLabel();
        
        dup();

        findPropStrict( "isNaN" );
        swap();
        callProperty( "isNaN", 1 ); 
        
        iffalse( notNan );
        
        trace( message );
        pushString( message );
        instructions.append( OP_throw );
        
        target( notNan );
        label();
    }
    
    /**
     * Duplicate and trace the stack top value.
     * 
     * @param message the message prefix
     */
    public void traceValue( String message ) {
        dup();
        pushString( message );
        swap();
        add();
        trace();
    }
    
    public void convertToBoolean() { instructions.append( OP_convert_b ); }
    public void convertToDouble () { instructions.append( OP_convert_d ); }
    public void convertToInt    () { instructions.append( OP_convert_i ); }
    public void convertToObject () { instructions.append( OP_convert_o ); }
    public void convertToString () { instructions.append( OP_convert_s ); }
    public void convertToUInt   () { instructions.append( OP_convert_u ); }
    
	/**
	 * Push a value, according to type
	 */
	public void push( Object obj ) {
		if( obj == null ) {
			pushNull();
		}
		else if( obj instanceof String ) {
			pushString( (String) obj );
		}
		else if( obj instanceof Integer ) {
			pushInt( (Integer) obj );
		}
		else if( obj instanceof Double ) {
			pushDouble( (Double) obj );			
		}
		else if( obj instanceof Boolean ) {
			pushBoolean( (Boolean) obj );			
		}		
		else {
			throw new IllegalArgumentException( "Don't know how to push " + obj.getClass() );
		}
	}

	/**
	 * Coerce stack top to an object
	 */
	public void coerceToObject() {
	    instructions.append( OP_coerce_o );
	}

    /**
     * Coerce stack top to any-type
     */
    public void coerceToAny() {
        instructions.append( OP_coerce_a );
    }
	
    /**
     * Push NaN
     */
    public void pushNaN() {
        instructions.append( OP_pushnan );
    }
	
	/**
	 * Push null
	 */
	public void pushNull() {
		instructions.append( OP_pushnull );
	}

    /**
     * Push undefined
     */
    public void pushUndefined() {
        instructions.append( OP_pushundefined );
    }

	/**
	 * Push a signed int
	 */
	public void pushInt( int value ) {
		Operation op = null;
		if( value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE ) {
			op = OP_pushbyte;
		}
		else if( value <= Short.MAX_VALUE && value >= Short.MIN_VALUE ) {
			op = OP_pushshort;
		}
		else {
			op = OP_pushint;
		}
		
		instructions.append( op, value );
	}

	/**
	 * Push an unsigned int
	 */
	public void pushUInt( int value ) {
		instructions.append( OP_pushuint, value );
	}
	
	/**
	 * Push a double
	 */
	public void pushDouble( double value ) {
		instructions.append( OP_pushdouble, value );
	}

	/**
	 * Push a boolean
	 */
	public void pushBoolean( boolean value ) {
		if( value ) instructions.append( OP_pushtrue );
		else        instructions.append( OP_pushfalse );
	}
	
	/**
	 * Call a property on an object.  There is no return value.
	 * 
	 * @param qualifiedName the prop name
	 * @param argCount the number of arguments
	 */
	public void callPropVoid( String qualifiedName, int argCount ) {
		instructions.append( OP_callpropvoid, new AVM2QName( qualifiedName ), argCount );
	}
	   
    /**
     * Call a property on an object.  There is no return value.
     * 
     * @param qualifiedName the prop name
     * @param argCount the number of arguments
     */
    public void callPropVoid( AVM2QName qualifiedName, int argCount ) {
        instructions.append( OP_callpropvoid, qualifiedName, argCount );
    }
	
    /**
     * Call a property on an object.
     * 
     * @param qualifiedName the prop name
     * @param argCount the number of arguments
     */
    public void callProperty( AVM2QName qualifiedName, int argCount ) {
        instructions.append( OP_callproperty, qualifiedName, argCount );
    }

    /**
     * Call a property on an object.
     * 
     * @param qualifiedName the prop name
     * @param argCount the number of arguments
     */
    public void callProperty( String qualifiedName, int argCount ) {
        instructions.append( OP_callproperty, new AVM2QName( qualifiedName ), argCount );
    }

    /**
     * Call a method with the given ABC file method index
     * @param methodIndex the method index
     * @param argCount the number of arguments
     */
    public void callStatic( int methodIndex, int argCount ) {
        instructions.append( OP_callstatic, methodIndex, argCount );
    }
    
	/**
	 * Push a string
	 */
	public void pushString( String s ) {
		instructions.append( OP_pushstring, s );
	}
	
	/**
	 * Find and push the object with the given named property.
	 */
	public void findPropStrict( String qualifiedName ) {
		instructions.append( OP_findpropstrict, new AVM2QName( qualifiedName ) );
	}

    /**
     * Find and push the object with the given named property.
     */
    public void findPropStrict( AVM2Name name ) {
        instructions.append( OP_findpropstrict, name );
    }
	
    /**
     * Find and push the object with the given named property (or global).
     */
    public void findProperty( String qualifiedName ) {
        instructions.append( OP_findproperty, new AVM2QName( qualifiedName ) );
    }

    /**
     * Find and push the object with the given named property (or global).
     */
    public void findProperty( AVM2Name name ) {
        instructions.append( OP_findproperty, name );
    }
    
	/**
	 * Set up the initial scope for a script or method
	 */
	public void setupInitialScope() {
		getLocal( thisValue );
		pushScope();
	}

	/**
     * Set up the initial scope for a script or method as a "with" scope (so 
     * that dynamic properties can be found).  Also set up the "this" var.
     */
    public void setupDynamicScope() {
        getLocal( thisValue );
        dup();
        pushWith();
        dup();
        setProperty( "this" );
    }
	
	/**
	 * Push stack top onto the scope chain
	 */
	public void pushScope() {
		instructions.append( OP_pushscope );
	}

	/**
	 * Pop from the scope chain
	 */
	public void popScope() {
		instructions.append( OP_popscope );
	}
	
	/**
	 * Pop top stack value
	 */
	public void pop() {
	    instructions.append( OP_pop );
	}
	
	/**
	 * Push a local register
	 */
	public void getLocal( LocalValue<Instruction> local ) {
	    instructions.append( OP_getlocal, local );
	}

	/**
	 * Set a local register
	 * @param index the register index
	 */
	public void setLocal( LocalValue<Instruction> local ) {
	    instructions.append( OP_coerce_a );
        instructions.append( OP_setlocal, local );
	}
	
	/** 
	 * Append a returnVoid operation - but only if the previous instruction
	 * is not a return.
	 */
	public void returnVoid() {
	    Instruction last = instructions.last();
	    if( last != null 
	     && ( last.operation == OP_returnvalue
	       || last.operation == OP_returnvoid )) {
	        return;
	    }
	    
		instructions.append( OP_returnvoid );
	}

    /** 
     * Append a returnValue operation
     */
    public void returnValue() {
        instructions.append( OP_returnvalue );
    }
    
	/**
	 * Push the scope object at the given index on the scope stack
	 */
	public void getScopeObject( int index ) {
		instructions.append( OP_getscopeobject, index );
	}
	
	/**
	 * Find and get a property.
	 * @param name the qualified name
	 */
	public void getLex( String name ) {
		instructions.append( OP_getlex, new AVM2QName( name ) );
	}

	/**
	 * Find and get a property.
	 * @param name the qualified name
	 */
	public void getLex( AVM2Name name ) {
		instructions.append( OP_getlex, name );
	}

	/**
	 * Pop the superclass and create a new instance of the given class.
	 */
	public void newClass( AVM2Class clazz ) {
		instructions.append( OP_newclass, clazz.name );
	}
	
	/**
	 * Create a new array and initialize it by popping values from the stack
	 * @param length the array size
	 */
	public void newArray( int length ) {
	    instructions.append( OP_newarray, length );
	}
	
	/**
	 * Push a function closure
	 * 
	 * @param function the function - which must have a fixed index
	 */
	public void newFunction( AVM2Method function ) {
	    instructions.append( OP_newfunction, function.index );	    
	}
	
	/**
	 * Create and initialize a new object
	 * 
	 * @param propCount the number of name/value pairs on the stack
	 */
	public void newObject( int propCount ) {
	    instructions.append( OP_newobject, propCount );
	}
	
	/**
	 * Initialize a property of an object
	 */
	public void initProperty( AVM2Name name ) {
		instructions.append( OP_initproperty, name );		
	}
	
	/**
	 * Call the super constructor
	 * @param argCount the argument count
	 */
	public void constructSuper( int argCount ) {
		instructions.append( OP_constructsuper, argCount );
	}

    /**
     * Construct an object from a property function
     * 
     * @param name the property name
     * @param argCount the argument count
     */
    public void constructProp( String name, int argCount ) {
        instructions.append( OP_constructprop, new AVM2QName( name ), argCount );
    }

	
	/**
	 * Throw the exception from the stack
	 */
	public void throwException() {
	    instructions.append( OP_throw );
	}
	
	/**
	 * Start a minimal no-arg constructor for a class.  Assumes that the
	 * static initializer has already been generated.  The constructor is left
	 * open (no return operation).
	 * 
	 * @param superArgs arguments to send to the super-contructor
	 * 
	 * @return the wrapper for adding to the constructor
	 */
	public static AVM2Code startNoArgConstructor( AVM2Class avm2Class, Object... superArgs ) {
		
        AVM2Method cons = avm2Class.constructor = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
		AVM2MethodBody body = cons.methodBody;
		body.scopeDepth = avm2Class.staticInitializer.methodBody.scopeDepth + 1;
			
		AVM2Code code = new AVM2Code( body );
		code.setupDynamicScope();
        code.trace( "entering constructor for " + avm2Class.name );

		
		code.getLocal( code.thisValue );

		int argCount = 0;
		for( Object superArg : superArgs ) {
		    argCount++;
		    code.push( superArg );
		}
		
		code.constructSuper( argCount );
		
		return code;
	}

    /**
     * Start a minimal static initializer for a class.  The code is left
     * open (no return operation).
     * 
     * @return the wrapper for adding to the initializer
     */
    public static AVM2Code startStaticInitializer( AVM2Class avm2Class, int classScopeDepth ) {
        
        AVM2Method staticInit = avm2Class.staticInitializer = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
        AVM2MethodBody initBody = staticInit.methodBody;
        initBody.scopeDepth = classScopeDepth;

        AVM2Code code = new AVM2Code( initBody );
        code.setupDynamicScope();
        code.trace( "entering static initializer for " + avm2Class.name );
        
        return code;
    }

	
    /**
     * Generate the default static initializer for a class if there is none.
     */    
    public static void defaultStaticInit( AVM2Class avm2Class, int classScopeDepth ) {
        
    	if( avm2Class.staticInitializer != null ) return;
    	
        AVM2Method staticInit = avm2Class.staticInitializer = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
        AVM2MethodBody initBody = staticInit.methodBody;
        initBody.scopeDepth = classScopeDepth;

        AVM2Code code = new AVM2Code( initBody );
        code.setupInitialScope();
        code.returnVoid();
        code.analyze();
    }
    
    /**
     * Start a class initialization script
     */
    public static ClassInitializationScript classInitializationScript( AVM2Class avm2Class, boolean append ) {
        return new ClassInitializationScript( avm2Class, append );        
    }
    
    /**
     * Create a standalone script.
     * 
     * @param abc the ABC file to add the script to
     */
    public static AVM2Code standaloneScript( AVM2ABCFile abc ) {
        AVM2Script script = abc.prependScript();
        script.script.methodBody.scopeDepth = 1;
        AVM2Code code = new AVM2Code( script.script.methodBody );
        code.setupInitialScope();
        return code;
    }
    
    /**
     * Wrapper around a class initialization script.
     * 
     * finish() should be called at the end
     */
    public static class ClassInitializationScript {
       
        private final AVM2Class avm2Class;
        private final AVM2Code  code;
        private int numSuperclasses;
        
        ClassInitializationScript( AVM2Class avm2Class, boolean append ) {
            this.avm2Class = avm2Class;
        
            AVM2Script script = append ? 
                                    avm2Class.abcFile.addScript() :
                                    avm2Class.abcFile.prependScript();
            AVM2MethodBody body   = script.script.methodBody;
            body.scopeDepth = 1;

            code = new AVM2Code( body );
            code.setupInitialScope();
            code.findPropStrict( avm2Class.name );
            code.trace( "entering class initialization script for " + avm2Class.name );
            
            AVM2QName classQName = avm2Class.name;
            
            //--add the class as a trait of this script
            AVM2ClassSlot slot = script.traits.addClass( classQName, classQName );
            slot.indexId = script.traits.traits.size() - 1;
        }
        
        /**
         * Access the code for the script
         */
        public AVM2Code code() {
            return code;
        }
        
        /**
         * Add a superclass.  Superclasses should be added in order from
         * highest to immediate.
         */        
        public void addSuperclass( String name ) {
            addSuperclass( new AVM2QName( name ) );
        }
        
        /**
         * Add a superclass.  Superclasses should be added in order from
         * highest to immediate.
         */
        public void addSuperclass( AVM2QName name ) {
            code.getLex( name );
            code.pushScope();           
            
            numSuperclasses++;
        }
        
        /**
         * Finish the script
         * @return the scope depth for the class
         */
        public int finish() {
            
            //push the superclass and create the new class   
            code.getLex( avm2Class.superclass );
            code.newClass( avm2Class );
                
            //tear down the scope stack, except the global object
            for( int i = 0; i < numSuperclasses; i++ ) {
                code.popScope();   
            }
            
            //initialize the class slot (of the script object)
            code.initProperty( avm2Class.name );
            
            code.returnVoid();
            code.analyze();
            
            return code.body.maxScope;
        }
    }
}
