package org.javaswf.j2avm.model.attributes;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.code.expression.ExpressionBuilder;
import org.javaswf.j2avm.model.code.instruction.InstructionParser;
import org.javaswf.j2avm.model.code.instruction.InstructionResolver;
import org.javaswf.j2avm.model.code.statement.LabelStatement;
import org.javaswf.j2avm.model.code.statement.StatementCursor;
import org.javaswf.j2avm.model.code.statement.StatementList;
import org.javaswf.j2avm.model.code.statement.StatementPrinter;
import org.javaswf.j2avm.model.code.statement.TryCatch;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.ObjectOrArrayType;
import org.javaswf.j2avm.model.types.ObjectType;
import org.javaswf.j2avm.model.types.PrimitiveType;
import org.javaswf.j2avm.model.types.VoidType;


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
    
    /** The statements - may be empty for a native or abstract method */
    public final StatementList statements = new StatementList();
    
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
		StatementCursor cur = code.statements.cursorAtStart();
		
		if( returnType == VoidType.VOID ) {
			cur.insert().voidReturn();			
		}
		
		//return null for non-primitive methods
		else if( returnType instanceof ObjectOrArrayType ) {
			cur.insert().returnValue( ExpressionBuilder.constantNull() );
		}
		else if(((PrimitiveType) returnType).isIntType ) {			
			cur.insert().returnValue( ExpressionBuilder.constantInt( 0 ) );
		}
		else if( returnType == PrimitiveType.LONG ) {
			cur.insert().returnValue( ExpressionBuilder.constantLong( 0L ) );
		}
		else if( returnType == PrimitiveType.FLOAT ) {
			cur.insert().returnValue( ExpressionBuilder.constantFloat( 0f ) );
		}
		else if( returnType == PrimitiveType.DOUBLE ) {
			cur.insert().returnValue( ExpressionBuilder.constantDouble( 0.0 ) );
		}
		
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
        
        InstructionResolver resolver = new InstructionResolver( pool, code.statements );
        InstructionParser   parser   = new InstructionParser();
        parser.parseInstructions( new DataInputStream( new ByteArrayInputStream( bytecode )), codeSize, resolver );
        
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

        	LabelStatement startLabel   = code.statements.label( start );
        	LabelStatement endLabel     = code.statements.label( end );
        	LabelStatement handlerLabel = code.statements.label( handler );
        	
        	code.statements.addHandler( startLabel, endLabel, exType, handlerLabel );
        }
        
        //attributes
        int attrCount = in.readUnsignedShort();
        for (int i = 0; i < attrCount; i++) {
        	AttributeModel.parseAttr( code.attributes, in, pool );            
        }
        
        resolver.positionLabels();
        
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

        StatementPrinter sp = new StatementPrinter( out );
        StatementCursor  sc = statements.cursorAtStart();
        while( sc.visitNext( sp ) );
                
        if( statements.hasExceptionHandlers() ) {
            out.println();
            out.println( "handlers {" );
            out.indent();

            for( Iterator<TryCatch> it = statements.handlers(); it.hasNext(); ) {
				TryCatch tc = it.next();
				out.print( "try( " );
				out.print( tc.tryStart.name );
				out.print( " .. " );
				out.print( tc.tryEnd.name );
				out.print( " ) catch ( " );
				out.print( tc.exceptionType.name );
				out.print( " ) goto " );
				out.print( tc.handlerStart.name );
				out.println();
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
