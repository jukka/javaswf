package org.javaswf.j2avm.model.attributes;

import org.epistem.io.IndentingPrintWriter;

/**
 * Debug info about a local variable (signature rather than type)
 *
 * @author nickmain
 */
public class LocalVariableTypeInfo {

    /** The first instruction offset at which the variable has a value */
    public final int startOffset;
    
    /** The offset length during which the value is valid */
    public final int offsetLength;
    
    /** The variable name */
    public final String name;
    
    /** The variable signature */
    public final String signature;
    
    /** The variable index */
    public final int varIndex;
   
    
    public LocalVariableTypeInfo( int startOffset, int offsetLength, String name,
                                  String signature, int varIndex ) {
        
        this.startOffset  = startOffset;
        this.offsetLength = offsetLength;
        this.name         = name;
        this.signature    = signature;
        this.varIndex     = varIndex;
    }
 
    /** @see java.lang.Object#equals(java.lang.Object) */
    @Override
    public boolean equals(Object obj) {
        if( obj == null ) return false;
        if( !( obj instanceof LocalVariableTypeInfo )) return false;
        
        LocalVariableTypeInfo info = (LocalVariableTypeInfo) obj;
        
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
                   (startOffset + offsetLength)  + " ) = " + name + " : " + signature );
    }
}
    