/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.as.quickstarts.datagrid;

import javax.inject.Inject;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.configuration.global.GlobalConfiguration;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.cache.CacheMode;

/**
 * Creates a DefaultCacheManager which is configured programmatically.
 * Infinispan's libraries need to be bundled with the application.
 *
 * @author Burr Sutter
 * @author Martin Gencur
 *
 */
@ApplicationScoped
public class MyCacheManagerProvider {

   private static final long ENTRY_LIFESPAN = 60 * 1000; //60 seconds

   @Inject
   private Logger log;

   private DefaultCacheManager manager;

   public DefaultCacheManager getCacheManager() {
      if (manager == null) {
         log.info("\n\n DefaultCacheManager does not exist - constructing a new one\n\n");

         GlobalConfiguration glob = new GlobalConfigurationBuilder()
               .clusteredDefault() // Builds a default clustered configuration
               .transport().addProperty("configurationFile", "jgroups-udp.xml") //provide a specific JGroups configuration
               .globalJmxStatistics().allowDuplicateDomains(true).enable()  //This method enables the jmx statistics of
                     // the global configuration and allows for duplicate JMX domains
               .build();  // Builds the GlobalConfiguration object
         Configuration loc = new ConfigurationBuilder()
               .jmxStatistics().enable()  //Enable JMX statistics
               .clustering().cacheMode(CacheMode.DIST_SYNC)  //Set Cache mode to DISTRIBUTED with SYNCHRONOUS replication
               .hash().numOwners(2) //Keeps two copies of each key/value pair
               .expiration().lifespan(ENTRY_LIFESPAN) //Set expiration - cache entries expire after some time (given by
                     // the lifespan parameter) and are removed from the cache (cluster-wide).
               .build();
         manager = new DefaultCacheManager(glob, loc, true);
      }
      return manager;
   }

   @PreDestroy
   public void cleanUp() {
      manager.stop();
      manager = null;
   }

}
