package com.anotherbigidea.flash.avm2;

import org.epistem.io.PipelineInterface;

/**
 * Interface for passing the contents of an ABC file or tag (ActionScript 3
 * Bytecode - as used by the AVM2)
 * 
 * The method call order must be:
 *   version
 *   intPool
 *   uintPool
 *   doublePool
 *   stringPool
 *   namespacePool
 *   namespaceSetPool
 *   namePool
 *   methods
 *   metadata
 *   classes
 *   scripts
 *   methodBodies
 *   done
 *   
 * @author nickmain
 */
public interface ABC extends PipelineInterface {

    /** Current minor version */
    public static final int MINOR_VERSION_16 = 16;
    /** Current major version */
    public static final int MAJOR_VERSION_46 = 46;
       
    /**
     * Pass the version info
     */
    public void version( int majorVersion, int minorVersion );
    
    /**
     * Pass the int pool
     */
    public void intPool( int[] ints );

    /**
     * Pass the uint pool
     */
    public void uintPool( long[] uints );

    /**
     * Pass the double pool
     */
    public void doublePool( double[] doubles );

    /**
     * Pass the String pool
     */
    public void stringPool( String[] strings );

    /**
     * Pass the namespace pool 
     * 
     * @param count the number of namespaces (index zero is counted but not passed)
     * @return interface to receive the namespaces, null to skip
     */
    public Namespaces namespacePool( int count );

    /**
     * Pass the namespace set pool 
     * 
     * @param count the number of namespace sets (index zero is counted but not passed)
     * @return interface to receive the sets, null to skip
     */
    public NamespaceSets namespaceSetPool( int count );
    
    /**
     * Pass the name pool 
     * 
     * @param count the number of names (index zero is counted but not passed)
     * @return interface to receive the names, null to skip
     */
    public Names namePool( int count );
        
    /**
     * Pass the method information
     * 
     * @param count the number of methodInfo items
     * @return interface to receive the method infos, null to skip
     */
    public MethodInfos methods( int count );
    
    /**
     * Pass the metadata definitions
     * 
     * @param count the number of metadata definitions
     * @return interface to receive the metadata info, null to skip
     */
    public Metadata metadata( int count );
        
    /**
     * Pass the instanceInfo and classInfo 
     * 
     * @param count the number of classes
     * @return interface to receive the class details, null to skip
     */
    public ClassInfos classes( int count );
    
    /**
     * Pass the script information
     * 
     * @param count the number of scripts
     * @return interface to receive the scripts, null to skip
     */
    public Scripts scripts( int count );

    /**
     * Pass the method body information
     * 
     * @param count the number of method bodies
     * @return interface to receive the method bodies, null to skip
     */
    public MethodBodies methodBodies( int count );
    
    //========================================================================
    //========================================================================
    //========================================================================
    
    /**
     * Interface for passing exception handler info
     */
    public interface ExceptionHandlers extends PipelineInterface {
        
        /**
         * @param start intruction offset for start of covered code
         * @param end intruction offset for end of covered code
         * @param target intruction offset for start of handler code
         * @param typeIndex name index for exception type to catch
         * @param nameIndex name index for variable name
         */
        public void exceptionHandler( int start, int end, int target, int typeIndex, int nameIndex );
    }
    
    /**
     * Interface for passing a method body.
     * 
     * Call order is
     *   instructions
     *   exceptionHandlers
     *   traits
     *   done
     */
    public interface MethodBody extends PipelineInterface {
        
        /**
         * Pass the method instructions.
         * @param codeSize the byte size of the code
         * @return interface to receive the instructions, null to skip
         */
        public Instructions instructions( int codeSize );
        
        /**
         * Pass the exception handlers for a method
         * @param handlerCount the number of handlers
         * @return interface to receive the handler info, null to skip
         */
        public ExceptionHandlers exceptionHandlers( int handlerCount );

        /**
         * Pass the activation traits
         * 
         * @param traitCount number of traits
         * @return interface to accept the traits - null to skip 
         */
        public Traits traits( int traitCount );
    }

    /**
     * Pass raw bytecode data rather than individual instructions
     */
    public interface RawBytecode extends Instructions {
        //nada
    }
    
    /**
     * Interface for passing method instructions
     */
    public interface Instructions extends PipelineInterface {

        /**
         * Pass the raw bytecode rather than individual instructions
         */
        public void bytecode( byte[] code );
        
        /**
         * Pass a location in the code that can be the target for branches
         * or exception handlers
         */
        public void target( int address );
        
        //=========================================================
        // Following methods were generated by 
        // javaswf.tools.InstructionInterfaceBuilder
        //=========================================================
        
        public void add( );
        public void add_i( );
        public void astype( int name_index );
        public void astypelate( );
        public void bitand( );
        public void bitnot( );
        public void bitor( );
        public void bitxor( );
        public void bkpt( );
        public void bkptline( int line_number );
        public void call( int arg_count );
        public void callmethod( int disp_id, int arg_count );
        public void callproperty( int name_index, int arg_count );
        public void callproplex( int name_index, int arg_count );
        public void callpropvoid( int name_index, int arg_count );
        public void callstatic( int method_id, int arg_count );
        public void callsuper( int name_index, int arg_count );
        public void callsupervoid( int name_index, int arg_count );
        public void checkfilter( );
        public void coerce( int name_index );
        public void coerce_a( );
        public void coerce_b( );
        public void coerce_d( );
        public void coerce_i( );
        public void coerce_o( );
        public void coerce_s( );
        public void coerce_u( );
        public void construct( int arg_count );
        public void constructprop( int name_index, int arg_count );
        public void constructsuper( int arg_count );
        public void convert_b( );
        public void convert_d( );
        public void convert_i( );
        public void convert_o( );
        public void convert_s( );
        public void convert_u( );
        public void debug( );
        public void debugfile( int string_index );
        public void debugline( int line_number );
        public void declocal( int register );
        public void declocal_i( int register );
        public void decrement( );
        public void decrement_i( );
        public void deleteproperty( int name_index );
        public void divide( );
        public void dup( );
        public void dxns( int string_index );
        public void dxnslate( );
        public void equals( );
        public void esc_xattr( );
        public void esc_xelem( );
        public void finddef( int name_index );
        public void findproperty( int name_index );
        public void findpropstrict( int name_index );
        public void getdescendants( int name_index );
        public void getglobalscope( );
        public void getglobalslot( int slot_id );
        public void getlex( int name_index );
        public void getlocal( int register );
        public void getlocal0( );
        public void getlocal1( );
        public void getlocal2( );
        public void getlocal3( );
        public void getproperty( int name_index );
        public void getscopeobject( int scope_index );
        public void getslot( int slot_id );
        public void getsuper( int name_index );
        public void greaterequals( );
        public void greaterthan( );
        public void hasnext( );
        public void hasnext2( int target_register, int index_register );
        public void ifeq( int target );
        public void iffalse( int target );
        public void ifge( int target );
        public void ifgt( int target );
        public void ifle( int target );
        public void iflt( int target );
        public void ifne( int target );
        public void ifnge( int target );
        public void ifngt( int target );
        public void ifnle( int target );
        public void ifnlt( int target );
        public void ifstricteq( int target );
        public void ifstrictne( int target );
        public void iftrue( int target );
        public void in( );
        public void inclocal( int register );
        public void inclocal_i( int register );
        public void increment( );
        public void increment_i( );
        public void initproperty( int name_index );
        public void instanceof_( );
        public void istype( int name_index );
        public void istypelate( );
        public void jump( int offset );
        public void kill( int register );
        public void label( );
        public void lessequals( );
        public void lessthan( );
        public void lookupswitch( int defaultTarget, int[] caseTargets );
        public void lshift( );
        public void modulo( );
        public void multiply( );
        public void multiply_i( );
        public void negate( );
        public void negate_i( );
        public void newactivation( );
        public void newarray( int arg_count );
        public void newcatch( int catch_index );
        public void newclass( int class_index );
        public void newfunction( int method_index );
        public void newobject( int key_value_count );
        public void nextname( );
        public void nextvalue( );
        public void nop( );
        public void not( );
        public void pop( );
        public void popscope( );
        public void pushbyte( byte value );
        public void pushdouble( int double_index );
        public void pushfalse( );
        public void pushint( int int_index );
        public void pushnamespace( int namespace_index );
        public void pushnan( );
        public void pushnull( );
        public void pushscope( );
        public void pushshort( int value );
        public void pushstring( int string_index );
        public void pushtrue( );
        public void pushuint( int uint_index );
        public void pushundefined( );
        public void pushwith( );
        public void returnvalue( );
        public void returnvoid( );
        public void rshift( );
        public void setglobalslot( int slot_id );
        public void setlocal( int register );
        public void setlocal0( );
        public void setlocal1( );
        public void setlocal2( );
        public void setlocal3( );
        public void setproperty( int name_index );
        public void setslot( int slot_id );
        public void setsuper( int name_index );
        public void strictequals( );
        public void subtract( );
        public void subtract_i( );
        public void swap( );
        public void throw_( );
        public void timestamp( );
        public void typeof( );
        public void urshift( );
    }
    
    /**
     * Interface for passing traits.
     */
    public interface Traits extends PipelineInterface {
        
        /**
         * A slot
         * @param nameIndex the trait name
         * @param slotId slotId+1, zero means autoassign
         * @param typeIndex name index - zero means Object
         * @param valueIndex value index - zero means undefined
         * @param valueKind value kind, if value is not undefined
         * @param metadataIndices indices of any metadata
         */
        public void slot( int nameIndex, int slotId, int typeIndex, 
                          int valueIndex, int valueKind, int[] metadataIndices );
        
        /**
         * A constant
         * @param nameIndex the trait name
         * @param slotId slotId+1, zero means autoassign
         * @param typeIndex name index - zero means Object
         * @param valueIndex value index - zero means undefined
         * @param valueKind value kind, if value is not undefined
         * @param metadataIndices indices of any metadata
         */
        public void constant( int nameIndex, int slotId, int typeIndex, 
                              int valueIndex, int valueKind, int[] metadataIndices );
        
        /**
         * A method
         * 
         * @param nameIndex the trait name
         * @param dispId dispatch id - 0 means autoassign
         * @param methIndex method index
         * @param isFinal true if final, false if virtual
         * @param isOverride true if overriding, false if new
         * @param metadataIndices indices of any metadata
         */
        public void method( int nameIndex, int dispId, int methIndex, 
                            boolean isFinal, boolean isOverride, int[] metadataIndices );
        
        /**
         * A getter method
         * 
         * @param nameIndex the trait name
         * @param dispId dispatch id - 0 means autoassign
         * @param methIndex method index
         * @param isFinal true if final, false if virtual
         * @param isOverride true if overriding, false if new
         * @param metadataIndices indices of any metadata
         */
        public void getter( int nameIndex, int dispId, int methIndex, 
                            boolean isFinal, boolean isOverride, int[] metadataIndices );
        
        /**
         * A setter method
         * 
         * @param nameIndex the trait name
         * @param dispId dispatch id - 0 means autoassign
         * @param methIndex method index
         * @param isFinal true if final, false if virtual
         * @param isOverride true if overriding, false if new
         * @param metadataIndices indices of any metadata
         */
        public void setter( int nameIndex, int dispId, int methIndex, 
                            boolean isFinal, boolean isOverride, int[] metadataIndices );
        
        /**
         * A class reference
         *  
         * @param nameIndex the trait name
         * @param slotId slotId+1, zero means autoassign
         * @param classIndex class index
         * @param metadataIndices indices of any metadata
         */
        public void class_( int nameIndex, int slotId, int classIndex, 
                            int[] metadataIndices );

        /**
         * A function reference
         *  
         * @param nameIndex the trait name
         * @param slotId slotId+1, zero means autoassign
         * @param methIndex method index
         * @param metadataIndices indices of any metadata
         */
        public void function( int nameIndex, int slotId, int methIndex, 
                              int[] metadataIndices );        
    }
    
    /**
     * Interface for passing class instance and static info.
     * 
     * Call order is
     *   instanceInfo * classCount
     *   classInfo    * classCount
     *   done
     */
    public interface ClassInfos extends PipelineInterface {
        /**
         * Pass an instanceInfo
         * 
         * @param nameIndex index of the name
         * @param superIndex index of the superclass
         * @param flags flags
         * @param protectedNS index of the protected namespace - zero for none
         * @param interfaces indices of the implemented interfaces
         * @param constructorIndex index of the constructor method
         * @param traitCount the number of traits
         * @return interface to receive the traits, null to skip traits
         */
        public Traits instanceInfo( int nameIndex, int superIndex, int flags,
                                    int protectedNS, int[] interfaces,
                                    int constructorIndex, int traitCount );
        
        /**
         * Pass a classInfo
         * @param constructorIndex index of the static constructor
         * @param traitCount number of static traits
         * @return interface to receive the traits, null to skip traits
         */
        public Traits classInfo( int constructorIndex, int traitCount );
    }
    
    /**
     * Interface to pass script details
     */
    public interface Scripts extends PipelineInterface {
        
        /**
         * Pass a script
         * @param initializerIndex the method index of the initializer
         * @param traitCount number of traits
         * @return interface to receive the traits, null to skip traits
         */
        public Traits script( int initializerIndex, int traitCount );
    }

    /**
     * Interface to pass method bodies
     */
    public interface MethodBodies extends PipelineInterface {
        
        /**
         * Pass a method body
         * 
         * @param methodInfo the methodInfo index
         * @param maxStack the max stack
         * @param maxRegs the max registers
         * @param scopeDepth the scope depth
         * @param maxScope the max scope depth
         * 
         * @return interface to receive the body details, null to skip
         */
        public MethodBody methodBody( int methodInfo, int maxStack, int maxRegs,
                                      int scopeDepth, int maxScope );

    }
    
    /**
     * Interface to pass metadata definitions
     */
    public interface Metadata extends PipelineInterface {
        
        /**
         * Pass a metadata item
         * @param nameIndex name string index
         * @param keys key string indices - zero index for no key
         * @param values value string indices
         */
        public void metadata( int nameIndex, int[] keys, int[] values );
    }
    
    /**
     * Interface for passing method information
     */
    public interface MethodInfos extends PipelineInterface {
        
        /**
         * Pass method info
         * 
         * @param nameIndex name index (string) - zero for none
         * @param flags METHODINFO_* flags
         * @param returnType return type index - zero for any
         * @param paramTypes param types - zero for any
         * @param optValues value indices for optional params - null if none
         * @param optValKinds optional param kinds, null if none
         * @param paramNames parameter name indices, null if none
         */
        public void methodInfo( int nameIndex, int flags, int returnType, 
                                int[] paramTypes, int[] optValues, 
                                int[] optValKinds, int[] paramNames );
    }
    
    /**
     * Interface for passing the namespace pool
     */
    public interface Namespaces extends PipelineInterface {
        
        /**
         * Pass a namespace
         * @param kind the namespace kind
         * @param index the string index - may be zero (wildcard)
         */
        public void namespace( NamespaceKind kind, int index );
    }
    
    /**
     * Interface to receive the namespace set pool
     */
    public interface NamespaceSets extends PipelineInterface {
        
        /**
         * Pass a namespace set
         * @param namespaces indices of the namespaces in the set
         */
        public void namespaceSet( int[] namespaces );
    }
    
    /**
     * Interface to pass the name pool
     */
    public interface Names extends PipelineInterface {
        /**
         * Pass a name
         * 
         * @param kind the name kind
         * @param nameIndex the name index - may be zero
         * @param namespaceIndex the namespace index - may be zero
         * @param namespaceSetIndex the namespace set index - may be zero
         */
        public void name( MultiNameKind kind, 
                          int nameIndex, 
                          int namespaceIndex, 
                          int namespaceSetIndex );
    }   
}
