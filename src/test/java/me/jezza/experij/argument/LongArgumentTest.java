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
public final class LongArgumentTest extends BaseTest {
	private static final String TEST_0 = "LONG_PARAMETER_TEST_0";
	private static final String TEST_1 = "LONG_PARAMETER_TEST_1";

	private static final AtomicInteger longHolder = new AtomicInteger(0);

	@Test
	public void testLong() {
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(longHolder.get(), 0);
		param0();
		expect(results.size(), 1);
		expect(longHolder.get(), 10);

		longHolder.set(0);

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(longHolder.get(), 0);
		param1(5);
		expect(results.size(), 1);
		expect(longHolder.get(), 10);
	}

	@Control(TEST_0)
	public void param0() {
		longHolder.addAndGet(5);
	}

	@Experiment(TEST_0)
	public void param0_() {
		longHolder.addAndGet(5);
	}

	@Control(TEST_1)
	public void param1(int x) {
		longHolder.addAndGet(x);
	}

	@Experiment(TEST_1)
	public void param1_(int x) {
		longHolder.addAndGet(x);
	}
}
