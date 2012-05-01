/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
/**
 * Implementation of parser based on DOM4J.
 * 
 * @author baranowb
 * 
 */
@RequestScoped
@Default
public class DOM4JXMLParser extends XMLParser {

    //Inject instance of error holder
    @Inject
    private Errors errorHolder;
    
    // Get the SAXReader object
    private SAXReader dom4jReader = new SAXReader();
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    DOM4JXMLParser() throws Exception {
        // Set SAX error handler. So errors atleast show up in console
        this.dom4jReader.setErrorHandler(new ErrorHandler() {

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

        Document document = this.dom4jReader.read(is);

        List<Book> catalog = new ArrayList<Book>();
        Element root = document.getRootElement();
        if (!root.getQName().getName().equals("catalog")) {
            throw new RuntimeException("Wrong element: " + root.getQName());
        }

        Iterator children = root.elementIterator();
        while(children.hasNext()){
            Node n = (Node) children.next();
            String childName = n.getName();
            if (childName == null)
                continue;

            if (childName.equals("book")) {
                Book b = parseBook((Element) n);
                catalog.add(b);
            }
        }

        return catalog;

    }

    private Book parseBook(Element n) {
        Book b = new Book();
        Iterator children = n.elementIterator();
        /*
         * parse book element, we have to once more iterate over children.
         */
        while(children.hasNext()) {
            Node child = (Node) children.next();
            String childName = child.getName();
            //empty/text nodes dont have name
            if (childName == null)
                continue;

            if (childName.equals("author")) {
                Element childElement = (Element) child;

                String textVal = childElement.getTextTrim();
                b.setAuthor(textVal);
            } else if (childName.equals("title")) {
                Element childElement = (Element) child;

                String textVal = childElement.getTextTrim();
                b.setTitle(textVal);
            } else if (childName.equals("genre")) {
                Element childElement = (Element) child;

                String textVal = childElement.getTextTrim();
                b.setGenre(textVal);
            } else if (childName.equals("price")) {
                Element childElement = (Element) child;

                String textVal = childElement.getTextTrim();
                b.setPrice(Float.parseFloat(textVal));
            } else if (childName.equals("publish_date")) {
                Element childElement = (Element) child;

                String textVal = childElement.getTextTrim();
                Date d;
                try {

                    d = DATE_FORMATTER.parse(textVal);
                    b.setPublishDate(d);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

            } else if (childName.equals("description")) {
                Element childElement = (Element) child;
                String textVal = childElement.getTextTrim();
                b.setDescription(textVal);
            }

        }

        return b;
    }
}
