package me.jezza.experij.descriptor;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jezza
 */
public class DescriptorTest {
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
		Descriptor desc = Descriptor.from("(Ljava/lang/String;Ljava/lang/String;ZDZFC[BCJLjava/lang/String;ZJBFJD[FIZJS)D");
		Assert.assertTrue(desc.parameterCount() == 22);

		desc = Descriptor.from("([SJZDLjava/lang/String;CCSIDJSSFZCIB[J)C");
		Assert.assertTrue(desc.parameterCount() == 19);

		desc = Descriptor.from("(SDZZBJBBDDILjava/lang/String;Ljava/lang/String;FBZC)F");
		Assert.assertTrue(desc.parameterCount() == 17);

		desc = Descriptor.from("(BFJBBCJCBSLjava/lang/String;CB[Ljava/lang/String;ZCLjava/lang/String;C[FLjava/lang/String;ZI)S");
		Assert.assertTrue(desc.parameterCount() == 22);
	}

	@Test
	public void testEquals() throws Exception {
		Descriptor descriptor = Descriptor.from("(BFJBBCJCBSLjava/lang/String;CB[Ljava/lang/String;ZCLjava/lang/String;C[FLjava/lang/String;ZI)S");
		String input = descriptor.toString();
		Descriptor clone = Descriptor.from(input);
		Assert.assertTrue("Equals not consistent!", descriptor.equals(clone));
	}

	@Test
	public void testHashCode() throws Exception {
		Descriptor descriptor = Descriptor.from("(BFJBBCJCBSLjava/lang/String;CB[Ljava/lang/String;ZCLjava/lang/String;C[FLjava/lang/String;ZI)S");
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
		Descriptor.from("(Ljava/lang/String;Ljava/lang/String;ZDZFC[BCJLjava/lang/String;ZJBFJD[FIZJS)D");
		Descriptor.from("(Z[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[J[[B[[[F[J[D[[[[[[[[F[[[I[Z[J[S[)D");
		Descriptor.from("(Ljava/lang/String;Ljava/lang/String;ZDZFC[BCJL[[[[[[[[[[[java/lang/String;[[[[ZJBFJD[FIZJS)D");
		Descriptor.from("(Ljava/lang/String;[[[[[[[[[Ljava/lang/String;ZDZFC[BCJLjava/lang/String;ZJBFJD[FIZJS)D");
		Descriptor.from("(Ljava/lang/String;Ljava/lang/String;ZDZFC[BCJLjava/lang/String;ZJBFJD[FIZJS)D");
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

		try {
			Descriptor.from("(ZZ");
			throw new AssertionError("Descriptor constructor didn't throw a IllegalArgumentException on invalid input."); // If this happens, something has gone wrong.
		} catch (IllegalArgumentException e) {
			Assert.assertTrue("Invalid descriptor (Not closed): (ZZ".equals(e.getMessage()));
		}

		try {
			Descriptor.from("(V)V");
			throw new AssertionError("Descriptor constructor didn't throw a IllegalStateException on invalid input."); // If this happens, something has gone wrong.
		} catch (IllegalStateException e) {
			Assert.assertTrue("Void was declared as a parameter. This makes no sense.".equals(e.getMessage()));
		}
	}
}