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
     * Uses the ReWrtie ConfigurationBuilder to define a set of rules used for the URL rewriting. We define a single rule that
     * rewrites requests for the index page if the request is determined to originate from a mobile browser.
     * 
     * @param context the ServletContext
     * @return the ReWrite Configuration
     */
    @Override
    public Configuration getConfiguration(final ServletContext context) {
        return ConfigurationBuilder.begin().defineRule()
                .when(Direction.isInbound().and(Path.matches("/").or(Path.matches("/index.jsf"))).and(new HttpCondition() {
                    @Override
                    public boolean evaluateHttp(HttpServletRewrite httpServletRewrite, EvaluationContext evaluationContext) {
                        HttpServletRequest request = httpServletRewrite.getRequest();
                        String userAgentStr = request.getHeader("user-agent");
                        String httpAccept = request.getHeader("Accept");
                        UAgentInfo uAgentInfo = new UAgentInfo(userAgentStr, httpAccept);
                        return uAgentInfo.detectTierIphone() || uAgentInfo.detectTierTablet();
                    }
                })).perform(Forward.to("/mobile/"));
    }
}