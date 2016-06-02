package me.jezza;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		if (!params.isEmpty()) {
			Iterator<String> it = params.iterator();
			results.append(it.next());
			while (it.hasNext())
				results.append(", ").append(it.next());
		}
		results.append("):");
		// Append the control time.
		results.append(this.control);
		// Append the experiments.
		for (ExperimentTime exp : this.experiments.values())
			results.append('|').append(exp);
		return results.toString();
	}
}

