package me.jezza.experij.argument;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class BooleanArgumentTest extends AbstractArgumentTest {
	private static final String TEST_0 = "BOOLEAN_PARAMETER_TEST_0";
	private static final String TEST_1 = "BOOLEAN_PARAMETER_TEST_1";

	@Test
	public void testBoolean() {
		runTest(TEST_0, 10, this::param0);
		runTest(TEST_1, 8, () -> param1(true));
		runTest(TEST_1, 1, 7, () -> param1(false));
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
	public void param1(boolean x) {
		data += x ? 3 : 4;
	}

	@Experiment(TEST_1)
	public void param1_(boolean x) {
		data += x ? 5 : 3;
	}
}