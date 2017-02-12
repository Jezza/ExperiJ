package me.jezza.experij.asm;

import java.util.Arrays;

import me.jezza.experij.repackage.org.objectweb.asm.Type;

/**
 * @author Jezza
 */
final class Descriptor {
	public final String signature;
	public final String[] exceptions;
	public final boolean staticAccess;

	public final Type methodType;
	public final Type[] argumentTypes;
	public final Type returnType;

	public final int firstFreeIndex;

	private transient int hash;

	private Descriptor(String desc, final String signature, final String[] exceptions, boolean staticAccess) {
		if (desc == null)
			throw new NullPointerException("Descriptor input string is null");
		this.signature = signature;
		this.exceptions = exceptions;
		this.staticAccess = staticAccess;
		Type type = Type.getMethodType(desc);
		this.methodType = type;
		this.argumentTypes = type.getArgumentTypes();
		this.returnType = type.getReturnType();
		int index = staticAccess ? 0 : 1;
		for (Type argumentType : argumentTypes)
			index += argumentType.getSize();
		firstFreeIndex = index;
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
		return methodType.toString();
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
