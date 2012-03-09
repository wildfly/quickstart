package org.jboss.as.quickstart.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.inject.Inject;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jboss.as.quickstart.xml.annotation.SchemaURL;
import org.jboss.as.quickstart.xml.jaxp.errors.ErrorHolder;

/**
 * Simple abstract class to provide base for XML file parsing. It provides extending classes with document validation based on
 * '@SchemaURL' injected by CDI container.
 * 
 * @author baranowb
 * 
 */
public abstract class XMLParser<T> {

    @Inject
    private ErrorHolder errorHolder;
    
    @Inject
    @SchemaURL
    private URL schemaURL;

    public T parse(InputStream is) throws Exception {
        /*
         * Validate against schema before it triggers implementation.
         */
        StringBuffer xmlFile = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            xmlFile.append(line);
        }
        String xml = xmlFile.toString();
        // validate against schema.
        try {
            SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
            Schema schema = sf.newSchema(schemaURL);
            Validator validator = schema.newValidator();
            Source source = new StreamSource(new CharArrayReader(xml.toCharArray()));
            validator.validate(source);
        } catch (Exception e) {
            this.errorHolder.addErrorMessage("Failed to validate file against schema!", e);
            return null;
        }
        // parse file into catalog
        ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
        // ask extending class to parse
        T result = parseInternal(bais);
        return result;
    }

    /**
     * 
     * @param is
     * @return
     * @throws Exception
     */
    protected abstract T parseInternal(InputStream is) throws Exception;

}
