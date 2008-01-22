package com.anotherbigidea.flash.avm2.model;

import com.anotherbigidea.flash.avm2.NamespaceKind;

/**
 * A builder for a class that extends MovieClip. 
 * finish() must be called once all frame scripts have been added.
 *
 * @author nickmain
 */
public class AVM2MovieClip {

    /**
     * The name of the default MovieClip base class
     */
    public static final AVM2QName FLASH_DISPLAY_MOVIECLIP = new AVM2QName( "flash.display.MovieClip" );
    
	/**
	 * The AVM2Class being built
	 */
	public final AVM2Class avm2Class;
	
	/**
	 * The package internal namespace for this movie
	 */
	public final AVM2Namespace packageInternal;
	
	private AVM2Code constructorCode; 
	private AVM2Code staticInitializerCode;
    private final AVM2Code.ClassInitializationScript classScript;
    private boolean classScriptFinished = false;
	private final Object superArg;
    private final AVM2QName[] superClassNames;
	
	private int classScopeDepth;
    private int instanceScopeDepth;

	/**
	 * @param abc the ABC file to add to
	 * @param className the name of the new class
	 * @param superClassNames the superclasses to use beneath flash-display-MovieClip
	 * @param superArg an argument to send to the super-constructor - null for none
	 */
	public AVM2MovieClip( AVM2ABCFile abc, String className,
	                      Object superArg,
	                      boolean isMainClip,
	                      AVM2QName... superClassNames ) {

	    this.superClassNames = superClassNames;
	    this.superArg = superArg;
		AVM2QName name       = new AVM2QName( className );
        AVM2QName superclass = (superClassNames.length == 0) ? 
                                    FLASH_DISPLAY_MOVIECLIP :
                                    superClassNames[superClassNames.length-1];
		String packName = name.namespace.name;
		String protName = packName + ":" + name.name;
		
		packageInternal = new AVM2Namespace( NamespaceKind.PackageInternalNamespace, packName );
		
		avm2Class = abc.addClass( name, superclass, 
	                        false, false, false, 
	                        new AVM2Namespace( 
	                                NamespaceKind.ProtectedNamespace, protName ));
		
		classScript = AVM2Code.classInitializationScript( avm2Class, isMainClip );
	}
	
	/**
	 * Get the script for the class initialization.  This script cannot
	 * be accessed or altered if any other methods have been called.
	 */
	public final AVM2Code.ClassInitializationScript classScript() {
	    if( classScriptFinished ) throw new IllegalStateException( "Class script has been finished" );
	    return classScript;
	}
	
	private void finishClassScript() {
	    if( classScriptFinished ) return;

        classScript.addSuperclass( "Object" );
        classScript.addSuperclass( "flash.events.EventDispatcher" );
        classScript.addSuperclass( "flash.display.DisplayObject" );
        classScript.addSuperclass( "flash.display.InteractiveObject" );
        classScript.addSuperclass( "flash.display.DisplayObjectContainer" );
        classScript.addSuperclass( "flash.display.Sprite" );
        classScript.addSuperclass( "flash.display.MovieClip" );

        for( AVM2QName supername : superClassNames ) {
            classScript.addSuperclass( supername );
        }

        classScopeDepth = classScript.finish();
        
        staticInitializerCode = AVM2Code.startStaticInitializer( avm2Class, classScopeDepth );
        constructorCode       = AVM2Code.startNoArgConstructor( avm2Class, superArg );      
        instanceScopeDepth = classScopeDepth + 1;

        classScriptFinished = true;
	}
	
	/**
	 * Get the scope depth for the class
	 */
	public final int getClassScopeDepth() {
	    finishClassScript();
	    return classScopeDepth;
	}

    /**
     * Get the scope depth for the instance
     */
    public final int getInstanceScopeDepth() {
        finishClassScript();
        return instanceScopeDepth;
    }
	
	/**
	 * Add a frame script.
	 * 
	 * @param frameNumber the zero-based frame number
     * @param the name of the method to call in order to add each frame
	 * @return the wrapper for adding code to the frame script
	 */
	public final AVM2Code addFrame( int frameNumber, AVM2QName frameAddMethod ) {
	    finishClassScript();
	    
	    AVM2Method frameFunction = avm2Class.abcFile.addFunctionClosure( null, null );
	    
        constructorCode.getLocal( constructorCode.thisValue );
        constructorCode.pushInt( frameNumber );
        constructorCode.newFunction( frameFunction );
        constructorCode.callPropVoid( frameAddMethod, 2 );

	    AVM2MethodBody body = frameFunction.methodBody;
	    body.scopeDepth = instanceScopeDepth + 1;
	    
	    AVM2Code code = new AVM2Code( body );
	    return code;
	}

	/**
	 * Add init actions to a frame
	 * 
	 * @param frameNumber the frame number
	 * @param symbolId the symbol id for the actions
	 * @return the code for the actions
	 */
	public final AVM2Code addInitActions( int frameNumber, int symbolId, AVM2QName initAddMethod  ) {
	    finishClassScript();
	    
	    AVM2Method initFunction = avm2Class.abcFile.addFunctionClosure( null, null );
        
        constructorCode.getLocal( constructorCode.thisValue );
        constructorCode.pushInt( frameNumber );
        constructorCode.pushInt( symbolId );
        constructorCode.newFunction( initFunction );
        constructorCode.callPropVoid( initAddMethod, 3 );

        AVM2MethodBody body = initFunction.methodBody;
        body.scopeDepth = instanceScopeDepth + 1;
        
        AVM2Code code = new AVM2Code( body );
        return code;	    
	}
	
	/**
	 * Get the code for adding to the static initializer
	 */
	public final AVM2Code initializer() {
	    finishClassScript();
	    return staticInitializerCode;
	}
	
	/**
	 * Get the code for adding to the constructor
	 */
	public final AVM2Code constructor() {
	    finishClassScript();
	    return constructorCode;
	}
	
	/**
	 * Finish the movieclip.
	 */
	public final void finish() {
	    finishClassScript();
	    
	    staticInitializerCode.returnVoid();
	    staticInitializerCode.analyze();
        
        constructorCode.returnVoid();	   
        constructorCode.analyze();
	}
}
