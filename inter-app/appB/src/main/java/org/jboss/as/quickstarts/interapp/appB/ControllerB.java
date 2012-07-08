package org.jboss.as.quickstarts.interapp.appB;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.interapp.shared.Bar;
import org.jboss.as.quickstarts.interapp.shared.Foo;

/**
 * <p>
 * JSF Controller class that allows manipulation of Foo and Bar.
 * </p>
 * <p>
 * Note that whilst EJBs are used to provide inter application communication, this is not apparent to consumers of Foo and Bar,
 * which use CDI style injection.
 * </p>
 * 
 * @author Pete Muir
 * 
 */
@Named
public class ControllerB {

    @Inject
    private Foo foo;

    @Inject
    private Bar bar;

    public String getFoo() {
        return foo.getName();
    }

    public void setFoo(String name) {
        foo.setName(name);
    }

    public String getBar() {
        return bar.getName();
    }

    public void setBar(String name) {
        bar.setName(name);
    }

    public void sendAndUpdate() {
        // No-op
    }
}
