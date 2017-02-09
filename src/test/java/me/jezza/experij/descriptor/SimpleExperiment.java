package me.jezza.experij.descriptor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.jezza.experij.Execution;
import me.jezza.experij.ExperiJ;
import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import me.jezza.experij.interfaces.Results;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * @author Jezza
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SimpleExperiment {
	private static final AtomicInteger runs = new AtomicInteger();

	@Test
	public void testSimpleExperiment() throws Exception {
		Results simple = ExperiJ.results("Simple");
		Assert.assertTrue("Cache system has changed! Update Tests!", simple == ExperiJ.results("Simple"));
		Assert.assertTrue("The results weren't empty", simple.isEmpty());
		runs.set(0);
		controlMethod("InputString");
		int value = runs.get();
		Assert.assertTrue("Some of the experiments failed to run correctly: " + value, value == 11);
	}

	@Control("Simple")
	public static void controlMethod(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment1(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment2(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment3(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment4(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment5(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment6(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment7(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment8(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment9(String input) {
		runs.incrementAndGet();
	}

	@Experiment("Simple")
	private static void experiment10(String input) {
		runs.incrementAndGet();
	}

	@Test
	public void testSimpleExperimentResults() throws Exception {
		// Some basic results checks
		Results results = ExperiJ.results("Simple");
		Assert.assertTrue("There should only be one execution, but the execution size isn't 1. " + results.size(), results.size() == 1);
		Execution execution = results.get(0);
		Assert.assertNotNull("Execution shouldn't be null", execution);
		Assert.assertTrue("Experiment name is incorrect", "Simple".equals(execution.experimentName()));

		// Parameter checks
		List<String> params = execution.params();
		Assert.assertTrue("It was only executed with one parameter, but the parameter capture size isn't one.", params.size() == 1);
		try {
			params.add("asd");
			Assert.fail("Adding to the parameter capture list should have thrown an exception.");
		} catch (UnsupportedOperationException ignored) {
			// Expected functionality: You can't add anything to the parameter capture.
		}
		Assert.assertTrue("The parameter wasn't correctly captured: Expected: \"InputString\", Received: \"" + params.get(0) + "\".", "InputString".equals(params.get(0)));

		System.out.println(execution.control());
		Assert.assertTrue("Number of experiments wasn't 10", execution.experiments().size() == 10);
		System.out.println(execution.experiments());
	}
}
