package me.jezza.experij.descriptor.param;

import me.jezza.experij.descriptor.Param;
import me.jezza.experij.descriptor.Param;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class LongParam extends Param {
	public LongParam(int index, int arrayCount) {
		super(index, arrayCount, "J");
	}

	@Override
	public int loadCode() {
		return Opcodes.LLOAD;
	}

	@Override
	public int returnCode() {
		return Opcodes.LRETURN;
	}

	@Override
	public int storeCode() {
		return Opcodes.LSTORE;
	}
}
