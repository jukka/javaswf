package com.anotherbigidea.flash.avm2.model;

import static com.anotherbigidea.flash.avm2.Operation.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.instruction.Instruction;
import com.anotherbigidea.flash.avm2.instruction.InstructionList;
import com.anotherbigidea.flash.avm2.instruction.CodeAnalyzer;


/**
 * Wrapper around an instruction list to manage scope, stack and register
 * counts and capture some common AVM2 idioms.
 *
 * @author nickmain
 */
public final class AVM2Code {

	private final AVM2MethodBody body;
	private final InstructionList instructions;
	private Map<String, Integer> labelInts;
	private int labelIndex = 0;
	
	//--the register that holds the "this" object
	private int thisRegister = 0;
	
	//--the number of registers reserved for normal operations, the registers
	//  above this may be used for temporary purposes.
	private final int reservedRegisters;
	
	/**
	 * @param body the method/script body to wrap
	 */
	public AVM2Code( AVM2MethodBody body, int reservedRegisters ) {
		this.body              = body;
		this.instructions      = body.instructions;
		this.reservedRegisters = reservedRegisters;
	}

	/**
	 * Calculate and set the max-registers, max-stack, and max-scope for the
	 * method body, given the current instructions
	 */
	public void calcMaxes() {
	    new CodeAnalyzer( body ).analyze();
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
     * Set a member of an object - if the object is undefined then the operation
     * does nothing.  Value, name then object are on the stack. 
     */
    public void setMember_Safe() {
        // stack is: value|name|object|..
        
        int tempReg = reservedRegisters;
        
        setLocal( tempReg   );  // -> n|o|..
        setLocal( tempReg+1 );  // -> o|..
        dup();                  // -> o|o..
        pushUndefined();        // -> undefined|o|o..
        
        String ifundefined = newLabel();
        ifstricteq( ifundefined );  // -> o|..
        
        getLocal( tempReg+1 ); // -> n|o|..
        getLocal( tempReg   ); // -> v|n|o|..
        
        instructions.append( OP_setproperty, AVM2LateMultiname.EMPTY_PACKAGE );

        String exit = newLabel();
        jump( exit );
        
        target( ifundefined );
        label();        
        pop();  // -> ..
        
        target( exit );
        label();        
        killLocal( tempReg   );
        killLocal( tempReg+1 );
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
     * Set a variable on the current scope object - value then name are on the
     * stack, public namespace is assumed.  Assumption is that the scope
     * object is in register zero.
     */
    public void setLocalVariable() {
        int tempReg = reservedRegisters;
                
        setLocal( tempReg );  //value,name,.. -> name,...
        getLocal( 0 );        // -> object,name,...
        swap();               // -> name,object,...
        getLocal( tempReg );  // -> value,name.object,...
        instructions.append( OP_setproperty, AVM2LateMultiname.EMPTY_PACKAGE );
        killLocal( tempReg );
    }
    
    /**
     * Set the "this" variable to point at the item in local register zero
     */
    public void setThis() {
        getLocal( 0 );
        dup();
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
    public void killLocal( int index ) {
        instructions.append( OP_kill, index );
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
	 * Increment a local register as an int
	 */
	public void incLocal_i( int local ) {
	    instructions.append( OP_inclocal_i, local );
	}

    /**
     * Increment a local register as a number
     */
    public void incLocal( int local ) {
        instructions.append( OP_inclocal, local );
    }

    /**
     * Decrement a local register as an int
     */
    public void decLocal_i( int local ) {
        instructions.append( OP_declocal_i, local );
    }

    /**
     * Decrement a local register as a number
     */
    public void decLocal( int local ) {
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
	 * Get a property
	 */
	public void getProperty( String name ) {
        AVM2QName qname = new AVM2QName( name );
	    instructions.append( OP_getproperty, qname );
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
        pushString( message + ":" );
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
	private void push( Object obj ) {
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
	 * Set up the initial scope for a script or method
	 */
	public void setupInitialScope() {
		getLocal( 0 );
		pushScope();
	}

	/**
     * Set up the initial scope for a script or method as a "with" scope (so 
     * that dynamic properties can be found).  Also set up the "this" var.
     */
    public void setupDynamicScope() {
        getLocal( 0 );
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
	 * @param index the register index
	 */
	public void getLocal( int index ) {
		switch( index ) {
			case 0:  instructions.append( OP_getlocal0 ); break;
			case 1:  instructions.append( OP_getlocal1 ); break;
			case 2:  instructions.append( OP_getlocal2 ); break;
			case 3:  instructions.append( OP_getlocal3 ); break;
			default: instructions.append( OP_getlocal, index ); break;
		}
	}

	/**
	 * Set a local register
	 * @param index the register index
	 */
	public void setLocal( int index ) {
		switch( index ) {
			case 0:  instructions.append( OP_setlocal0 ); break;
			case 1:  instructions.append( OP_setlocal1 ); break;
			case 2:  instructions.append( OP_setlocal2 ); break;
			case 3:  instructions.append( OP_setlocal3 ); break;
			default: instructions.append( OP_setlocal, index ); break;
		}
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
	 * @param superArg an argument to send to the super-contructor, null for none
	 * 
	 * @return the wrapper for adding to the constructor
	 */
	public static AVM2Code startNoArgConstructor( AVM2Class avm2Class, String superArg ) {
		
        AVM2Method cons = avm2Class.constructor = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
		AVM2MethodBody body = cons.methodBody;
		body.scopeDepth = avm2Class.staticInitializer.methodBody.scopeDepth + 1;
			
		AVM2Code code = new AVM2Code( body );
		code.setupDynamicScope();
		
		code.getLocal( 0 );

		int argCount = 0;
		if( superArg != null ) {
		    argCount = 1;
		    code.pushString( superArg );
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
    public static AVM2Code startStaticInitializer( AVM2Class avm2Class, int classScopeDepth, int reservedRegisters ) {
        
        AVM2Method staticInit = avm2Class.staticInitializer = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
        AVM2MethodBody initBody = staticInit.methodBody;
        initBody.scopeDepth = classScopeDepth;

        AVM2Code code = new AVM2Code( initBody );
        code.setupDynamicScope();
        
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

        AVM2Code code = new AVM2Code( initBody, 1 );
        code.setupInitialScope();
        code.returnVoid();
        code.calcMaxes();
    }
    
    /**
     * Start a class initialization script
     */
    public static ClassInitializationScript classInitializationScript( AVM2Class avm2Class ) {
        return new ClassInitializationScript( avm2Class );        
    }
    
    /**
     * Create a standalone script.
     * 
     * @param abc the ABC file to add the script to
     */
    public static AVM2Code standaloneScript( AVM2ABCFile abc, int reservedRegisters ) {
        AVM2Script script = abc.prependScript();
        script.script.methodBody.scopeDepth = 1;
        AVM2Code code = new AVM2Code( script.script.methodBody, reservedRegisters );
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
        
        ClassInitializationScript( AVM2Class avm2Class ) {
            this.avm2Class = avm2Class;
        
            AVM2Script     script = avm2Class.abcFile.prependScript();
            AVM2MethodBody body   = script.script.methodBody;
            body.scopeDepth = 1;

            code = new AVM2Code( body, 1 );
            code.setupInitialScope();
            code.getScopeObject( 0 );
            
            AVM2QName classQName = avm2Class.name;
            
            //--add the class as a trait of this script
            AVM2ClassSlot slot = script.traits.addClass( classQName, classQName );
            slot.indexId = script.traits.traits.size() - 1;
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
            code.calcMaxes();
            
            return code.body.maxScope;
        }
    }
}
