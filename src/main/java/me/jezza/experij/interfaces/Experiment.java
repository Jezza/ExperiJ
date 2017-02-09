package me.jezza.experij.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.jezza.experij.repackage.org.objectweb.asm.Type;

/**
 * Placed on a method, this annotation informs ExperiJ that this method is apart of an experiment set, and should have at least one matching control method with the same experiment name.
 * If ExperiJ can't find one, it will throw an exception and fail to start.
 * <p>
 * The experiment methods MUST have the same method descriptor from the control, otherwise ExperiJ will throw an exception, and fail to start.
 *
 * @author Jezza
 * @see Control for more information.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Experiment {
	String DESCRIPTOR = Type.getDescriptor(Experiment.class);

	/**
	 * This has to be the same as the control you're trying to test it with, otherwise ExperiJ won't know what to do.
	 * If it can't find ANY experiment set that this method belongs to, it will throw an exception, and fail to start.
	 *
	 * @return - The experiment set's name.
	 */
	String value();
}
