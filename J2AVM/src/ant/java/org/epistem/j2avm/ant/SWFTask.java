package org.epistem.j2avm.ant;

import org.epistem.j2avm.swf.SWFBuilder;

/**
 * Implemented by ANT tasks that operate on a SWF file. These tasks should
 * only be used inside a SWFBuilderTask container.
 *
 * @author nickmain
 */
public interface SWFTask {

    /**
     * Set the SWF to operate on
     */
    public void setSWFBuilder( SWFBuilder swfBuilder );
}
