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

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.stats.Stats;
import org.jboss.as.quickstarts.datagrid.carmart.session.CarManager;
import org.jboss.as.quickstarts.datagrid.carmart.session.StatisticsProvider;


@Named("stats")
@RequestScoped
public class LocalStatisticsProvider implements StatisticsProvider {

    @Inject
    private LocalCacheContainerProvider provider;

    private Stats stats;

    @PostConstruct
    public void getStatsObject() {
        stats = ((DefaultCacheManager) provider.getCacheContainer()).getCache(CarManager.CACHE_NAME).getAdvancedCache()
                .getStats();
    }

    public String getRetrievals() {
        return String.valueOf(stats.getRetrievals());
    }

    public String getStores() {
        return String.valueOf(stats.getStores());
    }

    public String getCurrentEntries() {
        return String.valueOf(stats.getCurrentNumberOfEntries());
    }

    public String getHits() {
        return String.valueOf(stats.getHits());
    }

    public String getMisses() {
        return String.valueOf(stats.getMisses());
    }

    public String getRemoveHits() {
        return String.valueOf(stats.getRemoveMisses());
    }
}
