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
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.eviction.EvictionStrategy;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.LockingMode;
import org.infinispan.transaction.TransactionMode;
import org.infinispan.transaction.lookup.GenericTransactionManagerLookup;
import org.infinispan.util.concurrent.IsolationLevel;
import org.jboss.as.quickstarts.datagrid.carmart.session.CacheContainerProvider;

/**
 * {@link CacheContainerProvider}'s implementation creating a DefaultCacheManager 
 * which is configured programmatically. Infinispan's libraries need to be bundled 
 * with the application - this is called "library" mode.
 * 
 * The only difference against TomcatCacheContainerProvider is the transaction 
 * manager lookup class.
 * 
 * @author Martin Gencur
 * 
 */
@ApplicationScoped
public class JBossASCacheContainerProvider implements CacheContainerProvider {
    private Logger log = Logger.getLogger(this.getClass().getName());

    private BasicCacheContainer manager;

    public BasicCacheContainer getCacheContainer() {
        if (manager == null) {
            GlobalConfiguration glob = new GlobalConfigurationBuilder()
                .nonClusteredDefault() //Helper method that gets you a default constructed GlobalConfiguration, preconfigured for use in LOCAL mode
                .globalJmxStatistics().enable() //This method allows enables the jmx statistics of the global configuration.
                .jmxDomain("org.infinispan.carmart.tx")  //prevent collision with non-transactional carmart
                .build(); //Builds  the GlobalConfiguration object
            Configuration loc = new ConfigurationBuilder()
                .jmxStatistics().enable() //Enable JMX statistics
                .clustering().cacheMode(CacheMode.LOCAL) //Set Cache mode to LOCAL - Data is not replicated.
                .transaction().transactionMode(TransactionMode.TRANSACTIONAL).autoCommit(false) //Enable Transactional mode with autocommit false
                .lockingMode(LockingMode.OPTIMISTIC).transactionManagerLookup(new GenericTransactionManagerLookup()) //uses GenericTransactionManagerLookup - This is a lookup class that locate transaction managers in the most  popular Java EE application servers. If no transaction manager can be found, it defaults on the dummy transaction manager.
                .locking().isolationLevel(IsolationLevel.REPEATABLE_READ) //Sets the isolation level of locking
                .eviction().maxEntries(4).strategy(EvictionStrategy.LIRS) //Sets  4 as maximum number of entries in a cache instance and uses the LIRS strategy - an efficient low inter-reference recency set replacement policy to improve buffer cache performance
                .loaders().passivation(false).addFileCacheStore().purgeOnStartup(true) //Disable passivation and adds a FileCacheStore that is purged on Startup
                .build(); //Builds the Configuration object
            manager = new DefaultCacheManager(glob, loc, true);
            log.info("=== Using DefaultCacheManager (library mode) ===");
        }
        return manager;
    }

    @PreDestroy
    public void cleanUp() {
        manager.stop();
        manager = null;
    }
}
