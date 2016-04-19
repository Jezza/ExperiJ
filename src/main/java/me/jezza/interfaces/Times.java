package me.jezza.interfaces;

/**
 * @author Jezza
 */
public interface Times {
	double average();

	double median();

	boolean hasMode();

	double mode();

	double range();

	double max();

	double max(double upperBound);

	double min();

	double min(double lowerBound);
}
