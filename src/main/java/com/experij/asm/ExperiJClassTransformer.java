package com.experij.asm;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.experij.repackage.org.objectweb.asm.ClassVisitor;
import com.experij.repackage.org.objectweb.asm.ClassWriter;
import com.experij.lib.Skip;
import com.experij.repackage.org.objectweb.asm.ClassReader;

/**
 * @author Jezza
 */
public final class ExperiJClassTransformer implements ClassFileTransformer {
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classFileBuffer) throws IllegalClassFormatException {
		if (Skip.test(className))
			return null;
		ClassReader cr = new ClassReader(classFileBuffer);
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
		ClassVisitor cv = new ExperiJClassVisitor(cw, className);
		try {
			cr.accept(cv, 0);
		} catch (Exception e) {
			// Don't ask.
			System.out.flush();
			System.err.println("Failed to initialise ExperiJ in \"" + className.replace('/', '.') + "\" because of the following reason(s):");
			e.printStackTrace();
			System.err.flush();
			System.exit(-1);
		}
		return cw.toByteArray();
	}
}
