package com.experij.returns;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ShortReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "SHORT_RETURN_TEST_0";

	@Test
	public void testShort() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public short returnValue() {
		return 0;
	}

	@Experiment(TEST_0)
	public short returnCorrect() {
		return 0;
	}

	@Experiment(TEST_0)
	public short returnIncorrect() {
		return 1;
	}
}
