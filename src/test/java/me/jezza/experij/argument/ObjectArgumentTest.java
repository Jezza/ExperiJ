package me.jezza.experij.argument;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class ObjectArgumentTest extends AbstractArgumentTest {
	private static final String TEST_0 = "OBJECT_PARAMETER_TEST_0";
	private static final String TEST_1 = "OBJECT_PARAMETER_TEST_1";

	private Holder holder;

	@Test
	public void testObject() {
		holder = new Holder();
		runTest(TEST_0, 10, this::param0);
		holder = new Holder();
		runTest(TEST_1, 10, () -> param1(holder));
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

	public class Holder {
		public void add(int x) {
			data += x;
		}
	}
}