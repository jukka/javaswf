package org.epistem.template;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.epistem.template.Template.SubtemplateSegment;

/*pkg*/ class TemplateParser {

    private final Template template;
    private final TemplateSource source;
    
    public TemplateParser( Template template, TemplateSource source ) {
        this.template = template;
        this.source   = source;
    }
    
    /**
     * Parse a template from the given input
     * 
     * @param in the template input
     */
    /*pkg*/ void parse( Reader in ) throws IOException {
        
        BufferedReader rdr = (in instanceof BufferedReader) ?
                                 (BufferedReader) in :
                                 new BufferedReader( in );
        
        while( true ) {
            if( ! gatherStaticText( rdr )) break;
            if( ! gatherDirective ( rdr )) break;            
        }
        
        //find required subtemplates
        for ( Template.Segment segment : template.segments ) {
            if( segment instanceof SubtemplateSegment ) {
                SubtemplateSegment subseg = (SubtemplateSegment) segment;
                
                if( subseg.subtemplate == null ) {
                    subseg.subtemplate = template.subTemplates.get( subseg.subTemplateName );                    
                }

                if( subseg.subtemplate == null ) {
                    subseg.subtemplate = source.getTemplate( subseg.subTemplateName );                    
                }
            }
        }
    }
    
    /**
     * Gather a directive
     * @return true if a directive was found, false if EOF
     */
    private boolean gatherDirective( BufferedReader rdr ) throws IOException {        
        rdr.mark(1);        
        int c = rdr.read();
        
        if( c == '#' ) {
            template.segments.add( new Template.BeanValueSegment() );
            return true;
        }    
        
        if( c == '(' ) { //param names
            gatherParamNames( rdr );
            return true;
        }
        rdr.reset();

        String[] name  = gatherName( rdr );
        if( name.length == 0 ) throw new IOException( "Bad property name" );
        
        boolean  qMark = gather( rdr, '?' );
        boolean  pling = qMark ? false : gather( rdr, '!' );
        boolean  equal = gather( rdr, '=' );
        boolean  colon = gather( rdr, ':' );
        String   punc  = gatherPunctuation( rdr );
        
        int condition = 0;
        if     ( qMark ) condition = 1;
        else if( pling ) condition = 2;
        

        if( colon ) {
            String subTemplateName = gatherWord( rdr );
            if( subTemplateName.length() == 0 ) throw new IOException( "Invalid subtemplate name" );
            template.segments.add( new Template.SubtemplateSegment( name, punc, 0, null, subTemplateName ) );            
            return true;
        }

        if( equal ) {
            if( name.length > 1 ) throw new IOException( "Invalid subtemplate name: " + Arrays.toString( name ) );
            Template subtemplate = gatherSubtemplate( rdr );
            if( subtemplate == null ) throw new IOException( "Invalid subtemplate definition " + name[0] );
            
            template.subTemplates.put( name[0], subtemplate );
            return true;
        }
        
        Template subtemplate = gatherSubtemplate( rdr );
        if( subtemplate != null ) {
            template.segments.add( new Template.SubtemplateSegment( name, punc, condition, subtemplate, null ) );            
            return true;
        }
        
        template.segments.add( new Template.PropertySegment( name, punc ));
        return true;
    }

    private void gatherParamNames( BufferedReader rdr ) throws IOException {
        while( true ) {
            String name = gatherWord( rdr );
            if( name.length() == 0 ) throw new IOException( "Empty param name" );
            template.paramNames_internal.add( name );
            
            if( ! gather( rdr, ',' ) ) break;
        }
        
        if( ! gather( rdr, ')' ) ) throw new IOException( "Param names not terminated by parenthesis" );
        gather( rdr, '\r' );
        gather( rdr, '\n' );
    }
    
    private Template gatherSubtemplate( BufferedReader rdr ) throws IOException {
        if( ! gather( rdr, '{' ) ) return null;

        Template subtemplate = new Template();
        new TemplateParser( subtemplate, source ).parse( rdr );
        
        return subtemplate;
    }
    
    private String gatherWord( BufferedReader rdr ) throws IOException {
        StringBuilder buff = new StringBuilder();
        
        while( true ) {
            rdr.mark(1);
            int c = rdr.read();
            if( c == -1 ) break;
            
            if( ! Character.isJavaIdentifierPart( c ) ) {
                if( c != '#' ) rdr.reset();
                break;
            }
            buff.append( (char) c );
        }
        
        return buff.toString();
    }
    
    private String gatherPunctuation( BufferedReader rdr ) throws IOException {
        if( ! gather( rdr, '[' ) ) return "";
        
        StringBuilder buff = new StringBuilder();
        
        while( true ) {
            int c = rdr.read();
            if( c == -1 ) throw new IOException( "Unexpect end of source while reading punctuation" );
            if( c == ']' ) break;
            buff.append( (char) c );
        }
        
        return buff.toString();
    }
    
    private boolean gather( BufferedReader rdr, char c ) throws IOException {
        rdr.mark(1);
        if( rdr.read() == c ) return true;
        rdr.reset();
        return false;
    }
    
    private String[] gatherName( BufferedReader rdr ) throws IOException {
        List<String> parts = new ArrayList<String>();
            
        while( true ) {
            String name = gatherWord( rdr );
            if( name.length() == 0 ) throw new IOException( "Empty property name segment" );
            parts.add( name );
            
            if( ! gather( rdr, '.' ) ) break;
        }
        
        return parts.toArray( new String[ parts.size() ]);
    }
    
    /**
     * Gather static text
     * @return true if a # was found, false if EOF
     */
    private boolean gatherStaticText( BufferedReader rdr ) throws IOException {        
        StringBuilder buff = new StringBuilder();
        boolean result = true;
        
        while( true ) {
            
            //detect the end of subtemplate
            rdr.mark(2);
            if( rdr.read() == '}' && rdr.read() == '#' ) {
                result = false;
                break;                
            }
            
            rdr.reset();            
            int c = rdr.read();
            
            if( c == -1  ) {
                result = false;
                break;
            }
            
            //pound escape
            if( c == '\\' ) {
                rdr.mark(1);
                if( rdr.read() == '#' ) {
                    buff.append( '#' );
                    continue;
                }
                rdr.reset();
            }
            
            if( c == '#' ) break;
            
            buff.append( (char) c );
        }
        
        template.segments.add( new Template.StaticSegment( buff.toString()));        
        return result;
    }
}
