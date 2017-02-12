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
public final class ShortArgumentTest extends BaseTest {
	private static final String TEST_0 = "SHORT_PARAMETER_TEST_0";
	private static final String TEST_1 = "SHORT_PARAMETER_TEST_1";

	private static final AtomicInteger shortHolder = new AtomicInteger(0);

	@Test
	public void testShort() {
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(shortHolder.get(), 0);
		param0();
		expect(results.size(), 1);
		expect(shortHolder.get(), 10);

		shortHolder.set(0);

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(shortHolder.get(), 0);
		param1(20);
		expect(results.size(), 1);
		expect(shortHolder.get(), 40);
	}

	@Control(TEST_0)
	public void param0() {
		shortHolder.addAndGet(5);
	}

	@Experiment(TEST_0)
	public void param0_() {
		shortHolder.addAndGet(5);
	}

	@Control(TEST_1)
	public void param1(int x) {
		shortHolder.addAndGet(x);
	}

	@Experiment(TEST_1)
	public void param1_(int x) {
		shortHolder.addAndGet(x);
	}
}
