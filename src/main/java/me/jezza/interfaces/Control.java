package me.jezza.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.jezza.repackage.org.objectweb.asm.Type;

/**
 * @author Jezza
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Control {
	String DESCRIPTOR = Type.getDescriptor(Control.class);

	String value();
}
