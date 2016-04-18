package me.jezza;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Method;

import me.jezza.asm.ExperiJClassTransformer;

/**
 * @author Jezza
 */
public final class ExperiJ {
	/**
	 * Used as the entry point for methods.
	 */
	public static final String HIDDEN_EXPERIMENT_ENTRY_POINT_FORMAT = "$experiment$_{}";

	/**
	 * Used to rename the experiment methods, so that we can insert methods with the original names, thus gaining control of the flow.
	 */
	public static final String RENAME_METHOD_FORMAT = "{}_$hidden$";

	private ExperiJ() {
		throw new IllegalStateException();
	}

	public static void premain(String agentArgs, Instrumentation inst) {
		System.out.println("Injecting: " + ExperiJClassTransformer.class.getCanonicalName());
		inst.addTransformer(new ExperiJClassTransformer());
	}

	public static void main(String[] args) {
//		String input = "(Ljava/lang/String;ZBSIFDJC)V";
//		System.out.println(input);
//		Descriptor first = Descriptor.from(input);
//		System.out.println(first);

		System.out.println(Test.test());

		System.out.println("--Internals--");
		for (Method method : Test.class.getDeclaredMethods())
			System.out.println(method.toGenericString());
//		String generated = gen(256);
//		System.out.println(generated);
//		String temp = "ABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBAABBABAABBAABABBABAABABBAABBABAABABBABAABBAABABBABAABABBAABBABAABBAABABBAABBABAABABBABAABBAABABBA";
//		System.out.println(temp);
//		System.out.println(temp.equals(generated));
	}

	private static String gen(int length) {
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
}