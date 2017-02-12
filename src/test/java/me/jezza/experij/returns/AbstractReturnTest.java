package me.jezza.experij.returns;

import java.util.Map;

import me.jezza.experij.BaseTest;
import me.jezza.experij.ExperiJ;
import me.jezza.experij.ExperimentTime;
import me.jezza.experij.interfaces.Results;
import org.junit.jupiter.api.Assertions;

/**
 * @author Jezza
 */
class AbstractReturnTest extends BaseTest {
	protected void runTest(String experimentName, String correctMethod, String incorrectMethod, Runnable test) {
		Results results = ExperiJ.results(experimentName);
		expect(results.size(), 0);
		test.run();
		expect(results.size(), 1);
		Map<String, ExperimentTime> experiments = results.get(0).experiments();
		Assertions.assertTrue(experiments.get(correctMethod).equal(), "Failed to determine that the results were the same");
		Assertions.assertFalse(experiments.get(incorrectMethod).equal(), "Failed to determine that the results were different");
	}
}
