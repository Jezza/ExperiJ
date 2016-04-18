package me.jezza.descriptor.part;

import me.jezza.descriptor.Part;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ObjectPart extends Part {
	private final String className;

	public ObjectPart(int index, int arrayCount, String className) {
		super(index, arrayCount, Opcodes.ALOAD, Opcodes.ARETURN, Opcodes.ASTORE, 'L' + className + ';');
		this.className = className;
	}

	@Override
	protected String buildEqualitySignature(String data) {
		return "(Ljava/lang/Object;Ljava/lang/Object;)Z";
	}
}
