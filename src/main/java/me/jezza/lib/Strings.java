package me.jezza.lib;

/**
 * @author Jezza
 */
public final class Strings {
	private static final String OBJ_REP = "{}";
	private static final String NULL_REP = "null";
	private static final String SAFE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNPPQRSTUVWXYZ$_1234567890";

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

	public static boolean useable(CharSequence charSequence) {
		if (charSequence == null || charSequence.length() == 0)
			return false;
		for (int i = 0; i < charSequence.length(); i++)
			if (charSequence.charAt(i) > ' ')
				return true;
		return false;
	}

	public static String format(String target, Object... _params) {
		if (target == null)
			return null;
		if (_params == null || _params.length == 0 || !useable(target))
			return target;

		int index = target.indexOf(OBJ_REP);
		if (index < 0)
			return target;

		String[] params = new String[_params.length];
		int length = 0;
		for (int i = 0; i < _params.length; i++) {
			Object param = _params[i];
			String str = param == null ? NULL_REP : param.toString();
			length += str.length();
			params[i] = str;
		}

		StringBuilder builder = new StringBuilder(length);
		builder.append(target);
		for (String param : params) {
			builder.replace(index, index + OBJ_REP.length(), param);
			index = builder.indexOf(OBJ_REP, index + 1);
			if (index < 0)
				return builder.toString();
		}
		return builder.toString();
	}
}
