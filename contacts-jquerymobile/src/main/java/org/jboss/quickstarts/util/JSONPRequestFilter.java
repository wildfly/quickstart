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
package org.jboss.quickstarts.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * <p>
 * This filter wraps qualified requests for JSON content with that standard JSONP padding. This makes these calls accessible
 * cross-domain using standard JSONP approaches.
 * </p>
 * <p>
 * To qualify for wrapping the request must be made to the <i>/rest/*</i> path, and contain a query parameter call
 * <i>jsoncallback</> that defines the JSONP callback method to use with the response.
 * </p>
 *
 * @author balunasj
 *
 */
@WebFilter("/rest/*")
public class JSONPRequestFilter implements Filter {
    // The callback method to use
    private static final String CALLBACK_METHOD = "jsonpcallback";

    // This is a simple safe pattern check for the callback method
    public static final Pattern SAFE_PRN = Pattern.compile("[a-zA-Z0-9_\\.]+");

    public static final String CONTENT_TYPE = "application/javascript";

    @Override
    public void init(FilterConfig config) throws ServletException {
        // Nothing needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {

        if (!(request instanceof HttpServletRequest)) {
            throw new ServletException("Only HttpServletRequest requests are supported");
        }

        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        // extract the callback method from the request query parameters
        String callback = getCallbackMethod(httpRequest);

        if (!isJSONPRequest(callback)) {
            // Request is not a JSONP request move on
            chain.doFilter(request, response);
        } else {
            // Need to check if the callback method is safe
            if (!SAFE_PRN.matcher(callback).matches()) {
                throw new ServletException("JSONP Callback method '" + CALLBACK_METHOD + "' parameter not valid function");
            }

            // Will stream updated response
            final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

            // Create a custom response wrapper to adding in the padding
            HttpServletResponseWrapper responseWrapper = new HttpServletResponseWrapper(httpResponse) {

                @Override
                public ServletOutputStream getOutputStream() throws IOException {
                    return new ServletOutputStream() {
                        @Override
                        public void write(int b) throws IOException {
                            byteStream.write(b);
                        }

                        @Override
                        public boolean isReady() {
                            // The stream is always ready
                            return true;
                        }

                        @Override
                        public void setWriteListener(WriteListener writeListener) {
                            // Nothing to do
                        }
                    };
                }

                @Override
                public PrintWriter getWriter() throws IOException {
                    return new PrintWriter(byteStream);
                }
            };

            // Process the rest of the filter chain, including the JAX-RS request
            chain.doFilter(request, responseWrapper);

            // Override response content and encoding
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding("UTF-8");

            // Write the padded updates to the output stream.
            response.getOutputStream().write((callback + "(").getBytes());
            response.getOutputStream().write(byteStream.toByteArray());
            response.getOutputStream().write(");".getBytes());
        }
    }

    private String getCallbackMethod(HttpServletRequest httpRequest) {
        return httpRequest.getParameter(CALLBACK_METHOD);
    }

    private boolean isJSONPRequest(String callbackMethod) {
        // A simple check to see if the query parameter has been set.
        return (callbackMethod != null && callbackMethod.length() > 0);
    }

    @Override
    public void destroy() {
        // Nothing to do
    }
}
