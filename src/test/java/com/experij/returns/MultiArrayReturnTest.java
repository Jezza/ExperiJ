package com.experij.returns;

import com.experij.interfaces.Control;
import com.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public class MultiArrayReturnTest extends AbstractReturnTest {
	private static final String TEST_0 = "MULTI_ARRAY_RETURN_TEST_0";

	@Test
	public void testMultiArray() {
		runTest(TEST_0, "returnCorrect", "returnIncorrect", this::returnValue);
	}

	@Control(TEST_0)
	public String[][] returnValue() {
		return new String[][]{
				{
						"first",
						"second"
				},
				{
						"third",
						"fourth"
				}
		};
	}

	@Experiment(TEST_0)
	public String[][] returnCorrect() {
		return new String[][]{
				{
						"first",
						"second"
				},
				{
						"third",
						"fourth"
				}
		};
	}

	@Experiment(TEST_0)
	public String[][] returnIncorrect() {
		return new String[][]{
				{
						"first"
				},
				{
						"third"
				}
		};
	}
}