package me.jezza.experij;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import me.jezza.experij.lib.Strings;

/**
 * @author Jezza
 */
public final class ExperimentContext {
	public static final String INTERNAL_NAME = Strings.name(ExperimentContext.class);

	private final ExperimentResults results;
	private final String experimentName;
	private final String controlMethod;
	private final int experimentCount;
	private final Map<Long, ExperimentData> activeData = new HashMap<>();
	private final AtomicLong nextKey = new AtomicLong(0);


	protected ExperimentContext(String experimentName, String controlMethod, int experimentCount) {
		this.results = ExperiJ.RESULT_MAP.computeIfAbsent(experimentName, ExperimentResults::new);
		this.experimentName = experimentName;
		this.controlMethod = controlMethod;
		this.experimentCount = experimentCount;
	}

	public long startControl(String... params) {
		ExperimentData data = new ExperimentData(experimentName, controlMethod, experimentCount, params);
		long key = nextKey.getAndIncrement();
		if (ExperiJ.DEBUG)
			System.out.println("[Starting Control]:" + experimentName + ':' + controlMethod);
		activeData.put(key, data);
		data.startControl();
		return key;
	}

	public void stopControl(long key) {
		long end = System.nanoTime();
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalArgumentException("ExperimentContext key is invalid!");
		data.stopControl(end);
		if (ExperiJ.DEBUG)
			System.out.println("[Stopping Control]:" + experimentName + ':' + controlMethod + ':' + key);
	}

	public void start(long key, String methodName) {
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalArgumentException("ExperimentContext key is invalid!");
		if (ExperiJ.DEBUG)
			System.out.println("[Starting Experiment]:" + experimentName + ':' + methodName + ':' + key);
		data.start(methodName);
	}

	public void stop(long key, String methodName) {
		long end = System.nanoTime();
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalArgumentException("ExperimentContext key is invalid!");
		data.stop(methodName, end);
		if (ExperiJ.DEBUG)
			System.out.println("[Stopping Experiment]:" + experimentName + ':' + methodName + ':' + key);
	}

	public void reportEquality(long key, String methodName, boolean equal) {
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalArgumentException("ExperimentContext key is invalid!");
		data.equality(methodName, equal);
		if (ExperiJ.DEBUG)
			System.out.println("[Reporting Equality]:" + experimentName + ':' + methodName + ':' + key + ':' + equal);
	}

	public void error(long key, Throwable t) {
		ExperimentData data = activeData.get(key);
		if (data == null)
			throw new IllegalArgumentException("ExperimentContext key is invalid!");
		data.error(t);
		if (ExperiJ.DEBUG)
			System.out.println("[Errored]:" + experimentName);
	}

	public void compile(long key) {
		ExperimentData data = activeData.remove(key);
		if (data == null)
			throw new IllegalArgumentException("ExperimentContext key is invalid!");
		data.compile(results);
		if (ExperiJ.DEBUG)
			System.out.println("[Compiling]:" + experimentName);
	}
}
