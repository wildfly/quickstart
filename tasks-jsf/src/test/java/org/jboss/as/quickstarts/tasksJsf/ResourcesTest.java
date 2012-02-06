package org.jboss.as.quickstarts.tasksJsf;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Lukas Fryc
 */
@RunWith(Arquillian.class)
public class ResourcesTest {

    public static final String WEBAPP_SRC = "src/main/webapp";

    @Deployment
    public static WebArchive deployment() throws IllegalArgumentException, FileNotFoundException {
        return new DefaultDeployment().withPersistence().withFaces().getArchive()
                .addClasses(Resources.class, FacesContextStub.class);
    }

    @Inject
    Instance<FacesContext> facesContextInstance;

    @Inject
    Instance<Logger> loggerInstance;

    @Test
    public void facesContext_should_be_provided_from_current_context() {
        FacesContextStub.setCurrentInstance(new FacesContextStub("stub"));

        FacesContext facesContext = facesContextInstance.get();
        assertNotNull(facesContext);
        assertTrue(facesContext instanceof FacesContextStub);

        FacesContextStub.setCurrentInstance(null);

        facesContext = facesContextInstance.get();
        assertNull(facesContext);
    }

    @Test
    public void logger_should_be_provided_and_be_able_to_log_information_message() {
        Logger logger = loggerInstance.get();
        assertNotNull(logger);
        assertTrue(logger instanceof Logger);
        logger.log(Level.INFO, "test message");
    }

}
