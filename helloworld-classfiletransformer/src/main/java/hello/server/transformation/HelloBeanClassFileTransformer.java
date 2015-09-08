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

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

/**
 * A simple hello world class transformer that advices the <code>hello.server.ejb.HelloBean</code> EJB's sayHello method with
 * before and after log statements.<br>
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
     * Create a bytecode manipulator that will insert log statements before and after <code>hello.server.ejb.HelloBean</code>'s
     * <code>sayHello</code> method.
     */
    private final HelloByteCodeManipulator byteCodeTransformer = new HelloByteCodeManipulator("hello.server.ejb.HelloBean",
        "sayHello");

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
