package org.javaswf.j2avm.model.attributes;

import java.io.DataInput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.code.Frame;
import org.javaswf.j2avm.model.code.Instruction;
import org.javaswf.j2avm.model.code.InstructionCursor;
import org.javaswf.j2avm.model.code.InstructionList;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.parser.OperationConvertor;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;


/**
 * The Code attribute.
 *
 * @author nickmain
 */
public class CodeAttribute extends AttributeModel {

    /** Mutable map of attributes by name */
    public final Map<AttributeName,AttributeModel> attributes = new HashMap<AttributeName,AttributeModel>();
        
    public int maxStack;
    public int maxLocals;
    
    /** The instructions - may be empty for a native or abstract method */
    public final InstructionList instructions = new InstructionList();
    
    public CodeAttribute() {
        super( AttributeName.Code.name() );
    }
    
    /**
     * Create a do-nothing method body with the given return type.  Returns
     * zero or null, as appropriate.
     * 
     * @param returnType the method return type to simulate 
     */
    public static CodeAttribute dummyFor( JavaType returnType ) {
		CodeAttribute code = new CodeAttribute();
		InstructionCursor cur = code.instructions.cursorAtStart();
		
		//return null for non-primitive methods
		if( returnType instanceof ObjectOrArrayType ) {
			cur.pushNull();
		}
		else if( returnType == PrimitiveType.BYTE 
			  || returnType == PrimitiveType.BOOLEAN
			  || returnType == PrimitiveType.SHORT
			  || returnType == PrimitiveType.CHAR
			  || returnType == PrimitiveType.INT ) {
			
			cur.pushInt( 0 );
		}
		else if( returnType == PrimitiveType.LONG ) {
			cur.pushLong( 0L );					
		}
		else if( returnType == PrimitiveType.FLOAT ) {
			cur.pushFloat( 0f );
		}
		else if( returnType == PrimitiveType.DOUBLE ) {
			cur.pushDouble( 0.0 );
		}
		
		cur.methodReturn( returnType );
		
		return code;
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
                
            code.instructions.addHandler( convertor.labelAtOffset( start ), 
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
        
        if( instructions.hasExceptionHandlers() ) {
            out.println();
            out.println( "handlers {" );
            out.indent();

            for( Iterator<InstructionList.ExceptionHandler> it = instructions.handlers(); 
                 it.hasNext(); ) {
				it.next().dump( out );				
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
