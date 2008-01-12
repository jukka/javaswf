package com.anotherbigidea.flash.avm2.model.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.epistem.io.InStream;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.ArgType;
import com.anotherbigidea.flash.avm2.InstanceInfoFlags;
import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.MultiNameKind;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.ValueKind;
import com.anotherbigidea.flash.avm2.instruction.Instruction;
import com.anotherbigidea.flash.avm2.instruction.InstructionsImpl;
import com.anotherbigidea.flash.avm2.model.AVM2ABCFile;
import com.anotherbigidea.flash.avm2.model.AVM2Class;
import com.anotherbigidea.flash.avm2.model.AVM2ClassSlot;
import com.anotherbigidea.flash.avm2.model.AVM2DefaultValue;
import com.anotherbigidea.flash.avm2.model.AVM2ExceptionHandler;
import com.anotherbigidea.flash.avm2.model.AVM2FunctionSlot;
import com.anotherbigidea.flash.avm2.model.AVM2Getter;
import com.anotherbigidea.flash.avm2.model.AVM2Metadata;
import com.anotherbigidea.flash.avm2.model.AVM2Method;
import com.anotherbigidea.flash.avm2.model.AVM2MethodBody;
import com.anotherbigidea.flash.avm2.model.AVM2MethodSlot;
import com.anotherbigidea.flash.avm2.model.AVM2Name;
import com.anotherbigidea.flash.avm2.model.AVM2Namespace;
import com.anotherbigidea.flash.avm2.model.AVM2QName;
import com.anotherbigidea.flash.avm2.model.AVM2Script;
import com.anotherbigidea.flash.avm2.model.AVM2Setter;
import com.anotherbigidea.flash.avm2.model.AVM2Slot;
import com.anotherbigidea.flash.avm2.model.AVM2Trait;
import com.anotherbigidea.flash.avm2.model.AVM2Traits;

/**
 * ABC implementation to build AVM2ABCFile objects.
 *
 * @author nickmain
 */
public class AVM2ABCBuilder implements ABC {

    /** The object that is built */
    public AVM2ABCFile file;

    
    final ConstantPool pool = new ConstantPool();
    
    final List<AVM2Method>   methods  = new ArrayList<AVM2Method>();
    final List<AVM2Method>   closures = new ArrayList<AVM2Method>();
    final List<AVM2Metadata> metadata = new ArrayList<AVM2Metadata>();
    
    /** @see com.anotherbigidea.flash.avm2.ABC#version(int, int) */
    public void version(int majorVersion, int minorVersion) {
        file = new AVM2ABCFile( majorVersion, minorVersion );        
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#classes(int) */
    public ClassInfos classes(int count) {
        return new ABC.ClassInfos() {
            int classIndex = 0;
            
            /** @see com.anotherbigidea.flash.avm2.ABC.ClassInfos#classInfo(int, int) */
            public Traits classInfo(int constructorIndex, int traitCount) {
                
                AVM2Class clazz = null;
                for( AVM2Class c : file.classes.values() ) {
                    if( c.index == classIndex ) {
                        clazz = c;
                        break;
                    }
                }
                classIndex++;
                
                clazz.staticInitializer = methods.get( constructorIndex );
                closures.set( constructorIndex, null );
                
                return new TraitsImpl( clazz.staticTraits );
            }

            /** @see com.anotherbigidea.flash.avm2.ABC.ClassInfos#instanceInfo(int, int, int, int, int[], int, int) */
            public Traits instanceInfo(int nameIndex, int superIndex, int flags, int protectedNS, int[] interfaces, int constructorIndex, int traitCount) {
                
                boolean isSealed    = InstanceInfoFlags.Sealed.isSet( flags );
                boolean isInterface = InstanceInfoFlags.Interface.isSet( flags );
                boolean isFinal     = InstanceInfoFlags.Final.isSet( flags );
                
                AVM2QName     name = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
                AVM2Name      superclass = AVM2Name.atIndex( pool, superIndex );                    
                AVM2Namespace protectedNamespace = AVM2Namespace.atIndex( pool, protectedNS );
                
                AVM2Class clazz = file.addClass(name, superclass, isSealed, isFinal, isInterface, protectedNamespace);
                clazz.constructor = methods.get( constructorIndex );
                closures.set( constructorIndex, null );

                for( int i : interfaces ) {
                    clazz.addInterface( AVM2Name.atIndex( pool, i ));
                }
                
                return new TraitsImpl( clazz.traits );
            }

            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // nada
            }
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#doublePool(double[]) */
    public void doublePool(double[] doubles) {
        pool.initDoublePool( doubles );
        
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#intPool(int[]) */
    public void intPool(int[] ints) {
        pool.initIntPool( ints );            
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#metadata(int) */
    public Metadata metadata(int count) {
        return new ABC.Metadata() {
            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // nada
            }

            /** @see com.anotherbigidea.flash.avm2.ABC.Metadata#metadata(int, int[], int[]) */
            public void metadata(int nameIndex, int[] keys, int[] values) {
                AVM2Metadata md = new AVM2Metadata( pool.stringAt( nameIndex ));
                metadata.add( md );
                
                for (int i = 0; i < values.length; i++) {
                    String key = (keys[i] != 0) ? pool.stringAt( keys[i] ) : null;
                    String val = pool.stringAt( values[i] );                        
                    md.addValue( key, val );
                }
            }                
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#methodBodies(int) */
    public MethodBodies methodBodies(int count) {
        return new ABC.MethodBodies() {
            /** @see com.anotherbigidea.flash.avm2.ABC.MethodBodies#methodBody(int, int, int, int, int) */
            public MethodBody methodBody(int methodInfo, int maxStack, int maxRegs, int scopeDepth, int maxScope) {
                
                final AVM2MethodBody body = methods.get( methodInfo ).methodBody;
                body.maxStack     = maxStack;
                body.maxRegisters = maxRegs;
                body.scopeDepth   = scopeDepth;
                body.maxScope     = maxScope;
                
                return new ABC.MethodBody() {

                    final RawBytecodeImpl bytecodeImpl = new RawBytecodeImpl( body );
                    
                    /** @see org.epistem.io.PipelineInterface#done() */
                    public void done() {
                        bytecodeImpl.processInstructions();
                    }

                    /** @see com.anotherbigidea.flash.avm2.ABC.MethodBody#exceptionHandlers(int) */
                    public ExceptionHandlers exceptionHandlers(int handlerCount) {
                        return new ABC.ExceptionHandlers() {
                            /** @see com.anotherbigidea.flash.avm2.ABC.ExceptionHandlers#exceptionHandler(int, int, int, int, int) */
                            public void exceptionHandler(int start, int end, int target, int typeIndex, int nameIndex) {
                                
                                body.exceptionHandlers.add( 
                                    new AVM2ExceptionHandler(
                                        start, end, target,
                                        AVM2Name.atIndex( pool, typeIndex ),
                                        (nameIndex > 0) ? AVM2Name.atIndex( pool, nameIndex ) : null
                                    ));
                            }

                            /** @see org.epistem.io.PipelineInterface#done() */
                            public void done() {
                                // nada                                    
                            }
                        };
                    }

                    /** @see com.anotherbigidea.flash.avm2.ABC.MethodBody#instructions(int) */
                    public Instructions instructions(int codeSize) {                            
                        return bytecodeImpl;
                    }

                    /** @see com.anotherbigidea.flash.avm2.ABC.MethodBody#traits(int) */
                    public Traits traits(int traitCount) {
                        return new TraitsImpl( body.activationTraits );
                    }
                };
            }

            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // nada
            }
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#methods(int) */
    public MethodInfos methods(int count) {
        return new ABC.MethodInfos() {
            int index = 0;
            
            /** @see com.anotherbigidea.flash.avm2.ABC.MethodInfos#methodInfo(int, int, int, int[], int[], int[], int[]) */
            public void methodInfo(int nameIndex, int flags, int returnType, int[] paramTypes, int[] optValues, int[] optValKinds, int[] paramNames) {
                
                AVM2Name retType = AVM2Name.atIndex( pool, returnType );                    
                AVM2Method m = new AVM2Method( retType, MethodInfoFlags.decode( flags ), index++ );
                methods.add( m );
                closures.add( m );
                
                if( nameIndex > 0 ) m.name = pool.stringAt( nameIndex );
                
                for( int i = 0; i < paramTypes.length; i++ ) {
                    AVM2Name type = AVM2Name.atIndex( pool, paramTypes[i] );
                    String   name = null;
                    if( paramNames != null && i < paramNames.length ) {
                        name = pool.stringAt( paramNames[i] );
                    }
                    
                    AVM2DefaultValue value = null;                        
                    if( optValKinds != null 
                     && i >= (paramTypes.length - optValKinds.length) ) {
                        int idx = i - (paramTypes.length - optValKinds.length);
                        
                        if( optValKinds[ idx ] == 0 ) {
                            //System.err.println( "value index ---> " + optValues[ idx ] );
                        }
                        else {
                            ValueKind kind = ValueKind.fromValue( optValKinds[ idx ] );
                            Object    val  = kind.getPoolValue( pool, optValues[ idx ] );
                            value = new AVM2DefaultValue( kind, val );
                        }
                    }
                    
                    m.addParameter( name, type, value );
                }
            }

            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // nada
            }
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#namePool(int) */
    public Names namePool(int count) {
        return new ABC.Names() {
            /** @see com.anotherbigidea.flash.avm2.ABC.Names#name(com.anotherbigidea.flash.avm2.MultiNameKind, int, int, int) */
            public void name(MultiNameKind kind, int nameIndex, int namespaceIndex, int namespaceSetIndex) {
                pool.addName( kind, nameIndex, namespaceIndex, namespaceSetIndex );                    
            }

            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // done                    
            }
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#namespacePool(int) */
    public Namespaces namespacePool(int count) {
        return new ABC.Namespaces() {
            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // nada
            }

            /** @see com.anotherbigidea.flash.avm2.ABC.Namespaces#namespace(com.anotherbigidea.flash.avm2.NamespaceKind, int) */
            public void namespace(NamespaceKind kind, int index) {
                pool.addNamespace( kind, index );
            }
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#namespaceSetPool(int) */
    public NamespaceSets namespaceSetPool(int count) {
        return new ABC.NamespaceSets() {
            /** @see com.anotherbigidea.flash.avm2.ABC.NamespaceSets#namespaceSet(int[]) */
            public void namespaceSet(int[] namespaces) {
                pool.addNamespaceSet( namespaces );
            }

            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // nada
            }
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#scripts(int) */
    public Scripts scripts(int count) {
        return new ABC.Scripts() {
            /** @see com.anotherbigidea.flash.avm2.ABC.Scripts#script(int, int) */
            public Traits script(int initializerIndex, int traitCount) {                    
                AVM2Script script = file.addScript( methods.get( initializerIndex ));
                closures.set( initializerIndex, null );

                return new TraitsImpl( script.traits );
            }

            /** @see org.epistem.io.PipelineInterface#done() */
            public void done() {
                // nada
            }
        };
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#stringPool(java.lang.String[]) */
    public void stringPool(String[] strings) {
        pool.initStringPool( strings );
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#uintPool(long[]) */
    public void uintPool(long[] uints) {
        pool.initUIntPool( uints );            
    }

    /** @see org.epistem.io.PipelineInterface#done() */
    public void done() {
        //save the methods that were not used
        
        for( AVM2Method method : closures ) {
            if( method != null ) {
                file.addFunctionClosure( method );
            }
        }
    }        
 
   
    public class TraitsImpl implements ABC.Traits {

        final AVM2Traits traits;
        
        TraitsImpl( AVM2Traits traits ) {
            this.traits   = traits;
        }

        private void initMetadata( int[] metadataIndices, AVM2Trait trait ) {
            for( int i : metadataIndices ) {
                trait.metadata.add( metadata.get( i ));
            }
        }
        
        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#class_(int, int, int, int[]) */
        public void class_(int nameIndex, int slotId, int classIndex, int[] metadataIndices) {
            
            AVM2QName name  = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
            AVM2Class clazz = null;
            for( AVM2Class c : file.classes.values() ) {
                if( c.index == classIndex ) {
                    clazz = c;
                    break;
                }
            }
            
            AVM2ClassSlot slot = traits.addClass( name, clazz.name );
            slot.indexId = slotId - 1;
            
            initMetadata( metadataIndices, slot );           
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#constant(int, int, int, int, int, int[]) */
        public void constant(int nameIndex, int slotId, int typeIndex, int valueIndex, int valueKind, int[] metadataIndices) {
            AVM2QName name = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
            AVM2Name  type = AVM2Name.atIndex( pool, typeIndex );

            AVM2DefaultValue value = null;
            if( valueKind > 0 ) {
                ValueKind kind = ValueKind.fromValue( valueKind );
                Object    val  = kind.getPoolValue( pool, valueIndex );
                value = new AVM2DefaultValue( kind, val );
            }
            
            AVM2Slot slot = traits.addConst( name, type, value );
            slot.indexId = slotId - 1;
            initMetadata( metadataIndices, slot );
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#function(int, int, int, int[]) */
        public void function(int nameIndex, int slotId, int methIndex, int[] metadataIndices) {
            AVM2QName name = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
            AVM2Method method = methods.get( methIndex );
            closures.set( methIndex, null );
            
            AVM2FunctionSlot slot = traits.addFunction( name, method );
            slot.indexId = slotId - 1;
            initMetadata( metadataIndices, slot );            
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#getter(int, int, int, boolean, boolean, int[]) */
        public void getter(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices) {
            AVM2QName name = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
            AVM2Method method = methods.get( methIndex );
            closures.set( methIndex, null );

            AVM2Getter slot = traits.addGetter( name, method, isFinal, isOverride );
            slot.indexId = dispId - 1;
            initMetadata( metadataIndices, slot );            
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#method(int, int, int, boolean, boolean, int[]) */
        public void method(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices) {
            AVM2QName name = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
            AVM2Method method = methods.get( methIndex );
            closures.set( methIndex, null );

            AVM2MethodSlot slot = traits.addMethod( name, method, isFinal, isOverride );
            slot.indexId = dispId - 1;
            initMetadata( metadataIndices, slot );            
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#setter(int, int, int, boolean, boolean, int[]) */
        public void setter(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices) {
            AVM2QName name = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
            AVM2Method method = methods.get( methIndex );
            closures.set( methIndex, null );

            AVM2Setter slot = traits.addSetter( name, method, isFinal, isOverride );
            slot.indexId = dispId - 1;
            initMetadata( metadataIndices, slot );            
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#slot(int, int, int, int, int, int[]) */
        public void slot(int nameIndex, int slotId, int typeIndex, int valueIndex, int valueKind, int[] metadataIndices) {
            
            AVM2QName name = (AVM2QName) AVM2Name.atIndex( pool, nameIndex );
            AVM2Name  type = AVM2Name.atIndex( pool, typeIndex );

            AVM2DefaultValue value = null;
            if( valueKind > 0 ) {
                ValueKind kind = ValueKind.fromValue( valueKind );
                Object    val  = kind.getPoolValue( pool, valueIndex );
                value = new AVM2DefaultValue( kind, val );
            }
            
            AVM2Slot slot = traits.addVar( name, type, value );
            slot.indexId = slotId - 1;
            initMetadata( metadataIndices, slot );
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada
        }
    }

    public class RawBytecodeImpl extends InstructionsImpl implements ABC.RawBytecode {

        final AVM2MethodBody body;
        
        final List<Instruction> instructionList = new ArrayList<Instruction>();
        final Set<Integer>      targetAddresses = new HashSet<Integer>();
        
        RawBytecodeImpl( AVM2MethodBody body ) {
            this.body     = body;
        }
        
        /** @see com.anotherbigidea.flash.avm2.instruction.InstructionsImpl#bytecode(byte[]) */
        @Override
        public void bytecode(byte[] code) {
            InStream in = new InStream( code );

            while( in.getBytesRead() < code.length ) {
                try {
                    Instruction instr = Instruction.parse( in );
                    targetAddresses.addAll( instr.getTargetAddresses() );
                    instructionList.add( instr );
                    
                } catch( IOException ioe ) {
                    throw new RuntimeException( ioe );
                }            
            }   
        }
        
        /** Convert instruction args and add to the body */
        public void processInstructions() {
            //gather the exception handler offsets
            for( AVM2ExceptionHandler h : body.exceptionHandlers ) {
                targetAddresses.add( h.start  );
                targetAddresses.add( h.end    );
                targetAddresses.add( h.handler );
            }
            
            for( Instruction i : instructionList ) {
                
                //add target if necessary
                if( targetAddresses.contains( i.getOffset())) {
                    body.instructions.appendTarget( i.getOffset());
                }
                
                Object[] args = i.arguments;
                for( int j = 0; j < args.length; j++ ) {
                    args[j] = makePooless( i.operation.argTypes[j], args[j] );
                }
                
                body.instructions.append( i );
            }
        }
        
        /**
         * Convert a value to a form that does not rely on the constant pool.
         */
        public Object makePooless( ArgType type, Object value ) {
            
            switch( type ) {
                case CLASS_INDEX: {
                    return file.classAtIndex((Integer) value ).name;
                }
                case INT_INDEX: {
                    return pool.intAt( (Integer) value );
                }
                case UINT_INDEX: {
                    return pool.uintAt( (Integer) value );
                }
                case NAMESPACE_INDEX: {
                    return new AVM2Namespace( pool.namespaceAt( (Integer) value ));
                }
                case STRING_INDEX: {
                    return pool.stringAt( (Integer) value );
                }
                case DOUBLE_INDEX: {
                    return pool.doubleAt( (Integer) value );
                }
                case NAME_INDEX: {
                    return AVM2Name.atIndex( pool, (Integer) value );
                }
                case SWITCH_OFFSETS:
                case DEFAULT_OFFSET:
                case CATCH_INDEX:
                case METHOD_INDEX:
                case LINE_NUMBER:
                case ARG_COUNT:
                case KEY_VALUE_COUNT:    
                case REGISTER:
                case TARGET_REGISTER:
                case INDEX_REGISTER:
                case METHOD_ID:
                case SHORT:    
                case DISP_ID:
                case SLOT_ID: 
                case SCOPE_INDEX: 
                case BYTE:
                case OFFSET:
                default: return value;
            }
        }

    }

 
}
