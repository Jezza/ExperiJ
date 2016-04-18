package me.jezza.lib;

/**
 * @author Jezza
 */
public final class Equality {
	public static final String INTERNAL_NAME = "me/jezza/lib/Equality";

	private Equality() {
		throw new IllegalStateException();
	}

	public static boolean equals(boolean a, boolean b) {
		return a == b;
	}

	public static boolean equals(byte a, byte b) {
		return a == b;
	}

	public static boolean equals(char a, char b) {
		return a == b;
	}

	public static boolean equals(double a, double b) {
		return a == b;
	}

	public static boolean equals(float a, float b) {
		return a == b;
	}

	public static boolean equals(long a, long b) {
		return a == b;
	}

	public static boolean equals(short a, short b) {
		return a == b;
	}

	public static boolean equals(Object a, Object b) {
		return a == b || a != null && a.equals(b);
	}
}
