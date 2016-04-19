package me.jezza.descriptor;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jezza
 */
public class DescriptorTest {

	private static final String[] PARAMETERS = {
			"Ljava/lang/String;", "Z", "B", "S", "I", "F", "D", "J", "C"
	};

	private static final String[] RETURN_VALUES = {
			"Ljava/lang/String;", "Z", "B", "S", "I", "F", "D", "J", "C", "V"
	};

	private static final Random random = new Random();

	private Descriptor randomDescriptor() {
		StringBuilder descriptor = new StringBuilder("(");
		// Random length, at least 16 parameters, and no more than 32.
		int length = 16 + random.nextInt(16);
		for (int i = 0; i < length; i++) {
			while (random.nextFloat() > 0.92F)
				descriptor.append('[');
			descriptor.append(PARAMETERS[random.nextInt(PARAMETERS.length)]);
		}
		descriptor.append(')');
		while (random.nextFloat() > 0.92F)
			descriptor.append('[');
		descriptor.append(RETURN_VALUES[random.nextInt(RETURN_VALUES.length)]);
		return Descriptor.from(descriptor.toString(), null, null, false);
	}

	@Test
	public void testReturnPart() throws Exception {
	}

	@Test
	public void testSignature() throws Exception {
	}

	@Test
	public void testExceptions() throws Exception {
	}

	@Test
	public void testLoadAll() throws Exception {
	}

	@Test
	public void testEquality() throws Exception {
	}

	@Test
	public void testParameterCount() throws Exception {
		Assert.assertTrue(randomDescriptor().parameterCount() >= 16);

		String input = "(Ljava/lang/String;Ljava/lang/String;ZDZFC[BCJLjava/lang/String;ZJBFJD[FIZJS)D";
		Descriptor from = Descriptor.from(input);
		Assert.assertTrue(from.parameterCount() == 22);

		input = "([SJZDLjava/lang/String;CCSIDJSSFZCIB[J)C";
		from = Descriptor.from(input);
		Assert.assertTrue(from.parameterCount() == 19);

		input = "(SDZZBJBBDDILjava/lang/String;Ljava/lang/String;FBZC)F";
		from = Descriptor.from(input);
		Assert.assertTrue(from.parameterCount() == 17);

		input = "(BFJBBCJCBSLjava/lang/String;CB[Ljava/lang/String;ZCLjava/lang/String;C[FLjava/lang/String;ZI)S";
		from = Descriptor.from(input);
		Assert.assertTrue(from.parameterCount() == 22);
	}

	@Test
	public void testEquals() throws Exception {
		Descriptor descriptor = randomDescriptor();
		String input = descriptor.toString();
		Descriptor clone = Descriptor.from(input);
		Assert.assertTrue("Equals not consistent!", descriptor.equals(clone));
	}

	@Test
	public void testHashCode() throws Exception {
		Descriptor descriptor = randomDescriptor();
		Assert.assertTrue("HashCode not consistent!", descriptor.hashCode() == descriptor.hashCode());
	}

	@Test
	public void testToString() throws Exception {
		String testInput = "(ILjava/lang/String;FZCJSBDIC[SZFCCJBZFFLjava/lang/String;[FSFC)V";
		Descriptor test = Descriptor.from(testInput);
		Assert.assertTrue(testInput.equals(test.toString()));

		testInput = "(Ljava/lang/String;Ljava/lang/String;FZLjava/lang/String;SDCBILjava/lang/String;Ljava/lang/String;DFFCFJLjava/lang/String;)B";
		test = Descriptor.from(testInput);
		Assert.assertTrue(testInput.equals(test.toString()));

		testInput = "(FCDDZZ[BSDBI[IDBCC[C)B";
		test = Descriptor.from(testInput);
		Assert.assertTrue(testInput.equals(test.toString()));

		testInput = "(Ljava/lang/String;SCLjava/lang/String;B[[CCCCFBZFFDS[IFF)Z";
		test = Descriptor.from(testInput);
		Assert.assertTrue(testInput.equals(test.toString()));

		testInput = "(JJBZCFZBIDJBFLjava/lang/String;CCILjava/lang/String;)Z";
		test = Descriptor.from(testInput);
		Assert.assertTrue(testInput.equals(test.toString()));
	}

	@Test
	public void testFrom() throws Exception {
		Descriptor descriptor = randomDescriptor();
		Assert.assertTrue(descriptor != null);
		Assert.assertTrue(descriptor.signature() == null);
		Assert.assertTrue(descriptor.exceptions() == null);
	}
}