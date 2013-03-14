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

import org.apache.deltaspike.core.api.exclude.annotation.Exclude;
import org.apache.deltaspike.core.impl.exclude.extension.ExcludeExtension;

/**
 * This Bean should be excluded because it uses the {@link Exclude} annotation. But it won't be excluded because the
 * {@link ExcludeExtension} is deactivated on {@link ExcludeExtensionDeactivator}
 * 
 * 
 * @author <a href="mailto:benevides@redhat.com">Rafael Benevides</a>
 * 
 */
@Exclude
public class MyBean {

}
