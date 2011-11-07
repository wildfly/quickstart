package org.jboss.as.quickstarts.test;

import static org.junit.Assert.assertNotNull;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jboss.as.quickstarts.controller.RegistrantRegistration;
import org.jboss.as.quickstarts.model.Registrant;
import org.jboss.as.quickstarts.util.Resources;

@RunWith(Arquillian.class)
public class RegistrantRegistrationTest {
   @Deployment
   public static Archive<?> createTestArchive() {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Registrant.class, RegistrantRegistration.class, Resources.class)
            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
   }

   @Inject
   RegistrantRegistration registrantRegistration;

   @Inject
   Logger log;

   @Test
   public void testRegister() throws Exception {
      Registrant newRegistrant = registrantRegistration.getNewRegistrant();
      newRegistrant.setName("Jane Doe");
      newRegistrant.setEmail("jane@mailinator.com");
      newRegistrant.setPhoneNumber("2125551234");
      registrantRegistration.register();
      assertNotNull(newRegistrant.getId());
      log.info(newRegistrant.getName() + " was persisted with id " + newRegistrant.getId());
   }
   
}
