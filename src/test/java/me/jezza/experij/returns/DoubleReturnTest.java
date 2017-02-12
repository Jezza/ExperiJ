package me.jezza.experij.returns;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class DoubleReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "DOUBLE_RETURN_TEST_0";

	@Test
	public void testDouble() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public double returnValue() {
		return 0D;
	}

	@Experiment(TEST_0)
	public double returnCorrect() {
		return 0D;
	}

	@Experiment(TEST_0)
	public double returnIncorrect() {
		return 1D;
	}
}
