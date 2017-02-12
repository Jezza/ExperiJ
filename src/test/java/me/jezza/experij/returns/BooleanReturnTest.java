package me.jezza.experij.returns;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class BooleanReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "BOOLEAN_RETURN_TEST_0";

	@Test
	public void testBoolean() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public boolean returnValue() {
		return true;
	}

	@Experiment(TEST_0)
	public boolean returnCorrect() {
		return true;
	}

	@Experiment(TEST_0)
	public boolean returnIncorrect() {
		return false;
	}
}