package com.experij.argument;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ByteArgumentTest extends AbstractArgumentTest {
	private static final String TEST_0 = "BYTE_PARAMETER_TEST_0";
	private static final String TEST_1 = "BYTE_PARAMETER_TEST_1";

	@Test
	public void testByte() {
		runTest(TEST_0, 10, this::param0);
		runTest(TEST_1, 10, () -> param1((byte) 5));
	}

	@Control(TEST_0)
	public void param0() {
		data += 5;
	}

	@Experiment(TEST_0)
	public void param0_() {
		data += 5;
	}

	@Control(TEST_1)
	public void param1(byte x) {
		data += x;
	}

	@Experiment(TEST_1)
	public void param1_(byte x) {
		data += x;
	}
}