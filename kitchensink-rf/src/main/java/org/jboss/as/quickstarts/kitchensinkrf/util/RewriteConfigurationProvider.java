/**
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
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
 **/
package org.jboss.as.quickstarts.kitchensinkrf.util;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.config.Direction;
import org.ocpsoft.rewrite.context.EvaluationContext;
import org.ocpsoft.rewrite.servlet.config.Forward;
import org.ocpsoft.rewrite.servlet.config.HttpCondition;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.Path;
import org.ocpsoft.rewrite.servlet.http.event.HttpServletRewrite;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * This class provides the configuration for ReWrite.
 *
 * @author <a href="http://community.jboss.org/people/bleathem">Brian Leathem</a>
 */
public class RewriteConfigurationProvider extends HttpConfigurationProvider {

    @Override
    public int priority() {
        return 10;
    }

    /**
     * Uses the ReWrtie ConfigurationBuilder to define a set of rules used for the URL rewriting.  We define a single
     * rule that rewrites requests for the index page if the request is determined to originate from a mobile browser.
     *
     * @param context the ServletContext
     * @return the ReWrite Configuration
     */
    @Override
    public Configuration getConfiguration(final ServletContext context) {
        return ConfigurationBuilder.begin()
                .defineRule()
                .when(Direction.isInbound().and(Path.matches("/").or(Path.matches("/index.jsf"))).and(new HttpCondition() {
                    @Override
                    public boolean evaluateHttp(HttpServletRewrite httpServletRewrite, EvaluationContext evaluationContext) {
                        HttpServletRequest request = httpServletRewrite.getRequest();
                        String userAgentStr = request.getHeader("user-agent");
                        String httpAccept = request.getHeader("Accept");
                        UAgentInfo uAgentInfo = new UAgentInfo(userAgentStr, httpAccept);
                        return uAgentInfo.detectTierIphone() || uAgentInfo.detectTierTablet();
                    }
                }))
                .perform(Forward.to("/mobile/"));
    }
}