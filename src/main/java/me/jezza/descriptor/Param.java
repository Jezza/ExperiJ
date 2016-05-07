package me.jezza.descriptor;

import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public abstract class Param {
	protected final int index;
	protected final String data;
	protected final String equalitySignature;
	protected final String stringSignature;

	public Param(int index, int arrayCount, String data) {
		this.index = index;
		if (arrayCount == 0) {
			this.data = data;
		} else {
			StringBuilder builder = new StringBuilder(arrayCount + data.length());
			for (int i = 0; i < arrayCount; i++)
				builder.append('[');
			builder.append(data);
			this.data = builder.toString();
		}
		equalitySignature = buildEqualitySignature(data);
		stringSignature = buildStringSignature(data);
	}

	protected String buildEqualitySignature(String data) {
		return '(' + data + data + ")Z";
	}

	protected String buildStringSignature(String data) {
		return '(' + data + ")Ljava/lang/String;";
	}

	public Param invokeEquals(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "me/jezza/lib/Equality", "equals", equalitySignature, false);
		return this;
	}

	public Param invokeToString(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", stringSignature, false);
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
