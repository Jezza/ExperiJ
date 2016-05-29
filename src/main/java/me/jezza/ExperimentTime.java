package me.jezza;

/**
 * @author Jezza
 */
public final class ExperimentTime {
	private final String methodName;
	private final long time;
	private final boolean equal;
	private final Throwable t;

	ExperimentTime(String methodName, long time, boolean equal, Throwable t) {
		this.methodName = methodName;
		this.time = time;
		this.equal = equal;
		this.t = t;
	}

	public String methodName() {
		return methodName;
	}

	public long time() {
		return time;
	}

	public boolean equal() {
		return equal;
	}

	public boolean corrupt() {
		return t != null;
	}

	public Throwable caughtError() {
		return t;
	}
}
