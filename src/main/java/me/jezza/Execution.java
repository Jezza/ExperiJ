package me.jezza;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Jezza
 */
public final class Execution {
	private final String experimentName;
	private final List<String> params;
	private final ControlTime control;
	private final Map<String, ExperimentTime> experiments;

	Execution(String experimentName, List<String> params, ControlTime control, Map<String, ExperimentTime> experiments) {
		this.experimentName = experimentName;
		this.params = params;
		this.control = control;
		this.experiments = Collections.unmodifiableMap(experiments);
	}

	public String experimentName() {
		return experimentName;
	}

	public List<String> params() {
		return params;
	}

	public ControlTime control() {
		return control;
	}

	public Map<String, ExperimentTime> experiments() {
		return experiments;
	}

	@Override
	public String toString() {
		StringBuilder results = new StringBuilder();
		results.append(experimentName).append('(');
		// Append all of the parameters.
		List<String> params = this.params;
		if (!params.isEmpty())
			params.forEach(results::append);
		results.append("):");

		// Append the control time.
		results.append(this.control).append('|');
		// Append the experiments.
		Map<String, ExperimentTime> experiments = this.experiments;
		for (Entry<String, ExperimentTime> entry : experiments.entrySet()) {
			ExperimentTime exp = entry.getValue();
			results.append(exp.methodName()).append(':');
			long time = exp.time();
			if (time == ExperiJ.ERRORED) {
				results.append("Errored=(").append(exp.caughtError().getMessage()).append("\")");
			} else if (time == ExperiJ.UNSET) {
				results.append("Unfinished");
			} else {
				results.append(time);
			}
			results.append(':').append(exp.equal()).append('|');
		}
		results.deleteCharAt(results.length() - 1);
		return results.toString();
	}
}

