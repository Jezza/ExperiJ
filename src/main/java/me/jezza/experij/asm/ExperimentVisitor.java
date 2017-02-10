package me.jezza.experij.asm;

import static me.jezza.experij.lib.Strings.format;

import me.jezza.experij.ExperiJ;
import me.jezza.experij.ExperimentContext;
import me.jezza.experij.descriptor.Descriptor;
import me.jezza.experij.descriptor.Param;
import me.jezza.experij.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ExperimentVisitor extends MethodVisitor implements Opcodes {
	private static final String STRING_VALUE_OF_SIGNATURE_FORMAT = "({})Ljava/lang/String;";

	private final ClassExperiment experiment;
	private final boolean staticAccess;

	protected ExperimentVisitor(ClassExperiment experiment, MethodVisitor mv, boolean staticAccess) {
		super(ASM5, mv);
		this.experiment = experiment;
		this.staticAccess = staticAccess;
	}

	/**
	 * If the experiment isn't statically aware, calling this will load the reference object, commonly referred to as 'this' within code.
	 * If the experiment IS statically aware, calling this will do nothing.
	 *
	 * @return - Chaining
	 */
	public ExperimentVisitor loadThis() {
		if (!staticAccess)
			visitVarInsn(ALOAD, 0);
		return this;
	}

	/**
	 * Loads all of the method's parameters.
	 *
	 * @return - Chaining.
	 */
	public ExperimentVisitor loadAllParameters() {
		experiment.loadAllParameters(this);
		return this;
	}

	/**
	 * Loads the experiment's object back onto the stack.
	 *
	 * @param memoryIndex - The memory index that will be used for loading. NOTE: NOT THE OFFSET.
	 * @return - Chaining
	 */
	public ExperimentVisitor load(int memoryIndex) {
		visitVarInsn(experiment.loadCode(), memoryIndex);
		return this;
	}

	/**
	 * Shorthand to store the experiment's object.
	 * Using the experiment's object's specific bytecode to do the storing.
	 *
	 * @param offset - The memory offset from {@link ClassExperiment#firstIndex()}
	 * @return - The final memory index that was used. Use this to load the object back onto the stack.
	 */
	public int store(int offset) {
		return opcode(experiment.storeCode(), offset);
	}

	/**
	 * Executes the given opcode using the offset passed in for the parameter.
	 *
	 * @param opcode - The opcode to execute.
	 * @param offset - The offset from {@link ClassExperiment#firstIndex()}
	 * @return - The final memory index. Use this to load the object back onto the stack.
	 */
	public int opcode(int opcode, int offset) {
		int memoryIndex = experiment.firstIndex() + offset;
		visitVarInsn(opcode, memoryIndex);
		return memoryIndex;
	}

	public void loadInt(int value) {
		switch (value) {
			case 1:
				visitInsn(ICONST_1);
				break;
			case 2:
				visitInsn(ICONST_2);
				break;
			case 3:
				visitInsn(ICONST_3);
				break;
			case 4:
				visitInsn(ICONST_4);
				break;
			case 5:
				visitInsn(ICONST_5);
				break;
			default:
				if (value <= Byte.MAX_VALUE) {
					visitIntInsn(BIPUSH, value);
				} else if (value <= Short.MAX_VALUE) {
					visitIntInsn(SIPUSH, value);
				} else {
					visitLdcInsn(value);
				}
		}
	}

	public boolean voidMethod() {
		return experiment.returnCode() == RETURN;
	}

	/**
	 * @param offset - Offset from first initial free memory index.
	 * @return - The index that the results object is stored within, NOTE: NOT the offset.
	 */
	public int createResults(int offset) {
		int resultIndex = experiment.firstIndex() + offset;
		visitLdcInsn(experiment.name());
		visitLdcInsn(experiment.controlMethod());
		loadInt(experiment.size());
		visitMethodInsn(INVOKESTATIC, ExperiJ.INTERNAL_NAME, "context", "(Ljava/lang/String;Ljava/lang/String;I)L" + ExperimentContext.INTERNAL_NAME + ";", false);
		visitVarInsn(ASTORE, resultIndex);
		return resultIndex;
	}

	/**
	 * Fires the startControl() method within {@link ExperimentContext} to signal the start of the control.
	 *
	 * @param resultIndex - The memory index of the results.
	 * @return - Chaining.
	 */
	public ExperimentVisitor startControl(int resultIndex) {
		// Load the results variable
		visitVarInsn(ALOAD, resultIndex);
		Descriptor desc = experiment.desc();
		int paramCount = desc.parameterCount();
		// Load the array size
		loadInt(paramCount);
		// Create array of a string type
		visitTypeInsn(ANEWARRAY, "java/lang/String");

		for (int i = 0; i < paramCount; i++) {
			// Duplicate the array so we can set values within it. (As the AASTORE pops the array, the index, and the value.)
			visitInsn(DUP);
			// Load the array index that will be used
			loadInt(i);
			// Grab the parameter to load
			Param param = desc.parameter(i);
			// Grab the load code and confirm it's not an invalid load code. (The void parameter has an invalid load code)
			int loadCode = param.loadCode;
			if (loadCode < 0)
				throw new IllegalStateException("Illegal load code call");
			// Load the parameter from the stack at the given index.
			mv.visitVarInsn(loadCode, param.index);
			// Call String.valueOf. (This next call should leave a string on the stack, so we can store it straight into the array)
			invokeStringConversion(this, param);
			// And actually execute the store command, which pops all of the necessary arguments off of the stack.
			visitInsn(AASTORE);
		}
		// Invoke
		visitMethodInsn(INVOKEVIRTUAL, ExperimentContext.INTERNAL_NAME, "startControl", "([Ljava/lang/String;)J", false);
		return this;
	}

	private void invokeStringConversion(MethodVisitor mv, Param param) {
		// TODO This is just so I can easily get a string. This doesn't take into account arrays, etc.
		String target = param.data.charAt(0) == 'L' ? "Ljava/lang/Object;" : param.data;
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", format(STRING_VALUE_OF_SIGNATURE_FORMAT, target), false);
	}

	/**
	 * Fires the stopControl() method within {@link ExperimentContext} to signal the end of the control.
	 *
	 * @param resultIndex - The memory index of the results.
	 * @param keyIndex    - The memory index of the key used to identify the specific experiment run.
	 * @return - Chaining.
	 */
	public ExperimentVisitor stopControl(int resultIndex, int keyIndex) {
		// Load the results variable
		visitVarInsn(ALOAD, resultIndex);
		// Load the unique key index
		visitVarInsn(LLOAD, keyIndex);
		// Invoke
		visitMethodInsn(INVOKEVIRTUAL, ExperimentContext.INTERNAL_NAME, "stopControl", "(J)V", false);
		return this;
	}

	/**
	 * Fires a generic start() method within {@link ExperimentContext} to signal the start of an experiment.
	 *
	 * @param index       - The experiment's index
	 * @param resultIndex - The memory index of the {@link ExperimentContext} object
	 * @param keyIndex    - The memory index of the key used to identify the specific experiment run.
	 * @return - Chaining.
	 */
	public ExperimentVisitor startExperiment(int index, int resultIndex, int keyIndex) {
		return invokeResults(index, resultIndex, keyIndex, true);
	}

	/**
	 * Fires a generic stop() method within {@link ExperimentContext} to signal the stop of an experiment.
	 * NOTE: stop(Ljava/lang/String;)J returns a long that should be used to declare the equality of the experiments.
	 *
	 * @param index       - The experiment's index
	 * @param resultIndex - The memory index of the {@link ExperimentContext} object
	 * @param keyIndex    - The memory index of the key used to identify the specific experiment run.
	 * @return - Chaining.
	 */
	public ExperimentVisitor stopExperiment(int index, int resultIndex, int keyIndex) {
		return invokeResults(index, resultIndex, keyIndex, false);
	}

	/**
	 * Just a delegate method that holds the common code from starting and stopping an experiment's measurement.
	 *
	 * @param index       - The experiment's index
	 * @param resultIndex - The memory index of the {@link ExperimentContext} object
	 * @param keyIndex    - The memory index of the key used to identify the specific experiment run.
	 * @param start       - if the call should start or stop the experiment's measurement.
	 * @return - Chaining.
	 */
	private ExperimentVisitor invokeResults(int index, int resultIndex, int keyIndex, boolean start) {
		// Load the results variable
		visitVarInsn(ALOAD, resultIndex);
		// Load the unique key index
		visitVarInsn(LLOAD, keyIndex);
		// Load the method name
		visitLdcInsn(experiment.names(index));
		// Invoke
		visitMethodInsn(INVOKEVIRTUAL, ExperimentContext.INTERNAL_NAME, start ? "start" : "stop", "(JLjava/lang/String;)V", false);
		return this;
	}

	public ExperimentVisitor invokeControl() {
		experiment.invokeControl(this);
		return this;
	}

	public ExperimentVisitor invokeExperiment(int index) {
		experiment.invokeExperiment(this, index);
		return this;
	}

	public ExperimentVisitor reportEquality(int resultIndex, int keyIndex, int experimentIndex, int controlMemoryIndex, int experimentMemoryIndex) {
		// Load the results variable
		visitVarInsn(ALOAD, resultIndex);
		// Load the equality key
		visitVarInsn(LLOAD, keyIndex);
		// Load the method name
		visitLdcInsn(experiment.names(experimentIndex));
		// We need to check if it's just a void method, because then we can't compare the results.
		if (voidMethod()) {
			// Load true, because it's a void method.
			visitInsn(ICONST_1);
		} else {
			// Load the initial control result, and this experiment's result.
			int loadCode = experiment.loadCode();
			// Load the control value onto the stack
			visitVarInsn(loadCode, controlMemoryIndex);
			// Load the experiment result onto the stack
			visitVarInsn(loadCode, experimentMemoryIndex);
			// Dynamic equality checks.
			experiment.invokeEquals(mv);
		}
		// Fire the reporting method
		mv.visitMethodInsn(INVOKEVIRTUAL, ExperimentContext.INTERNAL_NAME, "reportEquality", "(JLjava/lang/String;Z)V", false);
		return this;
	}

	public ExperimentVisitor compile(int resultIndex, int keyIndex) {
		// Load the results variable
		visitVarInsn(ALOAD, resultIndex);
		// Load the execution key
		visitVarInsn(LLOAD, keyIndex);
		// Fire the compile method
		mv.visitMethodInsn(INVOKEVIRTUAL, ExperimentContext.INTERNAL_NAME, "compile", "(J)V", false);
		return this;
	}

	public ExperimentVisitor error(int resultIndex, int keyIndex, int errorIndex) {
		// Load the results variable
		visitVarInsn(ALOAD, resultIndex);
		// Load the execution key
		visitVarInsn(LLOAD, keyIndex);
		// Load the throwable
		visitVarInsn(ALOAD, errorIndex);
		mv.visitMethodInsn(INVOKEVIRTUAL, ExperimentContext.INTERNAL_NAME, "error", "(JLjava/lang/Throwable;)V", false);
		return this;
	}
}