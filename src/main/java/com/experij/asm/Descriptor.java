package com.experij.asm;

import java.util.Arrays;

import com.experij.repackage.org.objectweb.asm.Type;

/**
 * @author Jezza
 */
final class Descriptor {
	final String signature;
	final String[] exceptions;

	final Type methodType;
	final Type[] argumentTypes;
	final Type returnType;

	final int firstFreeIndex;

	private transient int hash;

	Descriptor(String desc, final String signature, final String[] exceptions, boolean staticAccess) {
		if (desc == null)
			throw new NullPointerException("Descriptor input string is null");
		this.signature = signature;
		this.exceptions = exceptions;
		Type type = Type.getMethodType(desc);
		this.methodType = type;
		this.argumentTypes = type.getArgumentTypes();
		this.returnType = type.getReturnType();
		int index = staticAccess ? 0 : 1;
		for (Type argument : argumentTypes) {
			// Confirm that the argument isn't a void type
			if (argument.getSort() == Type.VOID)
				throw new IllegalStateException("Argument type is void. " + desc);
			index += argument.getSize();
		}
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
}
