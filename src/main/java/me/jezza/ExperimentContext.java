package me.jezza;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Jezza
 */
public final class ExperimentContext {
	public static final String INTERNAL_NAME = "me/jezza/ExperimentContext";

	private final ExperimentResults results;
	private final String experimentName;
	private final String controlMethod;

	private final Map<Long, ExperimentData> activeData = new TreeMap<>();

	private final AtomicLong nextKey = new AtomicLong(0);

	protected ExperimentContext(String experimentName, String controlMethod) {
		this.results = ExperiJ.RESULT_MAP.get(experimentName);
		this.experimentName = experimentName;
		this.controlMethod = controlMethod;
	}

	public long startControl() {
		ExperimentData data = new ExperimentData(controlMethod);
		long key = nextKey.getAndIncrement();
		System.out.println("[Starting Control]:" + experimentName + ':' + controlMethod);
		activeData.put(key, data);
		data.startControl();
		return key;
	}

	public void stopControl(long key) {
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalStateException("ExperimentContext key is invalid!");
		data.stopControl();
		System.out.println("[Stopping Control]:" + experimentName + ':' + controlMethod + ':' + key);
	}

	public void start(long key, String methodName) {
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalStateException("ExperimentContext key is invalid!");
		System.out.println("[Starting Experiment]:" + experimentName + ':' + methodName + ':' + key);
		data.start(methodName);
	}

	public void stop(long key, String methodName) {
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalStateException("ExperimentContext key is invalid!");
		data.stop(methodName);
		System.out.println("[Stopping Experiment]:" + experimentName + ':' + methodName + ':' + key);
	}

	public void reportEquality(long key, String methodName, boolean equal) {
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalStateException("ExperimentContext key is invalid!");
		data.equality(methodName, equal);
		System.out.println("[Reporting Equality]:" + experimentName + ':' + methodName + ':' + key + ':' + equal);
	}

	public void compile(long key) {
		ExperimentData data = activeData.remove(key);
		if (data == null)
			throw new IllegalStateException("ExperimentContext key is invalid!");
		results.addDataEntry(data);
	}
}
