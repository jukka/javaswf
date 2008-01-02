package com.anotherbigidea.flash.avm2.model;

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
	 * The AVM2Class being built
	 */
	public final AVM2Class avm2Class;
	
	/**
	 * The package internal namespace for this movie
	 */
	public final AVM2Namespace packageInternal;
	
	private final AVM2Code consCode; //the constructor code
	private final int instanceScopeDepth;
	private final SortedMap<Integer, AVM2QName> frames = new TreeMap<Integer, AVM2QName>();
	
	/**
	 * @param abc the ABC file to add to
	 * @param className the name of the new class
	 */
	public AVM2MovieClip( AVM2ABCFile abc, String className ) {

		AVM2QName name       = new AVM2QName( className );
		AVM2QName superclass = new AVM2QName( "flash.display.MovieClip" );
		String packName = name.namespace.name;
		String protName = packName + ":" + name.name;
		
		packageInternal = new AVM2Namespace( NamespaceKind.PackageInternalNamespace, packName );
		
		avm2Class = abc.addClass( name, superclass, 
	                        false, false, false, 
	                        new AVM2Namespace( 
	                                NamespaceKind.ProtectedNamespace, protName ));
		
		int scopeDepth = 
			AVM2Code.classInitializationScript( 
				avm2Class, "Object",
			               "flash.events.EventDispatcher",
			               "flash.display.DisplayObject",
			               "flash.display.InteractiveObject",
			               "flash.display.DisplayObjectContainer",
			               "flash.display.Sprite",
			               "flash.display.MovieClip" );
		
		AVM2Code.defaultStaticInit( avm2Class, scopeDepth );
		consCode = AVM2Code.startNoArgConstructor( avm2Class );
		
		instanceScopeDepth = scopeDepth + 1;
	}
	
	/**
	 * Add a frame script.
	 * 
	 * @param frameNumber the zero-based frame number
	 * @return the wrapper for adding code to the frame script
	 */
	public AVM2Code addFrame( int frameNumber ) {
	    AVM2Method frameMethod = new AVM2Method( null, null );
	    AVM2QName name = new AVM2QName( packageInternal, "frame" + (frameNumber+1));
	    avm2Class.traits.addMethod( name, frameMethod, false, false );
	    frames.put( frameNumber, name );
	    
	    AVM2MethodBody body = frameMethod.methodBody;
	    body.scopeDepth = instanceScopeDepth;
	    
	    AVM2Code code = new AVM2Code( body );
	    code.setupInitialScope();
	    return code;
	}
	
	/**
	 * Finish the movieclip.
	 */
	public void finish() {
	    
	    //--write out the call to register the frame scripts
        consCode.findPropStrict( "addFrameScript" );
        for( Map.Entry<Integer, AVM2QName> entry : frames.entrySet() ) {
	        int       frameNumber = entry.getKey();
	        AVM2QName scriptName  = entry.getValue();
	        
	        consCode.pushInt( frameNumber );
	        consCode.getLex( scriptName );
	    }
        consCode.callPropVoid( "addFrameScript", frames.size() * 2 );
	    
	    consCode.returnVoid();	   
	    consCode.calcMaxes();
	}
}
