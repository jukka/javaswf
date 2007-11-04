package org.javaswf.j2avm.model.code.statement;

/**
 * A switch case.
 *
 * @author nickmain
 */
public class SwitchCase implements Comparable<SwitchCase> {

	/*pkg*/ LabelStatement label;
	
    /**
     * The case value
     */
    public final int caseValue;
    
    /**
     * The target name
     */
    public final Object targetName;
    
    /**
     * Get the target label 
     * 
     * @return null if the switch is not part of a statement list
     */
    public LabelStatement label() {
    	return label;
    }
    
    SwitchCase( int caseValue, Object targetName ) {
        this.caseValue  = caseValue;
        this.targetName = targetName; 
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
            && targetName.equals( other.targetName );
    }

    @Override
    public int hashCode() {        
        return targetName.hashCode() * caseValue;
    }
}
