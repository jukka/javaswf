/*
 * Created on Jun 1, 2003
 */
package org.epistem.io;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.LinkedList;

/**
 * A PrintWriter that has added indenting capability.
 * Also has XML writing capability
 * 
 * @author D.N.G. Main
 */
public class IndentingPrintWriter extends PrintWriter {

    /**
     * Writer to system out.
     */
    public static final IndentingPrintWriter SYSOUT = new IndentingPrintWriter( System.out );

    /**
     * Writer to system err.
     */
    public static final IndentingPrintWriter SYSERR = new IndentingPrintWriter( System.err );
    
    private String mIndent;
    private int    mIndentLevel = 0;
    private char   mLineSep;
    private boolean mStartOfLine = true; 
    
    /**
     * Uses an indent of 4 spaces and initial level of zero (no indent).
     */
    public IndentingPrintWriter( Writer writer ) {
        this( writer, "    ", 0 );
    }

    /**
     * Uses an indent of 4 spaces and initial level of zero (no indent).
     */
    public IndentingPrintWriter( PrintStream stream ) {
        this( stream, "    ", 0 );
    }

    /**
     * Uses a given indent and initial level.
     */
    public IndentingPrintWriter( Writer writer, String indent, int level ) {
        super( writer );
        mIndent = indent;
        mIndentLevel = level;
        String lineSep = System.getProperty( "line.separator" );
        mLineSep = lineSep.charAt( lineSep.length() - 1 );
    }

    /**
     * Uses a given indent and initial level.
     */
    public IndentingPrintWriter( PrintStream stream, String indent, int level ) {
        this( new PrintWriter( stream ), indent, level );
    }

    
    public String getIndent() {return mIndent;}
    public int getIndentLevel() {return mIndentLevel;}
    public void setIndent(String indent) {mIndent = indent;}
    public void setIndentLevel(int indentLevel) {mIndentLevel = indentLevel;}

    /**
     * Get the wrapped writer.
     */
    public Writer getWrappedWriter() {
        return super.out;
    }
    
    /**
     * Increase the indent.
     */
    public void indent() { mIndentLevel++; }
    
    /**
     * Decrease the indent.
     */
    public void unindent() { mIndentLevel = (mIndentLevel > 0) ? (mIndentLevel-1) : 0; }
    
    private void writeIndent() {
        for (int i = 0; i < mIndentLevel; i++) print( mIndent );        
    }
    
    /** Write a char */
    public void write( char c ) {
        write( (int) c );
    }
    
    /**
     * @see java.io.Writer#write(char[], int, int)
     */
    public void write(char[] buf, int off, int len) {
        for (int i = off; i < off + len; i++) {
            write( buf[i] );
        }

    }

    /**
     * Write text within a fixed width field.  If the text is larger than the
     * field then it is written in its entirety, if smaller then the field is
     * padded with spaces.
     * 
     * @param text the text to write
     * @param fieldWidth the width of the field
     * @param rightAlign true for right alignment, false for left
     */
    public void writeField( String text, int fieldWidth, boolean rightAlign ) {
        int size = text.length();
        int spaces = fieldWidth - size;

        if( spaces <= 0 ) {
            write( text );
            return; 
        }  

        if( ! rightAlign ) write( text ); 
        while( spaces-- > 0 ) write( " " );
        if( rightAlign ) write( text ); 
    }

    /**
     * @see java.io.Writer#write(char[])
     */
    public void write(char[] buf) {
        write( buf, 0, buf.length );
    }

    /**
     * @see java.io.Writer#write(int)
     */
    public void write(int c) {
                
        if( ! xmlTags.isEmpty() ) {
            closeTag();
            checkIndent();
            String s = escapeXMLChar( (char)c, false );
            try {
                out.write( s );
            } catch( IOException ioe ) {
                throw new RuntimeException(ioe);
            }
        } else {        
            checkIndent();
            super.write(c);
        }
        
        if( c == '\n' || c == mLineSep ) {
            mStartOfLine = true;
        }
    }

    /** Write the indent if at start of line */
    private void checkIndent() {
        if( mStartOfLine ) {
            mStartOfLine = false;
            writeIndent();
        }
    }
    
    /**
     * @see java.io.Writer#write(java.lang.String, int, int)
     */
    public void write(String s, int off, int len) {
        write( s.toCharArray(), off, len );
    }

    /**
     * @see java.io.Writer#write(java.lang.String)
     */
    public void write(String s) {
        write( s, 0, s.length() );
    }

    /** Write an escaped string surrounded by double quotes */
    public void writeDoubleQuotedString( String s ) {
        write( "\"" );
        writeEscapedString( s );
        write( "\"" );
    }

    /** Write an escaped string surrounded by single quotes */
    public void writeSingleQuotedString( String s ) {
        write( "'" );
        writeEscapedString( s );
        write( "'" );
    }

    /**
     * @see java.io.PrintWriter#println()
     */
    public void println() {
        super.println();
        mStartOfLine = true;
    }

    /** Write a string, escaping control chars and quotes */
    public void writeEscapedString( String s ) {
        char[] cc = s.toCharArray();

        for (int i = 0; i < cc.length; i++) {
            char c = cc[i];
            
            switch( c ) {
                case '\"': write( "\\\"" ); break;
                case '\'': write( "\\\'" ); break;
                case '\n': write( "\\n"  ); break;
                case '\r': write( "\\r"  ); break;
                case '\f': write( "\\f"  ); break;
                case '\t': write( "\\t"  ); break;
                case '\b': write( "\\b"  ); break;
                case '\0': write( "\\0"  ); break;
                case '\\': write( "\\\\" ); break;
                
                default: {
                    if( c < 32 || c > 127 ) {
                        String hex = Integer.toHexString( c );
                        while( hex.length() < 4 ) hex = "0" + hex;
                        write( "\\u" );                       
                        write( hex );                     
                    } else {
                        write( c );
                    }
                    
                    break;
                }
            }
        }        
    }
    
    /** 
     * Write a string, escaping the standard XML entities, directly to the
     * wrapped stream (no indentation or newline handling).
     * 
     * @param escapeControlChars true to also escape controls chars
     */
    public void writeEscapedXMLString( String s, boolean escapeControlChars ) {
        char[] cc = s.toCharArray();

        for (int i = 0; i < cc.length; i++) {
            char c = cc[i];            
            writeDirect( escapeXMLChar( c, escapeControlChars ) );
        }        
    }
    
    /** 
     * Escape the standard XML entities
     * 
     * @param escapeControlChars true to also escape controls chars
     */
    public String escapeXMLChar( char c, boolean escapeControlChars ) {
        switch( c ) {
            case '\"': return "&quot;";
            case '\'': return "&apos;";
            case '>' : return "&gt;";
            case '<' : return "&lt;";
            case '&' : return "&amp;";
            
            default: {
                if( c < 32 && escapeControlChars ) {
                    return "&#" + ((int) c) + ";";
                } else {
                    return "" + c;
                }
            }
        }
    }
    
    /** Escape a string */
    public static String escapeString( String s ) {
        StringWriter sw = new StringWriter();
        IndentingPrintWriter ipw = new IndentingPrintWriter( sw );
        ipw.writeEscapedString( s );
        ipw.flush();
        
        return sw.toString();
    }
    
    //====XML Machinery follows====
    private LinkedList<String> xmlTags = new LinkedList<String>();
    private boolean tagIsOpen = false;  //if XML opening tag is still open
    private boolean elementJustEnded = false;
    
    /**
     * Write the standard UTF-8 XML header directive.
     */
    public void writeXMLHeader() {
        writeDirect( "<?xml version='1.0' encoding='UTF-8'?>\n" );
    }
    
    /** Close any open XML tag */
    private void closeTag() {
        if( tagIsOpen ) {
            tagIsOpen = false;
            writeDirect( ">" );
            write( "\n" );
        }
        elementJustEnded = false;
    }
    
    /** Write directly to the wrapped stream */
    private void writeDirect( String s ) {
        flush();
        try {
            out.write( s );
        } catch( IOException ioe ) {
            throw new RuntimeException(ioe);
        }
    }
        
    /**
     * Start an XML element
     * 
     * @param tag the element tag
     */
    public void startElement( String tag ) {
        closeTag();
        checkIndent();
        writeDirect( "<" + tag );
        indent();
        tagIsOpen = true;
        xmlTags.addFirst( tag );
    }
    
    /**
     * Write a single-line element.
     * @param tag the tag
     * @param content the element content - null to skip
     * @param attributes the attributes - alternate name and value
     */
    public void element( String tag, Object content, Object...attributes ) {
        startElement( tag );
        unindent();
        xmlTags.removeFirst();
        tagIsOpen = false;

        for (int i = 0; i < attributes.length-1; i += 2 ) {
            attribute( ""+attributes[i] , attributes[i+1]);
        }
        
        if( content != null ) {
            writeDirect( ">" );
            writeEscapedXMLString( ""+content, true );
            writeDirect( "</" + tag + ">" );
        } else {
            writeDirect( "/>" );            
        }
        
        write( "\n" );
        elementJustEnded = true;
    }
    
    /**
     * Write an empty element.
     * @param tag the tag
     */
    public void element( String tag ) {
        element( tag, null );
    }
    
    /**
     * End the currently open XML element.
     */
    public void endElement() {
        unindent();
        String tag = xmlTags.removeFirst();
        
        if( tagIsOpen ) {
            tagIsOpen = false;
            writeDirect( "/>" );
            write( "\n" );
            elementJustEnded = true;
            return;
        }
        
        if( ! elementJustEnded ) write( "\n" );
        checkIndent();
        writeDirect( "</" + tag + ">" );
        write( "\n" );
        elementJustEnded = true;
    }

    /**
     * End all the open XML elements
     */
    public void endAllElements() {
        while( ! xmlTags.isEmpty() ) endElement();
    }
    
    /**
     * Write an XML attribute.  If the opening tag of the current XML element
     * has been closed (because content text has been written) then the
     * attribute will appear outside of that tag.
     * 
     * @param name the attribute name
     * @param value the attribute value (via a toString)
     */
    public void attribute( String name, Object value ) {
        writeDirect( " " );
        writeDirect( name );
        writeDirect( "='" );
        writeEscapedXMLString( "" + value, true );
        writeDirect( "'" );        
    }
}
