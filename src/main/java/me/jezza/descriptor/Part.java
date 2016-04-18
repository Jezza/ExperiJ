package me.jezza.descriptor;

import me.jezza.lib.Equality;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * TODO: Revisit. These classes are really annoying, and kinda confusing...
 *
 * @author Jezza
 */
public abstract class Part {
	protected final int index;
	protected final int arrayCount;
	protected final int loadCode;
	protected final int storeCode;
	protected final int returnCode;
	protected final String data;
	protected final String equalitySignature;

	public Part(int index, int arrayCount, int loadCode, int returnCode, int storeCode, String data) {
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
		equalitySignature = buildEqualitySignature(data);
	}

	protected String buildEqualitySignature(String data) {
		return '(' + data + data + ")Z";
	}

	public void equality(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, Equality.INTERNAL_NAME, "equals", equalitySignature, false);
	}

	public void load(MethodVisitor mv) {
		mv.visitVarInsn(loadCode(), index);
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
