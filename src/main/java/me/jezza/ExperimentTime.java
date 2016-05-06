package me.jezza;

/**
 * @author Jezza
 */
public final class ExperimentTime extends ControlTime {
	private final boolean equal;

	ExperimentTime(String methodName, long time, boolean equal) {
		super(methodName, time);
		this.equal = equal;
	}

	public boolean equal() {
		return equal;
	}
}
