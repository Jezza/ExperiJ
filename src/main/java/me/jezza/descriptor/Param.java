package me.jezza.descriptor;

import static me.jezza.lib.Strings.format;

import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * TODO Move the array logic into here, so we only have one {@link Param} class, instead of different ones for all types.
 *
 * @author Jezza
 */
public abstract class Param {
	private static final String EQUAL_SIGNATURE_FORMAT = "({}{})Z";
	protected static final String EQUAL_SIGNATURE = "(Ljava/lang/Object;Ljava/lang/Object;)Z";

	private static final String STRING_VALUE_OF_SIGNATURE_FORMAT = "({})Ljava/lang/String;";
	protected static final String STRING_VALUE_OF_SIGNATURE = "(Ljava/lang/Object;)Ljava/lang/String;";

	protected final int index;
	protected final int arrayCount;
	protected final String data;

	public Param(int index, int arrayCount, String data) {
		this.index = index;
		this.arrayCount = arrayCount;
		if (arrayCount == 0) {
			this.data = data;
		} else {
			int length = data.length();
			char[] c = new char[arrayCount + length];
			for (int i = 0; i < arrayCount; i++)
				c[i] = '[';
			for (int i = 0; i < length; i++)
				c[arrayCount + i] = data.charAt(i);
			this.data = new String(c);
		}
	}

	public Param invokeEquals(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "me/jezza/lib/Equality", "equals", format(EQUAL_SIGNATURE_FORMAT, data, data), false);
		return this;
	}

	public Param invokeValueOf(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", format(STRING_VALUE_OF_SIGNATURE_FORMAT, data), false);
		return this;
	}

	public Param load(MethodVisitor mv) {
		mv.visitVarInsn(loadCode(), index);
		return this;
	}

	public abstract int loadCode();

	public abstract int storeCode();

	public abstract int returnCode();

	@Override
	public String toString() {
		return data;
	}
}
