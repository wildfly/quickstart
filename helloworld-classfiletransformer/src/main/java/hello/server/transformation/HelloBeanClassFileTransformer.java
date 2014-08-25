package hello.server.transformation;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * A simple hello world class transformer that advices the <code>hello.server.ejb.HelloBean</code> EJB's sayHello method with
 * before and after <code>System.out.println</code> statements.<br>
 * <br>
 *
 * This transformer is registered in the WAR's <code>META-INF/jboss-deployment-structure.xml</code> file.
 *
 * @author <a href="mailto:moelholm@gmail.com">Nicky Moelholm</a>
 */
public class HelloBeanClassFileTransformer implements ClassFileTransformer {

    // ------------------------------------------------------------------------
    // Member fields
    // ------------------------------------------------------------------------

    /**
     * Create a bytecode manipulator that will insert <code>System.out.println</code> statements before and after
     * <code>hello.server.ejb.HelloBean</code>'s <code>sayHello</code> method.
     */
    private final HelloByteCodeManipulator byteCodeTransformer = new HelloByteCodeManipulator("hello.server.ejb.HelloBean", "sayHello");

    // ------------------------------------------------------------------------
    // Contract required by the ClassFileTransformer API
    // ------------------------------------------------------------------------

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (!byteCodeTransformer.shouldAccept(className)) {
            return classfileBuffer;
        }

        return byteCodeTransformer.transform(loader, className, classfileBuffer);
    }

}
