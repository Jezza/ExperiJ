package me.jezza.descriptor.part;

import me.jezza.descriptor.Part;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class DoublePart extends Part {
	public DoublePart(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.DLOAD, Opcodes.DRETURN, Opcodes.DSTORE, "D");
	}
}
