package com.anotherbigidea.flash.avm1.ops;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1BlockContainer;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.writers.ActionTextWriter;

import static com.anotherbigidea.flash.SWFActionCodes.*;

/**
 * A function definition
 *
 * @author nickmain
 */
public class Function extends AVM1Operation implements AVM1BlockContainer {

    public static enum PreloadingFlag {        
        PRELOAD_PARENT   (START_FUNCTION2_PRELOAD_PARENT   ),
        PRELOAD_ROOT     (START_FUNCTION2_PRELOAD_ROOT     ),
        SUPRESS_SUPER    (START_FUNCTION2_SUPRESS_SUPER    ),
        PRELOAD_SUPER    (START_FUNCTION2_PRELOAD_SUPER    ),
        SUPRESS_ARGUMENTS(START_FUNCTION2_SUPRESS_ARGUMENTS),
        PRELOAD_ARGUMENTS(START_FUNCTION2_PRELOAD_ARGUMENTS),
        SUPRESS_THIS     (START_FUNCTION2_SUPRESS_THIS     ),
        PRELOAD_THIS     (START_FUNCTION2_PRELOAD_THIS     ),
        PRELOAD_GLOBAL   (START_FUNCTION2_PRELOAD_GLOBAL   );
        
        public final int bits;
        private PreloadingFlag( int bits ) { this.bits = bits; }
        
        /** Decode a set of bits */
        public static void decode( int bits, Set<PreloadingFlag> flags ) {
            for( PreloadingFlag flag : values() ) {
                if(( bits & flag.bits ) != 0 ) flags.add( flag );
            }
        }
        
        /** Encode a set of flags */
        public static int encode( Set<PreloadingFlag> flags ) {
            int bits = 0;
            for( PreloadingFlag flag : flags ) {
                bits |= flag.bits;
            }
            return bits;
        }
    }
    
    public final AVM1ActionBlock body = new AVM1ActionBlock( this );

    public final String name;  //empty for an anonymous function
    public final int numRegistersToAllocate;
    public final Set<PreloadingFlag> flags = EnumSet.noneOf( PreloadingFlag.class );
    public final String[] paramNames;
    public final int[] registersForArguments;
    
    public Function( String name, int numRegistersToAllocate,
                     String[] paramNames,
                     int[] registersForArguments ) {
        
        if( registersForArguments == null ) registersForArguments = new int[ paramNames.length ];
        
        this.name = name;
        this.numRegistersToAllocate = numRegistersToAllocate;
        this.paramNames = paramNames;
        this.registersForArguments = registersForArguments;
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1BlockContainer#subBlocks() */
    public AVM1ActionBlock[] subBlocks() {
        return new AVM1ActionBlock[] { body };
    }
    
    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#write(com.anotherbigidea.flash.interfaces.SWFActionBlock) */
    @Override
    public void write( SWFActionBlock block ) throws IOException {
        int bits = PreloadingFlag.encode( flags );
        
        SWFActionBlock bodyBlock = 
            block.startFunction2( 
                name, numRegistersToAllocate, bits, paramNames, registersForArguments );
        
        body.write( bodyBlock );
    }

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#print(com.anotherbigidea.flash.writers.ActionTextWriter) */
    @Override
    public void print(ActionTextWriter writer) throws IOException {
        int bits = PreloadingFlag.encode( flags );        
        writer.startFunction2( name, numRegistersToAllocate, bits, paramNames, registersForArguments );
        body.print( writer );
        writer.end();
    }
}
