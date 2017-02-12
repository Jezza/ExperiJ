package me.jezza.experij.argument;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class DoubleArgumentTest extends AbstractArgumentTest {
	private static final String TEST_0 = "DOUBLE_PARAMETER_TEST_0";
	private static final String TEST_1 = "DOUBLE_PARAMETER_TEST_1";

	@Test
	public void testDouble() {
		runTest(TEST_0, 10, this::param0);
		runTest(TEST_1, 10, () -> param1(5D));
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
	public void param1(double x) {
		data += (int) x;
	}

	@Experiment(TEST_1)
	public void param1_(double x) {
		data += (int) x;
	}
}