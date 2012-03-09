package org.jboss.as.quickstart.xml.jaxp.sax;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Alternative;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jboss.as.quickstart.xml.XMLParser;
import org.jboss.as.quickstart.xml.catalog.Book;
import org.jboss.as.quickstart.xml.catalog.Catalog;
import org.jboss.as.quickstart.xml.jaxp.FileUploadServlet;
import org.jboss.as.quickstart.xml.jaxp.errors.ErrorHolder;
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
public class SAXXMLParser extends XMLParser<Catalog> {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    
    //Inject instance of error holder
    @Inject
    private ErrorHolder errorHolder;
    
    private SAXParser parser;
    private SAXHandler saxHandler;

    SAXXMLParser() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        this.parser = factory.newSAXParser();
        this.saxHandler = new SAXHandler();
    }

    @Override
    public Catalog parseInternal(InputStream is) throws Exception {

        this.parser.parse(is, this.saxHandler);
        return this.saxHandler.catalog;
    }

    @SuppressWarnings("unchecked")
    private class SAXHandler extends DefaultHandler {

        private Catalog catalog;
        private Book book;

        private String currentElementValue;

        @Override
        public void startDocument() throws SAXException {
            this.catalog = new Catalog();
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
