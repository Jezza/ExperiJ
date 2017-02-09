package me.jezza.experij.descriptor.param;

import me.jezza.experij.descriptor.Param;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ShortParam extends Param {
	public ShortParam(int index, int arrayCount) {
		super(index, arrayCount, "S");
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
