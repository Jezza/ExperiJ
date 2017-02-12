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
public final class BooleanArgumentTest extends BaseTest {
	private static final String TEST_0 = "BOOLEAN_PARAMETER_TEST_0";
	private static final String TEST_1 = "BOOLEAN_PARAMETER_TEST_1";

	private static final AtomicInteger booleanHolder = new AtomicInteger(0);

	@Test
	public void testBoolean() {
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(booleanHolder.get(), 0);
		param0();
		expect(results.size(), 1);
		expect(booleanHolder.get(), 10);

		booleanHolder.set(0);

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(booleanHolder.get(), 0);
		param1(true);
		expect(results.size(), 1);
		expect(booleanHolder.get(), 5);
	}

	@Control(TEST_0)
	public void param0() {
		booleanHolder.addAndGet(5);
	}

	@Experiment(TEST_0)
	public void param0_() {
		booleanHolder.addAndGet(5);
	}

	@Control(TEST_1)
	public void param1(boolean x) {
		booleanHolder.addAndGet(x ? 5 : 0);
	}

	@Experiment(TEST_1)
	public void param1_(boolean x) {
		booleanHolder.addAndGet(x ? 0 : 5);
	}
}