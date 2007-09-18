package org.javaswf.j2avm.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.flags.ClassFlag;

/**
 * A model of the parsed form of a class-file.
 *
 * @author nickmain
 */
public final class ClassModel {

	/**
	 * The name of the class
	 */
	private final String name;
	
	private final Set<String>      interfaces  = new HashSet<String>();
	private final Set<FieldModel>  fields      = new HashSet<FieldModel>();
	private final Set<MethodModel> methods     = new HashSet<MethodModel>();    
	private final Map<String,AttributeModel> attributes = new HashMap<String,AttributeModel>();    
	private final Collection<ClassFlag> flags;    

    private final String superclass;
    private final int majorVersion;
    private final int minorVersion;
    
    /**
     * @param name the class name
     * @param superclass the superclass - null if none
     * @param majorVersion the class major version
     * @param minorVersion the class minor version
     * @param flags the class flags
     */
    public ClassModel( String name, String superclass, 
    	               int majorVersion, int minorVersion,
    	               Collection<ClassFlag> flags ) {
    	this.name         = name;
    	this.superclass   = superclass;
    	this.majorVersion = majorVersion;
    	this.minorVersion = minorVersion;
    	this.flags        = flags;
    }
}
