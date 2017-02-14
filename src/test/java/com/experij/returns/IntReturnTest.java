package com.experij.returns;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class IntReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "INT_RETURN_TEST_0";

	@Test
	public void testInt() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public int returnValue() {
		return 0;
	}

	@Experiment(TEST_0)
	public int returnCorrect() {
		return 0;
	}

	@Experiment(TEST_0)
	public int returnIncorrect() {
		return 1;
	}
}
