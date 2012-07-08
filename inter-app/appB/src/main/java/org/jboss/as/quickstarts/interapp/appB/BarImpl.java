package org.jboss.as.quickstarts.interapp.appB;

import javax.ejb.Singleton;

import org.jboss.as.quickstarts.interapp.shared.Bar;

/**
 * The Bar bean is registered as an EJB singleton, allowing it to be used in other applications.
 * 
 * @author Pete Muir
 * 
 */
@Singleton
public class BarImpl implements Bar {
    
    private String name;
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }

}
