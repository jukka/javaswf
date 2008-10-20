package com.anotherbigidea.flash.avm1.ops;

import static com.anotherbigidea.flash.SWFActionCodes.*;
import static com.anotherbigidea.flash.avm1.ops.Function.PreloadingFlag.*;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import com.anotherbigidea.flash.avm1.AVM1ActionBlock;
import com.anotherbigidea.flash.avm1.AVM1OpVisitor;
import com.anotherbigidea.flash.avm1.AVM1Operation;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.writers.ActionTextWriter;

/**
 * A function definition
 *
 * @author nickmain
 */
public class Function extends AVM1Operation {

    public static enum PreloadingFlag {        
        PRELOAD_PARENT    (START_FUNCTION2_PRELOAD_PARENT   ),
        PRELOAD_ROOT      (START_FUNCTION2_PRELOAD_ROOT     ),
        SUPPRESS_SUPER    (START_FUNCTION2_SUPRESS_SUPER    ),
        PRELOAD_SUPER     (START_FUNCTION2_PRELOAD_SUPER    ),
        SUPPRESS_ARGUMENTS(START_FUNCTION2_SUPRESS_ARGUMENTS),
        PRELOAD_ARGUMENTS (START_FUNCTION2_PRELOAD_ARGUMENTS),
        SUPPRESS_THIS     (START_FUNCTION2_SUPRESS_THIS     ),
        PRELOAD_THIS      (START_FUNCTION2_PRELOAD_THIS     ),
        PRELOAD_GLOBAL    (START_FUNCTION2_PRELOAD_GLOBAL   );
        
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
    
    //--Fake register stores for the common values and params
    public final StoreInRegister[] paramRegisters;
    public final StoreInRegister   thisRegister;
    public final StoreInRegister   argumentsRegister;
    public final StoreInRegister   superRegister;
    public final StoreInRegister   rootRegister;
    public final StoreInRegister   parentRegister;
    public final StoreInRegister   globalRegister;    
    
    public Function( String name, int numRegistersToAllocate,
                     String[] paramNames,
                     int[] registersForArguments,
                     int preloadingFlags ) {
        
        if( registersForArguments == null ) registersForArguments = new int[ paramNames.length ];
        
        this.name = name;
        this.numRegistersToAllocate = numRegistersToAllocate;
        this.paramNames = paramNames;
        this.registersForArguments = registersForArguments;
        
        Function.PreloadingFlag.decode( preloadingFlags, flags );
        int regNum = 1;
        
        thisRegister      = flags.contains( PRELOAD_THIS      ) ? new StoreInRegister( regNum++ ) : null; 
        argumentsRegister = flags.contains( PRELOAD_ARGUMENTS ) ? new StoreInRegister( regNum++ ) : null; 
        superRegister     = flags.contains( PRELOAD_SUPER     ) ? new StoreInRegister( regNum++ ) : null; 
        rootRegister      = flags.contains( PRELOAD_ROOT      ) ? new StoreInRegister( regNum++ ) : null; 
        parentRegister    = flags.contains( PRELOAD_PARENT    ) ? new StoreInRegister( regNum++ ) : null; 
        globalRegister    = flags.contains( PRELOAD_GLOBAL    ) ? new StoreInRegister( regNum++ ) : null; 
        
        paramRegisters = new StoreInRegister[ registersForArguments.length ];
        for( int i = 0; i < paramRegisters.length; i++ ) {
            if( registersForArguments[i] == 0 ) continue;            
            paramRegisters[i] = new StoreInRegister( registersForArguments[i] );
        }
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

    /** @see com.anotherbigidea.flash.avm1.AVM1Operation#accept(com.anotherbigidea.flash.avm1.AVM1OpVisitor) */
    @Override
    public void accept(AVM1OpVisitor visitor) {
        visitor.visitFunction( this );        
    }
}
