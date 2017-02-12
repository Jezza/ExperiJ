package me.jezza.experij;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import me.jezza.experij.interfaces.Results;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Jezza
 */
public final class SimpleExperiment extends BaseTest {
	private static final AtomicInteger runs = new AtomicInteger();

	private static final String TEST_NAME = "SimpleExperiment";

	@Test
	public void testSimpleExperiment() throws Exception {
		Results results = ExperiJ.results(TEST_NAME);
		Assertions.assertTrue(results == ExperiJ.results(TEST_NAME), "Cache system has changed! Update Tests!");
		expect(results.size(), 0);
		runs.set(0);
		controlMethod("InputString");
		int value = runs.get();
		expect(value, 11);
		expect(results.size(), 1);
	}

	@Control(TEST_NAME)
	public static void controlMethod(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment1(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment2(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment3(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment4(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment5(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment6(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment7(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment8(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment9(String input) {
		runs.incrementAndGet();
	}

	@Experiment(TEST_NAME)
	private static void experiment10(String input) {
		runs.incrementAndGet();
	}

	private static final String TEST_RESULTS = "Results";

	@Test
	public void testSimpleExperimentResults() throws Exception {
		String input = "InputString";

		results(input);
		// Some basic results checks
		Results results = ExperiJ.results(TEST_RESULTS);
		Assertions.assertTrue(results.size() == 1, "There should only be one execution, but the execution size isn't 1. " + results.size());
		Execution execution = results.get(0);
		Assertions.assertNotNull(execution, "Execution shouldn't be null");
		Assertions.assertTrue(TEST_RESULTS.equals(execution.experimentName()), "Experiment name is incorrect");

		// Parameter checks
		List<String> params = execution.params();
		Assertions.assertTrue(params.size() == 1, "It was only executed with one parameter, but the parameter capture size isn't one.");
		try {
			params.add("asd");
			Assertions.fail("Adding to the parameter capture list should have thrown an exception.");
		} catch (UnsupportedOperationException ignored) {
			// Expected functionality: You can't add anything to the parameter capture.
		}
		Assertions.assertTrue(input.equals(params.get(0)), "The parameter wasn't correctly captured: Expected: \"" + input + "\", Received: \"" + params.get(0) + "\".");
	}

	@Control(TEST_RESULTS)
	private static void results(String input) {
	}

	@Experiment(TEST_RESULTS)
	private static void results0(String input) {
	}
}
