package me.jezza.interfaces;

import java.util.Map;

/**
 * @author Jezza
 */
public interface Results {
	long executionCount();

	Map<String, Object> times();
}
