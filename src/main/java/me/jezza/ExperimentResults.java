package me.jezza;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.jezza.interfaces.Results;

/**
 * @author Jezza
 */
public final class ExperimentResults implements Results, Serializable {
	private final List<ExperimentData> data = new ArrayList<>();

	private long executions = 0;
	private double average = 0;
	private double median = 0;
	private double max = 0;
	private double min = 0;

	protected ExperimentResults(String experimentName) {
		System.out.println("Creating results for: " + experimentName);
	}

	@Override
	public long executionCount() {
		return 0;
	}

	@Override
	public Map<String, Object> times() {
		return null;
	}

	protected void addDataEntry(ExperimentData data) {
	}
}
