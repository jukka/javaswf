package org.epistem.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/** 
 * A source of templates
 *
 * @author nickmain
 */
public abstract class TemplateSource {

    public static final String TEMPLATE_SUFFIX = ".template";
    
    //template cache
    private Map<String, Template> cache = new HashMap<String, Template>();
    
    /**
     * Get the template with the given name.  The TEMPLATE_SUFFIX is added to
     * the name if not already present.
     * 
     * @throws IOException if the template does not exist or there was a
     *                     problem parsing the template
     */
    public Template getTemplate( String name ) throws IOException {
        if( ! name.endsWith( TEMPLATE_SUFFIX ) ) name += TEMPLATE_SUFFIX;
        
        Template template = cache.get( name );
        if( template == null ) {
            Reader rdr = getTemplateReader( name );
            template = new Template();
            cache.put( name, template ); //put this in first to allow circular references
            new TemplateParser( template, this ).parse( rdr );
        }
        
        return template;
    }
        
    /**
     * Get a template source that reads from the given class loader.
     */
    public static TemplateSource forClassLoader( ClassLoader loader ) {
        return new ClassLoaderTemplateSource( loader );
    }

    /**
     * Get a template source that reads from the class loader that loaded this class.
     */
    public static TemplateSource forClassLoader() {
        return new ClassLoaderTemplateSource( TemplateSource.class.getClassLoader() );
    }
    
    /**
     * Get a template source that uses the given filesystem base directory.
     */
    public static TemplateSource forDirectory( File directory ) {
        return new FileTemplateSource( directory );
    }

    /**
     * Get a template source that uses the given map of template strings
     */
    public static TemplateSource forMap( Map<String, String> map ) {
        return new MapTemplateSource( map );
    }
    
    /**
     * Get a reader for the template name.
     * @throws IOException if the template could not be found
     */
    protected abstract Reader getTemplateReader( String name ) throws IOException;
    
    /** Loads templates from a class loader */
    private static class ClassLoaderTemplateSource extends TemplateSource {

        private final ClassLoader loader;
        
        /**
         * Use the given class loader
         */
        public ClassLoaderTemplateSource( ClassLoader loader ) {
            this.loader = loader;
        }
        
        /**@see org.epistem.template.TemplateSource#getTemplateReader(java.lang.String) */
        public Reader getTemplateReader( String name ) throws IOException {
            
            InputStream in = loader.getResourceAsStream( name );
            if( in == null ) throw new FileNotFoundException( "Could not found template resource " + name );
            return new InputStreamReader( in );
        }
    }
    
    /** Loads templates from the file system */
    private static class FileTemplateSource extends TemplateSource {

        private final File dir;
        
        /**
         * Use the given directory
         */
        public FileTemplateSource( File dir ) {
            this.dir = dir;
        }
        
        /**@see org.epistem.template.TemplateSource#getTemplateReader(java.lang.String) */
        public Reader getTemplateReader( String name ) throws IOException {
            File file = new File( dir, name );
            if( ! file.exists() || ! file.isFile() ) {
                throw new FileNotFoundException( "Could not found template file " + file.getCanonicalPath() );
            }

            return new FileReader( file );
        }
    }

    /** Loads templates from the given Map */
    private static class MapTemplateSource extends TemplateSource {

        private final Map<String, String> map;
        
        /**
         * Use the given directory
         */
        public MapTemplateSource( Map<String, String> map ) {
            this.map = map;
        }
        
        /**@see org.epistem.template.TemplateSource#getTemplateReader(java.lang.String) */
        public Reader getTemplateReader( String name ) throws IOException {
            String s = map.get( name );
            if( s == null ) {
                throw new FileNotFoundException( "Could not found template string " + name );
            }

            return new StringReader( s );
        }
    }
}
