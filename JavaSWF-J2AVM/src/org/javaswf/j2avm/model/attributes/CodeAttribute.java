package org.javaswf.j2avm.model.attributes;

import static org.epistem.jclass.JAttribute.Name.Code;

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
import org.epistem.jclass.JAttribute;
import org.epistem.jclass.JClassLoader;
import org.epistem.jclass.code.Instruction;
import org.epistem.jclass.io.internal.ConstantPool;
import org.epistem.jclass.io.parser.ClassFileParser;
import org.epistem.jclass.reference.JClassReference;

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
        public final JClassReference exceptionType;
        
        public ExceptionHandler( int startOffset, int endOffset, int handlerOffset, JClassReference exceptionType ) {
            this.startOffset   = startOffset;
            this.endOffset     = endOffset;
            this.handlerOffset = handlerOffset;
            this.exceptionType = exceptionType;
        }
        
        /**
         * Dump for debug purposes
         */
        public void dump( IndentingPrintWriter out ) {
            String type = (exceptionType != null) ? exceptionType.className : "<any>";
            out.println( "try{ " + startOffset + " .. " + endOffset + 
                         " } catch( " + type + " ) --> " + handlerOffset );
        }
    }
    
    /** Mutable map of attributes by name */
    public final Map<String,AttributeModel> attributes = new HashMap<String,AttributeModel>();
    
    /**
     * Get a standard attribute
     * @param name the attribute name
     * @return null if the attribute does not exist
     */
    public final AttributeModel getAttribute( AttributeModel.Name name ) {
        return attributes.get( name.name() );
    }
    
    public final int maxStack;
    public final int maxLocals;
    
    /** The exception handlers in order of decreasing precedence */
    public final List<ExceptionHandler> handlers = new ArrayList<ExceptionHandler>();
    
    /** The instructions - keyed by offset */
    public final SortedMap<Integer, Instruction> instructions = new TreeMap<Integer, Instruction>();
    
    public CodeAttribute( int maxStack, int maxLocals ) {
        super( Code.name() );
        
        this.maxLocals = maxLocals;
        this.maxStack  = maxStack;
    }
    
    public static CodeAttribute parse( ConstantPool pool, JClassLoader loader, DataInput in ) throws IOException {
        int maxStack  = in.readUnsignedShort();
        int maxLocals = in.readUnsignedShort();
        
        CodeAttribute code = new CodeAttribute( maxStack, maxLocals );
        
        //parse the bytecode
        int codeSize = in.readInt();
        byte[] bytecode = new byte[ codeSize ];
        in.readFully( bytecode );
        
        CountingDataInput dataIn = new CountingDataInput( 
                                       new DataInputStream( 
                                           new ByteArrayInputStream( bytecode )));
        
        while( dataIn.count < codeSize ) {
            Instruction instr = Instruction.parse( loader, pool, dataIn );
            code.instructions.put( instr.offset, instr );
        }
        
        //exception handlers
        int handlerCount = in.readUnsignedShort();
        for (int i = 0; i < handlerCount; i++) {
            int start   = in.readUnsignedShort();
            int end     = in.readUnsignedShort();
            int handler = in.readUnsignedShort();
            int type    = in.readUnsignedShort();
            
            JClassReference exType = (type != 0) ? 
                new JClassReference( loader, pool.getClassName( type ) ) :
                null;
                
            code.handlers.add( new ExceptionHandler( start, end, handler, exType ));
        }
        
        //attributes
        int attrCount = in.readUnsignedShort();
        for (int i = 0; i < attrCount; i++) {
            ClassFileParser.parseAttribute( code.attributes, in, loader, pool );            
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

        for( Instruction i : instructions.values() ) {
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
