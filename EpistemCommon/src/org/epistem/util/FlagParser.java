package org.epistem.util;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * Utility for parsing flags to produce enums.
 * 
 * @author nickmain
 *
 * @see Flag
 */
public final class FlagParser<E extends Enum<E>> {

	private final Class<E> enumClass;
	private final Map<Integer, E> bits2enums = new HashMap<Integer, E>();
	
	/**
	 * Make a parser for the given class
	 */
	public static <T extends Enum<T>> FlagParser<T> forClass( Class<T> clazz ) {
	    return new FlagParser<T>( clazz );
	}
	
	private FlagParser( Class<E> enumClass ) {
		this.enumClass = enumClass;
		
		try {
			for( Field f : enumClass.getFields() ) {				
				if( enumClass.isAssignableFrom( f.getType() ) ) {
					Flag flag = f.getAnnotation( Flag.class );
					if( flag != null ) {
						
						@SuppressWarnings( "unchecked" )
						E e = (E) f.get(null);
						
						bits2enums.put( flag.value(), e );
					}
				}
			}
		} catch( Exception ex ) {
			throw new RuntimeException( ex );
		}
	}
	
	/**
	 * Parse a set of enum constants from a set of flag bits.
	 * 
	 * @param flags the flag bits to parse
	 * @return set of enum constants
	 */
	public Set<E> parse( int flags ) {
		EnumSet<E> set = EnumSet.noneOf( enumClass );
				
		for( Map.Entry<Integer, E> entry : bits2enums.entrySet() ) {
			int bits = entry.getKey();
			E   e    = entry.getValue();
			
			if(( flags & bits ) != 0 ) set.add( e );
		}
		
		return set;
	}
}
