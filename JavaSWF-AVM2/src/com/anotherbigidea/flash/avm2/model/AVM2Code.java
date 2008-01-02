package com.anotherbigidea.flash.avm2.model;

import static com.anotherbigidea.flash.avm2.Operation.*;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.instruction.InstructionList;
import com.anotherbigidea.flash.avm2.instruction.MaxValueAnalyzer;


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
	
	/**
	 * @param body the method/script body to wrap
	 */
	public AVM2Code( AVM2MethodBody body ) {
		this.body = body;
		this.instructions = body.instructions;
	}

	/**
	 * Calculate and set the max-registers, max-stack, and max-scope for the
	 * method body, given the current instructions
	 */
	public void calcMaxes() {
	    new MaxValueAnalyzer( body ).analyze();
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
	 * Append a returnVoid operation
	 */
	public void returnVoid() {
		instructions.append( OP_returnvoid );
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
        code.calcMaxes();
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
        code.calcMaxes();
        
        return body.maxScope;
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
}
