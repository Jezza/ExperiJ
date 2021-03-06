package com.experij.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author Jezza
 */
public final class Strings {
	private static final char[] OBJ_REP_CHARS = "{}".toCharArray();
	private static final String SAFE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ$_1234567890";
	private static final String[] EMPTY = new String[0];

	private Strings() {
		throw new IllegalStateException();
	}

	public static String confirmSafe(String name) {
		if (!useable(name)) {
			throw new IllegalArgumentException("Experiment name must not be empty: " + name);
		}
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (SAFE_CHARS.indexOf(c) == -1)
				throw new IllegalArgumentException(format("Invalid character in experiment name: \"{}\"[{}] = '{}'.", name, Integer.toString(i), Character.toString(c)));
		}
		return name;
	}

	public static String internalName(Class<?> clazz) {
		return clazz.getName().replace('.', '/');
	}

	public static boolean useable(CharSequence charSequence) {
		if (charSequence == null || charSequence.length() == 0)
			return false;
		for (int i = 0; i < charSequence.length(); i++)
			if (charSequence.charAt(i) > ' ')
				return true;
		return false;
	}

	/**
	 * Replaces occurrences of "{}" in the original string with String-representations of the given objects.<br>
	 * <br>
	 * <i>NOTE: This method is very cheap to call and is highly optimized.!</i>
	 */
	public static String format(final String original, final Object... objects) {
		if (original == null)
			return null;
		int length = original.length();
		if (length == 0)
			return "";
		if (objects == null || objects.length == 0)
			return original;

		int objectLength = objects.length;
		int calculatedLength = length;
		final String[] params = new String[objectLength];
		while (--objectLength >= 0) {
			final Object obj = objects[objectLength];
			if (obj != null) {
				final String param = obj.toString();
				calculatedLength += param.length();
				params[objectLength] = param;
			} else {
				calculatedLength += 4;
				params[objectLength] = "null";
			}
		}
		objectLength = objects.length;

		final char[] rep = OBJ_REP_CHARS;
		final int repLength = rep.length;
		final char[] result = new char[calculatedLength];
		char[] param;
		original.getChars(0, length, result, 0);
		for (int i = 0, index = 0, end, paramLength; i < objectLength; i++) {
			index = indexOf(result, 0, length, rep, 0, repLength, index);
			if (index < 0)
				return new String(result, 0, length);
			end = index + repLength;
			if (end > length)
				end = length;
			param = params[i].toCharArray();
			paramLength = param.length;
			// Shifts the entire result array down to fit the parameter.
			System.arraycopy(result, end, result, index + paramLength, length - end);
			// Copies the parameter into the result array.
			System.arraycopy(param, 0, result, index, paramLength);
			// The new length of the used characters.
			length = length + paramLength - (end - index);
			// Moves the index to AFTER the parameter we just inserted.
			index += paramLength;
		}
		return new String(result, 0, length);
	}

	/**
	 * Performs an indexOf search on a char array, with another char array.
	 * Think of it as lining up the two arrays, and returning the index that it matches.
	 * Or just think of it as an indexOf...
	 *
	 * @param source    - the characters being searched.
	 * @param target    - the characters being searched for.
	 * @param fromIndex - the index to begin searching from.
	 * @return - the index that the target array was found at within the source array
	 */
	public static int indexOf(final char[] source, final char[] target, final int fromIndex) {
		return indexOf(source, 0, source.length, target, 0, target.length, fromIndex);
	}

	/**
	 * Performs an indexOf search on a char array, with another char array.
	 * Think of it as lining up the two arrays, and returning the index that it matches.
	 *
	 * @param source       - the characters being searched.
	 * @param sourceOffset - offset of the source string.
	 * @param sourceCount  - count of the source string.
	 * @param target       - the characters being searched for.
	 * @param targetOffset - offset of the target string.
	 * @param targetCount  - count of the target string.
	 * @param fromIndex    - the index to begin searching from.
	 * @return - the index that the target array was found at within the source array
	 */
	public static int indexOf(final char[] source, final int sourceOffset, final int sourceCount, final char[] target, final int targetOffset, final int targetCount, int fromIndex) {
		if (fromIndex >= sourceCount)
			return targetCount == 0 ? sourceCount : -1;
		if (fromIndex < 0)
			fromIndex = 0;
		if (targetCount == 0)
			return fromIndex;

		final char first = target[targetOffset];
		final int max = sourceOffset + sourceCount - targetCount;

		for (int i = sourceOffset + fromIndex; i <= max; i++) {
			// Look for first character.
			if (source[i] != first)
				while (++i <= max && source[i] != first) ;
			// Found first character, now look at the rest of v2
			if (i <= max) {
				int j = i + 1;
				final int end = j + targetCount - 1;
				for (int k = targetOffset + 1; j < end && source[j] == target[k]; j++, k++) ;

				if (j == end)
					return i - sourceOffset; // Found whole string.
			}
		}
		return -1;
	}

	/**
	 * Formats a chunk of text, replacing defined tokens by the start and end, and passes the value off to the given function.
	 * <p>
	 * eg:
	 * <pre>
	 *  String result = formatKey("This is a {hello}", "{", "}", k -&lt; Integer.toString(k.length()));
	 * </pre>
	 * result = "This is a 5"
	 * <p>
	 * <pre>
	 *  String[] values = {"First", "Second"};
	 *  String result = formatKey("This is a [0][1]", "[", "]", k -&lt; values[Integer.valueOf(k)]);
	 * </pre>
	 * result = "This is a FirstSecond"
	 *
	 * @param input     - The text to scan and alter
	 * @param startKey  - The series of characters that start the token
	 * @param endKey    - The series of characters that end the token
	 * @param transform - The function to apply to the value found between the startKey and the endKey
	 * @return - The formatted result.
	 */
	public static String formatToken(final String input, final String startKey, final String endKey, final Function<String, String> transform) {
		final int startKeyLength = startKey.length();
		final int endKeyLength = endKey.length();

		final StringBuilder output = new StringBuilder(input.length());
		int start;
		int end = -endKeyLength;
		int diff;

		while (true) {
			end += endKeyLength;
			start = input.indexOf(startKey, end);
			if (start < 0)
				break;
			diff = start - end;
			if (diff > 0) {
				if (diff == 1) {
					output.append(input.charAt(end));
				} else {
					output.append(input, end, start);
				}
			}
			if (start + 1 >= input.length())
				throw new IllegalArgumentException("Unmatched token @ position: " + start);
			end = input.indexOf(endKey, start + startKeyLength);
			if (end < 0)
				throw new IllegalArgumentException("Unmatched token @ position: " + start);
			diff = end - start - startKeyLength;
			if (diff > 0) {
				if (diff > 1) {
					output.append(transform.apply(input.substring(start + startKeyLength, end)));
				} else {
					output.append(transform.apply(String.valueOf(input.charAt(start + startKeyLength))));
				}
			} else {
				output.append(transform.apply(""));
			}
		}

		if (-1 < end && end < input.length())
			output.append(input.substring(end));
		return output.toString();
	}

	/**
	 * Differs from {@link String#split(String)} because of a number of reasons.
	 * 1) Won't return empty strings.
	 * 2) Works with a literal representation, and not regex.
	 * (Yes, I know there's a hotpath for {@link String#split(String)} with single non-regex meta characters, but this treats the whole string as a literal delimiter)
	 * 3) *Occasionally* faster than {@link String#split(String)}
	 *
	 * @param target - The string to perform the split on.
	 * @param on     - the delimiter. (The string to split the target on.)
	 * @return - an array of strings computed by splitting the given string around literal matches of the given delimiter.
	 */
	public static String[] split(String target, String on) {
		if (target == null || target.isEmpty())
			return EMPTY;
		int start = target.indexOf(on);
		if (start < 0)
			return new String[]{target};
		List<String> results = new ArrayList<>();
		if (start > 0)
			results.add(trimSubstring(target, 0, start));
		int onLength = on.length();
		int last = start + onLength;
		while ((start = target.indexOf(on, last)) >= 0) {
			int diff = start - last;
			if (diff > 0) {
				if (diff == 1) {
					char c = target.charAt(last);
					if (!Character.isWhitespace(c))
						results.add(String.valueOf(c));
				} else {
					results.add(trimSubstring(target, last, start));
				}
			}
			last = start + onLength;
		}
		if (last < target.length())
			results.add(trimSubstring(target, last, target.length()));
		return results.toArray(EMPTY);
	}

	/**
	 * Performs an inlined trim of the substring.
	 * (Basically just means we determine the "trimmed" position before performing a substring.)
	 *
	 * @param target - The {@link String} to substring.
	 * @param start  - The start of the substring. (Inclusive)
	 * @param end    - The end of the substring. (Exclusive)
	 * @return - The trimmed substring.
	 */
	public static String trimSubstring(final String target, int start, int end) {
		if (start < 0)
			throw new StringIndexOutOfBoundsException(start);
		int diff = end - start;
		if (diff < 0)
			throw new StringIndexOutOfBoundsException(diff);
		if (diff == 1) {
			final char c = target.charAt(start);
			return Character.isWhitespace(c) ? "" : String.valueOf(c);
		}
		final int length = target.length();
		if (end > length)
			throw new StringIndexOutOfBoundsException(end);
		while (start < end && target.charAt(start) <= ' ')
			start++;
		while (start < end && target.charAt(end - 1) <= ' ')
			end--;
		diff = end - start;
		if (diff < 0)
			throw new StringIndexOutOfBoundsException(diff);
		return (start > 0 || end < length) ? target.substring(start, end) : target;
	}
}
