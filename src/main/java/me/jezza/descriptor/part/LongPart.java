package me.jezza.descriptor.part;

import me.jezza.descriptor.Part;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class LongPart extends Part {
	public LongPart(int index, int arrayCount) {
		super(index, arrayCount, Opcodes.LLOAD, Opcodes.LRETURN, Opcodes.LSTORE, "J");
	}
}
