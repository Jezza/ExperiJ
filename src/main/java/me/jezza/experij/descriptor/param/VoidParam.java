package me.jezza.experij.descriptor.param;

import me.jezza.experij.descriptor.Param;
import me.jezza.experij.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class VoidParam extends Param {

	public VoidParam(int index) {
		super(index, 0, "V");
	}

	@Override
	public int loadCode() {
		return 0;
	}

	@Override
	public int returnCode() {
		return Opcodes.RETURN;
	}

	@Override
	public int storeCode() {
		return 0;
	}

	@Override
	public Param invokeEquals(MethodVisitor mv) {
		throw new IllegalStateException("A void declaration is attempting to be equaled. This makes no sense.");
	}

	@Override
	public Param invokeValueOf(MethodVisitor mv) {
		throw new IllegalStateException("A void declaration is attempting to be string'd. This makes no sense.");
	}

	@Override
	public Param load(MethodVisitor mv) {
		throw new IllegalStateException("A void declaration is attempting to be loaded. This makes no sense.");
	}
}
