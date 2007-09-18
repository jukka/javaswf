package org.javaswf.j2avm.model.attributes;

import java.util.Collection;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.util.Flag;
import org.epistem.util.FlagParser;
import org.javaswf.j2avm.model.types.ObjectType;

/**
 * Information about an inner class
 *
 * @author nickmain
 */
public class InnerClass {

    /**
     * The flags that apply to inner classes
     */
    public static enum InnerClassFlag {
        
        @Flag( 0x0001 ) InnerClassIsPublic    ,
        @Flag( 0x0002 ) InnerClassIsPrivate   ,
        @Flag( 0x0004 ) InnerClassIsProtected ,
        @Flag( 0x0008 ) InnerClassIsStatic    ,
        @Flag( 0x0010 ) InnerClassIsFinal     ,
        @Flag( 0x0200 ) InnerClassIsInterface ,
        @Flag( 0x0400 ) InnerClassIsAbstract  ,
        @Flag( 0x2000 ) InnerClassIsAnnotation,
        @Flag( 0x4000 ) InnerClassIsEnum      ,
        @Flag( 0x1000 ) InnerClassIsSynthetic ;

        /**
         * Parser for the flags
         */
        public static final FlagParser<InnerClassFlag> parser = 
            new FlagParser<InnerClassFlag>( InnerClassFlag.class );
    }
    
    /**
     * The simple name of the inner class in the original source.
     * This is null if the inner class is anonymous.
     */
    public final String simpleName;
    
    /**
     * The inner class flags
     */
    public final Collection<InnerClassFlag> flags;
    
    /**
     * The outer (containing) class - null if the inner class is not a member.
     */
    public final ObjectType outerClass;
    
    /**
     * The inner class
     */
    public final ObjectType innerClass;
    
    /**
     * @param simpleName null if the class is anonymous
     * @param flags the flags
     * @param outerClass null if the inner class is not a member
     * @param innerClass the inner class
     */
    public InnerClass( String simpleName, 
                       ObjectType outerClass, ObjectType innerClass,
                       Collection<InnerClassFlag> flags ) {
        this.outerClass = outerClass;
        this.innerClass = innerClass;
        this.simpleName = simpleName;
        this.flags      = flags;
    }

    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof InnerClass )) return false;
        return ((InnerClass) obj).innerClass.equals( innerClass );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return innerClass.hashCode();
    }
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        out.println( "innerclass {" );
        out.indent();
        
        out.println( "simple name: " + simpleName );
        out.println( "inner class: " + innerClass );
        out.println( "outer class: " + outerClass );
        out.print  ( "inner flags: " + flags );
        out.println();
        
        out.unindent();
        out.println( "}" );
    }
}
