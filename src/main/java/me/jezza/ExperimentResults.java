package me.jezza;

import java.util.HashMap;
import java.util.Map;

import me.jezza.interfaces.Results;

/**
 * @author Jezza
 */
public final class ExperimentResults implements Results {
	public static final String INTERNAL_NAME = "me/jezza/ExperimentResults";
	public static final String CREATE_DESCRIPTOR = "(Ljava/lang/String;Ljava/lang/String;)L" + INTERNAL_NAME + ';';

	private static final Map<String, ExperimentResults> RESULTS = new HashMap<>();

	private final String experimentName;
	private final String controlMethod;

	private ExperimentResults(String experimentName, String controlMethod) {
		this.experimentName = experimentName;
		this.controlMethod = controlMethod;
	}

	public long startControl() {
		System.out.println("[Starting Control]:" + experimentName + ':' + controlMethod);
		return 1;
	}

	public void stopControl(long key) {
		System.out.println("[Stopping Control]:" + experimentName + ':' + controlMethod + ':' + key);
	}

	public void start(long key, String methodName) {
		System.out.println("[Starting Experiment]:" + experimentName + ':' + methodName + ':' + key);
	}

	public void stop(long key, String methodName) {
		System.out.println("[Stopping Experiment]:" + experimentName + ':' + methodName + ':' + key);
	}

	public void reportEquality(long key, String methodName, boolean equal) {
		System.out.println("[Reporting Equality]:" + experimentName + ':' + methodName + ':' + key + ':' + equal);
	}

	public static Results from(String experimentName) {
		return RESULTS.get(experimentName);
	}

	public static ExperimentResults create(String experimentName, String controlMethod) {
		return RESULTS.computeIfAbsent(experimentName, (k) -> new ExperimentResults(experimentName, controlMethod));
	}
}
