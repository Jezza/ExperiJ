package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class CharParam extends Param {
	public CharParam(int index, int arrayCount) {
		super(index, arrayCount, "C");
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
