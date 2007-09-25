package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.code.CodeLabel;
import org.javaswf.j2avm.model.code.Frame;
import org.javaswf.j2avm.model.code.Instruction;
import org.javaswf.j2avm.model.code.InstructionList;
import org.javaswf.j2avm.model.code.LabelTargetter;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.parser.OperationConvertor;
import org.javaswf.j2avm.model.types.ObjectType;


/**
 * The Code attribute.
 *
 * @author nickmain
 */
public class CodeAttribute extends AttributeModel {

    /** An exception handler */
    public static class ExceptionHandler implements LabelTargetter {
        /** First instruction in the try block */        
        public final CodeLabel start;
        
        /** First instruction after the try block */
        public final CodeLabel end;
        
        /** First instruction of the catch handler */
        public final CodeLabel handler;
        
        /** The type to catch - may be null (means any exception) */
        public final ObjectType exceptionType;
        
        public ExceptionHandler( CodeLabel start, 
        		                 CodeLabel end, 
        		                 CodeLabel handler, 
        		                 ObjectType exceptionType ) {
        	
            this.start         = start;
            this.end           = end;
            this.handler       = handler;
            this.exceptionType = exceptionType;
            
            start  .targetters.add( this );
            end    .targetters.add( this );
            handler.targetters.add( this );
        }
        
        /**
         * Remove this handler from the targetted CodeLabels
         */
        public void remove() {
            start  .targetters.remove( this );
            end    .targetters.remove( this );
            handler.targetters.remove( this );            
        }
        
        /** @see org.javaswf.j2avm.model.code.LabelTargetter#targets(Set) */
        public void targets( Set<CodeLabel> targets ) {
            targets.add( start );
            targets.add( end );
            targets.add( handler );
        }

        /** @see org.javaswf.j2avm.model.code.LabelTargetter#release() */
        public void release() {
            start  .targetters.remove( this );
            end    .targetters.remove( this );
            handler.targetters.remove( this );            
        }

        /**
         * Dump for debug purposes
         */
        public void dump( IndentingPrintWriter out ) {
            String type = (exceptionType != null) ? exceptionType.name : "<any>";
            out.println( "try{ " + start + " .. " + end + 
                         " } catch( " + type + " ) --> " + handler );
        }
    }
    
    /** Mutable map of attributes by name */
    public final Map<AttributeName,AttributeModel> attributes = new HashMap<AttributeName,AttributeModel>();
        
    public int maxStack;
    public int maxLocals;
    
    /** The exception handlers in order of decreasing precedence */
    private final List<ExceptionHandler> handlers = new ArrayList<ExceptionHandler>();
    
    /** The instructions - may be empty for a native or abstract method */
    public final InstructionList instructions = new InstructionList();;
    
    public CodeAttribute() {
        super( AttributeName.Code.name() );
    }
    
    /**
     * Append a new exception handler
     * 
     * @return the new handler
     */
    public final ExceptionHandler addHandler( CodeLabel start, 
                                              CodeLabel end, 
                                              CodeLabel handler, 
                                              ObjectType exceptionType ) {
        ExceptionHandler eh = 
            new ExceptionHandler( start, end, handler, exceptionType );
        
        handlers.add( eh );
        return eh;
    }
    
    /**
     * Get an iterator over the exception handlers in order of decreasing
     * importance. The remove operation is supported.
     */
    public final Iterator<ExceptionHandler> handlers() {
        return new Iterator<ExceptionHandler>() {            
            private final Iterator<ExceptionHandler> it = handlers.iterator();
            private ExceptionHandler handler;            
            
            /** @see java.util.Iterator#hasNext() */
            public boolean hasNext() {
                return it.hasNext();
            }

            /** @see java.util.Iterator#next() */
            public ExceptionHandler next() {
                handler = it.next();
                return handler;
            }

            /** @see java.util.Iterator#remove() */
            public void remove() {
                it.remove();
                handler.release();
            }
        };
    }
    
    public static CodeAttribute parse( ConstantPool pool, DataInput in ) throws IOException {
    	
        CodeAttribute code = new CodeAttribute();
        code.maxStack  = in.readUnsignedShort();
        code.maxLocals = in.readUnsignedShort();
        
        //parse the bytecode
        int codeSize = in.readInt();
        byte[] bytecode = new byte[ codeSize ];
        in.readFully( bytecode );

        //parse the bytecode
    	OperationConvertor convertor = new OperationConvertor( code.instructions, pool, bytecode );
    	convertor.convert();
        
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
                
            code.addHandler( convertor.labelAtOffset( start ), 
				             convertor.labelAtOffset( end ), 
				             convertor.labelAtOffset( handler ), 
				             exType );
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
    public final void dump( IndentingPrintWriter out ) {
        out.println( name + " {" );
        out.indent();
        
        out.println( "max stack : " + maxStack );
        out.println( "max locals: " + maxLocals );
        out.println();

        for( Instruction i : instructions ) {
        	Frame frame = i.frameBefore();
        	if( frame != null ) {
        		frame.dump( out );
        	}
        	
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
