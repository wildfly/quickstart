/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.servlet.filterlistener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * A silly Servlet Filter that removes the letters a, e, i, o, and u (but not <a
 * href="http://oxforddictionaries.com/page/200">sometimes y</a>) from all request parameter values. To achieve this, a wrapper
 * is placed around the request object. This wrapper returns a different set of parameters than those that the JBoss EAP
 * container parsed from the original HTTP request.
 * <p>
 * This is just one simple example of what you can do with a filter. In real life, you will find filters useful for these kinds
 * of things:
 * <ul>
 * <li>Accepting or rejecting requests based on security requirements (by calling {@link HttpServletRequest#getUserPrincipal()}
 * or examining attributes of the {@link HttpSession} to see if the user is authenticated)
 * <li>Logging access to certain resources (this could also be done with a Request Listener)
 * <li>Caching responses (by wrapping the response's output stream in a {@link ByteArrayOutputStream} on cache miss, and
 * replaying saved responses on cache hit)
 * <li>Performing compression on request or response data (again, by wrapping the request's input stream or the response's
 * output stream)
 * </ul>
 * <p>
 * Note that this application also employs a {@linkplain ParameterDumpingRequestListener Request Listener}, which will see all
 * requests before this Filter sees them.
 *
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 */
@WebFilter("/*")
public class VowelRemoverFilter implements Filter {

    private ServletContext servletContext;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        // It is common to save a reference to the ServletContext here in case it is needed
        // in the destroy() call.
        servletContext = filterConfig.getServletContext();

        // To see this log message at run time, check out the terminal window where you started JBoss EAP.
        servletContext.log("VowelRemoverFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        final Map<String, String[]> filteredParams = Collections.unmodifiableMap(removeVowels(request.getParameterMap()));

        // Here, we wrap the request so that the servlet (and other filters further down the chain)
        // will see our filteredParams rather than the original request's parameters. Wrappers can be
        // as benign or as crazy as you like. Use your imagination!
        HttpServletRequestWrapper wrappedRequest = new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public Map<String, String[]> getParameterMap() {
                return filteredParams;
            }

            @Override
            public String getParameter(String name) {
                return filteredParams.get(name) == null ? null : filteredParams.get(name)[0];
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return Collections.enumeration(filteredParams.keySet());
            }

            @Override
            public String[] getParameterValues(String name) {
                return filteredParams.get(name);
            }
        };

        // Some notes on other filter use cases:
        // 1. We could have also wrapped the response object if we wanted to capture
        // or manipulate the output
        // 2. If we just wanted to examine the request or session, we could do that in
        // a filter or in a Request Listener (see ParameterDumpingRequestListener)
        // 3. You don't have to wrap the request or response at all if you just want
        // to set request or session attributes
        // 4. You don't have to call chain.doFilter(). The filter can handle the request
        // directly (for example, to forward to a login page or send a "403 Forbidden"
        // response if the user is not logged in)

        try {
            servletContext.log("VowelRemoverFilter invoking filter chain...");

            // This is where other filters, and ultimately the servlet or JSP, get a chance to handle the request
            chain.doFilter(wrappedRequest, response);

        } finally {

            // The try .. finally is important here because another filter or the
            // servlet itself may throw an exception
            servletContext.log("VowelRemoverFilter done filtering request");
        }
    }

    /**
     * Performs the vowel removal work of this filter.
     *
     * @param parameterMap the map of parameter names and values in the original request.
     * @return A copy of the original map with all the same keys, but whose values do not contain vowels.
     */
    private Map<String, String[]> removeVowels(Map<String, String[]> parameterMap) {
        Map<String, String[]> m = new HashMap<>();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String[] orig = entry.getValue();
            String[] vowelless = new String[orig.length];
            for (int i = 0; i < orig.length; i++) {
                vowelless[i] = orig[i].replaceAll("[aeiou]", "");
            }
            m.put(entry.getKey(), vowelless);
        }
        return m;
    }

    @Override
    public void destroy() {
        servletContext.log("VowelRemoverFilter destroyed");
        servletContext = null;
    }

}
