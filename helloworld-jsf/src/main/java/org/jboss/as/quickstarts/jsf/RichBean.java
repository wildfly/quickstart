package org.jboss.as.quickstarts.jsf;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 * <p>
 * {@link RichBean} is the JSF backing bean for the application, holding the input data to be
 * re-displayed.
 * </p>
 * 
 */
@Named
@SessionScoped
public class RichBean implements Serializable {

    private static final long serialVersionUID = -6239437588285327644L;

    private String name;

    @PostConstruct
    public void postContruct() {
        name = "John";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}