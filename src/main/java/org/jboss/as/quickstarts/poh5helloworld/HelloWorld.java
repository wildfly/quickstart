package org.jboss.as.quickstarts.poh5helloworld;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * A simple REST service which is able to say hello to someone using
 * HelloService
 * Please take a look at the web.xml where JAX-RS is enabled
 * And notice the @PathParam which expects the URL to contain /json/David or /xml/Mary
 * @author bsutter@redhat.com
 * 
 */

@Path("/")
public class HelloWorld {
	@Inject
	HelloService helloService;

	@GET
	@Path("/json/{name}")
	@Produces("application/json")
	public String getHelloWorldJSON(@PathParam("name") String name) {
		System.out.println("name: " + name);
		return "{\"result\":\"" + helloService.createHelloMessage(name) + "\"}";
	}

	@GET
	@Path("/xml/{name}")
	@Produces("application/xml")
	public String getHelloWorldXML(@PathParam("name") String name) {
		System.out.println("name: " + name);
		return "<xml><result>" + helloService.createHelloMessage(name)
				+ "</result></xml>";
	}

}
