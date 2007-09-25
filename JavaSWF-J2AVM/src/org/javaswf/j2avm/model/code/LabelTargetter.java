package org.javaswf.j2avm.model.code;

import java.util.Set;

/**
 * Implemented by Instructions and exception handlers that target a
 * CodeLabel
 *
 * @author nickmain
 */
public interface LabelTargetter {

    /**
     * Get the target labels - placing them into the given set
     */
    public void targets( Set<CodeLabel> labels );
    
    /**
     * Release all the targets
     */
    public void release();
}
