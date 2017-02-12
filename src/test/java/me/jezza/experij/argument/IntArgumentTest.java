package me.jezza.experij.argument;

import java.util.concurrent.atomic.AtomicInteger;

import me.jezza.experij.BaseTest;
import me.jezza.experij.ExperiJ;
import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import me.jezza.experij.interfaces.Results;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class IntArgumentTest extends BaseTest {
	private static final String TEST_0 = "INT_PARAMETER_TEST_0";
	private static final String TEST_1 = "INT_PARAMETER_TEST_1";

	private static final AtomicInteger intHolder = new AtomicInteger(0);

	@Test
	public void testInt() {
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(intHolder.get(), 0);
		param0();
		expect(results.size(), 1);
		expect(intHolder.get(), 10);

		intHolder.set(0);

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(intHolder.get(), 0);
		param1(10);
		expect(results.size(), 1);
		expect(intHolder.get(), 20);
	}

	@Control(TEST_0)
	public void param0() {
		intHolder.addAndGet(5);
	}

	@Experiment(TEST_0)
	public void param0_() {
		intHolder.addAndGet(5);
	}

	@Control(TEST_1)
	public void param1(int x) {
		intHolder.addAndGet(x);
	}

	@Experiment(TEST_1)
	public void param1_(int x) {
		intHolder.addAndGet(x);
	}
}
