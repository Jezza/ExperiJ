package me.jezza.experij.returns;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class CharReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "CHAR_RETURN_TEST_0";

	@Test
	public void testChar() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public char returnValue() {
		return 'a';
	}

	@Experiment(TEST_0)
	public char returnCorrect() {
		return 'a';
	}

	@Experiment(TEST_0)
	public char returnIncorrect() {
		return 'b';
	}
}
