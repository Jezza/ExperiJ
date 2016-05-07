package me.jezza.asm;

import static me.jezza.lib.Strings.format;

import me.jezza.ExperiJ;
import me.jezza.descriptor.Descriptor;
import me.jezza.interfaces.Control;
import me.jezza.interfaces.Experiment;
import me.jezza.repackage.org.objectweb.asm.*;

/**
 * @author Jezza
 */
public final class ExperiJMethodVisitor extends MethodVisitor implements Opcodes {
	private final ExperiJClassVisitor cv;
	private final boolean staticAccess;
	private final String methodName;
	private final Descriptor desc;

	private boolean isExperiment;
	private ClassExperiment experiment;
	private MethodVisitor method;

	protected ExperiJMethodVisitor(ExperiJClassVisitor cv, MethodVisitor mv, int access, String methodName, String desc, final String signature, final String[] exceptions) {
		super(ASM5, mv);
		this.cv = cv;
		this.staticAccess = (access & ACC_STATIC) != 0;
		this.methodName = methodName;
		this.desc = Descriptor.from(desc, signature, exceptions, staticAccess);
	}

	@Override
	public void visitParameter(String name, int access) {
		super.visitParameter(name, access);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return super.visitAnnotationDefault();
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
	public AnnotationVisitor visitParameterAnnotation(int parameter, String desc, boolean visible) {
		return super.visitParameterAnnotation(parameter, desc, visible);
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public void visitAttribute(Attribute attr) {
		super.visitAttribute(attr);
	}

	@Override
	public void visitCode() {
		if (isExperiment) {
			mv.visitCode();
			if (!staticAccess) {
				// Load 'this'
				mv.visitVarInsn(ALOAD, 0);
			}
			// Load all of the method's parameters, and invoke the entry point.
			experiment.loadParameters(mv).invokeEntryPoint(mv);
			// Return the value that was given back with the specific return code that we got from the descriptor.
			mv.visitInsn(experiment.returnCode());
			// Write the maximums, and then end.
			mv.visitMaxs(0, 1);
			mv.visitEnd();

			// Write the original method, but with a new name, so we can call upon it later.
			method = experiment.createMethod(cv, format(ExperiJ.RENAME_METHOD_FORMAT, methodName));
		}
		super.visitCode();
	}

	@Override
	public void visitFrame(int type, int nLocal, Object[] local, int nStack, Object[] stack) {
		if (isExperiment) {
			method.visitFrame(type, nLocal, local, nStack, stack);
		} else {
			super.visitFrame(type, nLocal, local, nStack, stack);
		}
	}

	@Override
	public void visitInsn(int opcode) {
		if (isExperiment) {
			method.visitInsn(opcode);
		} else {
			super.visitInsn(opcode);
		}
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
		if (isExperiment) {
			method.visitMethodInsn(opcode, owner, name, desc, itf);
		} else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	@Override
	public void visitIincInsn(int var, int increment) {
		if (isExperiment) {
			method.visitIincInsn(var, increment);
		} else {
			super.visitIincInsn(var, increment);
		}
	}

	@Override
	public void visitIntInsn(int opcode, int operand) {
		if (isExperiment) {
			method.visitIntInsn(opcode, operand);
		} else {
			super.visitIntInsn(opcode, operand);
		}
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (isExperiment) {
			method.visitFieldInsn(opcode, owner, name, desc);
		} else {
			super.visitFieldInsn(opcode, owner, name, desc);
		}
	}

	@Override
	public void visitLdcInsn(Object cst) {
		if (isExperiment) {
			method.visitLdcInsn(cst);
		} else {
			super.visitLdcInsn(cst);
		}
	}

	@Override
	public void visitJumpInsn(int opcode, Label label) {
		if (isExperiment) {
			method.visitJumpInsn(opcode, label);
		} else {
			super.visitJumpInsn(opcode, label);
		}
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		if (isExperiment) {
			method.visitTypeInsn(opcode, type);
		} else {
			super.visitTypeInsn(opcode, type);
		}
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		if (isExperiment) {
			method.visitVarInsn(opcode, var);
		} else {
			super.visitVarInsn(opcode, var);
		}
	}

	@Override
	public void visitLabel(Label label) {
		if (isExperiment) {
			method.visitLabel(label);
		} else {
			super.visitLabel(label);
		}
	}

	@Override
	public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		if (isExperiment) {
			return method.visitInsnAnnotation(typeRef, typePath, desc, visible);
		}
		return super.visitInsnAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		if (isExperiment) {
			method.visitTryCatchBlock(start, end, handler, type);
		} else {
			super.visitTryCatchBlock(start, end, handler, type);
		}
	}

	@Override
	public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String desc, boolean visible) {
		if (isExperiment) {
			return method.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
		}
		return super.visitTryCatchAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
		if (isExperiment) {
			method.visitLocalVariable(name, desc, signature, start, end, index);
		} else {
			super.visitLocalVariable(name, desc, signature, start, end, index);
		}
	}

	@Override
	public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String desc, boolean visible) {
		if (isExperiment) {
			return method.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
		}
		return super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, desc, visible);
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		if (isExperiment) {
			method.visitLineNumber(line, start);
		} else {
			super.visitLineNumber(line, start);
		}
	}

	@Override
	public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
		if (isExperiment) {
			method.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
		} else {
			super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
		}
	}

	@Override
	public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
		if (isExperiment) {
			method.visitLookupSwitchInsn(dflt, keys, labels);
		} else {
			super.visitLookupSwitchInsn(dflt, keys, labels);
		}
	}

	@Override
	public void visitMultiANewArrayInsn(String desc, int dims) {
		if (isExperiment) {
			method.visitMultiANewArrayInsn(desc, dims);
		} else {
			super.visitMultiANewArrayInsn(desc, dims);
		}
	}

	@Override
	public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
		if (isExperiment) {
			method.visitTableSwitchInsn(min, max, dflt, labels);
		} else {
			super.visitTableSwitchInsn(min, max, dflt, labels);
		}
	}

	@Override
	public void visitMaxs(int maxStack, int maxLocals) {
		if (isExperiment) {
			method.visitMaxs(maxStack, maxLocals);
		} else {
			super.visitMaxs(maxStack, maxLocals);
		}
	}

	@Override
	public void visitEnd() {
		super.visitEnd();
	}
}
