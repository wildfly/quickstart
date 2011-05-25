package org.jboss.weld.examples.numberguess;


import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class Generator implements Serializable 
{
   private static final long serialVersionUID = -7213673465118041882L;

   private java.util.Random random = new java.util.Random(System.currentTimeMillis());
   
   private int maxNumber = 100;
   
   java.util.Random getRandom()
   {
      return random;
   }
   
   @Produces @Random int next() 
   { 
      //a number between 1 and 100
      return getRandom().nextInt(maxNumber - 1) + 1; 
   }
   
   @Produces @MaxNumber int getMaxNumber()
   {
      return maxNumber;
   }
} 
