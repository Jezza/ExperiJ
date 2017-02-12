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
public final class ArrayArgumentTest extends BaseTest {
	private static final String TEST_0 = "ARRAY_PARAMETER_TEST_0";
	private static final String TEST_1 = "ARRAY_PARAMETER_TEST_1";

	private static final String[] INPUT = {
			"first",
			"second",
			"third",
			"fourth",
			"fifth"
	};

	private static int size;

	@Test
	public void testObject() {
		size = 0;
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(size, 0);
		param0();
		expect(results.size(), 1);
		expect(size, 10);

		size = 0;

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(size, 0);
		param1(INPUT);
		expect(results.size(), 1);
		expect(size, 10);
	}

	@Control(TEST_0)
	public void param0() {
		size += 5;
	}

	@Experiment(TEST_0)
	public void param0_() {
		size += 5;
	}

	@Control(TEST_1)
	public void param1(String[] x) {
		size += x.length;
	}

	@Experiment(TEST_1)
	public void param1_(String[] x) {
		size += x.length;
	}
}