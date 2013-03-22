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
package org.jboss.as.quickstarts.datagrid;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.Cache;
import java.util.Set;

/**
 * Retrieves entries from the cache.
 * 
 * @author Burr Sutter
 * 
 */
@Named
@RequestScoped
public class GetController {

    @Inject
    private Logger log;

    @Inject
    DefaultCacheManager m;

    private String key;

    private String message;

    private StringBuffer allKeyValues = new StringBuffer();

    // Called by the get.xhtml - get button
    public void getOne() {
        Cache<String, String> c = m.getCache();
        message = c.get(key);
        log.info("get: " + key + " " + message);
    }

    // Called by the get.xhtml - get all button
    public void getAll() {
        Cache<String, String> c = m.getCache();

        Set<String> keySet = c.keySet();
        for (String key : keySet) {

            String value = c.get(key);
            log.info("k: " + key + " v: " + value);

            allKeyValues.append(key + "=" + value + ", ");
        } // for

        if (allKeyValues == null || allKeyValues.length() == 0) {
            message = "Nothing in the Cache";
        } else {
            // remote trailing comma
            allKeyValues.delete(allKeyValues.length() - 2, allKeyValues.length());
            message = allKeyValues.toString();
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

}
