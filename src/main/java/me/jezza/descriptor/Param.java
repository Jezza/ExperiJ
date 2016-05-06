package me.jezza.descriptor;

import me.jezza.lib.Equality;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * TODO: Revisit. These classes are really annoying, and kinda confusing...
 *
 * @author Jezza
 */
public abstract class Param {
	protected final int index;
	protected final int arrayCount;
	protected final int loadCode;
	protected final int storeCode;
	protected final int returnCode;
	protected final String data;
	protected final String equalitySignature;
	protected final String stringSignature;

	public Param(int index, int arrayCount, int loadCode, int returnCode, int storeCode, String data) {
		this.index = index;
		this.arrayCount = arrayCount;
		this.loadCode = loadCode;
		this.storeCode = storeCode;
		this.returnCode = returnCode;
		if (arrayCount == 0) {
			this.data = data;
		} else {
			StringBuilder builder = new StringBuilder(arrayCount + data.length());
			for (int i = 0; i < arrayCount; i++)
				builder.append('[');
			builder.append(data);
			this.data = builder.toString();
		}
		equalitySignature = equalitySignature(data);
		stringSignature = stringSignature(data);
	}

	protected String equalitySignature(String data) {
		return '(' + data + data + ")Z";
	}

	protected String stringSignature(String data) {
		return '(' + data + ")Ljava/lang/String;";
	}

	public Param equality(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, Equality.INTERNAL_NAME, "equals", equalitySignature, false);
		return this;
	}

	public Param string(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", stringSignature, false);
		return this;
	}

	public Param load(MethodVisitor mv) {
		mv.visitVarInsn(loadCode(), index);
		return this;
	}

	public int loadCode() {
		return loadCode;
	}

	public int storeCode() {
		return storeCode;
	}

	public int returnCode() {
		return returnCode;
	}

	@Override
	public String toString() {
		return data;
	}
}
