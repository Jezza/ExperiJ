package me.jezza.experij.descriptor.param;

import me.jezza.experij.descriptor.Param;
import me.jezza.experij.descriptor.Param;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class FloatParam extends Param {
	public FloatParam(int index, int arrayCount) {
		super(index, arrayCount, "F");
	}

	@Override
	public int loadCode() {
		return Opcodes.FLOAD;
	}

	@Override
	public int returnCode() {
		return Opcodes.FRETURN;
	}

	@Override
	public int storeCode() {
		return Opcodes.FSTORE;
	}
}
