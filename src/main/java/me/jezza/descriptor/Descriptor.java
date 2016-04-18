package me.jezza.descriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.jezza.descriptor.part.*;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;

/**
 * @author Jezza
 */
public final class Descriptor {
	private static final Part[] EMPTY = new Part[0];

	private final String signature;
	private final String[] exceptions;
	private final Part[] parameters;
	private final Part returnPart;

	private int hash;
	private String toString;

	private Descriptor(String desc, final String signature, final String[] exceptions) {
		this.signature = signature;
		this.exceptions = exceptions;
		final char[] chars = desc.toCharArray();
		if (chars.length < 3 || chars[0] != '(')
			throw new IllegalArgumentException("Illegal Descriptor.");
		Part part;
		int expected;
		int arrayCount = 0;
		List<Part> parameters = new ArrayList<>(4);
		for (int pos = 1; pos < chars.length; pos = pos == expected ? pos + 1 : pos) {
			char c = chars[pos];
			expected = pos;
			switch (c) {
				case '[':
					arrayCount++;
				case ')':
					continue;
				case 'L':
					// Object
					StringBuilder objectClass = new StringBuilder();
					int tempPos = pos + 1;
					while (tempPos < chars.length) {
						c = chars[tempPos++];
						if (c == ';')
							break;
						objectClass.append(c);
					}
					pos = tempPos;
					part = new ObjectPart(parameters.size() + 1, arrayCount, objectClass.toString());
					break;
				case 'Z':
					// Boolean
					part = new BooleanPart(parameters.size() + 1, arrayCount);
					break;
				case 'B':
					// Byte
					part = new BytePart(parameters.size() + 1, arrayCount);
					break;
				case 'S':
					// Short
					part = new ShortPart(parameters.size() + 1, arrayCount);
					break;
				case 'I':
					// Int
					part = new IntPart(parameters.size() + 1, arrayCount);
					break;
				case 'F':
					// Float
					part = new FloatPart(parameters.size() + 1, arrayCount);
					break;
				case 'D':
					// Double
					part = new DoublePart(parameters.size() + 1, arrayCount);
					break;
				case 'J':
					// Long
					part = new LongPart(parameters.size() + 1, arrayCount);
					break;
				case 'C':
					// Char
					part = new CharPart(parameters.size() + 1, arrayCount);
					break;
				case 'V':
					part = new VoidPart();
					break;
				default:
					throw new UnsupportedOperationException("Unknown Descriptor Byte: " + c);
			}
			parameters.add(part);
			arrayCount = 0;
		}
		if (parameters.isEmpty())
			throw new IllegalArgumentException("");
		if (parameters.size() == 1) {
			this.parameters = EMPTY;
			this.returnPart = parameters.get(0);
		} else {
			this.parameters = parameters.subList(0, parameters.size() - 1).toArray(EMPTY);
			this.returnPart = parameters.get(parameters.size() - 1);
		}
	}

	public Part returnPart() {
		return returnPart;
	}

	public String signature() {
		return signature;
	}

	public String[] exceptions() {
		return exceptions;
	}

	public void loadAll(MethodVisitor mv) {
		if (parameters.length > 0)
			for (Part part : parameters)
				part.load(mv);
	}

	public void equality(MethodVisitor mv) {
		returnPart.equality(mv);
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
		if (toString == null) {
			StringBuilder builder = new StringBuilder("(");
			for (Part part : parameters)
				builder.append(part);
			toString = builder.append(')').append(returnPart).toString();
		}
		return toString;
	}

	public static Descriptor from(String desc, final String signature, final String[] exceptions) {
		return new Descriptor(desc, signature, exceptions);
	}
}
