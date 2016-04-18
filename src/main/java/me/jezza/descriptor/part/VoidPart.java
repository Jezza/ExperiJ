package me.jezza.descriptor.part;

import me.jezza.descriptor.Part;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class VoidPart extends Part {

	public VoidPart() {
		super(0, 0, 0, Opcodes.RETURN, 0, "V");
	}

	@Override
	public void load(MethodVisitor mv) {
		throw new IllegalStateException("A void declaration is attempting to be loaded. This makes no sense.");
	}
}
