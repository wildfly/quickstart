/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.as.quickstart.cdi.extension;

import java.lang.reflect.Field;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

import org.jboss.as.quickstart.cdi.extension.model.Creature;
import org.w3c.dom.Element;

/**
 * Wrapper for the standard {@link InjectionTarget} to add the field values from the XML file.
 */
public class XmlBackedWrappedInjectionTarget<X extends Creature> implements InjectionTarget<X> {
    private final InjectionTarget<X> wrapped;
    private final Element xmlBacking;

    public XmlBackedWrappedInjectionTarget(InjectionTarget<X> it, Element xmlElement) {
        wrapped = it;
        xmlBacking = xmlElement;
    }

    @Override
    public void inject(X instance, CreationalContext<X> ctx) {
        wrapped.inject(instance, ctx);

        final Class<? extends Creature> klass = instance.getClass();
        for (Field field : klass.getDeclaredFields()) {
            field.setAccessible(true);
            final String fieldValueFromXml = xmlBacking.getAttribute(field.getName());
            try {
                if (field.getType().isAssignableFrom(Integer.TYPE)) {
                    field.set(instance, Integer.parseInt(fieldValueFromXml));
                } else if (field.getType().isAssignableFrom(String.class)) {
                    field.set(instance, fieldValueFromXml);
                } else {
                    // TODO: left up for the reader
                    throw new InjectionException("Cannot convert to type " + field.getType());
                }
            } catch (IllegalAccessException e) {
                throw new InjectionException("Cannot access field " + field);
            }
        }
    }

    @Override
    public void postConstruct(X instance) {
        wrapped.postConstruct(instance);
    }

    @Override
    public void preDestroy(X instance) {
        wrapped.preDestroy(instance);
    }

    @Override
    public X produce(CreationalContext<X> ctx) {
        return wrapped.produce(ctx);
    }

    @Override
    public void dispose(X instance) {
        wrapped.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return wrapped.getInjectionPoints();
    }
}
