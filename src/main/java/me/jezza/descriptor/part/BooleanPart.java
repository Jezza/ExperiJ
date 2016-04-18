package me.jezza.descriptor.part;

import me.jezza.descriptor.Part;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class BooleanPart extends Part {
	public BooleanPart(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.ILOAD, Opcodes.IRETURN, Opcodes.ISTORE, "Z");
	}
}
