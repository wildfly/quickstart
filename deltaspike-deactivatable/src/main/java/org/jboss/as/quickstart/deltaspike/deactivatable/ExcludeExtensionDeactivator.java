/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
