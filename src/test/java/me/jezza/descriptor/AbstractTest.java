package me.jezza.descriptor;

import java.util.Random;

/**
 * @author Jezza
 */
public abstract class AbstractTest {

	public static final String[] PARAMETERS = {
			"Ljava/lang/Object;", "Z", "B", "S", "I", "F", "D", "J", "C"
	};

	public static final String[] RETURN_VALUES = {
			"Ljava/lang/Object;", "Z", "B", "S", "I", "F", "D", "J", "C", "V"
	};

	public static final float DEFAULT_ARRAY_PERCENTAGE = 0.92F;

	public static final int MAX_ARRAY_SIZE = 32;

	public static final Random random = new Random();

	public static Descriptor randomDescriptor() {
		// Random length, at least 16 parameters, and no more than 32.
		return randomDescriptor(16 + random.nextInt(16), DEFAULT_ARRAY_PERCENTAGE, false);
	}

	public static Descriptor randomDescriptor(boolean staticAccess) {
		// Random length, at least 16 parameters, and no more than 32.
		return randomDescriptor(16 + random.nextInt(16), DEFAULT_ARRAY_PERCENTAGE, staticAccess);
	}

	public static Descriptor randomDescriptor(float arrayPercentage) {
		return randomDescriptor(16 + random.nextInt(16), arrayPercentage, false);
	}

	public static Descriptor randomDescriptor(float arrayPercentage, boolean staticAccess) {
		return randomDescriptor(16 + random.nextInt(16), arrayPercentage, staticAccess);
	}

	public static Descriptor randomDescriptor(int length) {
		return randomDescriptor(length, DEFAULT_ARRAY_PERCENTAGE, false);
	}

	public static Descriptor randomDescriptor(int length, boolean staticAccess) {
		return randomDescriptor(length, DEFAULT_ARRAY_PERCENTAGE, staticAccess);
	}

	public static Descriptor randomDescriptor(int length, float arrayPercentage) {
		return randomDescriptor(length, arrayPercentage, false);
	}

	public static Descriptor randomDescriptor(int length, float arrayPercentage, boolean staticAccess) {
		StringBuilder descriptor = new StringBuilder("(");
		int arrayCount = 0;
		if (length - 1 > 0) {
			for (int i = 0; i < length - 1; i++) {
				while (random.nextFloat() > arrayPercentage && arrayCount++ < MAX_ARRAY_SIZE)
					descriptor.append('[');
				descriptor.append(PARAMETERS[random.nextInt(PARAMETERS.length)]);
			}
		}
		descriptor.append(')');
		String returnValue = RETURN_VALUES[random.nextInt(RETURN_VALUES.length)];
		if (!returnValue.equals("V")) {
			arrayCount = 0;
			while (random.nextFloat() > arrayPercentage && arrayCount++ < MAX_ARRAY_SIZE)
				descriptor.append('[');
		}
		descriptor.append(returnValue);
		return Descriptor.from(descriptor.toString(), null, null, staticAccess);
	}
}
