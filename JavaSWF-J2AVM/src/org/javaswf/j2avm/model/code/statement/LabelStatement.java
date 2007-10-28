package org.javaswf.j2avm.model.code.statement;

import java.util.HashSet;
import java.util.Set;

/**
 * A label statement that forms the target for branches
 *
 * @author nickmain
 */
public final class LabelStatement extends Statement {

	final Set<LabelTargetter> targetters = new HashSet<LabelTargetter>();

    /**
     * The toString-unique (within method) name of the label.
     */
    public final Object name;
    
    LabelStatement( Object name ) {
        this.name = name;
    }
    
    /** @see org.javaswf.j2avm.model.code.statement.Statement#accept(org.javaswf.j2avm.model.code.statement.StatementVisitor) */
	@Override
	public void accept( StatementVisitor visitor ) {
		visitor.visitLabel( this );
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
        } 
        else if( !name.toString().equals( other.name.toString() ) ) return false;
        return true;
    }
    
    @Override
    public String toString() {
    	return name.toString();
    }
}
