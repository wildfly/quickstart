package hello.server.transformation;

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
            System.out.format("Instrumenting %s%n", clazz.getName(), m.getLongName());
            m.insertBefore(createMethodEntryCode(m));
            m.insertAfter(createMethodExitCode(), false);
            System.out.format("Successfully instrumented %s%n", clazz.getName(), m.getLongName());
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private String createMethodEntryCode(CtMethod m) {
        StringBuilder code = new StringBuilder();
        code.append("{    System.out.println(\"INTERCEPTED MethodInvocation - before method invocation\"); }");
        return code.toString();
    }

    private String createMethodExitCode() {
        StringBuilder code = new StringBuilder();
        code.append("{    System.out.println(\"INTERCEPTED MethodInvocation - after method invocation\"); }");
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
