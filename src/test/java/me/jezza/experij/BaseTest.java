package me.jezza.experij;

import org.junit.jupiter.api.Assertions;

/**
 * @author Jezza
 */
public abstract class BaseTest {
	protected void expect(int got, int expected) {
		Assertions.assertTrue(expected == got, "expected: " + expected + ", got: " + got);
	}

	protected void expect(long got, long expected) {
		Assertions.assertTrue(expected == got, "expected: " + expected + ", got: " + got);
	}

	protected void expect(float got, float expected) {
		Assertions.assertTrue(expected == got, "expected: " + expected + ", got: " + got);
	}

	protected void expect(double got, double expected) {
		Assertions.assertTrue(expected == got, "expected: " + expected + ", got: " + got);
	}
}
