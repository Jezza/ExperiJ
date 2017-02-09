package me.jezza.experij;

import java.lang.reflect.Method;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import me.jezza.experij.interfaces.Results;
import me.jezza.experij.lib.Equality;
import org.junit.Test;

/**
 * @author Jezza
 */
public class BasicTest {
	public static final Results HELLO = ExperiJ.results("Hello");
	public static final Results TEST_THING = ExperiJ.results("TestThing");

	private static final char[] digits = {'0', '1'};

	@Test
	public void run() {
		try {
			for (int i = 0; i < 16; i++)
				System.out.println(integerToBinaryString(1 << i));
		} catch (Throwable t) {
			t.printStackTrace();
		}

		System.out.println("--Internals--");
		for (Method method : BasicTest.class.getDeclaredMethods())
			System.out.println(method.toGenericString());

		System.out.println("--Results--");
		System.out.println(BasicTest.TEST_THING.toString().replaceAll("[|,]", "\n").replaceAll("[ \\[\\]]", ""));
	}

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
		return new String(buf);
	}

	@Experiment("TestThing")
	private static String countBits(int length) {
		char[] c = new char[length];
		for (int i = 0; i < length; i++)
			c[i] = oddBinary(i) ? 'B' : 'A';
		return new String(c);
	}

	private static boolean oddBinary(int val) {
		int count = 0;
		do {
			if ((val & 1) == 1)
				count++;
		} while ((val >>>= 1) != 0);
		return (count & 1) == 1;
	}

	@Experiment("TestThing")
	private static String bitFlip(int length) {
		char[] c = new char[length];
		for (int i = 0; i < length; i++)
			c[i] = fastBit(i) ? 'B' : 'A';
		return new String(c);
	}

	private static boolean fastBit(int val) {
		val ^= val >> 16;
		val ^= val >> 8;
		val ^= val >> 4;
		return ((0x6996 >> (val & 0xf)) & 1) == 1;
	}

	// String temp = "ABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBA";

	private static String _$experiment$_TestThing(int length) {
		ExperimentContext context = ExperiJ.context("TestThing", "integerToBinaryString", 2);
		long key = context.startControl();
		String control = integerToBinaryString(length);
		context.stopControl(key);

		String experiment;
		try {
			context.start(key, "fastToBinaryString");
			experiment = fastToBinaryString(length);
			context.stop(key, "fastToBinaryString");
			context.reportEquality(key, "fastToBinaryString", Equality.equals(control, experiment));
		} catch (Throwable e) {
			context.error(key, e);
		}

		try {
			context.start(key, "countBits");
			experiment = countBits(length);
			context.stop(key, "countBits");
			context.reportEquality(key, "countBits", Equality.equals(control, experiment));
		} catch (Throwable e) {
			context.error(key, e);
 		}

		context.compile(key);
		return control;
	}
}