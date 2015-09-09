/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello.server.transformation;

import java.util.logging.Logger;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * A super simple byte code writer class based on <code>javassist</code> for doing the hard work.<br>
 * <br>
 * This class is hardly useful in real projects (because it is rather limited in what it is able to do right now). But you can
 * use it as inspiration for your own wonderful bytecode adventures.
 *
 * @author <a href="mailto:moelholm@gmail.com">Nicky Moelholm</a>
 */
public class HelloByteCodeManipulator {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    private static final Logger logger = Logger.getLogger(HelloByteCodeManipulator.class.getName());

    // ------------------------------------------------------------------------
    // Member fields
    // ------------------------------------------------------------------------

    private final String classToAccept;
    private final String methodToAdvice;

    // ------------------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------------------

    public HelloByteCodeManipulator(String classToAccept, String methodToAdvice) {
        this.classToAccept = classToAccept.replace('.', '/');
        this.methodToAdvice = methodToAdvice;
    }

    // ------------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------------

    public boolean shouldAccept(String className) {
        return className.startsWith(classToAccept);
    }

    public byte[] transform(ClassLoader loader, String className, byte[] classfileBuffer) {

        ClassPool pool = createClassPool(loader);

        CtClass clazz = getCtClass(className, pool);

        byte[] newByteCode = transformClassCode(className, classfileBuffer, pool, clazz);

        return newByteCode;
    }

    // ------------------------------------------------------------------------
    // Private functionality
    // ------------------------------------------------------------------------

    private byte[] transformClassCode(String className, byte[] classfileBuffer, ClassPool pool, CtClass clazz) {
        try {
            CtMethod[] ctMethods = clazz.getDeclaredMethods();
            for (CtMethod m : ctMethods) {
                if (m.getName().contains(methodToAdvice)) {
                    instrumentMethod(pool, clazz, m);
                }
            }
            return clazz.toBytecode();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            clazz.detach();
        }
    }

    private void instrumentMethod(ClassPool pool, CtClass clazz, CtMethod m) {
        try {
            logger.info(String.format("Instrumenting %s", clazz.getName(), m.getLongName()));
            m.insertBefore(createMethodEntryCode(m));
            m.insertAfter(createMethodExitCode(), false);
            logger.info(String.format("Successfully instrumented %s", clazz.getName(), m.getLongName()));
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private String createMethodEntryCode(CtMethod m) {
        StringBuilder code = new StringBuilder();
        code.append("{    logger.info(\"INTERCEPTED MethodInvocation - before method invocation\"); }");
        return code.toString();
    }

    private String createMethodExitCode() {
        StringBuilder code = new StringBuilder();
        code.append("{    logger.info(\"INTERCEPTED MethodInvocation - after method invocation\"); }");
        return code.toString();
    }

    private ClassPool createClassPool(ClassLoader loader) {
        try {
            HelloClassPool pool = new HelloClassPool();
            pool.addClassLoaderClassPath(loader);
            pool.addClassLoaderClassPath(Thread.currentThread().getContextClassLoader());
            return pool;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CtClass getCtClass(String className, ClassPool pool) {
        try {
            return pool.get(className.replace('/', '.'));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
