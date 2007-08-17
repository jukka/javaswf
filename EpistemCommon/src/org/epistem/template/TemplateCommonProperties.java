package org.epistem.template;

import java.util.Date;

/**
 * Bean that is at the end of all template bean lists in order to provide
 * common properties that are accessible in all templates.
 *
 * @author nickmain
 */
public class TemplateCommonProperties {

    /** Get the current date-time string */
    public String getTime() {
        return new Date().toString();
    }
    
    /** Get the current user name */
    public String getUserName() {
        return System.getProperty( "user.name" );        
    }
}
