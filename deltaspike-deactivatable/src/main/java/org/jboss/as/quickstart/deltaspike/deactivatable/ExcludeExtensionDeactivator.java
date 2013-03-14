/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstart.deltaspike.deactivatable;

import org.apache.deltaspike.core.impl.exclude.extension.ExcludeExtension;
import org.apache.deltaspike.core.spi.activation.ClassDeactivator;
import org.apache.deltaspike.core.spi.activation.Deactivatable;

/**
 * This class implements {@link ClassDeactivator}.
 * 
 * Returning 'false' or 'true' allows to de-/activate the class in question. Retuning null means that the current
 * class-deactivator doesn't have information about the class in question and can't provide a result.
 * 
 * This implementation is configured at src/main/resources/META-INF/apache-deltaspike.properties
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
public class ExcludeExtensionDeactivator implements ClassDeactivator {

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.deltaspike.core.spi.activation.ClassDeactivator#isActivated(java.lang.Class)
     */
    @Override
    public Boolean isActivated(Class<? extends Deactivatable> targetClass) {
        //Deactivate ExcludeExtension
        if (ExcludeExtension.class.equals(targetClass)) {
            return false;
        }
        //No information about other extensions
        return null;
    }

}
