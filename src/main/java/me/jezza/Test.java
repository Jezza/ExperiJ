package me.jezza;

import me.jezza.interfaces.Control;
import me.jezza.interfaces.Experiment;
import me.jezza.interfaces.Results;

/**
 * @author Jezza
 */
public class Test {
	public static final Results RESULTS = ExperiJ.results("TestThing");

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

	private static String toBinary(int val) {
		int mag = Integer.SIZE - Integer.numberOfLeadingZeros(val);
		int chars = Math.max(mag, 1);
		char[] buf = new char[chars];
		do {
			buf[--chars] = digits[val & 1];
			val >>>= 1;
		} while (val != 0 && chars > 0);
		// Use special constructor which takes over "buf".
		return new String(buf);
	}

	@Experiment("TestThing")
	private static String countBits(int length) {
		StringBuilder result = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			if ((countOnes(i) & 1) != 0) {
				result.append('B');
			} else {
				result.append('A');
			}
		}
		return result.toString();
	}

	private static int countOnes(int val) {
		int chars = Math.max(Integer.SIZE - Integer.numberOfLeadingZeros(val), 1);
		int count = 0;
		do {
			if (digits[val & 1] == '1')
				count++;
			val >>>= 1;
		} while (val != 0 && --chars > 0);
		return count;
	}

	// String temp = "ABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBA";

	private static String $experiment$_TestThing_(int length) {
		ExperimentContext context = ExperiJ.context("TestExperiment", "integerToBinary");
		long key = context.startControl();
		String control = integerToBinaryString(length);
		context.stopControl(key);

		context.start(key, "fastToBinaryString");
		String experiment = fastToBinaryString(length);
		context.stop(key, "fastToBinaryString");
		context.reportEquality(key, "fastToBinaryString", control.equals(experiment));

		context.start(key, "countBits");
		experiment = countBits(length);
		context.stop(key, "countBits");
		context.reportEquality(key, "fastToBinaryString", control.equals(experiment));

		context.compile(key);

		return control;
	}
}