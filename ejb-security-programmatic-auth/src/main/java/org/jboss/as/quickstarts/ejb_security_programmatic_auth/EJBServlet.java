/*
 * Copyright 2023 JBoss by Red Hat.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package org.jboss.as.quickstarts.ejb_security_programmatic_auth;

 import java.io.IOException;
 import java.io.PrintWriter;

 import jakarta.servlet.annotation.WebServlet;
 import jakarta.servlet.http.HttpServlet;
 import jakarta.servlet.http.HttpServletRequest;
 import jakarta.servlet.http.HttpServletResponse;
 /**
 * <p>
 * A simple servlet which indicates successful deployment of the quickstart.
 * </p>
 *
 * <p>
 * The servlet is registered and mapped to /ejb-security-programmatic-auth using the {@linkplain WebServlet
 * @HttpServlet}.
 * </p>
 *
 * @author Prarthona Paul
 *
 */

@WebServlet("/ejb-security-programmatic-auth")
public class EJBServlet extends HttpServlet {

    static String PAGE_HEADER = "<html><head><title>helloworld</title></head><body>";

    static String PAGE_FOOTER = "</body></html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println(PAGE_HEADER);
        writer.println("ejb-security-programmatic-auth quickstart deployed successfully. You can find the available operations in the included README file.");
        writer.println(PAGE_FOOTER);
        writer.close();
    }
}