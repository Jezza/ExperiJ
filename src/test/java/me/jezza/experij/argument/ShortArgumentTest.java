package me.jezza.experij.argument;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ShortArgumentTest extends AbstractArgumentTest {
	private static final String TEST_0 = "SHORT_PARAMETER_TEST_0";
	private static final String TEST_1 = "SHORT_PARAMETER_TEST_1";

	@Test
	public void testShort() {
		runTest(TEST_0, 10, this::param0);
		runTest(TEST_1, 40, () -> param1((short) 20));
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
	public void param1(short x) {
		data += x;
	}

	@Experiment(TEST_1)
	public void param1_(short x) {
		data += x;
	}
}
