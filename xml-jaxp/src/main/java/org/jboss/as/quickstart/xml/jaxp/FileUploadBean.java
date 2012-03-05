package org.jboss.as.quickstart.xml.jaxp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstart.xml.XMLParser;
import org.jboss.as.quickstart.xml.catalog.Catalog;
import org.jboss.as.quickstart.xml.jaxp.errors.ErrorHolder;

/**
 * Backing bean to provide example with required logic to parse provided XML file.<br>
 * 
 * @author baranowb
 * 
 */
// Annotated as:
// - SessionScope bean to tie its lifecycle to session. This is required to make it shared between UploadServlet invocation and JSF actions.
// - Named - to make it visible to JSF engine, it equivalent to @ManagedBean
@SessionScoped
@Named(value = "fileUploadBean")
public class FileUploadBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -4542914921835861304L;

    // data, catalog which is displayed in h:dataTable
    private Catalog catalog;

    @Inject
    private ErrorHolder errorHolder;
    // Inject XMLParsor with 'Catalog' as type. Instance is created by container.
    // Implementation alternative is control
    @Inject
    private XMLParser<Catalog> xmlParser;

    /**
     * Getter for books catalog.
     * 
     * @return
     */
    public Catalog getCatalog() {
        return catalog;
    }

    /**
     * Setter for books catalog.
     * 
     * @param catalog
     */
    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Action method invoked from UploadServlet once it parses request with 'multipart/form-data' form data and fetches uploaded
     * file.<br>
     * This method expects file to be passed as {@link InputStream}.
     * 
     * @param stringBuffer
     * @throws IOException
     */
    @SuppressWarnings("static-access")
    public void parseUpload(InputStream is) {
        try {
            this.catalog = this.xmlParser.parse(is);
        } catch (Exception e) {
            errorHolder.addErrorMessage("warning", e);
            return;
        }
    }

}
