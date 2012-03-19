package org.jboss.as.quickstart.xml;

import java.net.URL;

public class Resources {

   public static URL getResource(String path) {
      return Resources.class.getResource(path);
   }
   
}
