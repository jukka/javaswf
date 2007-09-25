package org.javaswf.j2avm.model;

import java.io.DataInput;
import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.epistem.io.IndentingPrintWriter;
import org.javaswf.j2avm.model.attributes.AttributeModel;
import org.javaswf.j2avm.model.attributes.AttributeName;
import org.javaswf.j2avm.model.attributes.CodeAttribute;
import org.javaswf.j2avm.model.code.CodeLabel;
import org.javaswf.j2avm.model.code.Frame;
import org.javaswf.j2avm.model.code.Instruction;
import org.javaswf.j2avm.model.code.InstructionList;
import org.javaswf.j2avm.model.code.LabelTargetter;
import org.javaswf.j2avm.model.flags.MethodFlag;
import org.javaswf.j2avm.model.parser.ConstantPool;
import org.javaswf.j2avm.model.types.JavaType;
import org.javaswf.j2avm.model.types.Signature;
import org.javaswf.j2avm.model.types.ValueType;

/**
 * Model of a parsed Java method
 *
 * @author nickmain
 */
public final class MethodModel {

	public final Signature signature;
	
	public final JavaType returnType;
	
	
	/** The method flags */
	public final Collection<MethodFlag> flags;
	
	/** Attributes by name */
	public final Map<AttributeName,AttributeModel> attributes = 
		new EnumMap<AttributeName,AttributeModel>(AttributeName.class);

	/**
	 * @param sig the method signature
	 * @param returnType the return type
	 * @param flags the method flags
	 */
	public MethodModel( Signature sig, ValueType returnType, 
			            Collection<MethodFlag> flags ) {
		this.flags      = flags;
		this.signature  = sig;
		this.returnType = returnType;
	}
	
	/**
	 * Parse a method
	 */
	/*pkg*/ MethodModel( DataInput in, ConstantPool pool ) throws IOException {
        
        int flagBits = in.readUnsignedShort();
        int nameIdx  = in.readUnsignedShort();

        String name = pool.getUTF8Value( nameIdx );
        flags = MethodFlag.parser.parse( flagBits );    
        
        int sigIdx = in.readUnsignedShort();
        String sig = pool.getUTF8Value( sigIdx );
        
        String rt = sig.substring( sig.indexOf(")") + 1 );
        returnType = JavaType.fromName( ConstantPool.decodeTypeName( rt ) );
        
        sig = sig.substring(1,sig.indexOf(")") );        
        String[] ptypes = ConstantPool.readTypes( sig );
        
        ValueType[] params = new ValueType[ ptypes.length ];
        for (int p = 0; p < params.length; p++) {
            params[p] = ValueType.fromName( ptypes[p] );
        }

        signature = new Signature( name, params );
        
        int numAttrs = in.readUnsignedShort();       
        for (int i = 0; i < numAttrs; i++) {
        	AttributeModel.parseAttr( attributes, in, pool );
        }
	}
	
    /**
     * Dump the model
     */
    public void dump( IndentingPrintWriter ipw ) {
    	ipw.print( "method " + signature + " : " + returnType + " " + flags );
    	
    	if( ! attributes.isEmpty()) {
    		ipw.println( " {" );
    		ipw.indent();

    		for( AttributeModel a : attributes.values() ) {
        		ipw.println();
        		a.dump( ipw );    		    		
        	}
    		
    		ipw.unindent();
    		ipw.print( "}" );
    	}
    	
    	ipw.println();
    }
    
    
    /**
     * Determine the frames for each instruction.
     */
    public void determineFrames() {
    	CodeAttribute code = (CodeAttribute) attributes.get( AttributeName.Code );
    	if( code == null ) return;
    	
    	InstructionList instructions = code.instructions;
    	if( instructions.isEmpty() ) return;
    	
    	LinkedList<IncomingFrame> queue = new LinkedList<IncomingFrame>();
    	Frame frame1 = (flags.contains( MethodFlag.MethodIsStatic )) ?
    			           Frame.staticMethod( this ) :
    			           Frame.instanceMethod( this );
    	queue.add( new IncomingFrame( frame1, instructions.first()) );

    	boolean processedHandlers = false;
    	
    	while( ! queue.isEmpty() ) {
        	while( ! queue.isEmpty() ) {
        		
        		IncomingFrame infr = queue.removeFirst();
        		Instruction   insn = infr.insn;
        		
        		//merge the incoming frame and if it caused a change in the
        		//outgoing frame then enqueue downstream instructions
        		if( infr.insn.mergeIncomingFrame( infr.incoming ) ) {
        			Frame out = insn.frameAfter();
        			
        			if( insn.flowsToNext() ) {
        				queue.add( new IncomingFrame( out, insn.next() ));
        			}
        			
        			if( insn instanceof LabelTargetter ) {
        				LabelTargetter targetter = (LabelTargetter) insn;
        				
        				Set<CodeLabel> labels = new HashSet<CodeLabel>();
        				targetter.targets( labels );
        				
        				for( CodeLabel target : labels ) {
        					queue.add( new IncomingFrame( out, target ));
        				}
        			}
        		}        		
        	}
        	
        	//process the exception handlers
        	if( ! processedHandlers ) {
        		for( Iterator<CodeAttribute.ExceptionHandler> i = code.handlers(); i.hasNext(); ) {
					CodeAttribute.ExceptionHandler handler = i.next();
					
					Frame f = Frame.forHandler( handler );
	        		queue.add( new IncomingFrame( f, handler.handler ) );
	        	}
        		
        		processedHandlers = true;
        	}
    	}
    }
    
    private class IncomingFrame {
    	Frame       incoming;
    	Instruction insn;    	
    	
    	IncomingFrame( Frame incoming, Instruction insn ) {
    		this.incoming = incoming;
    		this.insn     = insn;
    	}
    }
}
