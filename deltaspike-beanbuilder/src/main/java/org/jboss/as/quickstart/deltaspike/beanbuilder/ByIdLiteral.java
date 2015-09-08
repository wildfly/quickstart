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
package org.jboss.as.quickstart.deltaspike.beanbuilder;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

import org.apache.deltaspike.core.util.bean.BeanBuilder;

/**
 * This class represents the {@link ById} annotation with its value. It is used by the {@link BeanBuilder} to set the
 * {@link Bean} {@link Qualifier}
 *
 */
public class ByIdLiteral extends AnnotationLiteral<ById> implements ById {

    private static final long serialVersionUID = -8702546749474134989L;

    private final String value;

    public ByIdLiteral(String v) {
        this.value = v;
    }

    @Override
    public String value() {
        return value;
    }

}
