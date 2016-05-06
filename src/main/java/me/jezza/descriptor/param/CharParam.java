package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class CharParam extends Param {
	public CharParam(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.ILOAD, Opcodes.IRETURN, Opcodes.ISTORE, "C");
	}
}
