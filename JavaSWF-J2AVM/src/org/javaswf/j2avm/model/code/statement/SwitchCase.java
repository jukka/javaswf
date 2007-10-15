package org.javaswf.j2avm.model.code.statement;

/**
 * A switch case.
 *
 * @author nickmain
 */
public class SwitchCase implements Comparable<SwitchCase> {

    /**
     * The case value
     */
    public final int caseValue;
    
    /**
     * The target statement
     */
    public final LabelStatement target;
    
    SwitchCase( int caseValue, LabelStatement target ) {
        this.caseValue = caseValue;
        this.target    = target; 
    }

    /**
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo( SwitchCase sc ) {       
        return caseValue - sc.caseValue;
    }

    @Override
    public boolean equals( Object obj ) {
        if( obj == null || !( obj instanceof SwitchCase )) return false;
        
        SwitchCase other = (SwitchCase) obj;
        return caseValue == other.caseValue 
            && target.equals( other.target );
    }

    @Override
    public int hashCode() {        
        return target.hashCode() * caseValue;
    }
}
