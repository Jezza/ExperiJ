package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ObjectParam extends Param {

	public ObjectParam(int index, int arrayCount, String data) {
		super(index, arrayCount, data);
	}

	@Override
	public Param invokeEquals(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "me/jezza/lib/Equality", "equals", "(Ljava/lang/Object;Ljava/lang/Object;)Z", false);
		return this;
	}

	@Override
	public Param invokeValueOf(MethodVisitor mv) {
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;", false);
		return this;
	}

	@Override
	public int loadCode() {
		return Opcodes.ALOAD;
	}

	@Override
	public int returnCode() {
		return Opcodes.ARETURN;
	}

	@Override
	public int storeCode() {
		return Opcodes.ASTORE;
	}
}
