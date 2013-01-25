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
         //remote trailing comma
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
