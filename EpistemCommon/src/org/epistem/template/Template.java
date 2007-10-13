package org.epistem.template;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateException;

/**
 * Simple wrapper around a Freemarker template
 *
 * @author nickmain
 */
public class Template {

	private final freemarker.template.Template template;
		
	/**
	 * @param name the template name
	 * @throws IOException if the template could not be loaded
	 */
	public Template( String name ) throws IOException {
		this( Template.class, name );
	}

	/**
	 * @param relativeClass the class relative to which the template is located
	 * @param name the template name
	 * @throws IOException if the template could not be loaded
	 */
	public Template( Class<?> relativeClass, String name ) throws IOException {
        Configuration cfg = new Configuration();
        cfg.setObjectWrapper(new DefaultObjectWrapper());
		cfg.setClassForTemplateLoading( relativeClass, "" );
		template = cfg.getTemplate( name );
	}
	
	/**
	 * Process the template
	 * @param out the target for the template output
	 * @param model the model to pass to the Freemarker template
	 */
	public void process( Writer out, Object model ) throws Exception {
        template.process( model, out);
        out.flush();
	}
}
