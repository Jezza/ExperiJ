package me.jezza.descriptor;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jezza
 */
public class DescriptorTest extends AbstractTest {
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
		Descriptor descriptor = randomDescriptor();
		Assert.assertTrue(descriptor.toString(), descriptor.parameterCount() >= 16);

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
		// The first couple just test the random descriptor method, making sure that nothing broke.
		Assert.assertTrue(randomDescriptor() != null);
		Descriptor desc = randomDescriptor();
		Assert.assertTrue(desc.toString(), desc.signature() == null);
		Assert.assertTrue(desc.toString(), desc.exceptions() == null);

		desc = randomDescriptor(64, 1F);
		Assert.assertFalse(desc.toString(), desc.toString().contains("["));

		// Actually testing the descriptor parsing.
		desc = randomDescriptor(1);
		Assert.assertTrue(desc.toString(), desc.parameterCount() == 1);
		Assert.assertTrue(desc.toString(), desc.returnPart().index == 2);

		desc = randomDescriptor(1, true);
		Assert.assertTrue(desc.toString(), desc.parameterCount() == 1);
		Assert.assertTrue(desc.toString(), desc.returnPart().index == 1);

		desc = randomDescriptor(2);
		Assert.assertTrue(desc.toString(), desc.parameterCount() == 2);
		Assert.assertTrue(desc.toString(), desc.returnPart().index == 3);

		desc = randomDescriptor(2, true);
		Assert.assertTrue(desc.toString(), desc.parameterCount() == 2);
		Assert.assertTrue(desc.toString(), desc.returnPart().index == 2);

		desc = randomDescriptor(2, -1F);
		Assert.assertTrue(desc.toString(), desc.parameterCount() == 2);
		Assert.assertTrue(desc.toString(), desc.returnPart().index == 3);

		desc = randomDescriptor(2, -1F, true);
		Assert.assertTrue(desc.toString(), desc.parameterCount() == 2);
		Assert.assertTrue(desc.toString(), desc.returnPart().index == 2);
	}

	@Test
	public void testFromExceptions() throws Exception {
		try {
			Descriptor.from(null);
			throw new AssertionError("Descriptor constructor didn't throw a NullPointerException on null input."); // If this happens, something has gone wrong.
		} catch (NullPointerException e) {
			Assert.assertTrue("Descriptor input string is null".equals(e.getMessage()));
		}

		try {
			Descriptor.from("");
			throw new AssertionError("Descriptor constructor didn't throw a IllegalArgumentException on invalid input."); // If this happens, something has gone wrong.
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Illegal Descriptor.".equals(e.getMessage()));
		}

		try {
			Descriptor.from(")");
			throw new AssertionError("Descriptor constructor didn't throw a IllegalArgumentException on invalid input."); // If this happens, something has gone wrong.
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Illegal Descriptor.".equals(e.getMessage()));
		}

		try {
			Descriptor.from("(  ");
			throw new AssertionError("Descriptor constructor didn't throw a UnsupportedOperationException on invalid input."); // If this happens, something has gone wrong.
		} catch (UnsupportedOperationException e) {
			Assert.assertTrue("Unknown Descriptor Byte:  ".equals(e.getMessage()));
		}

//		try {
//			Descriptor.from("(ZZ");
//			throw new AssertionError("Descriptor constructor didn't throw a UnsupportedOperationException on invalid input."); // If this happens, something has gone wrong.
//		} catch (UnsupportedOperationException e) {
//			Assert.assertTrue("Unknown Descriptor Byte:  ".equals(e.getMessage()));
//		}
	}
}