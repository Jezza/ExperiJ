package me.jezza.lib;

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * A simple helper class that is used to determine if a class should be loaded based off of System properties.
 *
 * @author Jezza
 */
public class Skip {

	/**
	 * The string that the property will be split on.
	 */
	public static final String SPLITTER = ",";

	/**
	 * If a list part starts with this, it will check if the className {@link String#startsWith(String)} it.
	 * Similar to {@link #ENDS_WITH}
	 */
	public static final String STARTS_WITH = "sw|";

	/**
	 * If a list part starts with this, it will check if the className {@link String#endsWith(String)} it.
	 * Similar to {@link #STARTS_WITH}
	 */
	public static final String ENDS_WITH = "ew|";

	/**
	 * {@link #BLACKLIST} and {@link #WHITELIST} can be used in unison.
	 * <p>
	 * Used to declare a number of predicates for classes that ExperiJ shouldn't touch.
	 * For example, if you were to try to run this with another library that isn't available at runtime, and you have classes that extend that, ExperiJ will fail, as that's just a limitation of the technology.
	 * <p>
	 * You might want to tell ExperiJ to not touch it, and simply just throw a package or the class name in question into this key, it's split over the String that is defined within {@link #SPLITTER}, so you can build lists, along with various predicates on those.
	 * Such as {@link #STARTS_WITH} or {@link #ENDS_WITH}, as if you start a string with that, it'll use the corresponding method in {@link String}.
	 * <p>
	 * Be sure to wrap the entire argument in quotes if you have multiple blacklist values:
	 * <pre>
	 *     -Dexperij.blacklist = "name, sw|org"
	 * </pre>
	 * That argument above will be split into:
	 * <pre>
	 *     className.contains("name")
	 * </pre>
	 * and
	 * <pre>
	 *     className.startsWith("org")
	 * </pre>
	 * <p>
	 * Maybe you don't want ExperiJ to touch the first anonymous class:
	 * <pre>
	 *     -Dexperij.blacklist = "ew|$1"
	 * </pre>
	 * Or maybe you just don't want ExperiJ to touch ANY anonymous class:
	 * <pre>
	 *     -Dexperij.blacklist = "$"
	 * </pre>
	 */
	public static final String BLACKLIST = "experij.blacklist";

	/**
	 * In the same vein as {@link #BLACKLIST}, but instead of declaring what ExperiJ can't touch, you're declaring what it can.
	 * {@link #BLACKLIST} and {@link #WHITELIST} can be used in unison.
	 * <p>
	 * If you want ExperiJ to ONLY touch certain classes, this is for you. The property is split over the String that is defined within {@link #SPLITTER}, so you can build lists, along with various predicates on those.
	 * Such as {@link #STARTS_WITH} or {@link #ENDS_WITH}, as if you start a string with that, it'll use the corresponding method in {@link String}.
	 * <p>
	 * Be sure to wrap the entire argument in quotes if you have multiple values:
	 * <pre>
	 *     -Dexperij.whitelist = "name, sw|org"
	 * </pre>
	 * <p>
	 * Maybe you only want ExperiJ to touch anonymous classes:
	 * <pre>
	 *     -Dexperij.whitelist = "$"
	 * </pre>
	 */
	public static final String WHITELIST = "experij.whitelist";

	/**
	 * The internal combination of all configured filters.
	 * Calling {@link #test(String)} will call {@link Predicate#test(Object)} on this, unless it's null, in which case, the {@link #test(String)} will always pass.
	 */
	private static final Predicate<String> filter;

	static {
		Predicate<String> blacklist = filters(BLACKLIST, Predicate::or);
		Predicate<String> whitelist = filters(WHITELIST, Predicate::and);
		if (blacklist != null && whitelist != null)
			filter = blacklist.and(whitelist.negate());
		else if (blacklist != null)
			filter = blacklist;
		else if (whitelist != null)
			filter = whitelist.negate();
		else
			filter = null;
	}

	/**
	 * Private constructor, as this is a helper class.
	 */
	private Skip() {
		throw new IllegalStateException();
	}

	/**
	 * This is used to determine if the class should be skipped, or if ExperiJ should start transforming it.
	 * If no filter was configured, then by default, everything will pass this test.
	 *
	 * @param className - The class to check.
	 * @return - true if filter is either null, or the filter passed that has been configured from all the given system properties.
	 */
	public static boolean test(String className) {
		return filter == null || filter.test(className);
	}

	/**
	 * Used as a way to load the filters before all of the classes, might as well do it early.
	 * That does mean we need to be REALLY fast initialising stuff.
	 *
	 * @return - The canonical name of the class. (This is just used for printing.)
	 */
	public static String load() {
		return Skip.class.getCanonicalName();
	}

	/**
	 * Builds the internal filter for a given System Property key.
	 *
	 * @param key   - The system property key from which to retrieve the values to split, and construct the filter from.
	 * @param merge - The function that accepts two predicates to return a merged predicate that provides the behaviour for when multiple filters are found.
	 * @return - A predicate that encases all of this.
	 */
	private static Predicate<String> filters(String key, BiFunction<Predicate<String>, Predicate<String>, Predicate<String>> merge) {
		Predicate<String> filter = null;
		String filters = System.getProperty(key);
		if (filters != null && filters.length() > 0) {
			String[] split = Strings.split(filters, SPLITTER);
			if (split == null || split.length == 0)
				return null;
			for (String item : split) {
				final Predicate<String> _filter;
				if (item.startsWith(STARTS_WITH)) {
					String with = item.substring(STARTS_WITH.length());
					_filter = input -> input.startsWith(with);
				} else if (item.startsWith(ENDS_WITH)) {
					String with = item.substring(ENDS_WITH.length());
					_filter = input -> input.endsWith(with);
				} else {
					_filter = input -> input.contains(item);
				}
				filter = filter != null ? merge.apply(filter, _filter) : _filter;
			}
		}
		return filter;
	}
}
