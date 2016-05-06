package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class LongParam extends Param {
	public LongParam(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.LLOAD, Opcodes.LRETURN, Opcodes.LSTORE, "J");
	}
}
