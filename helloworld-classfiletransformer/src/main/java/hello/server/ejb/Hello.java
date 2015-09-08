package hello.server.ejb;

import javax.jws.WebService;

/**
 * @author <a href="mailto:moelholm@gmail.com">Nicky Moelholm</a>
 */
@WebService
public interface Hello {

    String sayHello(String caller);

}
