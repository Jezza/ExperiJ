package me.jezza.experij.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import me.jezza.experij.ExperiJ;
import me.jezza.experij.repackage.org.objectweb.asm.Type;

/**
 * This annotation is placed on the "Control" method.
 * That method's value and descriptor will be used through the experiment set.
 * So, if something is wrong, either because of an off method signature, or because of an incorrect return value from an {@link Experiment} method, the {@link Results} will contain it.
 *
 * All Experiment sets must have at least 1 {@link Control} and one {@link Experiment}, otherwise ExperiJ will throw an exception, and fail to start.
 * All methods within an experiment set must have a consistent descriptor, otherwise ExperiJ will throw an exception, and fail to start.
 * This includes return values, exception signatures, etc.
 *
 * @author Jezza
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Control {
	String DESCRIPTOR = Type.getDescriptor(Control.class);

	/**
	 * This name tells ExperiJ what to call this experiment set.
	 * The experiment name has to be unique from the other experiments.
	 * All other methods with the Experiment annotation with the same value will be grouped together.
	 * It's probably best to store the name in a static final as there's not a great need to change it.
	 *
	 * If it's not unique, ExperiJ will throw an exception, and fail to start.
	 * The reason that the names have to be unique is because you can retrieve the results from anywhere.
	 * <pre>
	 *     {@link ExperiJ#results(String)} // Will return the {@link Results} for the experiment set.
	 * </pre>
	 *
	 * @return - The experiment set's name.
	 */
	String value();
}
