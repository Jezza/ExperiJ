package me.jezza;

import static me.jezza.lib.Strings.format;

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

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		ControlTime that = (ControlTime) o;
		return time == that.time && methodName.equals(that.methodName);

	}

	@Override
	public int hashCode() {
		return 31 * methodName.hashCode() + (int) (time ^ (time >>> 32));
	}

	@Override
	public String toString() {
		return format("{}:{}", methodName, Long.toString(time));
	}
}
