package me.jezza.experij.argument;

import me.jezza.experij.BaseTest;
import me.jezza.experij.ExperiJ;
import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import me.jezza.experij.interfaces.Results;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class DoubleArgumentTest extends BaseTest {
	private static final String TEST_0 = "DOUBLE_PARAMETER_TEST_0";
	private static final String TEST_1 = "DOUBLE_PARAMETER_TEST_1";

	private static double doubleHolder = 0D;

	@Test
	public void testDouble() {
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(doubleHolder, 0);
		param0();
		expect(results.size(), 1);
		expect(doubleHolder, 10);

		doubleHolder = 0;

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(doubleHolder, 0);
		param1(5D);
		expect(results.size(), 1);
		expect(doubleHolder, 10);
	}

	@Control(TEST_0)
	public void param0() {
		doubleHolder += 5D;
	}

	@Experiment(TEST_0)
	public void param0_() {
		doubleHolder += 5D;
	}

	@Control(TEST_1)
	public void param1(double x) {
		doubleHolder += x;
	}

	@Experiment(TEST_1)
	public void param1_(double x) {
		doubleHolder += x;
	}
}