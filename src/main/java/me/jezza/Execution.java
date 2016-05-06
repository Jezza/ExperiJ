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
		results.append(experimentName);
		results.append('(');
		// Append all of the parameters.
		List<String> params = this.params;
		if (!params.isEmpty())
			params.forEach(results::append);
		results.append("):");

		// Append the control time.
		ControlTime control = this.control;
		results.append(control.methodName()).append(':').append(control.time());
		results.append(',');
		// Append the experiments.
		Map<String, ExperimentTime> experiments = this.experiments;
		for (Entry<String, ExperimentTime> entry : experiments.entrySet()) {
			ExperimentTime exp = entry.getValue();
			results.append(exp.methodName()).append(':').append(exp.time()).append(':').append(exp.equal());
			results.append(',');
		}
		results.deleteCharAt(results.length() - 1);
		return results.toString();
	}
}

