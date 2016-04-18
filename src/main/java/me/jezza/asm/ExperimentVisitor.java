package me.jezza.asm;

import me.jezza.ExperimentResults;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ExperimentVisitor extends MethodVisitor implements Opcodes {
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
	public ExperimentVisitor loadParameters() {
		experiment.loadParameters(this);
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

	/**
	 * @param offset - Offset from first initial free memory index.
	 * @return - The index that the results object is stored within, NOTE: NOT the offset.
	 */
	public int createResults(int offset) {
		int resultIndex = experiment.firstIndex() + offset;
		visitLdcInsn(experiment.name());
		visitLdcInsn(experiment.originalControlMethod());
		visitMethodInsn(INVOKESTATIC, ExperimentResults.INTERNAL_NAME, "create", ExperimentResults.CREATE_DESCRIPTOR, false);
		visitVarInsn(ASTORE, resultIndex);
		return resultIndex;
	}

	/**
	 * Fires the startControl() method within {@link ExperimentResults} to signal the start of the control.
	 *
	 * @param resultIndex - The memory index of the results.
	 * @return - Chaining.
	 */
	public ExperimentVisitor startControl(int resultIndex) {
		// Load the results variable
		visitVarInsn(ALOAD, resultIndex);
		// Invoke
		visitMethodInsn(INVOKEVIRTUAL, ExperimentResults.INTERNAL_NAME, "startControl", "()J", false);
		return this;
	}

	/**
	 * Fires the stopControl() method within {@link ExperimentResults} to signal the end of the control.
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
		visitMethodInsn(INVOKEVIRTUAL, ExperimentResults.INTERNAL_NAME, "stopControl", "(J)V", false);
		return this;
	}

	/**
	 * Fires a generic start() method within {@link ExperimentResults} to signal the start of an experiment.
	 *
	 * @param index       - The experiment's index
	 * @param resultIndex - The memory index of the {@link ExperimentResults} object
	 * @param keyIndex    - The memory index of the key used to identify the specific experiment run.
	 * @return - Chaining.
	 */
	public ExperimentVisitor startExperiment(int index, int resultIndex, int keyIndex) {
		return invokeResults(index, resultIndex, keyIndex, true);
	}

	/**
	 * Fires a generic stop() method within {@link ExperimentResults} to signal the stop of an experiment.
	 * NOTE: stop(Ljava/lang/String;)J returns a long that should be used to declare the equality of the experiments.
	 *
	 * @param index       - The experiment's index
	 * @param resultIndex - The memory index of the {@link ExperimentResults} object
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
	 * @param resultIndex - The memory index of the {@link ExperimentResults} object
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
		visitLdcInsn(experiment.originalNames(index));
		// Invoke
		visitMethodInsn(INVOKEVIRTUAL, ExperimentResults.INTERNAL_NAME, start ? "start" : "stop", "(JLjava/lang/String;)V", false);
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
		visitLdcInsn(experiment.originalNames(experimentIndex));
		// Load the initial control result, and this experiment's result.
		int loadCode = experiment.loadCode();
		visitVarInsn(loadCode, controlMemoryIndex);
		visitVarInsn(loadCode, experimentMemoryIndex);
		// Dynamic equality checks.
		experiment.equality(mv);
		// Fire the reporting method
		mv.visitMethodInsn(INVOKEVIRTUAL, ExperimentResults.INTERNAL_NAME, "reportEquality", "(JLjava/lang/String;Z)V", false);
		return this;
	}
}
