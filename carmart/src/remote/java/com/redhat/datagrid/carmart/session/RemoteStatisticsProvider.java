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
package com.redhat.datagrid.carmart.session;

import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.inject.Named;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.ServerStatistics;

import com.redhat.datagrid.carmart.session.CacheContainerProvider;
import com.redhat.datagrid.carmart.session.CarManager;
import com.redhat.datagrid.carmart.session.StatisticsProvider;

@Named("stats")
@RequestScoped
public class RemoteStatisticsProvider implements StatisticsProvider {

    @Inject
    private CacheContainerProvider provider;

    private Map<String, String> stats;

    @PostConstruct
    public void getStatsObject() {
        RemoteCache<String, Object> carCache = (RemoteCache) provider.getCacheContainer().getCache(CarManager.CACHE_NAME);
        stats = carCache.stats().getStatsMap();
    }

    public String getRetrievals() {
        return stats.get(ServerStatistics.RETRIEVALS);
    }

    public String getStores() {
        return stats.get(ServerStatistics.STORES);
    }

    public String getTotalEntries() {
        return stats.get(ServerStatistics.TOTAL_NR_OF_ENTRIES);
    }

    public String getHits() {
        return stats.get(ServerStatistics.HITS);
    }

    public String getMisses() {
        return stats.get(ServerStatistics.MISSES);
    }

    public String getRemoveHits() {
        return stats.get(ServerStatistics.REMOVE_HITS);
    }
}
