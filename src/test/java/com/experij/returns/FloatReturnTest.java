package com.experij.returns;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class FloatReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "FLOAT_RETURN_TEST_0";

	@Test
	public void testFloat() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public float returnValue() {
		return 0F;
	}

	@Experiment(TEST_0)
	public float returnCorrect() {
		return 0F;
	}

	@Experiment(TEST_0)
	public float returnIncorrect() {
		return 1F;
	}
}
