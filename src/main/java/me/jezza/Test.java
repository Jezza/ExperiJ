package me.jezza;

import me.jezza.interfaces.Control;
import me.jezza.interfaces.Experiment;

/**
 * @author Jezza
 */
public class Test {

	@Control("TestThing")
	public static String test() {
		System.out.println("Firing test");
		return ";";
	}

	@Experiment("TestThing")
	private static String test2() {
		System.out.println("Firing test2");
		return ";";
	}

//	@Control("TestExperiment")
//	public String control(String value) {
//		System.out.println("Testing time'C': " + value);
//		return "Value1";
//	}
//
//	@Experiment("TestExperiment")
//	private String experiment(String value) {
//		System.out.println("Testing time'1': " + value);
//		return "Value2";
//	}
//
//	@Experiment("TestExperiment")
//	private String experiment2(String value) {
//		System.out.println("Testing time'2': " + value);
//		return "Value1";
//	}

//	private String $experiment$TestExperiment_(String value) {
//		ExperimentResults results = ExperimentResults.from("TestExperiment");
//		results.startControl("control");
//		String control = control_hidden(value);
//		results.stopControl("control");
//		long key = 0;
//
//		results.start("experiment1");
//		String experiment1 = experiment1_hidden(value);
//		key = results.stop("experiment1");
//		results.reportEquality(key, "experiment1", control.equals(experiment1));
//
//		return control;
//	}
//
//	private String $experiment$TestExperiment__(String value) {
//		ExperimentResults results = ExperimentResults.from("TestExperiment");
//		results.startControl("control");
//		String control = control_hidden(value);
//		results.stopControl("control");
//
//		results.start("experiment1");
//		String experiment1 = experiment1_hidden(value);
//		results.stop("experiment1");
//		results.reportEquality("experiment1", control.equals(experiment1));
//
//		return control;
//	}
}