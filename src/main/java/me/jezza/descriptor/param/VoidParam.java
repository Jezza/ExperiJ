package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class VoidParam extends Param {

	public VoidParam() {
		super(0, 0, 0, Opcodes.RETURN, 0, "V");
	}

	@Override
	public Param load(MethodVisitor mv) {
		throw new IllegalStateException("A void declaration is attempting to be loaded. This makes no sense.");
	}
}
