package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class FloatParam extends Param {
	public FloatParam(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.FLOAD, Opcodes.FRETURN, Opcodes.FSTORE, "F");
	}
}
