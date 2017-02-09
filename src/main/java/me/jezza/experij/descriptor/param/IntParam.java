package me.jezza.experij.descriptor.param;

import me.jezza.experij.descriptor.Param;
import me.jezza.experij.descriptor.Param;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class IntParam extends Param {
	public IntParam(int index, int arrayCount) {
		super(index, arrayCount, "I");
	}

	@Override
	public int loadCode() {
		return Opcodes.ILOAD;
	}

	@Override
	public int returnCode() {
		return Opcodes.IRETURN;
	}

	@Override
	public int storeCode() {
		return Opcodes.ISTORE;
	}
}
