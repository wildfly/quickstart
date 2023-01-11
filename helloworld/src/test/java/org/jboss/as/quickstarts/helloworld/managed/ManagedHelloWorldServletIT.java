/*
 * Copyright 2022 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.helloworld.managed;

import java.net.URI;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.as.arquillian.api.ContainerResource;
import org.jboss.as.arquillian.container.ManagementClient;
import org.jboss.as.quickstarts.helloworld.HelloWorldServletIT;
import org.junit.runner.RunWith;


/**
 *
 * @author Emmanuel Hugonnet (c) 2022 Red Hat, Inc.
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ManagedHelloWorldServletIT extends HelloWorldServletIT {
    @ContainerResource
    private ManagementClient managementClient;

    @Override
    protected URI getHTTPEndpoint() {
        return managementClient.getWebUri().resolve("/HelloWorld");
    }

    @Override
    protected String getLineSeparator() {
        return System.lineSeparator();
    }
}
