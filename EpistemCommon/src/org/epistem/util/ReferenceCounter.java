// Created on Apr 28, 2005

package org.epistem.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Maintains reference counts for objects.
 *
 * @author nick
 */
public class ReferenceCounter {

	private Map referenceCounts = new HashMap();
	
	/**
	 * Get the reference count for an object.
	 */
	public int referenceCount( Object key ) {
		int[] count = (int[]) referenceCounts.get( key );
		if( count != null ) return count[0];
		return 0;
	}
	
	/**
	 * Clear all reference counts.
	 */
	public void clearAll() {
		referenceCounts.clear();
	}

	/**
	 * Clear all reference counts for a given object.
	 */
	public void clear( Object key ) {
		referenceCounts.remove( key );
	}
	
	/**
	 * Get the number of objects with references.
	 */
	public int size() {
		return referenceCounts.size();
	}
	
	/**
	 * Get a set of all objects that have references.
	 * 
	 * @return may be an empty set.
	 */
	public Set referencedObjects() {
		return referenceCounts.keySet();
	}
	
	/**
	 * Decrement the reference count for an object (assuming it is > 0).
	 * @return the new reference count.
	 */
	public int decrement( Object key ) {
		return decrement( key, 1 );		
	}

	/**
	 * Decrement the reference count for an object.
	 * @param count the amount to decrement by, must be >= 1
	 * @return the new reference count, >= 0
	 */
	public int decrement( Object key, int count ) {
		int[] ca = (int[]) referenceCounts.get( key );
		if( ca == null ) return 0;
		
		int c = ca[0] -= count;
		if( c <= 0 ) {
			referenceCounts.remove( key );
			return 0;
		}
		
		return c;		
	}
	
	/**
	 * Increment the reference count for an object.
	 * 
	 * @return the new reference count
	 */
	public int increment( Object key ) {
		return increment( key, 1 );
	}
	
	/**
	 * Increment the reference count for an object.
	 * 
	 * @param count the amount to increment, must be >= 1
	 * @return the new reference count
	 */
	public int increment( Object key, int count ) {
		int[] c = (int[]) referenceCounts.get( key );
		if( c == null ) {
			c = new int[] {count};
			referenceCounts.put( key, c );
			return count;
		}
		
		return c[0] += count;
	}

}
