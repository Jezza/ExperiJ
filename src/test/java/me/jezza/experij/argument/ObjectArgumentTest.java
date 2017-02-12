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
public final class ObjectArgumentTest extends BaseTest {
	private static final String TEST_0 = "OBJECT_PARAMETER_TEST_0";
	private static final String TEST_1 = "OBJECT_PARAMETER_TEST_1";

	private static Holder holder;

	@Test
	public void testObject() {
		holder = new Holder();
		Results results = ExperiJ.results(TEST_0);
		expect(results.size(), 0);
		expect(holder.value, 0);
		param0();
		expect(results.size(), 1);
		expect(holder.value, 10);

		holder = new Holder();

		results = ExperiJ.results(TEST_1);
		expect(results.size(), 0);
		expect(holder.value, 0);
		param1(holder);
		expect(results.size(), 1);
		expect(holder.value, 10);
	}

	@Control(TEST_0)
	public void param0() {
		holder.add(5);
	}

	@Experiment(TEST_0)
	public void param0_() {
		holder.add(5);
	}

	@Control(TEST_1)
	public void param1(Holder x) {
		x.add(5);
	}

	@Experiment(TEST_1)
	public void param1_(Holder x) {
		x.add(5);
	}

	public static class Holder {
		private int value;

		public void add(int x) {
			value += x;
		}
	}
}