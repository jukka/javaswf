package org.epistem.util;

import java.util.Properties;

/**
 * Simple util to parse command line args of the form name=value.
 *
 * @author nickmain
 */
public class CommandLineArgs {

    /**
     * Parse the command line args into a set of properties.  Args that do
     * not contain '=' are treated as properties with an empty value.
     */
    public static Properties parse( String[] args ) {
        Properties props = new Properties();
        
        for( String arg : args ) {
            int eq = arg.indexOf( "=" );
            if( eq < 0 ) {                
                props.setProperty( arg, "" );
                continue;
            }
            
            String name  = arg.substring( 0, eq );
            String value = arg.substring( eq + 1 );
            
            props.setProperty( name, value );
        }
        
        return props;
    }
    
}
