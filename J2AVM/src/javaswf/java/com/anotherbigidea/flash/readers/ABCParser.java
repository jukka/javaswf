package com.anotherbigidea.flash.readers;

import static com.anotherbigidea.flash.avm2.TraitKind.TraitSlot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.epistem.io.InStream;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.InstanceInfoFlags;
import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.MultiNameKind;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.TraitFlags;
import com.anotherbigidea.flash.avm2.TraitKind;
import com.anotherbigidea.flash.avm2.instruction.Instruction;

/**
 * Parser for the AVM2 ABC file format.
 *
 * @author nickmain
 */
public class ABCParser {

    private final ABC abcFile; //the target to pass parsed contents to
    private final InStream in;
   
    /**
     * @param abcFile the target 
     * @param in the source
     */
    public ABCParser( ABC abcFile, InStream in ) {
        this.abcFile = abcFile;
        this.in      = in;
    }
    
    /**
     * Parse the contents of the file - calling endABC at the end
     */
    public void parse() throws IOException {
        int minor = in.readUI16();
        int major = in.readUI16();                
        abcFile.version( major, minor );
        
        readIntPool();
        readUIntPool();
        readDoublePool();
        readStringPool();
        readNamespacePool();
        readNSSetPool();
        readNamePool();
        readMethodInfos();        
        readMetadata();
        
        int count = in.readVU30();
        ABC.ClassInfos classInfos = abcFile.classes( count );
        readInstanceInfos( classInfos, count );
        readClassInfos   ( classInfos, count );
        if( classInfos != null ) classInfos.done();
        
        readScripts();        
        readMethodBodies();
        
        abcFile.done();
    }

    private void readMethodBodies() throws IOException {
        int count = in.readVU30();
        
        ABC.MethodBodies bodies = abcFile.methodBodies( count );
        
        for( int i = 0; i < count; i++ ) {
            int methodInfo = in.readVU30();
            int maxStack   = in.readVU30();
            int maxRegs    = in.readVU30();
            int scopeDepth = in.readVU30();
            int maxScope   = in.readVU30();
            
            ABC.MethodBody body = (bodies != null) ?                 
                bodies.methodBody( methodInfo, maxStack, maxRegs, scopeDepth, maxScope ) :
                null;

            int codeLength = in.readVU30();
            byte[] code    = in.read( codeLength );

            List<Instruction> instructionList = new ArrayList<Instruction>();
            Set<Integer> targetAddresses = new HashSet<Integer>();

            //build the instruction list
            ABC.Instructions instrs = (body != null) ? body.instructions( codeLength ) : null;
            if( instrs != null && ! ( instrs instanceof ABC.RawBytecode )) {
                InStream in = new InStream( code );

                while( in.getBytesRead() < codeLength ) {
                    Instruction instr = Instruction.parse( in );
                    targetAddresses.addAll( instr.getTargetAddresses() );
                    instructionList.add( instr );
                }                
            }
            
            //gather the exception handlers
            int handlerCount = in.readVU30();
            List<int[]> handlerList = new ArrayList<int[]>();

            for( int j = 0; j < handlerCount; j++ ) {                
                int start     = in.readVU30();
                int end       = in.readVU30();
                int target    = in.readVU30();
                int typeIndex = in.readVU30();
                int nameIndex = in.readVU30();
                
                handlerList.add( new int[] { start, end, target, typeIndex, nameIndex } );
                
                targetAddresses.add( start  );
                targetAddresses.add( end    );
                targetAddresses.add( target );
            }

            //pass the instructions
            if( instrs != null ) {
                
                if( instrs instanceof ABC.RawBytecode ) {
                    instrs.bytecode( code );
                }
                else {
                    for (Instruction in : instructionList) {
                        
                        //emit an address if the instruction is a target
                        if( targetAddresses.contains( in.getOffset() ) ) {
                            instrs.target( in.getOffset() );
                        }
                        
                        in.write( instrs );
                    }
                }
                
                instrs.done();
            }
            
            //pass the exception handlers
            ABC.ExceptionHandlers handlers = (body != null) ?
                    body.exceptionHandlers( handlerCount ) :
                    null;
            if( handlers != null) {
                for (int[] handler : handlerList) {
                    handlers.exceptionHandler( handler[0], handler[1], handler[2], handler[3], handler[4] );                    
                }
                
                handlers.done();
            }
            
            
            int traitCount = in.readVU30();
            ABC.Traits traits = (body != null) ?  body.traits( traitCount ) : null;            
            readTraits( traitCount, traits );
            
            if( body != null ) body.done();
        }
        
        if( bodies != null ) bodies.done();
    }    
    
    private void readScripts() throws IOException {
        int count = in.readVU30();
        
        ABC.Scripts scripts = abcFile.scripts( count );
        
        for( int i = 0; i < count; i++ ) {
            int initializerIndex = in.readVU30();            
            int traitCount = in.readVU30();
            ABC.Traits traits = (scripts != null) ? scripts.script( initializerIndex, traitCount ) : null;            
            readTraits( traitCount, traits );
        }
        
        if( scripts != null ) scripts.done();
    }
    
    private void readClassInfos( ABC.ClassInfos classInfos, int count ) throws IOException {
        for( int i = 0; i < count; i++ ) {
            int staticConsIndex = in.readVU30();            
            int traitCount = in.readVU30();
            ABC.Traits traits = (classInfos != null) ?
                                    classInfos.classInfo( staticConsIndex, traitCount ) :
                                    null;
            readTraits( traitCount, traits );
        }
    }
    
    private void readInstanceInfos( ABC.ClassInfos classInfos, int count ) throws IOException {
        for( int i = 0; i < count; i++ ) {
            
            int nameIndex  = in.readVU30();
            int superIndex = in.readVU30();
            int flags      = in.readUI8();

            int protectedNS = 0;
            if( InstanceInfoFlags.HasProtectedNS.isSet(flags) ) {
                protectedNS = in.readVU30();
            }
            
            int ifaceCount = in.readVU30();            
            int[] interfaces = new int[ ifaceCount ];
            for( int j = 0; j < interfaces.length; j++ ) {
                interfaces[j] = in.readVU30();
            }
            
            int constructorIndex = in.readVU30();
            
            int traitCount = in.readVU30();
            ABC.Traits traits =  (classInfos != null) ?
                    classInfos.instanceInfo( nameIndex, superIndex, flags, protectedNS, 
                                             interfaces, constructorIndex, traitCount ) :
                    null;
            
            readTraits( traitCount, traits );
        }        
    }

    private void readTraits( int count, ABC.Traits traits ) throws IOException {
        if( traits == null ) {
            traits = new ABC.Traits() { //Dummy implementation
                public void class_(int nameIndex, int slotId, int classIndex, int[] metadataIndices){}
                public void constant(int nameIndex, int slotId, int typeIndex, int valueIndex, int valueKind, int[] metadataIndices){}
                public void done(){}
                public void function(int nameIndex, int slotId, int methIndex, int[] metadataIndices){}
                public void getter(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices){}
                public void method(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices){}
                public void setter(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices){}
                public void slot(int nameIndex, int slotId, int typeIndex, int valueIndex, int valueKind, int[] metadataIndices){}                  
            };
        }
        
        for( int i = 0; i < count; i++ ) {
            int nameIndex = in.readVU30();
            int flags     = in.readUI8();
            
//            ((ABCDumper) abc).ipw.println( nameIndex + "-->0x" + Integer.toHexString(flags) );
//            
//            if( nameIndex == 119 || (flags & 0x0f) == 11 ) {
//                Hex.dump( ((ABCDumper) abc).ipw, in.read(64), 0, "", false );
//            }
                                    
            //System.out.println( "PARSE: " + nameIndex + " " + flags );
            
            TraitKind tkind = TraitKind.fromValue( flags );
            //flags >>= 4;
            
            switch( tkind ) {
                
                case TraitSlot:
                case TraitConst: {
                    int slotId     = in.readVU30();
                    int typeIndex  = in.readVU30();
                    int valueIndex = in.readVU30();
                    int valueKind  = (valueIndex != 0) ? in.readUI8() : 0;
                    
                    if( tkind == TraitSlot ) {
                        traits.slot( nameIndex, slotId, typeIndex, valueIndex, valueKind, readMetadataIndices( flags ) );
                    } else {
                        traits.constant( nameIndex, slotId, typeIndex, valueIndex, valueKind, readMetadataIndices( flags ) );
                    }
                    
                    break;
                }

                case TraitMethod:
                case TraitGetter:
                case TraitSetter: {
                    int dispId    = in.readVU30();
                    int methIndex = in.readVU30(); 
                    
                    boolean isFinal    = TraitFlags.IsFinal   .isSet( flags );
                    boolean isOverride = TraitFlags.IsOverride.isSet( flags );
                    
                    switch( tkind ) {
                        case TraitMethod: traits.method( nameIndex, dispId, methIndex, isFinal, isOverride, readMetadataIndices( flags ) ); break;
                        case TraitGetter: traits.getter( nameIndex, dispId, methIndex, isFinal, isOverride, readMetadataIndices( flags ) ); break;
                        case TraitSetter: traits.setter( nameIndex, dispId, methIndex, isFinal, isOverride, readMetadataIndices( flags ) ); break;
                    }
                    
                    break;
                }
                    
                case TraitClass: {
                    int slotId     = in.readVU30();
                    int classIndex = in.readVU30();
                    
                    traits.class_( nameIndex, slotId, classIndex, readMetadataIndices( flags ) );
                    break;
                }
                    
                case TraitFunction: {
                    int slotId    = in.readVU30();
                    int methIndex = in.readVU30();
                    
                    traits.function( nameIndex, slotId, methIndex, readMetadataIndices( flags ) );
                    break;
                }
            }
        }
        
        traits.done();
    }
    
    private int[] readMetadataIndices( int flags ) throws IOException {
        if( TraitFlags.HasMeta.isSet( flags )) {
            int metadataCount = in.readVU30();
            int[] metadataIndices = new int[ metadataCount ]; 
            
            for( int j = 0; j < metadataCount; j++ ) {
                metadataIndices[j] = in.readVU30();                    
            }
            
            return metadataIndices;
        } else {
            return new int[0];
        }
    }
        
    private void readMetadata() throws IOException {
        int count = in.readVU30();
        
        ABC.Metadata meta = abcFile.metadata( count );

        for( int i = 0; i < count; i++ ) {
            int nameIndex = in.readVU30();
            int valCount  = in.readVU30();
            
            int[] keys = new int[ valCount ];
            int[] vals = new int[ valCount ];
            for( int j = 0; j < vals.length; j++ ) {
                keys[j]= in.readVU30();
                vals[j]= in.readVU30();
            }
            
            if( meta != null ) meta.metadata( nameIndex, keys, vals );
        }
        
        if( meta != null ) meta.done();
    }
    
    private void readMethodInfos() throws IOException {
        int count = in.readVU30();
        
        ABC.MethodInfos infos = abcFile.methods( count );
        
        for( int i = 0; i < count; i++ ) {
            int paramCount = in.readVU30();
            int returnType = in.readVU30();
            
            int[] paramTypes = new int[ paramCount ];
            for( int j = 0; j < paramTypes.length; j++ ) {
                paramTypes[j] = in.readVU30();
            }
            
            int nameIndex = in.readVU30();
            int flags     = in.readUI8();
            
            int optionalCount = 0;
            int[] optValues   = null;
            int[] optValKinds = null;
            if( MethodInfoFlags.HasOptional.isSet(flags) ) {
                optionalCount = in.readVU30();
                
                optValues   = new int[ optionalCount ];
                optValKinds = new int[ optionalCount ];
                for( int j = 0; j < optionalCount; j++ ) {
                    optValues  [j] = in.readVU30();
                    optValKinds[j] = in.readUI8();
                }
            }
            
            int[] paramNames = null;
            if( MethodInfoFlags.HasParamNames.isSet(flags) ) {
                paramNames = new int[ paramCount ];
                for( int j = 0; j < paramNames.length; j++ ) {
                    paramNames[j] = in.readVU30();
                }
            }
            
            if( infos != null ) infos.methodInfo( nameIndex, flags, returnType, paramTypes, 
                                                  optValues, optValKinds, paramNames );
        }
        
        if( infos != null ) infos.done();
    }
    
    private void readNamePool() throws IOException {
        int count = in.readVU30();
        
        ABC.Names names = abcFile.namePool( count );        
        for( int i = 1; i < count; i++ ) {
            readName( names );
        } 
        if( names != null ) names.done();
    }
    
    private void readNSSetPool() throws IOException {
        int count = in.readVU30();
        
        ABC.NamespaceSets sets = abcFile.namespaceSetPool( count );

        for( int i = 1; i < count; i++ ) {
            int cnt2 = in.readVU30();

            int[] nspaces = new int[ cnt2 ];
            for( int j = 0; j < cnt2; j++ ) {
                nspaces[j] = in.readVU30();
            }            
            
            if( sets != null ) sets.namespaceSet( nspaces );
        }
        if( sets != null ) sets.done();        
    }
    
    private void readNamespacePool() throws IOException {
        int count = in.readVU30();
        
        ABC.Namespaces namespaces = abcFile.namespacePool( count );

        for( int i = 1; i < count; i++ ) {
            NamespaceKind kind  = NamespaceKind.parse(in);
            int index = in.readVU30();
            
            if( namespaces != null ) namespaces.namespace( kind, index );
        }
        if( namespaces != null ) namespaces.done();
    }
    
    private void readStringPool() throws IOException {
        int count = in.readVU30();
        if( count > 0 ) count--;
        String[] strings = new String[ count ];
        for( int i = 0; i < strings.length; i++ ) {            
            strings[i] = in.readVU30String();
        }
        abcFile.stringPool( strings );
    }
    
    private void readDoublePool() throws IOException {
        int count = in.readVU30();
        if( count > 0 ) count--;
        double[] doubles = new double[ count ];
        for( int i = 0; i < doubles.length; i++ ) {
            doubles[i] = in.readDoubleLE();
        }
        abcFile.doublePool( doubles );
    }
    
    private void readIntPool() throws IOException {
        int count = in.readVU30();
        if( count > 0 ) count--;
        int[] ints = new int[ count ];
        for( int i = 0; i < ints.length; i++ ) {
            ints[i] = in.readVS32();
        }
        abcFile.intPool( ints );
    }
    
    private void readUIntPool() throws IOException {
        int count = in.readVU30();
        if( count > 0 ) count--;
        long[] uints = new long[ count ];
        for( int i = 0; i < uints.length; i++ ) {
            uints[i] = in.readVU32();
        }
        abcFile.uintPool( uints );
    }
    
    private void readName( ABC.Names names ) throws IOException {        
        
        MultiNameKind kind = MultiNameKind.parse(in);
        int nameIndex      = 0;
        int namespaceIndex = 0;
        int nsSetIndex     = 0;
        
        switch( kind ) {
            case Qname: 
            case QnameA: {
                namespaceIndex = in.readVU30();
                nameIndex      = in.readVU30();
                break;
            }
    
            case RTQname:
            case RTQnameA: {
                nameIndex = in.readVU30();
                break;
            }
            
            case RTQnameL:
            case RTQnameLA: {
                break;
            }
    
            case Multiname:
            case MultinameA: {
                nameIndex  = in.readVU30();
                nsSetIndex = in.readVU30();
                break;
            }
    
            case MultinameL:
            case MultinameLA: {
                nsSetIndex = in.readVU30();
                break;
            }
        
            default: throw new IOException( "Unknown name kind: " + kind );
        }
        
        if( names != null ) names.name( kind, nameIndex, namespaceIndex, nsSetIndex );
    }    
}
