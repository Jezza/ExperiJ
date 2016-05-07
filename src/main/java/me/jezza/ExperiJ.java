package me.jezza;

import static me.jezza.lib.Strings.format;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import me.jezza.asm.ExperiJClassTransformer;
import me.jezza.interfaces.Results;
import me.jezza.test.Test;

/**
 * @author Jezza
 */
public final class ExperiJ {
	/**
	 * Used as the entry point for methods.
	 */
	public static final String HIDDEN_EXPERIMENT_ENTRY_POINT_FORMAT = "$experiment$_{}";

	/**
	 * Used to rename the experiment methods, so that we can insert methods with the original names, thus gaining control of the flow.
	 */
	public static final String RENAME_METHOD_FORMAT = "{}_$hidden$";


	public static final boolean DEBUG = Boolean.getBoolean("experij.debug");

	public static final boolean DISABLED = Boolean.getBoolean("experij.disable");

	protected static final Map<String, ExperimentResults> RESULT_MAP = new HashMap<>();
	protected static final Map<String, ExperimentContext> CONTEXT_MAP = new HashMap<>();

	private ExperiJ() {
		throw new IllegalStateException();
	}

	public static void premain(String agentArgs, Instrumentation inst) {
		if (DISABLED) {
			System.out.println("ExperiJ has been disabled via SystemProperties");
			return;
		}
		if (DEBUG)
			System.out.println("ExperiJ is in DEBUG mode.");
		System.out.println("Injecting: " + ExperiJClassTransformer.class.getCanonicalName());
		inst.addTransformer(new ExperiJClassTransformer());
	}

	public static void main(String[] args) {
		System.out.println(Test.integerToBinaryString(1));
		System.out.println(Test.integerToBinaryString(2));
		System.out.println(Test.integerToBinaryString(4));
		System.out.println(Test.integerToBinaryString(8));
		System.out.println(Test.integerToBinaryString(16));
		System.out.println(Test.integerToBinaryString(32));
		System.out.println(Test.integerToBinaryString(64));
		System.out.println(Test.integerToBinaryString(128));
		System.out.println(Test.integerToBinaryString(256));

		System.out.println("--Internals--");
		for (Method method : Test.class.getDeclaredMethods())
			System.out.println(method.toGenericString());

		System.out.println("--Results--");
		System.out.println(Test.TEST_THING);
	}

	public static Results results(String experimentName) {
		return RESULT_MAP.computeIfAbsent(experimentName, ExperimentResults::new).immutable();
	}

	public static ExperimentContext context(String experimentName, String controlMethod, int experimentCount) {
		return CONTEXT_MAP.computeIfAbsent(experimentName, k -> new ExperimentContext(k, controlMethod, experimentCount));
	}

	public static void setup(String experimentName) {
		if (RESULT_MAP.containsKey(experimentName))
			throw new IllegalStateException(format("Different Experiment Sets have the same name: {}", experimentName));
		results(experimentName);
	}
}