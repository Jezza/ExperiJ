package me.jezza;

import me.jezza.lib.Strings;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ExperimentTime that = (ExperimentTime) o;

		if (time != that.time || equal != that.equal)
			return false;
		if (!methodName.equals(that.methodName))
			return false;
		return t != null ? t.equals(that.t) : that.t == null;

	}

	@Override
	public int hashCode() {
		int result = 31 * methodName.hashCode() + (int) (time ^ (time >>> 32));
		result = 31 * result + (equal ? 1 : 0);
		return 31 * result + (t != null ? t.hashCode() : 0);
	}

	@Override
	public String toString() {
		return Strings.format(t == null ? "{}:{}:{}" : "{}:{}:{}:{}", methodName, Long.toString(time), Boolean.toString(equal), t);
	}
}
