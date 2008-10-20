package org.epistem.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation on an enum constant that represents a flag.
 * 
 * @author nickmain
 */
@Retention( RetentionPolicy.RUNTIME )
@Target   ( ElementType.FIELD )
public @interface Flag {

	/**
	 * The bit value of the flag
	 */
	int value();
}
