package org.javaswf.j2avm.model.parser;

import org.epistem.io.IndentingPrintWriter;

/**
 * A set of offsets for switch instructions.
 *
 * @author nickmain
 */
public class SwitchOffsets {

    /**
     * The case values, in ascending order
     */
    public final int[] cases;
    
    /**
     * The absolute offsets corresponding to each case value
     */
    public final int[] offsets;
    
    /**
     * The absolute offset for the default case
     */
    public final int defaultOffset;
    
    /**
     * @param defaultOffset the default absolute offset
     * @param cases the case values in ascending order
     * @param offsets the corresponding absolute branch offsets
     */
    public SwitchOffsets( int defaultOffset, int[] cases, int[] offsets ) {
        this.cases         = cases;
        this.offsets       = offsets;
        this.defaultOffset = defaultOffset;
    }
    
    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {   
        out.println();
        out.indent();
        out.indent();

        for (int i = 0; i < cases.length; i++) {
            out.writeField( "" + cases[i], 7, false );
            out.println( " --> " + offsets[i] );
        }
        
        out.print( "default --> " + defaultOffset );
                
        out.unindent();
        out.unindent();
    }
}
