package me.jezza.experij.argument;

import me.jezza.experij.BaseTest;
import me.jezza.experij.ExperiJ;
import me.jezza.experij.interfaces.Results;

/**
 * @author Jezza
 */
class AbstractArgumentTest extends BaseTest {
	int data;

	protected void runTest(String experimentName, int expected, Runnable test) {
		runTest(experimentName, 0, expected, test);
	}

	protected void runTest(String experimentName, int executionNumber, int expected, Runnable test) {
		data = 0;
		Results results = ExperiJ.results(experimentName);
		expect(results.size(), executionNumber);
		expect(data, 0);
		test.run();
		expect(results.size(), executionNumber + 1);
		expect(data, expected);
	}
}
