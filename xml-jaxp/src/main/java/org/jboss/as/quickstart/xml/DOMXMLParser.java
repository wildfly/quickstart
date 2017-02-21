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
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Implementation of parser based on JAXP DOM(W3C).
 *
 * @author baranowb
 *
 */
@RequestScoped
@Default
public class DOMXMLParser extends XMLParser {

    // Inject instance of error holder
    @Inject
    private Errors errorHolder;

    private DocumentBuilder builder;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    DOMXMLParser() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        /*
         * bizarrely, setValidating refers to DTD validation only, and we are using schema validation
         */
        factory.setValidating(false);
        factory.setNamespaceAware(true);

        this.builder = factory.newDocumentBuilder();

        // Set SAX error handler. So errors atleast show up in console
        this.builder.setErrorHandler(new ErrorHandler() {

            @Override
            public void warning(SAXParseException e) throws SAXException {
                errorHolder.addErrorMessage("warning", e);
            }

            @Override
            public void fatalError(SAXParseException e) throws SAXException {
                errorHolder.addErrorMessage("fatal error", e);
            }

            @Override
            public void error(SAXParseException e) throws SAXException {
                errorHolder.addErrorMessage("error", e);
            }
        });

    }

    @Override
    public List<Book> parseInternal(InputStream is) throws Exception {
        System.out.println("Parsing the document using the DOMXMLParser!");

        Document document = this.builder.parse(is);

        List<Book> catalog = new ArrayList<>();

        Element root = document.getDocumentElement();
        if (!root.getLocalName().equals("catalog")) {
            throw new RuntimeException("Wrong element: " + root.getTagName());
        }

        NodeList children = root.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {

            Node n = children.item(index);
            String childName = n.getLocalName();
            if (childName == null)
                continue;

            if (n.getLocalName().equals("book")) {
                Book b = parseBook((Element) n);
                catalog.add(b);
            }
        }

        return catalog;

    }

    private Book parseBook(Element n) {
        Book b = new Book();
        NodeList children = n.getChildNodes();
        /*
         * parse book element, we have to once more iterate over children.
         */
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            String childName = child.getLocalName();
            // empty/text nodes dont have name
            if (childName == null)
                continue;

            if (childName.equals("author")) {
                Element childElement = (Element) child;

                String textVal = getTextValue(childElement);
                b.setAuthor(textVal);
            } else if (childName.equals("title")) {
                Element childElement = (Element) child;

                String textVal = getTextValue(childElement);
                b.setTitle(textVal);
            } else if (childName.equals("genre")) {
                Element childElement = (Element) child;

                String textVal = getTextValue(childElement);
                b.setGenre(textVal);
            } else if (childName.equals("price")) {
                Element childElement = (Element) child;

                String textVal = getTextValue(childElement);
                b.setPrice(Float.parseFloat(textVal));
            } else if (childName.equals("publish_date")) {
                Element childElement = (Element) child;

                String textVal = getTextValue(childElement);
                Date d;
                try {

                    d = DATE_FORMATTER.parse(textVal);
                    b.setPublishDate(d);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            } else if (childName.equals("description")) {
                Element childElement = (Element) child;
                String textVal = getTextValue(childElement);
                b.setDescription(textVal);
            }

        }

        return b;
    }

    private String getTextValue(Element childElement) {
        NodeList nl = childElement.getChildNodes();
        if (nl != null && nl.getLength() > 0) {
            Node node = nl.item(0);
            if (node.getNodeType() == Node.TEXT_NODE) {
                return node.getNodeValue();
            }
        }
        return "";
    }
}
