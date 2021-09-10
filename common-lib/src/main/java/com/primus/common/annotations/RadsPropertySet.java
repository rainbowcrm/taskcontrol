package com.primus.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD,ElementType.TYPE })
@Retention( RetentionPolicy.RUNTIME )
public @interface RadsPropertySet {
	
	String jsonTag() default "";
	String xmlTag() default "";
	
	boolean excludeFromJSON() default false;
	boolean excludeFromXML() default false;
	boolean excludeFromMap() default false;
	
	boolean useBKForJSON() default false;
	boolean useBKForXML() default false;
	boolean useBKForMap() default false;
	
	boolean usePKForJSON() default false;
	boolean usePKForXML() default false;
	boolean usePKForMap() default false;
	
	boolean isPK() default false;
	boolean isBK() default false;

}
