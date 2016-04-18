package me.jezza.asm;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import me.jezza.repackage.org.objectweb.asm.ClassReader;
import me.jezza.repackage.org.objectweb.asm.ClassVisitor;
import me.jezza.repackage.org.objectweb.asm.ClassWriter;

/**
 * @author Jezza
 */
public final class ExperiJClassTransformer implements ClassFileTransformer {

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) throws IllegalClassFormatException {
		ClassReader cr = new ClassReader(classFileBuffer);
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
		ClassVisitor cv = new ExperiJClassVisitor(cw, className);
		try {
			cr.accept(cv, 0);
		} catch (Exception e) {
			// Don't ask.
			System.out.flush();
			System.err.println("ExperiJ failed to initialise with \"" + className.replace('/', '.') + "\" because of the following reason(s):");
			e.printStackTrace();
			System.err.flush();
			System.exit(-1);
		}
		return cw.toByteArray();
	}
}
