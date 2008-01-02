package babelswf;

import com.anotherbigidea.flash.avm2.model.AVM2Code;
import com.anotherbigidea.flash.avm2.model.AVM2QName;

/**
 * Enumeration of the BabelSWF runtime helpers
 *
 * @author nickmain
 */
public enum BabelSWFRuntime {

    RT_Stop       ( 0, true ),
    RT_Play       ( 0, true ),
    RT_Trace      ( 1, true ),
    RT_GetVariable( 1, false ),
    RT_GetMember  ( 2, false ),
    RT_SetMember  ( 3, true ),
    RT_Extends    ( 2, true );
    
    /** The name of the Babel runtime class */
    public static final AVM2QName BABEL_RUNTIME = new AVM2QName( "babelswf.Runtime" );

    /**
     * Generate a call to the runtime helper
     */
    public void generate( AVM2Code code ) {
                
        //--push the BABEL_RUNTIME down the stack to before the args
        if( argCount == 1 ) {
            code.getLex( BABEL_RUNTIME );
            code.swap();
        }
        else if( argCount > 1 ) {
            int register = code.specialRegisterMode( true );
            
            //save the args (but one) in regs
            for( int i = 0; i < argCount - 1; i++ ) {
                code.setLocal( register + i );
            }
            
            code.getLex( BABEL_RUNTIME );
            code.swap();
            
            //put the args back on the stack
            for( int i = argCount - 2; i >= 0 ; i-- ) {
                code.getLocal( register + i );
            }            
            
            code.specialRegisterMode( false );
        }
        else {
            code.getLex( BABEL_RUNTIME );
        }
        
        if( isVoid ) {
            code.callPropVoid( methodName, argCount );
        }
        else {
            code.callProperty( methodName, argCount );
        }
    }
    
    //--the AS3 methods name for the helper
    // Note the naming convention
    private final AVM2QName methodName = new AVM2QName( "action" + name().substring(3) );
    
    //--whether the helper is void
    private final boolean isVoid;
    
    //--arg count
    private final int argCount;
    
    private BabelSWFRuntime( int argCount, boolean isVoid ) {
        this.isVoid   = isVoid;
        this.argCount = argCount;
    }
}
