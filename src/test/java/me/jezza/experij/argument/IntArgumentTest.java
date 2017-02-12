package me.jezza.experij.argument;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class IntArgumentTest extends AbstractArgumentTest {
	private static final String TEST_0 = "INT_PARAMETER_TEST_0";
	private static final String TEST_1 = "INT_PARAMETER_TEST_1";

	@Test
	public void testInt() {
		runTest(TEST_0, 10, this::param0);
		runTest(TEST_1, 20, () -> param1(10));
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
	public void param1(int x) {
		data += x;
	}

	@Experiment(TEST_1)
	public void param1_(int x) {
		data += x;
	}
}
