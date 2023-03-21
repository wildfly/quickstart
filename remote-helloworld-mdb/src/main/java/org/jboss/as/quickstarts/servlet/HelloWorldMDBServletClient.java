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
package org.jboss.as.quickstarts.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jms.Destination;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSDestinationDefinition;
import jakarta.jms.JMSDestinationDefinitions;
import jakarta.jms.Queue;
import jakarta.jms.Topic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Definition of the two JMS destinations used by the quickstart
 * (one queue and one topic).
 */
@JMSDestinationDefinitions(
    value = {
        @JMSDestinationDefinition(
            name = "java:/queue/HELLOWORLDMDBQueue",
            interfaceName = "jakarta.jms.Queue",
            destinationName = "HelloWorldMDBQueue",
            properties = {"enable-amq1-prefix=false"}
        ),
        @JMSDestinationDefinition(
            name = "java:/topic/HELLOWORLDMDBTopic",
            interfaceName = "jakarta.jms.Topic",
            destinationName = "HelloWorldMDBTopic",
            properties = {"enable-amq1-prefix=false"}
        )
    }
)

/**
 * A simple servlet as client that sends several messages to a queue or a topic.
 * @author Emmanuel Hugonnet (c) 2023 Red Hat, Inc.
 */
@WebServlet("/HelloWorldMDBServletClient")
public class HelloWorldMDBServletClient extends HttpServlet {

    private static final long serialVersionUID = -8314035702649252239L;

    private static final int MSG_COUNT = 5;

    @Inject
    private transient JMSContext context;

    @Resource(lookup = "java:/queue/HELLOWORLDMDBQueue")
    private transient Queue queue;

    @Resource(lookup = "java:/topic/HELLOWORLDMDBTopic")
    private transient Topic topic;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        try (PrintWriter out = resp.getWriter()) {
            out.println("<h1>Quickstart: Example demonstrates the use of <strong>Jakarta Messaging 3.1</strong> and <strong>Jakarta Enterprise Beans 4.0 Message-Driven Bean</strong> in a JakartaEE server.</h1>");
            boolean useTopic = req.getParameterMap().keySet().contains("topic");
            final Destination destination = useTopic ? topic : queue;

            out.println("<p>Sending messages to <em>" + destination + "</em></p>");
            out.println("<h2>The following messages will be sent to the destination:</h2>");
            for (int i = 0; i < MSG_COUNT; i++) {
                String text = "This is message " + (i + 1);
                context.createProducer().send(destination, text);
                out.println("Message (" + i + "): " + text + "<br/>");
            }
            out.println("<p><i>Go to your JakartaEE server console or server log to see the result of messages processing.</i></p>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
