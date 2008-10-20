package com.anotherbigidea.flash.writers;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.epistem.io.OutStream;
import org.epistem.io.OutStreamWrapper;

import com.anotherbigidea.flash.avm2.ABC;
import com.anotherbigidea.flash.avm2.ArgType;
import com.anotherbigidea.flash.avm2.InstanceInfoFlags;
import com.anotherbigidea.flash.avm2.MethodInfoFlags;
import com.anotherbigidea.flash.avm2.MultiNameKind;
import com.anotherbigidea.flash.avm2.NamespaceKind;
import com.anotherbigidea.flash.avm2.Operation;
import com.anotherbigidea.flash.avm2.TraitFlags;
import com.anotherbigidea.flash.avm2.TraitKind;

/**
 * Implementation of ABC that writes to a SWF tag.
 *
 * @author nickmain
 */
public class ABCWriter implements ABC {

    private OutStreamWrapper out;
    
    /** Write to the given out stream */
    public ABCWriter( OutStream out ) {
        this.out = new OutStreamWrapper( out );
    }
     
    /** @see com.anotherbigidea.flash.avm2.ABC#version(int, int) */
    public void version( int majorVersion, int minorVersion ) {
        out.writeUI16( minorVersion );
        out.writeUI16( majorVersion );
    }
    
    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#classes(int) */
    public ClassInfos classes(int count) {
        out.writeVU30( count );
        return new ClassInfosWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#doublePool(double[]) */
    public void doublePool(double[] doubles) {
        out.writeVU30( doubles.length > 0 ? doubles.length + 1 : 0 );
        for (double d : doubles) {
            out.writeDoubleLE( d );
        }            
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#intPool(int[]) */
    public void intPool(int[] ints) {
        out.writeVU30( ints.length > 0 ? ints.length + 1 : 0 );
        for (int i : ints) {
            out.writeVS32( i );
        }
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#metadata(int) */
    public Metadata metadata(int count) {
        out.writeVU30( count );
        return new MetadataWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#methodBodies(int) */
    public MethodBodies methodBodies(int count) {
        out.writeVU30( count );
        return new MethodBodiesWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#methods(int) */
    public MethodInfos methods(int count) {
        out.writeVU30( count );
        return new MethodInfosWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#namePool(int) */
    public Names namePool(int count) {
        out.writeVU30( count );
        return new NamesWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#namespacePool(int) */
    public Namespaces namespacePool(int count) {
        out.writeVU30( count );
        return new NamespacesWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#namespaceSetPool(int) */
    public NamespaceSets namespaceSetPool(int count) {
        out.writeVU30( count );
        return new NamespaceSetsWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#scripts(int) */
    public Scripts scripts(int count) {
        out.writeVU30( count );
        return new ScriptsWriter();
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#stringPool(java.lang.String[]) */
    public void stringPool(String[] strings) {
        out.writeVU30( strings.length > 0 ? strings.length + 1 : 0 );
        for (String s : strings) {
            out.writeVU30String( s );
        }
    }

    /** @see com.anotherbigidea.flash.avm2.ABC.ABCFile#uintPool(long[]) */
    public void uintPool(long[] uints) {
        out.writeVU30( uints.length > 0 ? uints.length + 1 : 0 );
        for (long i : uints) {
            out.writeVU32( i );
        } 
    }

    /** @see com.anotherbigidea.flash.avm2.ABC#done() */
    public void done()  {
        out.flush();
    }
 
    public class ClassInfosWriter implements ABC.ClassInfos {

        /** @see com.anotherbigidea.flash.avm2.ABC.ClassInfos#classInfo(int, int) */
        public Traits classInfo(int constructorIndex, int traitCount) {
            out.writeVU30( constructorIndex );
            out.writeVU30( traitCount );

            return new TraitWriter();
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.ClassInfos#instanceInfo(int, int, int, int, int[], int, int) */
        public Traits instanceInfo(int nameIndex, int superIndex, int flags, int protectedNS, int[] interfaces, int constructorIndex, int traitCount) {
            out.writeVU30( nameIndex );
            out.writeVU30( superIndex );
            out.writeUI8 ( flags );
            
            if( InstanceInfoFlags.HasProtectedNS.isSet(flags) ) {
                out.writeVU30( protectedNS );
            }

            out.writeVU30( interfaces.length );
            for( int j = 0; j < interfaces.length; j++ ) {
                out.writeVU30( interfaces[j] );
            }
            
            out.writeVU30( constructorIndex );
            out.writeVU30( traitCount );

            return new TraitWriter();
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada            
        }        
    }
    
    public class MetadataWriter implements ABC.Metadata {

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Metadata#metadata(int, int[], int[]) */
        public void metadata(int nameIndex, int[] keys, int[] values) {
            out.writeVU30( nameIndex );
            out.writeVU30( values.length );
            
            for( int j = 0; j < values.length; j++ ) {
                out.writeVU30( keys  [j] );
                out.writeVU30( values[j] );
            }            
        }
    }
    
    public class ExceptionHandlersWriter implements ABC.ExceptionHandlers {

        private final Map<Integer, Integer> targetOffsets;
        
        /**
         * @param targetOffsets map of target address to actual offset
         */
        public ExceptionHandlersWriter( Map<Integer, Integer> targetOffsets ) {
            this.targetOffsets = targetOffsets;
        }
        
        /** @see com.anotherbigidea.flash.avm2.ABC.ExceptionHandlers#exceptionHandler(int, int, int, int, int) */
        public void exceptionHandler(int start, int end, int target, int typeIndex, int nameIndex) {
            out.writeVU30( targetOffsets.get( start ));
            out.writeVU30( targetOffsets.get( end ));
            out.writeVU30( targetOffsets.get( target ));
            out.writeVU30( typeIndex );
            out.writeVU30( nameIndex );
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada            
        }        
    }
    
    /** Proxy implementation for Instructions */
    public class InstructionsWriter {

        private class Fixup {
            int[] targetAddresses;
            int   argOffset;
            int   baseOffset;
            
            Fixup( Object arg, int argOffset, int baseOffset ) {
                this.argOffset  = argOffset;
                this.baseOffset = baseOffset;
                
                if( arg instanceof Integer ) {
                    targetAddresses = new int[] { ((Integer) arg).intValue() };
                } 
                else if( arg instanceof int[] ) {
                    targetAddresses = (int[]) arg;
                }
            }
        }
        
        private ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        private OutStream ostream = new OutStream( bytesOut );
        
        //required fixups 
        private final List<Fixup> fixups = new ArrayList<Fixup>();
        
        /** Map of target address to actual byte offset */
        public final Map<Integer, Integer> targetOffsets = new HashMap<Integer, Integer>();
        
        private final InvocationHandler handler = new InvocationHandler() {
            /** @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[]) */
            public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                methodCall( proxy, method, args );
                return null;
            }
        };
        
        /** Get the proxy that implements Instructions */
        public ABC.Instructions getProxy() {            
            return (ABC.Instructions) Proxy.newProxyInstance(
                ABC.Instructions.class.getClassLoader(), 
                new Class[] { ABC.Instructions.class }, 
                handler );        
        }
        
        private void methodCall(Object proxy, Method method, Object[] args) throws Throwable {
        
            String methodName = method.getName();
            if( methodName.equals( "done" ) ) {
                writeBytecode();
                return;
            }

            //record the actual offset for target address
            if( methodName.equals( "target" ) ) {
                int currentOffset = bytesOut.size();
                int address = ((Integer) args[0]).intValue();
                
                targetOffsets.put( address, currentOffset );
                return;
            }
            
            String enumName = "OP_" + methodName.replace( '_', ' ' ).trim().replace( ' ', '_' );
            Operation operation;
            try {
                operation = Operation.valueOf( enumName );
            } catch( IllegalArgumentException ex ) {
                //not an operation
                return;
            }
            
            int opOffset = bytesOut.size();
            ostream.writeUI8( operation.opcode );
            ostream.flush();
                        
            ArgType[] argTypes = operation.argTypes;
            for (int i = 0; i < argTypes.length; i++) {
                
                ArgType argType = argTypes[i];
                Object  arg     = args[i];
                
                //record the location of an offset that needs to be fixed up
                if( argType.isOffset() ) {
                    
                    int argOffset  = bytesOut.size() + argType.preambleBeforeOffsets( arg );
                    int adjustment = argType.offsetAdjustment();

                    fixups.add( new Fixup( arg, argOffset, opOffset + adjustment ));                    
                }
                
                argType.write( ostream, arg );
                ostream.flush();
            } 
        }
        
        //write the final bytecode
        private void writeBytecode() {
            byte[] bytecode = bytesOut.toByteArray();
            fixupBranchOffsets( bytecode );
            
            out.writeVU30( bytecode.length );
            out.write( bytecode );
        }
        
        //fix branch offsets in the code by inserting the actual target address
        private void fixupBranchOffsets( byte[] bytecode ) {
            
            for( Fixup fixup : fixups ) {
                int   argOffset  = fixup.argOffset;
                int   baseOffset = fixup.baseOffset;
                int[] targetAddresses = fixup.targetAddresses;
                
                for (int i = 0; i < targetAddresses.length; i++) {
                    int address = targetAddresses[i];
                    int actualOffset = targetOffsets.containsKey( address ) ?
                                           targetOffsets.get( address ) :
                                           address;
                    int offset  = actualOffset - baseOffset; //the actual offset required
                    
                    bytecode[ argOffset++ ] = (byte)(  offset         & 0xff );
                    bytecode[ argOffset++ ] = (byte)(( offset >> 8  ) & 0xff );
                    bytecode[ argOffset++ ] = (byte)(( offset >> 16 ) & 0xff );
                }
            }
        }
    }

    public class MethodBodyWriter implements ABC.MethodBody {

        private Map<Integer, Integer> targetOffsets;
        
        /** @see com.anotherbigidea.flash.avm2.ABC.MethodBody#instructions(int) */
        public Instructions instructions(int codeSize) {
            InstructionsWriter writer = new InstructionsWriter();
            targetOffsets = writer.targetOffsets;
            return writer.getProxy();
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.MethodBody#exceptionHandlers(int) */
        public ExceptionHandlers exceptionHandlers(int handlerCount) {
            out.writeVU30( handlerCount );
            return new ExceptionHandlersWriter( targetOffsets );
        }
        
        /** @see com.anotherbigidea.flash.avm2.ABC.MethodBody#traits(int) */
        public Traits traits(int traitCount) {
            out.writeVU30( traitCount );
            return new TraitWriter();
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada
        }
    }
    
    public class MethodBodiesWriter implements ABC.MethodBodies {

        /** @see com.anotherbigidea.flash.avm2.ABC.MethodBodies#methodBody(int, int, int, int, int) */
        public MethodBody methodBody(int methodInfo, int maxStack, int maxRegs, int scopeDepth, int maxScope) {
            out.writeVU30( methodInfo );
            out.writeVU30( maxStack );
            out.writeVU30( maxRegs );
            out.writeVU30( scopeDepth );
            out.writeVU30( maxScope );

            return new MethodBodyWriter();
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada
        }
    }
    
    public class MethodInfosWriter implements ABC.MethodInfos {

        /** @see com.anotherbigidea.flash.avm2.ABC.MethodInfos#methodInfo(int, int, int, int[], int[], int[], int[]) */
        public void methodInfo(int nameIndex, int flags, int returnType, int[] paramTypes, int[] optValues, int[] optValKinds, int[] paramNames) {
            out.writeVU30( paramTypes.length );
            out.writeVU30( returnType );

            
            for (int i : paramTypes) {
                out.writeVU30( i );            
            }
            
            out.writeVU30( nameIndex );
            out.writeUI8 ( flags );

            if( MethodInfoFlags.HasOptional.isSet(flags) ) {
                out.writeVU30( optValues.length );
                for (int i = 0; i < optValues.length; i++) {
                    out.writeVU30( optValues[i] );
                    out.writeUI8 ( optValKinds[i] );                
                }
            }
            
            if( MethodInfoFlags.HasParamNames.isSet(flags) ) {
                for( int j = 0; j < paramNames.length; j++ ) {
                    out.writeVU30( paramNames[j] );
                }
            }
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada
        }
    }
    
    public class NamesWriter implements ABC.Names {

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada            
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Names#name(com.anotherbigidea.flash.avm2.MultiNameKind, int, int, int) */
        public void name(MultiNameKind kind, int nameIndex, int namespaceIndex, int namespaceSetIndex) {
            kind.write( out );
            
            switch( kind ) {
                case Qname: 
                case QnameA: {
                    out.writeVU30( namespaceIndex );
                    out.writeVU30( nameIndex );
                    break;
                }
        
                case RTQname:
                case RTQnameA: {
                    out.writeVU30( nameIndex );
                    break;
                }
                
                case RTQnameL:
                case RTQnameLA: {
                    break;
                }
        
                case Multiname:
                case MultinameA: {
                    out.writeVU30( nameIndex );
                    out.writeVU30( namespaceSetIndex );
                    break;
                }
        
                case MultinameL:
                case MultinameLA: {
                    out.writeVU30( namespaceSetIndex );
                    break;
                }
            }         
        }        
    }
    
    public class NamespacesWriter implements ABC.Namespaces {

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada            
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Namespaces#namespace(com.anotherbigidea.flash.avm2.NamespaceKind, int) */
        public void namespace(NamespaceKind kind, int index) {
            kind.write( out );
            out.writeVU30( index );            
        }        
    }
    
    public class NamespaceSetsWriter implements ABC.NamespaceSets {

        /** @see com.anotherbigidea.flash.avm2.ABC.NamespaceSets#namespaceSet(int[]) */
        public void namespaceSet(int[] namespaces) {
            out.writeVU30( namespaces.length );
            
            for (int i : namespaces) {
                out.writeVU30( i );
            }
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada            
        }        
    }
    
    public class ScriptsWriter implements ABC.Scripts {

        /** @see com.anotherbigidea.flash.avm2.ABC.Scripts#script(int, int) */
        public Traits script(int initializerIndex, int traitCount) {
            out.writeVU30( initializerIndex );
            out.writeVU30( traitCount );
            
            return new TraitWriter();
        }

        /** @see org.epistem.io.PipelineInterface#done() */
        public void done() {
            // nada            
        }
    }
   
    class TraitWriter implements Traits {

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#class_(int, int, int, int[]) */
        public void class_(int nameIndex, int slotId, int classIndex, int[] metadataIndices)  {
            out.writeVU30( nameIndex );

            int flags = TraitKind.TraitClass.value;
            if( metadataIndices != null && metadataIndices.length > 0 ) {
                flags |= TraitFlags.HasMeta.value;
            }

            out.writeUI8( flags );
            out.writeVU30( slotId );
            out.writeVU30( classIndex );
            
            if( metadataIndices != null && metadataIndices.length > 0 ) {
                out.writeVU30( metadataIndices.length );

                for( int j = 0; j < metadataIndices.length; j++ ) {
                    out.writeVU30( metadataIndices[j] );                    
                }
            }
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#constant(int, int, int, int, int, int[]) */
        public void constant(int nameIndex, int slotId, int typeIndex, int valueIndex, int valueKind, int[] metadataIndices)  {

            writeSlot( nameIndex, slotId, typeIndex, valueIndex, valueKind, metadataIndices, TraitKind.TraitConst );
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#done() */
        public void done()  {
            // nada
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#function(int, int, int, int[]) */
        public void function(int nameIndex, int slotId, int methIndex, int[] metadataIndices)  {
            
            out.writeVU30( nameIndex );
            
            int flags = TraitKind.TraitFunction.value;
            if( metadataIndices != null && metadataIndices.length > 0 ) {
                flags |= TraitFlags.HasMeta.value;
            }
            
            out.writeUI8( flags );
            out.writeVU30( slotId );            
            out.writeVU30( methIndex );

            if( metadataIndices != null && metadataIndices.length > 0 ) {
                out.writeVU30( metadataIndices.length );

                for( int j = 0; j < metadataIndices.length; j++ ) {
                    out.writeVU30( metadataIndices[j] );                    
                }
            }
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#getter(int, int, int, boolean, boolean, int[]) */
        public void getter(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices)  {

            writeMethod(nameIndex, dispId, methIndex, isFinal, isOverride, metadataIndices, TraitKind.TraitGetter );
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#method(int, int, int, boolean, boolean, int[]) */
        public void method(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices)  {
            
            writeMethod(nameIndex, dispId, methIndex, isFinal, isOverride, metadataIndices, TraitKind.TraitMethod );
        }

        public void writeMethod( int nameIndex, int dispId, int methIndex, 
                                 boolean isFinal, boolean isOverride, 
                                 int[] metadataIndices, TraitKind kind )  {

            out.writeVU30( nameIndex );

            int flags = kind.value;
            if( metadataIndices != null && metadataIndices.length > 0 ) {
                flags |= TraitFlags.HasMeta.value;
            }
            if( isFinal    ) flags |= TraitFlags.IsFinal.value;
            if( isOverride ) flags |= TraitFlags.IsOverride.value;
            
            out.writeUI8( flags );
            out.writeVU30( dispId );
            out.writeVU30( methIndex );

            
            //System.out.println( "METHOD: " + nameIndex + " " + flags + " " + dispId + " " + methIndex );
            
            if( metadataIndices != null && metadataIndices.length > 0 ) {
                out.writeVU30( metadataIndices.length );

                for( int j = 0; j < metadataIndices.length; j++ ) {
                    out.writeVU30( metadataIndices[j] );                    
                }
            }            
        }
        
        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#setter(int, int, int, boolean, boolean, int[]) */
        public void setter(int nameIndex, int dispId, int methIndex, boolean isFinal, boolean isOverride, int[] metadataIndices)  {

            writeMethod(nameIndex, dispId, methIndex, isFinal, isOverride, metadataIndices, TraitKind.TraitSetter );
        }

        /** @see com.anotherbigidea.flash.avm2.ABC.Traits#slot(int, int, int, int, int, int[]) */
        public void slot(int nameIndex, int slotId, int typeIndex, int valueIndex, int valueKind, int[] metadataIndices)  {

            writeSlot( nameIndex, slotId, typeIndex, valueIndex, valueKind, metadataIndices, TraitKind.TraitSlot );
        }

        public void writeSlot( int nameIndex, int slotId, int typeIndex, 
                               int valueIndex, int valueKind,
                               int[] metadataIndices, TraitKind kind )  {

            out.writeVU30( nameIndex );
            
            int flags = kind.value;
            if( metadataIndices != null && metadataIndices.length > 0 ) {
                flags |= TraitFlags.HasMeta.value;
            }
            
            out.writeUI8( flags );
            out.writeVU30( slotId );
            out.writeVU30( typeIndex );            
            out.writeVU30( valueIndex );
            
            if( valueIndex != 0 ) out.writeUI8( valueKind );

            if( metadataIndices != null && metadataIndices.length > 0 ) {
                out.writeVU30( metadataIndices.length );

                for( int j = 0; j < metadataIndices.length; j++ ) {
                    out.writeVU30( metadataIndices[j] );                    
                }
            }
        }        
    }
}
