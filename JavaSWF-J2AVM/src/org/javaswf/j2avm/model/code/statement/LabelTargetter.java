package org.javaswf.j2avm.model.code.statement;

import java.util.Set;

/**
 * Implemented by statements that target LabelStatements
 *
 * @author nickmain
 */
public interface LabelTargetter {

    /**
     * Get the target labels - placing them into the given set
     */
    public void targets( Set<LabelStatement> labels );
    
    /**
     * Release all the targets
     */
    public void release();
	
}
