package com.experij.asm;

import java.util.HashMap;
import java.util.Map;

import com.experij.ExperiJ;
import com.experij.lib.Strings;
import com.experij.repackage.org.objectweb.asm.ClassVisitor;
import com.experij.repackage.org.objectweb.asm.Label;
import com.experij.repackage.org.objectweb.asm.MethodVisitor;
import com.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
final class ExperiJClassVisitor extends ClassVisitor implements Opcodes {
	private final String className;
	private final Map<String, ClassExperiment> experiments;

	ExperiJClassVisitor(ClassVisitor cv, String className) {
		super(ASM5, cv);
		this.className = className;
		experiments = new HashMap<>();
	}

	@Override
	public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		return new ExperiJMethodVisitor(this, mv, access, name, desc, signature, exceptions);
	}

	protected ClassExperiment registerExperiment(String experimentName, String methodName, Descriptor desc, boolean control, boolean staticAccess) {
		// Local var for the lambda
		String className = this.className;
		if (control)
			ExperiJ.setup(experimentName);
		ClassExperiment experiment = experiments.computeIfAbsent(Strings.confirmSafe(experimentName), k -> new ClassExperiment(className, k, staticAccess));
		return experiment.register(methodName, desc, control, staticAccess);
	}

	@Override
	public void visitEnd() {
		// This is where we generate all of the experiment code.
		super.visitEnd();
		for (ClassExperiment experiment : experiments.values()) {
			// Verify that the experiment was correctly built.
			experiment.validate();
			// Generate experiment code
			System.out.println("Generating Experiments: " + experiment.name);
			ExperimentVisitor ev = experiment.createEntryPoint(cv);
			ev.visitCode();
			// Load the results into a local variable with the offset of 0.
			int resultIndex = ev.createResults(0);
			// Fire the start measurement for the control
			ev.startControl(resultIndex);
			// Store the key, this is used the throughout the entire experiment.
			int keyIndex = ev.opcode(LSTORE, 1);
			// Load 'this', and all of the method's parameters, and fire the control method.
			ev.loadThis().loadAllParameters().invokeControl();
			// Store the control result after the key (The key is a long, and they take up two slots, so we need to store it after that.)
			int controlMemoryIndex = ev.voidMethod() ? -1 : ev.store(3);
			// Stop the measurement for the control, with the result index
			ev.stopControl(resultIndex, keyIndex);
			// Grab the return type size so we can store the experiment result in the correct slot.
			int size = ev.desc().returnType.getSize();
			// Begin running experiment code
			for (int i = 0; i < experiment.size(); i++) {
				Label start = new Label();
				Label end = new Label();
				Label handler = new Label();
				// Start try-catch block
				ev.visitTryCatchBlock(start, end, handler, "java/lang/Throwable");
				// Mark the start of the try-catch block.
				ev.visitLabel(start);
				// Start experiment measurement
				ev.startExperiment(i, resultIndex, keyIndex);
				// Load 'this', and all of the method parameters, and fire the experiment method
				ev.loadThis().loadAllParameters().invokeExperiment(i);
				// Work out the correct offset for the experiment's result, as store it for later for reference within the catch block, as we don't want to override the locals.
				int experimentIndex = 3 + size;
				// Store the result, which is the experiment index + the free index after the control result
				int experimentMemoryIndex = ev.voidMethod() ? -1 : ev.store(experimentIndex);
				// Stop the experiment measurement
				ev.stopExperiment(i, resultIndex, keyIndex);
				// Report the control <-> experimental equality
				ev.reportEquality(resultIndex, keyIndex, i, controlMemoryIndex, experimentMemoryIndex);
				// Jump to the end of the try-catch block.
				ev.visitJumpInsn(GOTO, end);
				// Start the handler block.
				ev.visitLabel(handler);
				// Store the throwable in the locals, taking care to not step on the locals by using the correct offset.
				int errorIndex = ev.opcode(ASTORE, experimentIndex + size);
				// Invoke error message on the results.
				ev.error(resultIndex, keyIndex, errorIndex);
				// End of try-catch block
				ev.visitLabel(end);
			}
			// Compile the data, and insert it into the context.
			ev.compile(resultIndex, keyIndex);
			if (!ev.voidMethod()) {
				// Load the control's return value, and return it.
				ev.load(controlMemoryIndex);
			}
			// End specific experiment code
			ev.visitInsn(experiment.returnCode());
			// These are completely ignored, because we configured it to compute the maximums automatically.
			ev.visitMaxs(0, 0);
			ev.visitEnd();
		}
	}
}
