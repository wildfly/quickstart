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

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstart.xml.Book;
import org.jboss.as.quickstart.xml.Errors;
import org.jboss.as.quickstart.xml.XMLParser;

/**
 * Backing bean to provide example with required logic to parse provided XML file.<br>
 *
 * @author baranowb
 *
 */
/*
 * Annotated as: - SessionScope bean to tie its lifecycle to session. This is required to make it shared between UploadServlet
 * invocation and JSF actions.
 */
@SessionScoped
public class FileUploadBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4542914921835861304L;

    // data, catalog which is displayed in h:dataTable
    private List<Book> catalog;

    @Inject
    private Errors errors;

    /*
     * Inject XMLParsor with 'Catalog' as type. Instance is created by container. Implementation alternative is controlled in
     * beans.xml
     */
    @Inject
    private XMLParser xmlParser;

    /**
     * Getter for books catalog.
     */
    @Produces
    @Named
    public List<Book> getCatalog() {
        return catalog;
    }

    /**
     * Action method invoked from UploadServlet once it parses request with 'multipart/form-data' form data and fetches uploaded
     * file.
     */
    public void parseUpload(InputStream is) {
        try {
            // Trigger parser and clear errors
            this.errors.getErrorMessages().clear();
            this.catalog = xmlParser.parse(is);
        } catch (Exception e) {
            this.errors.addErrorMessage("warning", e);
            return;
        }
    }
}
