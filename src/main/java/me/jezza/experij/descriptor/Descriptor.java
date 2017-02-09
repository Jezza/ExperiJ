package me.jezza.experij.descriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class Descriptor {
	private static final Param[] EMPTY = new Param[0];

	private final String signature;
	private final String[] exceptions;
	private final Param[] parameters;
	private final Param returnParam;

	private transient int hash;

	private Descriptor(String desc, final String signature, final String[] exceptions, boolean staticAccess) {
		if (desc == null)
			throw new NullPointerException("Descriptor input string is null");
		this.signature = signature;
		this.exceptions = exceptions;
		final char[] chars = desc.toCharArray();
		final int length = chars.length;
		if (length < 3 || chars[0] != '(')
			throw new IllegalArgumentException("Illegal Descriptor.");
		Param param = null;
		int arrayCount = 0;
		boolean closed = false;
		List<Param> parameters = new ArrayList<>(2);
		for (int expected, pos = 1; pos < length; pos = pos == expected ? pos + 1 : pos) {
			char c = chars[pos];
			expected = pos;
			int index = staticAccess ? parameters.size() : parameters.size() + 1;
			switch (c) {
				case '[':
					arrayCount++;
					while (++pos < length) {
						if (chars[pos] != '[')
							break;
						arrayCount++;
					}
					continue;
				case ')':
					if (closed)
						throw new IllegalArgumentException("Multiple closing characters: " + desc);
					closed = true;
					continue;
				case 'L':
					// Object
					pos++;
					while (pos < length)
						if (chars[pos++] == ';')
							break;
					param = new Param(index, arrayCount, new String(Arrays.copyOfRange(chars, expected, pos)), Opcodes.ALOAD, Opcodes.ASTORE, Opcodes.ARETURN);
					// This value will be reset anyway, but we don't want to trigger the object code that's going to be executed next.
					arrayCount = 0;
					break;
				case 'Z':
					// Boolean
					param = new Param(index, arrayCount, "Z", Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN);
					break;
				case 'B':
					// Byte
					param = new Param(index, arrayCount, "B", Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN);
					break;
				case 'S':
					// Short
					param = new Param(index, arrayCount, "S", Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN);
					break;
				case 'I':
					// Int
					param = new Param(index, arrayCount, "I", Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN);
					break;
				case 'F':
					// Float
					param = new Param(index, arrayCount, "F", Opcodes.FLOAD, Opcodes.FSTORE, Opcodes.FRETURN);
					break;
				case 'D':
					// Double
					param = new Param(index, arrayCount, "D", Opcodes.DLOAD, Opcodes.DSTORE, Opcodes.DRETURN);
					break;
				case 'J':
					// Long
					param = new Param(index, arrayCount, "J", Opcodes.LLOAD, Opcodes.LSTORE, Opcodes.LRETURN);
					break;
				case 'C':
					// Char
					param = new Param(index, arrayCount, "C", Opcodes.ILOAD, Opcodes.ISTORE, Opcodes.IRETURN);
					break;
				case 'V':
					if (arrayCount > 0)
						throw new IllegalStateException("Void was declared with an array. This makes no sense.");
					if (!closed)
						throw new IllegalStateException("Void was declared as a parameter. This makes no sense.");
					param = new Param(index, arrayCount, "V", -1, -1, Opcodes.RETURN);
					break;
				default:
					throw new UnsupportedOperationException("Unknown Descriptor Byte: " + c);
			}
			// Make sure it's not an object already, AND if it should be an array.
			if (c != 'L' && arrayCount > 0)
				param = new Param(index, arrayCount, String.valueOf(c), Opcodes.ALOAD, Opcodes.ASTORE, Opcodes.ARETURN);
			if (!closed)
				parameters.add(param);
			arrayCount = 0;
		}
		if (!closed)
			throw new IllegalArgumentException("Invalid descriptor (Not closed): " + desc);
		if (param == null)
			throw new IllegalArgumentException("Invalid descriptor (Invalid return type): " + desc);
		this.returnParam = param;
		this.parameters = parameters.isEmpty() ? EMPTY : parameters.toArray(EMPTY);
	}

	public Param returnPart() {
		return returnParam;
	}

	public String signature() {
		return signature;
	}

	public String[] exceptions() {
		return exceptions;
	}

	public Param parameter(int index) {
		return parameters[index];
	}

	public int parameterCount() {
		return parameters.length;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Descriptor that = (Descriptor) o;
		if (signature == null ? that.signature != null : !signature.equals(that.signature))
			return false;
		if (!toString().equals(that.toString()))
			return false;
		return Arrays.equals(exceptions, that.exceptions);
	}

	@Override
	public int hashCode() {
		if (this.hash == 0) {
			int hash = 31 + toString().hashCode();
			if (signature != null)
				hash = 31 * hash + signature.hashCode();
			if (exceptions != null && exceptions.length != 0) {
				for (String exception : exceptions)
					hash = 31 * hash + exception.hashCode();
			}
			this.hash = hash;
		}
		return hash;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("(");
		for (Param param : parameters)
			builder.append(param);
		return builder.append(')').append(returnParam).toString();
	}

	public static Descriptor from(String desc) {
		return new Descriptor(desc, null, null, false);
	}

	public static Descriptor from(String desc, boolean staticAccess) {
		return new Descriptor(desc, null, null, staticAccess);
	}

	public static Descriptor from(String desc, final String signature, final String[] exceptions, boolean staticAccess) {
		return new Descriptor(desc, signature, exceptions, staticAccess);
	}
}
