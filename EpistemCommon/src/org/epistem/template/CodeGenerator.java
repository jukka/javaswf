package org.epistem.template;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import freemarker.template.TemplateException;

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
	
	/**
	 * @param targetDir the target dir for generated files
	 */
	protected CodeGenerator( File targetDir ) {
		this.targetDir = targetDir;
	}
	
	/**
	 * Get the given named template (cached)
	 * 
	 * @param name the template name without suffix.
	 * @throws RuntimeException if the template could not be found or loaded
	 */
	protected final Template template( String name ) {
		try {
			return templates.get( name );
		} catch( IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	/**
	 * Generate a file.
	 * 
	 * @param filename the filename, within the target dir
	 * @param template the template to use
	 * @param model the model for the template
	 */
	protected final void generate( String   filename, 
			                       Template template, 
			                       Object   model ) 
		throws IOException, TemplateException {
		
		File target = new File( targetDir, filename );
		target.getParentFile().mkdirs();
		
		FileWriter writer = new FileWriter( target );
		try {
			template.process( writer, model );
		} finally {
			writer.close();
		}		
	}
}
