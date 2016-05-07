package me.jezza.descriptor.param;

import me.jezza.descriptor.Param;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ObjectParam extends Param {

	public ObjectParam(int index, int arrayCount, String data) {
		super(index, arrayCount, data);
	}

	@Override
	protected String buildEqualitySignature(String data) {
		return "(Ljava/lang/Object;Ljava/lang/Object;)Z";
	}

	@Override
	protected String buildStringSignature(String data) {
		return "(Ljava/lang/Object;)Ljava/lang/String;";
	}

	@Override
	public int loadCode() {
		return Opcodes.ALOAD;
	}

	@Override
	public int returnCode() {
		return Opcodes.ARETURN;
	}

	@Override
	public int storeCode() {
		return Opcodes.ASTORE;
	}
}
