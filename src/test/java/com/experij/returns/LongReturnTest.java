package com.experij.returns;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class LongReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "LONG_RETURN_TEST_0";

	@Test
	public void testLong() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public long returnValue() {
		return 0L;
	}

	@Experiment(TEST_0)
	public long returnCorrect() {
		return 0L;
	}

	@Experiment(TEST_0)
	public long returnIncorrect() {
		return 1L;
	}
}
