package org.jboss.as.quickstart.xml.jaxp.dom;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jboss.as.quickstart.xml.XMLParser;
import org.jboss.as.quickstart.xml.catalog.Book;
import org.jboss.as.quickstart.xml.catalog.Catalog;
import org.jboss.as.quickstart.xml.jaxp.errors.ErrorHolder;
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
@SuppressWarnings("unchecked")
@RequestScoped
@Default
public class DOMXMLParser extends XMLParser<Catalog> {

    //Inject instance of error holder
    @Inject
    private ErrorHolder errorHolder;
    
    private DocumentBuilder builder;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    DOMXMLParser() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // this is strictly DTD validation, one of quirks in XML manipulation
        // schema validation my be done by hand.
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
    public Catalog parseInternal(InputStream is) throws Exception {

        Document document = this.builder.parse(is);

        Catalog catalog = new Catalog();

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
        for (int index = 0; index < children.getLength(); index++) {
            Node child = children.item(index);
            String childName = child.getLocalName();
            //empty/text nodes dont have name
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
