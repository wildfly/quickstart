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
 * Annotated as: - SessionScope bean to tie its lifecycle to session. This is required to make it
 * shared between UploadServlet invocation and JSF actions.
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
    * Inject XMLParsor with 'Catalog' as type. Instance is created by container. Implementation
    * alternative is controlled in beans.xml
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
    * Action method invoked from UploadServlet once it parses request with 'multipart/form-data'
    * form data and fetches uploaded file.
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
