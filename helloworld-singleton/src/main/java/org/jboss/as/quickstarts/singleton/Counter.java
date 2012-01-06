package org.jboss.as.quickstarts.singleton;

import javax.ejb.Singleton;
import javax.inject.Named;

/**
 * This singleton stores stores two counters which are only reset when the application is restarted.
 * 
 * @author Serge Pagop (spagop@redhat.com)
 * 
 */
@Singleton
@Named
public class Counter {

   private int a = 1;
   private int b = 1;

   public void incrementA() {
      a++;
   }

   public void incrementB() {
      b++;
   }
   
   public int getA() {
      return a;
   }
   
   public int getB() {
      return b;
   }
}
