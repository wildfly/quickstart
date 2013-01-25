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

import java.io.IOException;
import java.io.PrintWriter;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.Cache;
import java.util.logging.Logger;

/**
 * A simple servlet storing value for key "hello" into the cache.
 *
 * @author Pete Muir
 *
 */
@SuppressWarnings("serial")
@WebServlet("/TestServletPut")
public class TestServletPut extends HttpServlet {

   private static final String PAGE_HEADER = "<html><head /><body>";

   private static final String PAGE_FOOTER = "</body></html>";

   @Inject
   private Logger log;

   @Inject
   DefaultCacheManager m;

   @Override
   protected void doGet(HttpServletRequest req, HttpServletResponse resp)
         throws ServletException, IOException {

      log.info("putting hello-world");
      Cache<String, String> c = m.getCache();
      c.put("hello", "world");

      PrintWriter writer = resp.getWriter();
      writer.println(PAGE_HEADER);
      writer.println("<h1>" + "Put Infinispan: " + c.get("hello") + "</h1>");
      writer.println(PAGE_FOOTER);
      writer.close();
   }

}
