package com.experij.returns;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ObjectReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "OBJECT_RETURN_TEST_0";
	private static final String TEST_1 = "OBJECT_RETURN_TEST_1";

	@Test
	public void testString() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
		runTest(TEST_1, "returnCorrect2", "returnIncorrect2", this::returnValue2);
	}

	@Control(TEST_0)
	public String returnValue() {
		return "first";
	}

	@Experiment(TEST_0)
	public String returnCorrect() {
		return "first";
	}

	@Experiment(TEST_0)
	public String returnIncorrect() {
		return "second";
	}

	@Control(TEST_1)
	public String returnValue2() {
		return "first";
	}

	@Experiment(TEST_1)
	public String returnCorrect2() {
		return "first";
	}

	@Experiment(TEST_1)
	public String returnIncorrect2() {
		return null;
	}
}