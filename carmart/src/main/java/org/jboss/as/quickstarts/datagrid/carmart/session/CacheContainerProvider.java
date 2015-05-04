/*
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
package org.jboss.as.quickstarts.datagrid.carmart.session;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.infinispan.commons.api.BasicCacheContainer;

/**
 * 
 * Subclasses should create an instance of a cache manager (DefaultCacheManager, RemoteCacheManager, etc.)
 * 
 * @author Martin Gencur
 * 
 */
public abstract class CacheContainerProvider {

    public static final String DATAGRID_HOST = "datagrid.host";
    public static final String HOTROD_PORT = "datagrid.hotrod.port";
    public static final String PROPERTIES_FILE = "META-INF" + File.separator + "datagrid.properties";

    abstract public BasicCacheContainer getCacheContainer();

    protected String jdgProperty(String name) {
        Properties props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return props.getProperty(name);
    }
}
