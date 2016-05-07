package me.jezza.descriptor;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import me.jezza.descriptor.param.*;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;

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
	private transient String toString;

	private Descriptor(String desc, final String signature, final String[] exceptions, boolean staticAccess) {
		this.signature = signature;
		this.exceptions = exceptions;
		final char[] chars = desc.toCharArray();
		final int length = chars.length;
		if (length < 3 || chars[0] != '(')
			throw new IllegalArgumentException("Illegal Descriptor.");
		Param param;
		int arrayCount = 0;
		List<Param> parameters = new LinkedList<>();
		for (int expected, pos = 1; pos < length; pos = pos == expected ? pos + 1 : pos) {
			char c = chars[pos];
			expected = pos;
			int index = staticAccess ? parameters.size() : parameters.size() + 1;
			switch (c) {
				case '[':
					arrayCount++;
				case ')':
					continue;
				case 'L':
					// Object
					StringBuilder data = new StringBuilder("L");
					pos++;
					while (pos < length) {
						c = chars[pos++];
						if (c == ';')
							break;
						data.append(c);
					}
					data.append(';');
					param = new ObjectParam(index, arrayCount, data.toString());
					break;
				case 'Z':
					// Boolean
					param = new BooleanParam(index, arrayCount);
					break;
				case 'B':
					// Byte
					param = new ByteParam(index, arrayCount);
					break;
				case 'S':
					// Short
					param = new ShortParam(index, arrayCount);
					break;
				case 'I':
					// Int
					param = new IntParam(index, arrayCount);
					break;
				case 'F':
					// Float
					param = new FloatParam(index, arrayCount);
					break;
				case 'D':
					// Double
					param = new DoubleParam(index, arrayCount);
					break;
				case 'J':
					// Long
					param = new LongParam(index, arrayCount);
					break;
				case 'C':
					// Char
					param = new CharParam(index, arrayCount);
					break;
				case 'V':
					param = new VoidParam();
					break;
				default:
					throw new UnsupportedOperationException("Unknown Descriptor Byte: " + c);
			}
			parameters.add(param);
			arrayCount = 0;
		}
		if (parameters.isEmpty())
			throw new IllegalArgumentException("");
		if (parameters.size() == 1) {
			this.parameters = EMPTY;
			this.returnParam = parameters.get(0);
		} else {
			int end = parameters.size() - 1;
			this.parameters = parameters.subList(0, end).toArray(EMPTY);
			this.returnParam = parameters.get(end);
		}
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

	public void loadAll(MethodVisitor mv) {
		if (parameters.length > 0)
			for (Param param : parameters)
				param.load(mv);
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
		if (toString == null) {
			StringBuilder builder = new StringBuilder("(");
			for (Param param : parameters)
				builder.append(param);
			toString = builder.append(')').append(returnParam).toString();
		}
		return toString;
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
