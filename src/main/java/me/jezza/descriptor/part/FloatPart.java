package me.jezza.descriptor.part;

import me.jezza.descriptor.Part;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class FloatPart extends Part {
	public FloatPart(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.FLOAD, Opcodes.FRETURN, Opcodes.FSTORE, "F");
	}
}
