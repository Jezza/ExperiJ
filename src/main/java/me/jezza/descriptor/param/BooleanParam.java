package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class BooleanParam extends Param {
	public BooleanParam(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.ILOAD, Opcodes.IRETURN, Opcodes.ISTORE, "Z");
	}
}
