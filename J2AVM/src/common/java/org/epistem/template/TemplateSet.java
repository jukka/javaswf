package org.epistem.template;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A set of templates that all share the same relative Class for loading and
 * that are cached by name.
 *
 * @author nickmain
 */
public class TemplateSet {

	public static final String DEFAULT_SUFFIX = ".ftl";
	
	private final Map<String, Template> cache = new HashMap<String, Template>();
	private final Class<?> relativeClass;
	private final String   templateSuffix;
	private boolean bypassCache = false;
	
	/**
	 * @param relativeClass the class that templates are relative to
	 * @param suffix the filename suffix for the template names
	 */
	public TemplateSet( Class<?> relativeClass, String suffix ) {
		if( ! suffix.startsWith( "." ) ) suffix = "." + suffix;
		this.templateSuffix = suffix;
		this.relativeClass  = relativeClass;
	}

	/**
	 * Bypass the cache for each request
	 * @param bypass true to bypass
	 */
	public void bypassCache( boolean bypass ) {
		this.bypassCache = bypass;
	}
	
	/**
	 * Use the default Freemarker template suffix.
	 * 
	 * @param relativeClass the class that templates are relative to
	 */
	public TemplateSet( Class<?> relativeClass ) {
		this( relativeClass, DEFAULT_SUFFIX );
	}

	/**
	 * Get a named template.
	 * 
	 * @param name the template name (without suffix)
	 * @return the requested template
	 * @throws IOException if the template was not found or could not be loaded
	 */
	public Template get( String name ) throws IOException {
		
		if( bypassCache ) {
			return new Template( relativeClass, name + templateSuffix );
		}
		
		Template template = cache.get( name );
		if( template == null ) {
			template = new Template( relativeClass, name + templateSuffix );
			cache.put( name, template );
		}
		
		return template;
	}
}
