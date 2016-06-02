package me.jezza;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO revisit: I either want to remove the method name parameter in a couple of methods, or actually make use of it.
 *
 * @author Jezza
 */
public final class ExperimentData {
	private final String experimentName;
	private final String controlMethod;
	private final List<String> params;
	private final Measurement[] measurements;

	private long control = -1;
	private int active = 0;

	public ExperimentData(String experimentName, String controlMethod, int experimentCount, String[] params) {
		this.experimentName = experimentName;
		this.controlMethod = controlMethod;
		this.params = Arrays.asList(params);
		measurements = new Measurement[experimentCount];
	}

	public void startControl() {
		control = System.nanoTime();
	}

	public void stopControl(long end) {
		control = end - control;

	}

	public void start(String methodName) {
		Measurement measurement = new Measurement();
		measurement.methodName = methodName;
		measurements[active] = measurement;
		measurement.time = System.nanoTime();
	}

	public void stop(String methodName, long end) {
		Measurement measurement = measurements[active];
		measurement.time = end - measurement.time;
	}

	public void equality(String methodName, boolean equal) {
		measurements[active++].equal = equal;
	}

	public void error(Throwable t) {
		Measurement measurement = measurements[active];
		measurement.time = ExperiJ.ERRORED;
		measurement.t = t;
		measurement.equal = false;
	}

	public void compile(ExperimentResults results) {
		Map<String, ExperimentTime> experiments = new HashMap<>();
		for (Measurement s : measurements)
			experiments.put(s.methodName, new ExperimentTime(s.methodName, s.time, s.equal, s.t));
		results.add(new Execution(experimentName, params, new ControlTime(controlMethod, control), experiments));
	}

	private static class Measurement {
		long time = ExperiJ.UNSET;
		String methodName;
		boolean equal;
		private Throwable t;
	}
}

