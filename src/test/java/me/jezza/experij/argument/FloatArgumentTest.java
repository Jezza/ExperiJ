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
public final class FloatArgumentTest extends BaseTest {
	private static final String TEST_0 = "FLOAT_PARAMETER_TEST_0";
	private static final String TEST_1 = "FLOAT_PARAMETER_TEST_1";

	private static float floatHolder = 0F;

	@Test
	public void testFloat() {
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(floatHolder, 0);
		param0();
		expect(results.size(), 1);
		expect(floatHolder, 10);

		floatHolder = 0;

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(floatHolder, 0);
		param1(5F);
		expect(results.size(), 1);
		expect(floatHolder, 10);
	}

	@Control(TEST_0)
	public void param0() {
		floatHolder += 5F;
	}

	@Experiment(TEST_0)
	public void param0_() {
		floatHolder += 5F;
	}

	@Control(TEST_1)
	public void param1(float x) {
		floatHolder += x;
	}

	@Experiment(TEST_1)
	public void param1_(float x) {
		floatHolder += x;
	}
}