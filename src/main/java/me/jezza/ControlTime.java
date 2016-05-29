package me.jezza;

/**
 * @author Jezza
 */
public final class ControlTime {
	private final String methodName;
	private final long time;

	ControlTime(String methodName, long time) {
		this.methodName = methodName;
		this.time = time;
	}

	public String methodName() {
		return methodName;
	}

	public long time() {
		return time;
	}
}
