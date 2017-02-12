package me.jezza.experij.returns;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ByteReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "BYTE_RETURN_TEST_0";

	@Test
	public void testByte() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public byte returnValue() {
		return 0;
	}

	@Experiment(TEST_0)
	public byte returnCorrect() {
		return 0;
	}

	@Experiment(TEST_0)
	public byte returnIncorrect() {
		return 1;
	}
}