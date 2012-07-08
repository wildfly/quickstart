package org.jboss.as.quickstarts.interapp.shared;

import javax.ejb.Local;

/**
 * Bar is provided in a shared API jar, that can be referenced by any application wishing to.
 * 
 * @author Pete Muir
 *
 */
@Local
public interface Foo {

    public void setName(String name);

    public String getName();
    
}
