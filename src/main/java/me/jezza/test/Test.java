package me.jezza.test;

import me.jezza.ExperiJ;
import me.jezza.interfaces.Control;
import me.jezza.interfaces.Experiment;
import me.jezza.interfaces.Results;

/**
 * @author Jezza
 */
public class Test {
	public static final Results HELLO = ExperiJ.results("Hello");
	public static final Results TEST_THING = ExperiJ.results("TestThing");

	private static final char[] digits = {'0', '1'};

	@Control("TestThing")
	public static String integerToBinaryString(int length) {
		StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			String bin = Integer.toBinaryString(i);
			if ((bin.chars().filter(c -> c == '1').count() & 1) != 0) {
				result.append('B');
			} else {
				result.append('A');
			}
		}
		return result.toString();
	}

	@Experiment("TestThing")
	private static String fastToBinaryString(int length) {
		StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			if ((toBinary(i).chars().filter(c -> c == '1').count() & 1) != 0) {
				result.append('B');
			} else {
				result.append('A');
			}
		}
		return result.toString();
	}

	@Experiment("TestThing")
	private static String countBits(int length) {
		StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			if (oddBinary(i)) {
				result.append('B');
			} else {
				result.append('A');
			}
		}
		return result.toString();
	}

	@Control("ArrayTest")
	private static int[] old(String creation) {
		return new int[]{creation.length()};
	}

	@Experiment("ArrayTest")
	private static int[] newSlow(String creation) {
		return new int[]{creation.length()};
	}

	@Experiment("ArrayTest")
	private static int[] newFast(String creation) {
		return new int[]{creation.length()};
	}

	private static String toBinary(int val) {
		int mag = Integer.SIZE - Integer.numberOfLeadingZeros(val);
		int chars = Math.max(mag, 1);
		char[] buf = new char[chars];
		do {
			buf[--chars] = digits[val & 1];
			val >>>= 1;
		} while (val != 0 && chars > 0);
		return new String(buf);
	}

	private static boolean oddBinary(int val) {
		int count = 0;
		do {
			if ((val & 1) == 1)
				count++;
		} while ((val >>>= 1) != 0);
		return (count & 1) == 1;
	}

	// String temp = "ABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBA";

//	private static String $experiment$_TestThing_(int length) {
//		ExperimentContext context = ExperiJ.context("TestExperiment", "integerToBinary", 2);
//		long key = context.startControl();
//		String control = integerToBinaryString(length);
//		context.stopControl(key);
//
//		context.start(key, "fastToBinaryString");
//		String experiment = fastToBinaryString(length);
//		context.stop(key, "fastToBinaryString");
//		context.reportEquality(key, "fastToBinaryString", control.equals(experiment));
//
//		context.start(key, "countBits");
//		experiment = countBits(length);
//		context.stop(key, "countBits");
//		context.reportEquality(key, "fastToBinaryString", control.equals(experiment));
//
//		context.compile(key);
//
//		return control;
//	}
}