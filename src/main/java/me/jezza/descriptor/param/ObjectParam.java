package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ObjectParam extends Param {
	private final String className;

	public ObjectParam(int index, int arrayCount, String className) {
		super(index, arrayCount, Opcodes.ALOAD, Opcodes.ARETURN, Opcodes.ASTORE, 'L' + className + ';');
		this.className = className;
	}

	@Override
	protected String equalitySignature(String data) {
		return "(Ljava/lang/Object;Ljava/lang/Object;)Z";
	}

	@Override
	protected String stringSignature(String data) {
		return "(Ljava/lang/Object;)Ljava/lang/String;";
	}
}
