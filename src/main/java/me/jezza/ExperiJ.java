package me.jezza;

import static me.jezza.lib.Strings.format;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

import me.jezza.asm.ExperiJClassTransformer;
import me.jezza.interfaces.Results;
import me.jezza.lib.Skip;

/**
 * @author Jezza
 */
public final class ExperiJ {
	/**
	 * Used as the entry point for methods.
	 */
	public static final String ENTRY_POINT_FORMAT = "$experiment$_{}";

	/**
	 * Used to rename the experiment methods, so that we can insert methods with the original names, thus gaining control of the flow.
	 */
	public static final String RENAMED_METHOD_FORMAT = "{}_$hidden$";

	/**
	 * TODO Might remove and replace it with an actual logging system.
	 */
	public static final boolean DEBUG = Boolean.getBoolean("experij.debug");

	/**
	 * If this System property is set, ExperiJ is completely disabled, and no experiments will be performed.
	 */
	public static final boolean DISABLED = Boolean.getBoolean("experij.disable");

	/**
	 * Used to show the default time field within {@link ControlTime} or {@link ExperimentTime} as unset, which means it wasn't started.
	 * This typically means that something errored before it could execute.
	 */
	public static final int UNSET = -1;

	/**
	 * Used to show the default time field within {@link ControlTime} or {@link ExperimentTime} as errored.
	 * This means it threw an exception.
	 */
	public static final int ERRORED = -2;

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
		// If we take to long in here, such as trying to print 3+ different statements, it starts colliding with other prints, so we just create a simple StringBuilder, and then dump it out all in one go.
		StringBuilder output = new StringBuilder();
		if (DEBUG)
			output.append("ExperiJ is in DEBUG mode.\n");
		output.append("Compiling ").append(Skip.load()).append("...\n");
		output.append("Injecting: ").append(ExperiJClassTransformer.class.getCanonicalName()).append('\n');
		System.out.println(output.toString());
		inst.addTransformer(new ExperiJClassTransformer());
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