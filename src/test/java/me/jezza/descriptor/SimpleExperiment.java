package me.jezza.descriptor;

import me.jezza.ExperiJ;
import me.jezza.interfaces.Control;
import me.jezza.interfaces.Experiment;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Jezza
 */
public class SimpleExperiment {

	@Test
	public void testSimpleExperiment() throws Exception {
		Assert.assertTrue(ExperiJ.results("Simple").isEmpty());
		controlMethod("InputString");
	}

	@Control("Simple")
	public static void controlMethod(String input) {
	}

	@Experiment("Simple")
	private static void experiment1(String input) {
	}

	@Experiment("Simple")
	private static void experiment2(String input) {
	}

	@Experiment("Simple")
	private static void experiment3(String input) {
	}

	@Experiment("Simple")
	private static void experiment4(String input) {
	}

	@Experiment("Simple")
	private static void experiment5(String input) {
	}

	@Experiment("Simple")
	private static void experiment6(String input) {
	}

	@Experiment("Simple")
	private static void experiment7(String input) {
	}

	@Experiment("Simple")
	private static void experiment8(String input) {
	}

	@Experiment("Simple")
	private static void experiment9(String input) {
	}

	@Experiment("Simple")
	private static void experiment10(String input) {
	}
}
