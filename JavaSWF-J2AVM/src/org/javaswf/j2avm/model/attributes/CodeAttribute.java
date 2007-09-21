package org.javaswf.j2avm.model.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.epistem.io.CountingDataInput;
import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.ClassModel;
import org.javaswf.j2avm.model.code.Instruction;
import org.javaswf.j2avm.model.code.InstructionList;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.ObjectType;


/**
 * The Code attribute.
 *
 * @author nickmain
 */
public class CodeAttribute extends AttributeModel {

    /** An exception handler */
    public static class ExceptionHandler {
        /** First instruction in the try block */        
        public final int startOffset;
        
        /** First instruction after the try block */
        public final int endOffset;
        
        /** First instruction of the catch handler */
        public final int handlerOffset;
        
        /** The type to catch - may be null (means any exception) */
        public final ObjectType exceptionType;
        
        public ExceptionHandler( int startOffset, int endOffset, int handlerOffset, ObjectType exceptionType ) {
            this.startOffset   = startOffset;
            this.endOffset     = endOffset;
            this.handlerOffset = handlerOffset;
            this.exceptionType = exceptionType;
        }
        
        /**
         * Dump for debug purposes
         */
        public void dump( IndentingPrintWriter out ) {
            String type = (exceptionType != null) ? exceptionType.name : "<any>";
            out.println( "try{ " + startOffset + " .. " + endOffset + 
                         " } catch( " + type + " ) --> " + handlerOffset );
        }
    }
    
    /** Mutable map of attributes by name */
    public final Map<AttributeName,AttributeModel> attributes = new HashMap<AttributeName,AttributeModel>();
        
    private int maxStack;
    private int maxLocals;
    
    /** The exception handlers in order of decreasing precedence */
    public final List<ExceptionHandler> handlers = new ArrayList<ExceptionHandler>();
    
    /** The instructions - may be empty for a native or abstract method */
    public final InstructionList instructions = new InstructionList();;
    
    public CodeAttribute() {
        super( AttributeName.Code.name() );
    }
    
    public CodeAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
        CodeAttribute code = new CodeAttribute();
        code.maxStack  = in.readUnsignedShort();
        code.maxLocals = in.readUnsignedShort();
        
        //parse the bytecode
        int codeSize = in.readInt();
        byte[] bytecode = new byte[ codeSize ];
        in.readFully( bytecode );
        
        CountingDataInput dataIn = new CountingDataInput( 
                                       new DataInputStream( 
                                           new ByteArrayInputStream( bytecode )));
        
        while( dataIn.count < codeSize ) {
            Instruction instr = Instruction.parse( pool, dataIn );
            code.instructions.put( instr.offset, instr );
        }
        
        //exception handlers
        int handlerCount = in.readUnsignedShort();
        for (int i = 0; i < handlerCount; i++) {
            int start   = in.readUnsignedShort();
            int end     = in.readUnsignedShort();
            int handler = in.readUnsignedShort();
            int type    = in.readUnsignedShort();
            
            ObjectType exType = (type != 0) ? 
                new ObjectType( pool.getClassName( type ) ) :
                null;
                
            code.handlers.add( new ExceptionHandler( start, end, handler, exType ));
        }
        
        //attributes
        int attrCount = in.readUnsignedShort();
        for (int i = 0; i < attrCount; i++) {
        	AttributeModel.parseAttr( code.attributes, in, pool );            
        }
        
        return code;
    }
    
    /**
     * Dump for debug purposes
     */
    public void dump( IndentingPrintWriter out ) {
        out.println( name + " {" );
        out.indent();
        
        out.println( "max stack : " + maxStack );
        out.println( "max locals: " + maxLocals );
        out.println();

        for( Instruction i : instructions ) {
            i.dump( out );
        }
        
        if( ! handlers.isEmpty() ) {
            out.println();
            out.println( "handlers {" );
            out.indent();

            for( ExceptionHandler handler : handlers ) {
                handler.dump( out );
            }
            
            out.unindent();
            out.println( "}" );
        }
        
        if( ! attributes.isEmpty() ) {
            out.println();
            out.println( "attributes {" );
            out.indent();
            
            for( AttributeModel attr : attributes.values() ) {
                attr.dump( out );
            }
            
            out.unindent();
            out.println( "}" );
        }
        
        out.unindent();
        out.println( "}" );
    }
}
