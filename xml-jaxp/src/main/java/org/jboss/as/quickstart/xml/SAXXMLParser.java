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

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Implementation of parser based on JAXP DOM(W3C).
 *
 * @author baranowb
 *
 */
@RequestScoped
@Alternative
public class SAXXMLParser extends XMLParser {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    // Inject instance of error holder
    @Inject
    private Errors errorHolder;

    private SAXParser parser;
    private SAXHandler saxHandler;

    SAXXMLParser() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        this.parser = factory.newSAXParser();
        this.saxHandler = new SAXHandler();
    }

    @Override
    public List<Book> parseInternal(InputStream is) throws Exception {

        this.parser.parse(is, this.saxHandler);
        return this.saxHandler.catalog;
    }

    private class SAXHandler extends DefaultHandler {

        private List<Book> catalog;
        private Book book;

        private String currentElementValue;

        @Override
        public void startDocument() throws SAXException {
            System.out.println("Parsing the document using the SAXXMLParser!");
            this.catalog = new ArrayList<>();
            this.book = null;
            super.startDocument();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (qName.equals("book")) {
                this.book = new Book();
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("book")) {
                this.catalog.add(this.book);
                this.book = null;
            } else if (qName.equals("author")) {

                this.book.setAuthor(this.currentElementValue);
            } else if (qName.equals("title")) {

                this.book.setTitle(this.currentElementValue);
            } else if (qName.equals("genre")) {

                this.book.setGenre(this.currentElementValue);
            } else if (qName.equals("price")) {

                this.book.setPrice(Float.parseFloat(this.currentElementValue));
            } else if (qName.equals("publish_date")) {

                Date d;
                try {

                    d = DATE_FORMATTER.parse(this.currentElementValue);
                    this.book.setPublishDate(d);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            } else if (qName.equals("description")) {

                this.book.setDescription(this.currentElementValue);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            this.currentElementValue = new String(ch, start, length);
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
            errorHolder.addErrorMessage("warning", e);
            super.warning(e);
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            errorHolder.addErrorMessage("error", e);
            super.error(e);
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            errorHolder.addErrorMessage("fatal error", e);
            super.fatalError(e);
        }

    }
}
