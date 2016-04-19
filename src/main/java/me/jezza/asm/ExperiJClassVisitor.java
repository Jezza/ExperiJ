package me.jezza.asm;

import static me.jezza.lib.Strings.confirmSafe;

import java.util.HashMap;
import java.util.Map;

import me.jezza.ExperiJ;
import me.jezza.descriptor.Descriptor;
import me.jezza.repackage.org.objectweb.asm.ClassVisitor;
import me.jezza.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
public final class ExperiJClassVisitor extends ClassVisitor implements Opcodes {
	private final String className;
	private final Map<String, ClassExperiment> experiments;

	public ExperiJClassVisitor(ClassVisitor cv, String className) {
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
		ClassExperiment experiment = experiments.computeIfAbsent(confirmSafe(experimentName), k -> new ClassExperiment(className, k, staticAccess));
		return experiment.register(methodName, desc, control, staticAccess);
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
		for (ClassExperiment experiment : experiments.values()) {
			experiment.validate();
			// Generate experiment code
			System.out.println("Generating Experiments: " + experiment.name());
			ExperimentVisitor ev = experiment.createEntryPoint(cv);
			ev.visitCode();
			// Load the results into a local variable with the offset of 0.
			int resultIndex = ev.createResults(0);
			// Fire the start measurement for the control
			ev.startControl(resultIndex);
			// Store the key, this is used the throughout the entire experiment.
			int keyIndex = ev.opcode(LSTORE, 1);
			// Load 'this', and all of the method's parameters, and fire the control method.
			ev.loadThis().loadParameters().invokeControl();
			// Store the result locally with the offset of 1 as results are taking the first index
			int controlMemoryIndex = ev.store(3);
			// Stop the measurement for the control, with the result index
			ev.stopControl(resultIndex, keyIndex);

			// Begin running experiment code
			for (int i = 0; i < experiment.size(); i++) {
				// Start experiment measurement
				ev.startExperiment(i, resultIndex, keyIndex);
				// Load 'this', and all of the method parameters, and fire the experiment method
				ev.loadThis().loadParameters().invokeExperiment(i);
				// Store the result, which is the experiment index + the free index after the control result
				int experimentMemoryIndex = ev.store(4);
				// Stop the experiment measurement
				ev.stopExperiment(i, resultIndex, keyIndex);
				// Report the control <-> experimental equality
				ev.reportEquality(resultIndex, keyIndex, i, controlMemoryIndex, experimentMemoryIndex);
			}
			// Compile the data, and insert it into the context.
			ev.compile(resultIndex, keyIndex);
			// Load the control's return value, and return it.
			ev.load(controlMemoryIndex);
			// End specific experiment code
			ev.visitInsn(experiment.returnCode());
			// These are completely ignored, because we configured it to compute the maximums automatically.
			ev.visitMaxs(0, 0);
			ev.visitEnd();
		}
	}
}
