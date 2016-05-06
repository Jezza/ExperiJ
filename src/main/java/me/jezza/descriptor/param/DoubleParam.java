package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class DoubleParam extends Param {
	public DoubleParam(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.DLOAD, Opcodes.DRETURN, Opcodes.DSTORE, "D");
	}
}
