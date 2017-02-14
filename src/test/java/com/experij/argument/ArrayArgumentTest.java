package com.experij.argument;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ArrayArgumentTest extends AbstractArgumentTest {
	private static final String TEST_0 = "ARRAY_PARAMETER_TEST_0";
	private static final String TEST_1 = "ARRAY_PARAMETER_TEST_1";

	private static final String[] INPUT = {
			"first",
			"second",
			"third",
			"fourth",
			"fifth"
	};

	@Test
	public void testArray() {
		runTest(TEST_0, 10, this::param0);
		runTest(TEST_1, 10, () -> param1(INPUT));
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
	public void param1(String[] x) {
		data += x.length;
	}

	@Experiment(TEST_1)
	public void param1_(String[] x) {
		data += x.length;
	}
}