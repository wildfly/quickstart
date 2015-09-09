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
package org.jboss.as.quickstart.xml.upload;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Simple servlet to handle multipart file upload. <br>
 * This servlet register itself as handler for '/upload' path in application. When request is made with said context, this
 * servlet is invoked to handle it. Once it is done, it redirects user agent to application root path.
 *
 * @author baranowb
 *
 */
// Mark this class as servlet and indicates that requests to
// '/upload' URL in application be handled by this servlet.
@WebServlet(urlPatterns = { "/upload" })
// configure Servlet 3.0 multipart. Limit file size to 1MB.
@MultipartConfig(maxFileSize = 1048576L)
public class FileUploadServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 127759768859064589L;

    private static final Logger logger = Logger.getLogger(FileUploadServlet.class.getName());

    // name of form fields which are looked up in multipart request
    public static final String INPUT_NAME = "file";

    @Inject
    private FileUploadBean fileUploadBean;

    // override 'POST' handler. Appliction will use 'POST' to send 'multipart/form-data'
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
         * check content type. Require 'multipart/form-data' header in most case contains more than value, some parameters are
         * present so make a check with String.contains(String). send 406, with error message if it does not contain proper type
         */
        final String reqContentType = req.getContentType();

        if (!reqContentType.contains("multipart/form-data")) {
            logger.severe("Received request which is not mulipart: " + reqContentType);
            resp.sendError(406, "Received request which is not mulipart: " + reqContentType);
            return;
        }

        /*
         * In servlet 3.0, Parts carry form data. Get Parts and perform some name & type checks. Parts contain all data sent in
         * form not only file, we need only file.
         */
        Collection<Part> fileParts = req.getParts();
        if (fileParts != null && fileParts.size() > 0) {
            for (Part p : fileParts) {
                String partContentType = p.getContentType();
                String partName = p.getName();
                if (partContentType != null && partContentType.equals("text/xml") && partName != null
                    && partName.equals(INPUT_NAME)) {

                    InputStream is = p.getInputStream();
                    fileUploadBean.parseUpload(is);
                    break;
                }
            }

        }
        /*
         * Fetch dispatcher for '/'. This will make 'rd' initialized to dispatcher handling for application root.
         */
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/");

        if (rd != null) {
            /*
             * Forward the request to default servlet handling calls to application root. In our case FacesServlet
             */
            rd.forward(req, resp);
            return;
        } else {
            // this is bad thing, lets throw exception to make user aware of that?
            throw new IllegalStateException("Container is not well!");
        }

    }
}
