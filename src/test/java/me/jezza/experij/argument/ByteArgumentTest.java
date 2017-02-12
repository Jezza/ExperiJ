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
public final class ByteArgumentTest extends BaseTest {
	private static final String TEST_0 = "BYTE_PARAMETER_TEST_0";
	private static final String TEST_1 = "BYTE_PARAMETER_TEST_1";

	private static final AtomicInteger byteHolder = new AtomicInteger(0);

	@Test
	public void testByte() {
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(byteHolder.get(), 0);
		param0();
		expect(results.size(), 1);
		expect(byteHolder.get(), 10);

		byteHolder.set(0);

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(byteHolder.get(), 0);
		param1((byte) 5);
		expect(results.size(), 1);
		expect(byteHolder.get(), 10);
	}

	@Control(TEST_0)
	public void param0() {
		byteHolder.addAndGet(5);
	}

	@Experiment(TEST_0)
	public void param0_() {
		byteHolder.addAndGet(5);
	}

	@Control(TEST_1)
	public void param1(byte x) {
		byteHolder.addAndGet(x);
	}

	@Experiment(TEST_1)
	public void param1_(byte x) {
		byteHolder.addAndGet(x);
	}
}