package org.javaswf.j2avm.model.attributes;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Debug info about a local variable
 *
 * @author nickmain
 */
public class LocalVariableInfo {

    /** The first instruction offset at which the variable has a value */
    public final int startOffset;
    
    /** The offset length during which the value is valid */
    public final int offsetLength;
    
    /** The variable name */
    public final String name;
    
    /** The variable type */
    public final ValueType type;
    
    /** The variable index */
    public final int varIndex;
   
    
    public LocalVariableInfo( int startOffset, int offsetLength, String name,
    		                  ValueType type, int varIndex ) {
        
        this.startOffset  = startOffset;
        this.offsetLength = offsetLength;
        this.name         = name;
        this.type         = type;
        this.varIndex     = varIndex;
    }
 
    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof LocalVariableInfo )) return false;
        
        LocalVariableInfo info = (LocalVariableInfo) obj;
        
        return ( info.startOffset == startOffset )
            && ( info.varIndex    == varIndex );
    }

    /** @see java.lang.Object#hashCode() */
    @Override
    public int hashCode() {
        return startOffset * varIndex;
    }

    /** Dump for debug purposes */
    public void dump( IndentingPrintWriter out ) {
        out.print( varIndex + " ( " + startOffset + " .. " + 
                   (startOffset + offsetLength)  + " ) = " + name + " : " + type );
    }
}
    