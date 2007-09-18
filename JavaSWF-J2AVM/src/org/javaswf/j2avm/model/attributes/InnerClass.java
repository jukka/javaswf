package org.javaswf.j2avm.model.attributes;

import org.epistem.io.IndentingPrintWriter;
import org.epistem.jclass.flags.ClassFlag;
import org.epistem.jclass.flags.MemberFlag;
import org.epistem.jclass.reference.JClassReference;

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
        
        InnerClassIsPublic    ( MemberFlag.MemberIsPublic.flag ),
        InnerClassIsPrivate   ( MemberFlag.MemberIsPrivate.flag ),
        InnerClassIsProtected ( MemberFlag.MemberIsProtected.flag ),
        InnerClassIsStatic    ( MemberFlag.MemberIsStatic.flag ),
        InnerClassIsFinal     ( MemberFlag.MemberIsFinal.flag ),
        InnerClassIsInterface ( ClassFlag.IsInterface.flag ),
        InnerClassIsAbstract  ( ClassFlag.IsAbstract.flag ),
        InnerClassIsAnnotation( ClassFlag.IsAnnotation.flag ),
        InnerClassIsEnum      ( ClassFlag.IsEnum.flag ),
        InnerClassIsSynthetic ( ClassFlag.IsSynthetic.flag );

        /** The flag value */
        public final int flag;

        /** Test whether this flag is set */
        public boolean isSet( int flags ) {
            return ( flag & flags) != 0;
        }
        
        /** Set this flag */
        public int set( int flags ) {
            return flags |= flag;
        }
        
        /** Set all the given flags */
        public static int set( InnerClassFlag...flags ) {
            int f = 0;
            for (InnerClassFlag flag : flags) {
                f = flag.set( f );
            }
            return f;
        }
        
        private InnerClassFlag( int flag ) { this.flag = flag; }
    }
    
    /**
     * The simple name of the inner class in the original source.
     * This is null if the inner class is anonymous.
     */
    public final String simpleName;
    
    /**
     * The inner class flags
     */
    public final int flags;
    
    /**
     * The outer (containing) class - null if the inner class is not a member.
     */
    public final JClassReference outerClass;
    
    /**
     * The inner class
     */
    public final JClassReference innerClass;
    
    /**
     * @param simpleName null if the class is anonymous
     * @param flags the flags
     * @param outerClass null if the inner class is not a member
     * @param innerClass the inner class
     */
    public InnerClass( String simpleName, int flags, 
                       JClassReference outerClass, JClassReference innerClass ) {
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
        out.print  ( "inner flags:" );
        for( InnerClassFlag flag : InnerClassFlag.values() ) {
            if( flag.isSet( flags ) ) out.print( " " + flag.name());
        }
        out.println();
        
        out.unindent();
        out.println( "}" );
    }
}
