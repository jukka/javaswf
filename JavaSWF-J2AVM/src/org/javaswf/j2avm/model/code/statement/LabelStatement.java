package org.javaswf.j2avm.model.code.statement;

/**
 * A label statement that forms the target for branches
 *
 * @author nickmain
 */
public class LabelStatement {

    /**
     * The unique (within method) name of the label.
     */
    public final String name;
    
    LabelStatement( String name ) {
        this.name = name;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if( this == obj ) return true;
        if( obj == null ) return false;
        if( getClass() != obj.getClass() ) return false;
        final LabelStatement other = (LabelStatement) obj;
        if( name == null ) {
            if( other.name != null ) return false;
        } else if( !name.equals( other.name ) ) return false;
        return true;
    }
}
