package org.epistem.j2avm.annotations.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation on fields and methods to indicate their AVM2 slot or dispatch id. 
 *
 * @author nickmain
 */
@Retention( RetentionPolicy.RUNTIME )
@Target({ ElementType.METHOD, ElementType.FIELD })
public @interface SlotId {
    int value();
}
