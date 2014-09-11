package hello.server.ejb;

import java.util.logging.Logger;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 * A simple EJB that exposes a web service <code>view</code> (SOAP/WSDL).<br>
 * <br>
 *
 * @author <a href="mailto:moelholm@gmail.com">Nicky Moelholm</a>
 */
@Stateless
@WebService
@PermitAll
public class HelloBean implements Hello {

    // ------------------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------------------

    private static final Logger logger = Logger.getLogger(HelloBean.class.getName());

    // ------------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------------

    public String sayHello(String caller) {

        logger.info(String.format("EJB method invoked from caller %s", caller));

        return String.format("Hello %s", caller);
    }
}
