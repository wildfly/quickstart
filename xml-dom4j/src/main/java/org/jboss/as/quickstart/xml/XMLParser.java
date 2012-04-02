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
 * Simple class to provide base for XML file parsing. It will validate any document using the schema
 * produced by {@link Constants}
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
      StringBuffer xmlFile = new StringBuffer();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
      String line = null;
      while ((line = bufferedReader.readLine()) != null) {
         xmlFile.append(line);
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
