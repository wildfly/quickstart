package org.jboss.as.quickstarts.numberguess;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * <p>
 * Random number generator.
 * </p>
 * 
 * <p>
 * Placing the random number generation, as well as the configuring the maximum number allows for a
 * more loosely coupled application. We can now change out the implementation of number generation
 * without any effect on the client code. We also produce a more intuitive design - both are
 * identifed by the fact they are numbers (int) and that they are qualified as the maximum number or
 * a random number.
 * </p>
 * 
 * <p>
 * We use the application scope to store the random number generator so that we use the same seed.
 * </p>
 * 
 * @author Pete Muir
 * 
 */
@ApplicationScoped
public class Generator implements Serializable {
   private static final long serialVersionUID = -7213673465118041882L;

   private java.util.Random random = new java.util.Random(System.currentTimeMillis());

   private int maxNumber = 100;

   java.util.Random getRandom() {
      return random;
   }

   @Produces
   @Random
   int next() {
      // a number between 1 and 100
      return getRandom().nextInt(maxNumber - 1) + 1;
   }

   @Produces
   @MaxNumber
   int getMaxNumber() {
      return maxNumber;
   }
}
