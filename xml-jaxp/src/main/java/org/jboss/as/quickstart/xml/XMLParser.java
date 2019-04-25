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
package org.jboss.as.quickstart.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

/**
 * Simple class to provide base for XML file parsing. It will validate any document using the schema produced by
 * {@link Constants}
 *
 * @author baranowb
 *
 */
public abstract class XMLParser {

    @Inject
    private Errors errorHolder;

    public List<Book> parse(InputStream is) throws Exception {
        /*
         * Validate against schema before it triggers implementation.
         */
        StringBuilder xmlFile = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is))) {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                xmlFile.append(line);
            }
        }
        String xml = xmlFile.toString();
        // validate against schema.
        try {
            URL schema = Resources.getResource("/catalog.xsd");
            Validator validator = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(schema)
                .newValidator();
            Source source = new StreamSource(new CharArrayReader(xml.toCharArray()));
            validator.validate(source);
        } catch (Exception e) {
            this.errorHolder.addErrorMessage("Validation Error", e);
            return null;
        }
        // parse file into catalog
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        // ask extending class to parse
        return parseInternal(bais);
    }

    /**
     *
     * @param is
     * @return
     * @throws Exception
     */
    protected abstract List<Book> parseInternal(InputStream is) throws Exception;

}
