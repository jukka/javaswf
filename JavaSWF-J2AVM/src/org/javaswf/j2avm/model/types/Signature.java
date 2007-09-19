package org.javaswf.j2avm.model.types;

/**
 * A method signature.
 *  
 * @author dmain
 */
public final class Signature {

	/** The method name */
	public final String name;
	
	/** The parameter types */
	public final ValueType[] paramTypes;
	
	private final int hashcode;	
	
	public Signature( String name, ValueType[] paramTypes ) {
		this.paramTypes = paramTypes;
		this.name       = name;
		
		int hash = name.hashCode();
		for( ValueType vt : paramTypes ) {
			hash *= vt.hashCode();
		}
		
		hashcode = hash;
	}

	@Override
	public boolean equals(Object obj) {
		if( obj == null || !( obj instanceof Signature )) return false;
		Signature sig = (Signature) obj;
		
		if( ! sig.name.equals( name ) ) return false;
		if( sig.paramTypes.length != paramTypes.length ) return false;
		
		for (int i = 0; i < paramTypes.length; i++) {
			if( ! paramTypes[i].equals( sig.paramTypes[i] ) ) return false;
		}
		
		return true;
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder( name );
		buff.append( "(" );
		
		boolean first = true;
		for( ValueType vt : paramTypes ) {
			if( first ) first = false;
			else buff.append( "," );
			
			buff.append( vt.name );
		}
		
		buff.append( ")" );
		return buff.toString();
	}
}
