package com.anotherbigidea.flash.avm2.model;

import static com.anotherbigidea.flash.avm2.Operation.*;

import java.util.EnumSet;

import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.instruction.InstructionList;


/**
 * Wrapper around an instruction list to manage scope, stack and register
 * counts and capture some common AVM2 idioms.
 *
 * @author nickmain
 */
public final class AVM2Code {

	private final AVM2MethodBody body;
	private final InstructionList instructions;
	
	private int stackSize;
	private int scopeSize;
	private int registersUsed;
	
	/* Special register mode is used to allow temporary register allocation
	 * for special purposes.  While in this mode register use counts towards
	 * the body's maxRegisters but not to registersUsed.
	 */
	private boolean specialRegisterMode;
	
	/**
	 * @param body the method/script body to wrap
	 */
	public AVM2Code( AVM2MethodBody body ) {
		this.body = body;
		this.instructions = body.instructions;
		
		stackSize = 0;
		scopeSize = body.scopeDepth;
		body.maxScope     = Math.max( body.maxScope, scopeSize );
		body.maxStack     = Math.max( body.maxStack, 0 );
        body.maxRegisters = Math.max( body.maxRegisters, 0 );
	}
	
	//register a stack push
	private void stack( int count ) {
		stackSize += count;
		body.maxStack = Math.max( stackSize, body.maxStack );
	}

	//register a scope push
	private void scope( int count ) {
		scopeSize += count;
		body.maxScope = Math.max( scopeSize, body.maxScope );
	}

	//register a register use
	private void local( int index ) {
		body.maxRegisters = Math.max( index + 1, body.maxRegisters );
		if( ! specialRegisterMode ) registersUsed = body.maxRegisters;
	}	
	
	/**
	 * Turn special register mode on or off.
	 * 
	 * @param on true for on
	 * @return the next available register index
	 */
	public int specialRegisterMode( boolean on ) {
	    specialRegisterMode = on;
	    return registersUsed;
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
	 * Duplicate the top stack value
	 */
	public void dup() {
		stack(1);
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
	    stack(-1);
	    instructions.append( OP_add );
	}

    /**
     * Add ints, with conversion if required.
     */
    public void addInts() {
        stack(-1);
        instructions.append( OP_add_i );
    }
	
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
	 * Push null
	 */
	public void pushNull() {
		stack(1);
		instructions.append( OP_pushnull );
	}

    /**
     * Push undefined
     */
    public void pushUndefined() {
        stack(1);
        instructions.append( OP_pushundefined );
    }

	/**
	 * Push a signed int
	 */
	public void pushInt( int value ) {
		stack(1);
		
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
		stack(1);		
		instructions.append( OP_pushuint, value );
	}
	
	/**
	 * Push a double
	 */
	public void pushDouble( double value ) {
		stack(1);
		instructions.append( OP_pushdouble, value );
	}

	/**
	 * Push a boolean
	 */
	public void pushBoolean( boolean value ) {
		stack(1);
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
		stack( -argCount - 1 );
		instructions.append( OP_callpropvoid, new AVM2QName( qualifiedName ), argCount );
	}
	   
    /**
     * Call a property on an object.  There is no return value.
     * 
     * @param qualifiedName the prop name
     * @param argCount the number of arguments
     */
    public void callPropVoid( AVM2QName qualifiedName, int argCount ) {
        stack( -argCount - 1 );
        instructions.append( OP_callpropvoid, qualifiedName, argCount );
    }
	
    /**
     * Call a property on an object.
     * 
     * @param qualifiedName the prop name
     * @param argCount the number of arguments
     */
    public void callProperty( AVM2QName qualifiedName, int argCount ) {
        stack( -argCount );
        instructions.append( OP_callproperty, qualifiedName, argCount );
    }
    
	/**
	 * Push a string
	 */
	public void pushString( String s ) {
		stack(1);
		instructions.append( OP_pushstring, s );
	}
	
	/**
	 * Find and push the object with the given named property.
	 */
	public void findPropStrict( String qualifiedName ) {
		stack(1);
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
	 * Push stack top onto the scope chain
	 */
	public void pushScope() {
		scope( 1 );
		stack( -1 );
		instructions.append( OP_pushscope );
	}

	/**
	 * Pop from the scope chain
	 */
	public void popScope() {
		scope( -1 );
		instructions.append( OP_popscope );
	}
	
	/**
	 * Push a local register
	 * @param index the register index
	 */
	public void getLocal( int index ) {
		local( index );
		stack( 1 );
		
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
		local( index );
		stack( -1 );
		
		switch( index ) {
			case 0:  instructions.append( OP_setlocal0 ); break;
			case 1:  instructions.append( OP_setlocal1 ); break;
			case 2:  instructions.append( OP_setlocal2 ); break;
			case 3:  instructions.append( OP_setlocal3 ); break;
			default: instructions.append( OP_setlocal, index ); break;
		}
	}
	
	/** 
	 * Append a returnVoid operation
	 */
	public void returnVoid() {
		instructions.append( OP_returnvoid );
	}
	
	/**
	 * Push the scope object at the given index on the scope stack
	 */
	public void getScopeObject( int index ) {
		stack(1);
		instructions.append( OP_getscopeobject, index );
	}
	
	/**
	 * Find and get a property.
	 * @param name the qualified name
	 */
	public void getLex( String name ) {
		stack(1);
		instructions.append( OP_getlex, new AVM2QName( name ) );
	}

	/**
	 * Find and get a property.
	 * @param name the qualified name
	 */
	public void getLex( AVM2Name name ) {
		stack(1);
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
		stack(-2);
		instructions.append( OP_initproperty, name );		
	}
	
	/**
	 * Call the super constructor
	 * @param argCount the argument count
	 */
	public void constructSuper( int argCount ) {
		stack( -argCount - 1 );
		instructions.append( OP_constructsuper, argCount );
	}
	
	/**
	 * Start a minimal no-arg constructor for a class.  Assumes that the
	 * static initializer has already been generated.  The constructor is left
	 * open (no return operation).
	 * 
	 * @return the wrapper for adding to the constructor
	 */
	public static AVM2Code startNoArgConstructor( AVM2Class avm2Class ) {
		
        AVM2Method cons = avm2Class.constructor = 
            new AVM2Method( null, EnumSet.noneOf( MethodInfoFlags.class ));
        
		AVM2MethodBody body = cons.methodBody;
		body.scopeDepth = avm2Class.staticInitializer.methodBody.scopeDepth + 1;
			
		AVM2Code code = new AVM2Code( body );
		code.setupInitialScope();
		code.getLocal( 0 );
		code.constructSuper( 0 );
		
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
    }
    
    /**
     * Generate a class initialization script and determine the class scope depth.
     * 
     * The script has the avm2 class object as a trait and is run when the
     * class is referenced.  
     * 
     * @param avm2Class the class in question
     * @param superclasses the superclass in order from highest to most immediate
     * 
     * @return the class scope depth
     */
    public static int classInitializationScript( AVM2Class avm2Class, String...superclasses ) {
        
        AVM2Script     script = avm2Class.abcFile.prependScript();
        AVM2MethodBody body   = script.script.methodBody;
        body.scopeDepth = 1;

        AVM2Code       code   = new AVM2Code( body );
        code.setupInitialScope();
        code.getScopeObject( 0 );
        
        AVM2QName classQName = avm2Class.name;
        
        //--add the class as a trait of this script
        AVM2ClassSlot slot = script.traits.addClass( classQName, classQName );
        slot.indexId = script.traits.traits.size() - 1;

        // build the scope stack for the new class (will be captured as a closure
        // by newclass)
        for( String superclass : superclasses ) {
        	code.getLex( superclass );
        	code.pushScope();        	
        }

        //push the superclass and create the new class   
        code.getLex( avm2Class.superclass );
        code.newClass( avm2Class );
        	
        //tear down the scope stack, except the global object
        for( int i = 0; i < superclasses.length; i++ ) {
            code.popScope();   
        }
        
        //initialize the class slot (of the script object)
        code.initProperty( classQName );
        
        code.returnVoid();
        
        return body.maxScope;
    }
}
