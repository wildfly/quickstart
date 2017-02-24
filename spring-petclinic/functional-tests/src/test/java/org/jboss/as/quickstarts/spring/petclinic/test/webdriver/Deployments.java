package org.jboss.as.quickstarts.spring.petclinic.test.webdriver;

import java.io.File;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

/**
 *
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 *
 */
public class Deployments {
    private static final String PETCLINIC = "../target/spring-petclinic.war";

    public static WebArchive archive() {
        WebArchive archive = ShrinkWrap.createFromZipFile(WebArchive.class, new File(PETCLINIC));

        String discriminator = System.getProperty("discriminator");
        if (discriminator.equals("jdbc")) {
            archive.addAsWebInfResource(new File("src/test/resources/jdbc_web.xml"), "web.xml");
        } else if (discriminator.equals("spring-data-jpa")) {
            archive.addAsWebInfResource(new File("src/test/resources/spring_data_jpa_web.xml"), "web.xml");
        }
        return archive;
    }
}
