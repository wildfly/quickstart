package org.jboss.as.quickstarts.erraihelloworld.server;

import javax.inject.Inject;
import javax.ws.rs.PathParam;

import org.jboss.as.quickstarts.erraihelloworld.client.shared.HelloWorldResource;


/**
 * A simple REST service which is able to say hello to someone using
 * HelloService. Please take a look at the web.xml where JAX-RS is enabled and
 * notice the @PathParam which expects the URL to contain {@code /json/David} or
 * {@code /xml/Mary}.
 *
 * @author bsutter@redhat.com
 */
public class HelloWorldResourceImpl implements HelloWorldResource {

  @Inject
  HelloService helloService;

  @Override
  public String getHelloWorldJSON(@PathParam("name") String name) {
    System.out.println("name: " + name);
    return helloService.createHelloMessage(name);
  }

  @Override
  public String getHelloWorldXML(@PathParam("name") String name) {
    System.out.println("name: " + name);
    return "<xml><result>" + helloService.createHelloMessage(name)
        + "</result></xml>";
  }

}
