package me.jezza.experij.returns;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ArrayReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "ARRAY_RETURN_TEST_0";

	@Test
	public void testArray() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public String[] returnValue() {
		return new String[]{
				"first",
				"second",
				"third"
		};
	}

	@Experiment(TEST_0)
	public String[] returnCorrect() {
		return new String[]{
				"first",
				"second",
				"third"
		};
	}

	@Experiment(TEST_0)
	public String[] returnIncorrect() {
		return new String[]{
				"first",
				"second"
		};
	}
}