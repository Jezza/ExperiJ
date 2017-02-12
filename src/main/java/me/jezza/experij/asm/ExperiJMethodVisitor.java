package me.jezza.experij.asm;

import static me.jezza.experij.lib.Strings.format;

import me.jezza.experij.ExperiJ;
import me.jezza.experij.interfaces.Control;
import me.jezza.experij.interfaces.Experiment;
import me.jezza.experij.repackage.org.objectweb.asm.AnnotationVisitor;
import me.jezza.experij.repackage.org.objectweb.asm.MethodVisitor;
import me.jezza.experij.repackage.org.objectweb.asm.Opcodes;

/**
 * @author Jezza
 */
final class ExperiJMethodVisitor extends MethodVisitor implements Opcodes {
	private final ExperiJClassVisitor cv;
	private final boolean staticAccess;
	private final String methodName;
	private final Descriptor desc;

	private boolean isExperiment;
	private ClassExperiment experiment;

	ExperiJMethodVisitor(ExperiJClassVisitor cv, MethodVisitor mv, int access, String methodName, String desc, final String signature, final String[] exceptions) {
		super(ASM5, mv);
		this.cv = cv;
		this.staticAccess = (access & ACC_STATIC) != 0;
		this.methodName = methodName;
		this.desc = Descriptor.from(desc, signature, exceptions, staticAccess);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (!isExperiment) {
			final boolean control = desc.equals(Control.DESCRIPTOR);
			if (control || desc.equals(Experiment.DESCRIPTOR)) {
				isExperiment = true;
				return new AnnotationVisitor(ASM5, super.visitAnnotation(desc, visible)) {

					@Override
					public void visit(String name, Object value) {
						super.visit(name, value);
						if (experiment != null)
							throw new IllegalStateException("Something has gone terribly wrong...");
						experiment = cv.registerExperiment((String) value, methodName, ExperiJMethodVisitor.this.desc, control, staticAccess);
					}
				};
			}
		}
		return super.visitAnnotation(desc, visible);
	}

	@Override
	public void visitCode() {
		// Rewrite this method with some code that invokes the entry point into the experiment, and then writes the original under a new method.
		if (isExperiment) {
			mv.visitCode();
			// If this method isn't static load 'this'
			if (!staticAccess)
				mv.visitVarInsn(ALOAD, 0);
			// Load all of the method's parameters, and invoke the entry point.
			experiment.loadAllParameters(mv).invokeEntryPoint(mv);
			// Return the value
			mv.visitInsn(experiment.returnCode());
			// The maxes are post-calculated, so don't worry about this.
			mv.visitMaxs(0, 0);
			// End this method, and begin the original.
			mv.visitEnd();
			// Write the original method, but with a new name, so we can call upon it later.
			mv = experiment.createMethod(cv, format(ExperiJ.RENAMED_METHOD_FORMAT, methodName));
		}
		super.visitCode();
	}
}
