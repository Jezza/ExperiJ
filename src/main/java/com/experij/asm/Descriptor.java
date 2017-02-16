package com.experij.asm;

import java.util.Arrays;

import com.experij.repackage.org.objectweb.asm.Type;

/**
 * @author Jezza
 */
final class Descriptor {

	/**
	 * The method's descriptor.
	 */
	private final String desc;

	/**
	 * The method's signature.
	 * May be <tt>null</tt> if the method parameters,
	 * return type, and exceptions do not use generic types.
	 */
	final String signature;

	/**
	 * The internal names of the method's exception classes
	 * (see{@link Type#getInternalName() getInternalName}).
	 * May be <tt>null</tt>.
	 */
	final String[] exceptions;

	/**
	 * The argument types of methods of this type.
	 */
	final Type[] argumentTypes;

	/**
	 * The return type of the method.
	 */
	final Type returnType;

	/**
	 * The first valid non-taken index in which we can load parameters.
	 */
	final int firstFreeIndex;

	Descriptor(String desc, final String signature, final String[] exceptions, boolean staticAccess) {
		if (desc == null)
			throw new NullPointerException("Descriptor input string is null");
		this.desc = desc;
		this.signature = signature;
		this.exceptions = exceptions;
		Type type = Type.getMethodType(desc);
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

	/**
	 * Checks if a given descriptor is a valid "subtype".
	 * Can we call this descriptor from our descriptor without the need to invent information.
	 *
	 * @param other - The descriptor to check.
	 * @return true, if the types are compatible.
	 */
	boolean matching(Descriptor other) {
		// TODO: 16/02/2017 - Allow specific variants. (Omission of parameters, etc.)
		return equals(other);
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
		if (!desc.equals(that.desc))
			return false;
		return Arrays.equals(exceptions, that.exceptions);
	}

	@Override
	public int hashCode() {
		int hash = 31 + toString().hashCode();
		if (signature != null)
			hash = 31 * hash + signature.hashCode();
		if (exceptions != null && exceptions.length != 0) {
			for (String exception : exceptions)
				hash = 31 * hash + exception.hashCode();
		}
		return hash;
	}

	@Override
	public String toString() {
		return desc;
	}
}
