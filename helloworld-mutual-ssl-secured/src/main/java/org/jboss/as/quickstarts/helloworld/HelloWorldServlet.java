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
package org.jboss.as.quickstarts.helloworld;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * <p>
 * A simple servlet taking advantage of features added in 3.0.
 * </p>
 * <p>
 * <p>
 * The servlet is registered and mapped to /HelloServlet using the {@linkplain WebServlet
 *
 * @author Giriraj Sharma
 * @HttpServlet}. The {@link HelloService} is injected by CDI.
 * </p>
 */
@SuppressWarnings("serial")
@WebServlet("/HelloWorld")
public class HelloWorldServlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";

    @Inject
    HelloService helloService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println(PAGE_HEADER);
        writer.println("<h2>" + helloService.createHelloMessage("World ") + "</h2>");
        writer.println("<h4> Caller Principal: " + req.getUserPrincipal() + "</h4>");
        writer.println("<h4> Client Certificate Pem: " + getPemFromCertificate(extractCertificate(req)) + "</h4>");
        writer.println(PAGE_FOOTER);
        writer.close();
    }

    protected X509Certificate extractCertificate(HttpServletRequest req) {
        X509Certificate[] certs = (X509Certificate[]) req.getAttribute("javax.servlet.request.X509Certificate");
        if (null != certs && certs.length > 0) {
            return certs[0];
        }
        throw new RuntimeException("No X.509 client certificate found in request");
    }

    public static String getPemFromCertificate(X509Certificate certificate) throws ServletException {
        if (certificate != null) {
            try {
                return Base64.getEncoder().encodeToString(certificate.getEncoded());
            } catch (CertificateEncodingException e) {
                throw new ServletException(e);
            }
        } else {
            return null;
        }
    }
}
