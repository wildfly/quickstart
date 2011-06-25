package org.jboss.as.quickstarts.helloworld;

/**
 * A simple CDI service which is able to say hello to someone
 * 
 * @author Pete Muir
 * 
 */
public class HelloService {

   String createHelloMessage(String name) {
      return "Hello " + name + "!";
   }

}
