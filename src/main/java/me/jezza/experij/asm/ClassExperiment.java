package me.jezza.experij.asm;

import static me.jezza.experij.lib.Strings.format;

import java.util.ArrayList;
import java.util.List;

import me.jezza.experij.ExperiJ;
import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import me.jezza.experij.repackage.org.objectweb.asm.ClassVisitor;
import me.jezza.experij.repackage.org.objectweb.asm.Label;
import me.jezza.experij.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;
import me.jezza.experij.repackage.org.objectweb.asm.Type;

/**
 * @author Jezza
 */
public final class ClassExperiment implements Opcodes {
	/**
	 * The default method access flags to be used when generating methods.
	 */
	public static final int DEFAULT_METHOD_ACCESS = ACC_PRIVATE | ACC_SYNTHETIC | ACC_FINAL;

	/**
	 * The default method access flags to be used when generating static methods.
	 */
	public static final int DEFAULT_STATIC_METHOD_ACCESS = DEFAULT_METHOD_ACCESS | ACC_STATIC;

	/**
	 * The class name that all the experiment data was found in.
	 */
	private final String className;

	/**
	 * Name of the experiment.
	 */
	private final String name;

	/**
	 * If the experiments are static, and should be treated as such.
	 */
	private final boolean staticAccess;

	/**
	 * The entry point's method name.
	 */
	private final String entryPoint;

	/**
	 * The name of the renamed control method.
	 */
	private String controlMethod;

	/**
	 * The names of all the experiments to run along side the control.
	 */
	private final List<String> methodNames;

	/**
	 * The descriptor of all the methods.
	 * NOTE: If any of the methods have a differing descriptor an exception WILL be thrown.
	 */
	private Descriptor desc = null;

	protected ClassExperiment(String className, String experimentName, boolean staticAccess) {
		this.className = className;
		this.name = experimentName;
		this.staticAccess = staticAccess;
		this.entryPoint = format(ExperiJ.ENTRY_POINT_FORMAT, experimentName);
		methodNames = new ArrayList<>(3);
		System.out.println("Registering: " + experimentName);
	}

	public String name() {
		return name;
	}

	public String className() {
		return className;
	}

	public String controlMethod() {
		return controlMethod;
	}

	public String names(int index) {
		return methodNames.get(index);
	}

	/**
	 * @return - The descriptor that all experiment methods and the control method should match.
	 */
	public Descriptor desc() {
		return desc;
	}

	/**
	 * @return - The bytecode value that has to be used when loading objects from the experiments.
	 */
	public int loadCode() {
		if (desc.returnType.getSort() == Type.VOID)
			throw new IllegalStateException("Invalid store code call.");
		return desc.returnType.getOpcode(ILOAD);
	}

	/**
	 * @return - The bytecode value that has to be used when storing objects from the experiments.
	 */
	public int storeCode() {
		if (desc.returnType.getSort() == Type.VOID)
			throw new IllegalStateException("Invalid store code call.");
		return desc.returnType.getOpcode(ISTORE);

	}

	/**
	 * @return - The bytecode value that has to be used when returning objects from the experiments.
	 */
	public int returnCode() {
		return desc.returnType.getOpcode(IRETURN);
	}

	/**
	 * @return - The bytecode value that has to be used when invoking the experiment methods.
	 */
	public int invokeCode() {
		return staticAccess ? INVOKESTATIC : INVOKEVIRTUAL;
	}

	/**
	 * @return - The access flags that have to be used when generating methods. (Accounts for static methods)
	 */
	public int methodAccess() {
		return staticAccess ? DEFAULT_STATIC_METHOD_ACCESS : DEFAULT_METHOD_ACCESS;
	}

	/**
	 * @return - The size of all the experiments, not including the control.
	 */
	public int size() {
		return methodNames.size();
	}

	/**
	 * @return - The first free memory index. Accounts for parameters, and 'this'.
	 */
	public int firstIndex() {
		return desc.firstFreeIndex;
	}

	/**
	 * Loads all the method's parameters onto the stack, ready to be used by another method.
	 * Typically used before firing the control and the experiments.
	 *
	 * @param mv - The {@link MethodVisitor} that should be used to write the instructions.
	 * @return - Chaining
	 */
	public ClassExperiment loadAllParameters(MethodVisitor mv) {
		Type[] argumentTypes = desc.argumentTypes;
		int count = argumentTypes.length;
		if (count > 0) {
			int index = staticAccess ? 0 : 1;
			for (Type argument : argumentTypes) {
				int loadCode = argument.getOpcode(ILOAD);
				if (loadCode < 0)
					throw new IllegalStateException("Illegal load code call");
				mv.visitVarInsn(loadCode, index);
				index += argument.getSize();
			}
		}
		return this;
	}

	public ClassExperiment convertToString(MethodVisitor mv, Type argument) {
		int sort = argument.getSort();
		if (sort == Type.ARRAY) {
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Arrays", "deepToString", "([Ljava/lang/Object;)Ljava/lang/String;", false);
			return this;
		}
		String target = argument.toString();
		if (sort == Type.OBJECT) {
			String type = argument.toString();
			// We don't need to convert strings...
			if (type.equals("Ljava/lang/String;"))
				return this;
			target = "Ljava/lang/Object;";
		}
		// String doesn't have a byte version, so we should convert it to an integer.
		if (sort == Type.BYTE) {
			target = Type.INT_TYPE.toString();
		}
		mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/String", "valueOf", format("({})Ljava/lang/String;", target), false);
		return this;
	}

	/**
	 * Inserts the bytecode instructions to dynamically equate the first two objects on the stack.
	 * The method it uses to determine which method to call is based on the return type.
	 *
	 * @param mv - The {@link MethodVisitor} that should be used to write the instructions.
	 * @return - Chaining
	 */
	public ClassExperiment invokeEquals(MethodVisitor mv, int loadCode, int controlMemoryIndex, int experimentMemoryIndex) {
		Type returnType = desc.returnType;
		switch (returnType.getSort()) {
			case Type.BOOLEAN:
			case Type.BYTE:
			case Type.CHAR:
			case Type.SHORT:
			case Type.INT: {
				// Load the control result into the locals
				mv.visitVarInsn(loadCode, controlMemoryIndex);
				// Load the experiment result into the locals
				mv.visitVarInsn(loadCode, experimentMemoryIndex);

				Label falseBranch = new Label();
				mv.visitJumpInsn(IF_ICMPNE, falseBranch);
				mv.visitInsn(ICONST_1);
				Label trueBranch = new Label();
				mv.visitJumpInsn(GOTO, trueBranch);
				mv.visitLabel(falseBranch);
				mv.visitInsn(ICONST_0);
				mv.visitLabel(trueBranch);
				return this;
			}
			case Type.FLOAT: {
				// Load the control result into the locals
				mv.visitVarInsn(loadCode, controlMemoryIndex);
				// Load the experiment result into the locals
				mv.visitVarInsn(loadCode, experimentMemoryIndex);

				generateTrueFalseBranch(mv, FCMPL);
				return this;
			}
			case Type.DOUBLE: {
				// Load the control result into the locals
				mv.visitVarInsn(loadCode, controlMemoryIndex);
				// Load the experiment result into the locals
				mv.visitVarInsn(loadCode, experimentMemoryIndex);

				generateTrueFalseBranch(mv, DCMPL);
				return this;
			}
			case Type.LONG: {
				// Load the control result into the locals
				mv.visitVarInsn(loadCode, controlMemoryIndex);
				// Load the experiment result into the locals
				mv.visitVarInsn(loadCode, experimentMemoryIndex);

				generateTrueFalseBranch(mv, LCMP);
				return this;
			}
			case Type.OBJECT: {
				// This might seem a bit complex but it's just checking: (a == null ? b == null : a.equals(b))

				// A label that can be used to jump to the end
				Label end = new Label();

				// Load the control result into the locals
				mv.visitVarInsn(loadCode, controlMemoryIndex);
				// Label that points to the code that checks the objects using the Object#equals(Object) method.
				Label fullCheckBranch = new Label();
				// Checks if the control result is null
				// If the control result is null, then we only care if the experiment result is null.
				// Else jump to the full check. (controlResult.equals(experimentResult))
				mv.visitJumpInsn(IFNONNULL, fullCheckBranch);
				// If we got here, then the control result is null, and we only care if the experiment result is null at this point.
				// Load the experiment result into the locals.
				mv.visitVarInsn(loadCode, experimentMemoryIndex);
				// This label points to the section that just loads false and then exits.
				Label falseBranch = new Label();
				// If it's not null, then jump to some code that just loads false, and then jumps to the end.
				mv.visitJumpInsn(IFNONNULL, falseBranch);
				// If we're in here, then the experiment result was also null, so we need to load true
				mv.visitInsn(ICONST_1);
				// Jump to the end
				mv.visitJumpInsn(GOTO, end);
				// Insert the falseBranch label.
				mv.visitLabel(falseBranch);
				// load false
				mv.visitInsn(ICONST_0);
				// Jump to the end.
				mv.visitJumpInsn(GOTO, end);
				// Insert the fullCheckBranch, as this is where we do the full check.
				mv.visitLabel(fullCheckBranch);
				// Load the control result back into the locals.
				mv.visitVarInsn(loadCode, controlMemoryIndex);
				// Load the experiment result into the locals.
				mv.visitVarInsn(loadCode, experimentMemoryIndex);
				// Invoke the equals method on the control with the experiment as the parameter
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "equals", "(Ljava/lang/Object;)Z", false);
				// Insert the end label so other code can jump to it when it's done.
				mv.visitLabel(end);
				return this;
			}
			case Type.ARRAY: {
				// Load the control result into the locals.
				mv.visitVarInsn(loadCode, controlMemoryIndex);
				// Load the experiment result into the locals.
				mv.visitVarInsn(loadCode, experimentMemoryIndex);
				mv.visitMethodInsn(INVOKESTATIC, "java/util/Arrays", "deepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", false);
				return this;
			}
			default:
				throw new IllegalStateException("Unknown type: " + returnType);
		}
	}

	private static void generateTrueFalseBranch(MethodVisitor mv, int opcode) {
		mv.visitInsn(opcode);
		Label falseBranch = new Label();
		mv.visitJumpInsn(IFNE, falseBranch);
		mv.visitInsn(ICONST_1);
		Label trueBranch = new Label();
		mv.visitJumpInsn(GOTO, trueBranch);
		mv.visitLabel(falseBranch);
		mv.visitInsn(ICONST_0);
		mv.visitLabel(trueBranch);
	}

	/**
	 * Registers a method as apart of this experiment set.
	 *
	 * @param methodName - The name of the method that the annotation was found on.
	 * @param desc       - The descriptor of the method, used to confirm that the methods have the same signature.
	 * @param control    - If this method is the control of the experiment set.
	 */
	public ClassExperiment register(String methodName, Descriptor desc, boolean control, boolean staticAccess) {
		if (this.staticAccess != staticAccess)
			throw new IllegalStateException("Multiple method signatures across experiment: " + name + ". All methods of an experiment have to have the same method signatures for now. (Including static bound methods). This might change in the future, if it does, this exception will be removed.");
		int hash = desc.hashCode();
		if (this.desc == null) {
			this.desc = desc;
		} else if (hash != this.desc.hashCode()) {
			throw new IllegalStateException("Multiple method signatures across experiment: " + name + ". All methods of an experiment have to have the same method signatures for now. This might change in the future, if it does, this exception will be removed.");
		}
		if (control) {
			if (this.controlMethod != null)
				throw new IllegalStateException("There are multiple control methods declared for the experiment: " + name);
			controlMethod = methodName;
		} else {
			if (methodNames.contains(methodName) || !methodNames.add(methodName))
				// I have no idea how this could even happen...
				throw new IllegalStateException("There are multiple experiment methods with the same name: " + methodName);
		}
		return this;
	}

	/**
	 * Used to confirm that the experiment was built correctly.
	 * Will throw exceptions if the data within this class is incorrect.
	 */
	public void validate() {
		if (controlMethod == null || desc == null)
			throw new IllegalStateException(format("No @{} was provided for: {}.", Control.class.getSimpleName(), name));
		if (methodNames.isEmpty())
			throw new IllegalStateException(format("No @{} was provided for: {}.", Experiment.class.getSimpleName(), name));
	}

	public ExperimentVisitor createEntryPoint(ClassVisitor cv) {
		return createMethod(cv, methodAccess(), entryPoint);
	}

	public ExperimentVisitor createMethod(ClassVisitor cv, String name) {
		return createMethod(cv, methodAccess(), name);
	}

	public ExperimentVisitor createMethod(ClassVisitor cv, int access, String name) {
		Descriptor desc = this.desc;
		return new ExperimentVisitor(this, cv.visitMethod(access, name, desc.toString(), desc.signature, desc.exceptions), staticAccess);
	}

	public ClassExperiment invokeEntryPoint(MethodVisitor mv) {
		return invoke(mv, entryPoint);
	}

	public ClassExperiment invokeControl(MethodVisitor mv) {
		return invoke(mv, format(ExperiJ.RENAMED_METHOD_FORMAT, controlMethod));
	}

	public ClassExperiment invokeExperiment(MethodVisitor mv, int index) {
		return invoke(mv, format(ExperiJ.RENAMED_METHOD_FORMAT, methodNames.get(index)));
	}

	private ClassExperiment invoke(MethodVisitor mv, String methodName) {
		mv.visitMethodInsn(invokeCode(), className, methodName, desc.toString(), false);
		return this;
	}
}
