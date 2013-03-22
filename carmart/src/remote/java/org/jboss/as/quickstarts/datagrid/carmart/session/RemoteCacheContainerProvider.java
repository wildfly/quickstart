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

import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import org.infinispan.api.BasicCacheContainer;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.jboss.as.quickstarts.datagrid.carmart.session.CacheContainerProvider;


/**
 * 
 * {@link CacheContainerProvider}'s implementation creating a HotRod client. 
 * JBoss Data Grid server needs to be running and configured properly 
 * so that HotRod client can remotely connect to it - this is called client-server mode.
 * 
 * @author Martin Gencur
 * 
 */
@ApplicationScoped
public class RemoteCacheContainerProvider extends CacheContainerProvider {

    private Logger log = Logger.getLogger(this.getClass().getName());

    private BasicCacheContainer manager;

    public BasicCacheContainer getCacheContainer() {
        if (manager == null) {
            manager = new RemoteCacheManager(jdgProperty(DATAGRID_HOST) + ":" + jdgProperty(HOTROD_PORT), true);
            log.info("=== Using RemoteCacheManager (Hot Rod) ===");
        }
        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }
}
