package com.anotherbigidea.flash.avm2.model;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
	
	private final AVM2Code constructorCode; 
	private final AVM2Code staticInitializerCode;
	private final int instanceScopeDepth;
	private final SortedMap<Integer, AVM2QName> frames = new TreeMap<Integer, AVM2QName>();
	
	private int classScopeDepth;
	
	/**
	 * @param abc the ABC file to add to
	 * @param className the name of the new class
	 * @param superClassName the superclass to use - if null defaults to FLASH_DISPLAY_MOVIECLIP
	 * @param superArg an argument to send to the super-constructor - null for none
	 */
	public AVM2MovieClip( AVM2ABCFile abc, String className,
	                      AVM2QName superClassName,
	                      String superArg ) {

		AVM2QName name       = new AVM2QName( className );
		AVM2QName superclass = (superClassName != null) ? 
		                           superClassName :
		                           FLASH_DISPLAY_MOVIECLIP;
		String packName = name.namespace.name;
		String protName = packName + ":" + name.name;
		
		packageInternal = new AVM2Namespace( NamespaceKind.PackageInternalNamespace, packName );
		
		avm2Class = abc.addClass( name, superclass, 
	                        false, false, false, 
	                        new AVM2Namespace( 
	                                NamespaceKind.ProtectedNamespace, protName ));
		
		AVM2Code.ClassInitializationScript clinit = AVM2Code.classInitializationScript( avm2Class );
		clinit.addSuperclass( "Object" );
		clinit.addSuperclass( "flash.events.EventDispatcher" );
		clinit.addSuperclass( "flash.display.DisplayObject" );
		clinit.addSuperclass( "flash.display.InteractiveObject" );
		clinit.addSuperclass( "flash.display.DisplayObjectContainer" );
		clinit.addSuperclass( "flash.display.Sprite" );
		clinit.addSuperclass( "flash.display.MovieClip" );

		if( ! superclass.equals( FLASH_DISPLAY_MOVIECLIP ) ) {
	        clinit.addSuperclass( superclass );		    
		}
		
		classScopeDepth = clinit.finish();
		
		staticInitializerCode = AVM2Code.startStaticInitializer( avm2Class, classScopeDepth );
		constructorCode       = AVM2Code.startNoArgConstructor( avm2Class, superArg );		
		instanceScopeDepth = classScopeDepth + 1;
	}
	
	/**
	 * Add a frame script.
	 * 
	 * @param frameNumber the zero-based frame number
	 * @return the wrapper for adding code to the frame script
	 */
	public final AVM2Code addFrame( int frameNumber ) {
	    AVM2Method frameMethod = new AVM2Method( null, null );
	    AVM2QName name = new AVM2QName( packageInternal, "frame" + (frameNumber+1));
	    avm2Class.traits.addMethod( name, frameMethod, false, false );
	    frames.put( frameNumber, name );
	    
	    AVM2MethodBody body = frameMethod.methodBody;
	    body.scopeDepth = instanceScopeDepth;
	    
	    AVM2Code code = new AVM2Code( body );
	    code.setupDynamicScope();
	    
	    return code;
	}
	
	/**
	 * Get the code for adding to the static initializer
	 */
	public final AVM2Code initializer() {
	    return staticInitializerCode;
	}
	
	/**
	 * Get the code for adding to the constructor
	 */
	public final AVM2Code constructor() {
	    return constructorCode;
	}
	
	/**
	 * Finish the movieclip.
	 */
	public final void finish() {
	    	    
	    //--complete the static initializer
	    staticInitializerCode.returnVoid();
	    staticInitializerCode.calcMaxes();
	    
	    //--write out the call to register the frame scripts
	    constructorCode.findPropStrict( "addFrameScript" );
        for( Map.Entry<Integer, AVM2QName> entry : frames.entrySet() ) {
	        int       frameNumber = entry.getKey();
	        AVM2QName scriptName  = entry.getValue();
	        
	        constructorCode.pushInt( frameNumber );
	        constructorCode.getLex( scriptName );
	    }
        constructorCode.callPropVoid( "addFrameScript", frames.size() * 2 );
	    
        afterFramesAdded( frames.size() );
        
        constructorCode.returnVoid();	   
        constructorCode.calcMaxes();
	}
	
	/**
	 * Called after frame scripts have been added to the movie to allow
	 * other code to be inserted.
	 */
	protected void afterFramesAdded( int frameCount ) {
	    //intended for overriding
	}
}
