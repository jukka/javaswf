package org.epistem.template;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Base for code generators.  
 * 
 * The assumption is that the Freemarker templates are in the same dir as the
 * class that extends this.  
 * 
 * The default Freemarker template filename suffix is also used.
 *
 * @author nickmain
 */
public abstract class CodeGenerator {

	private final TemplateSet templates = new TemplateSet( this.getClass() );
	private final File targetDir;
	private final Map<String, Object> model = new HashMap<String, Object>();
	
	/**
	 * @param targetDir the target dir for generated files
	 */
	protected CodeGenerator( File targetDir ) {
		this.targetDir = targetDir;
		
		model.put( "generator", this );
	}
	
	/**
	 * Add an item to the model
	 * @param name the item name
	 * @param item the item
	 */
	protected final void addToModel( String name, Object item ) {
		model.put( name, item );
	}
	
	/**
	 * Generate a file.
	 * 
	 * @param filename the filename, within the target dir
	 * @param templateName the template to use
	 */
	protected final void generate( String filename, String templateName ) 
		throws Exception {
		
		File target = new File( targetDir, filename );
		target.getParentFile().mkdirs();
		Template template = templates.get( templateName );
		
		FileWriter writer = new FileWriter( target );
		try {
			template.process( writer, model );
		} finally {
			writer.close();
		}		
	}
}
