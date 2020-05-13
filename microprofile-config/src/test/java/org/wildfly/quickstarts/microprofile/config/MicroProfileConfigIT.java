package org.wildfly.quickstarts.microprofile.config;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.wildfly.quickstarts.microprofile.config.custom.CustomPropertiesFileProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple tests for MicroProfile Config quickstart. Arquillian deploys an WAR archive to the application server, which
 * contains several endpoints exposing injected configuration values and verifies that they are correctly invoked.
 *
 * @author <a href="mstefank@redhat.com">Martin Stefanko</a>
 *
 */
@RunWith(Arquillian.class)
@RunAsClient
public class MicroProfileConfigIT {

    @ArquillianResource
    private URL deploymentURL;

    private Client client;

    @Before
    public void before() throws MalformedURLException {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() {
        if (client != null) {
            client.close();
        }
    }

    /**
     * Constructs a deployment archive
     *
     * @return the deployment archive
     */
    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
            .addPackages(true, ConfigResource.class.getPackage())
            .addAsResource("META-INF/microprofile-config.properties")
            .addAsResource("META-INF/services/org.eclipse.microprofile.config.spi.ConfigSource")
            .addAsResource("META-INF/services/org.eclipse.microprofile.config.spi.ConfigSourceProvider")
            .addAsResource("META-INF/services/org.eclipse.microprofile.config.spi.Converter")
            // enable CDI
            .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Tests that /config/value returns the value configured in microprofile-config.properties
     */
    @Test
    public void testConfigPropertiesValue() {
        Response response =  client
            .target(deploymentURL.toString())
            .path("/config/value")
            .request()
            .get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("MyPropertyFileConfigValue", response.readEntity(String.class));

        response.close();
    }

    /**
     * Tests that /config/required returns the default value from the `ConfigProperty` annotations
     */
    @Test
    public void testDefaultValue() {
        Response response =  client
            .target(deploymentURL.toString())
            .path("/config/required")
            .request()
            .get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("Default required prop value", response.readEntity(String.class));

        response.close();
    }

    /**
     * Tests that /config/optional returns the orElse value hard coded in the resource
     */
    @Test
    public void testOptionalValue() {
        Response response =  client
            .target(deploymentURL.toString())
            .path("/config/optional")
            .request()
            .get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("no optional value provided, use this as the default", response.readEntity(String.class));

        response.close();
    }

    /**
     * Tests that /config/all-props retuns an array containins string `config.prop` (as it is a configured property)
     */
    @Test
    public void testConfigObjectAllProps() {
        Response response =  client
            .target(deploymentURL.toString())
            .path("/config/all-props")
            .request()
            .get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertTrue(response.readEntity(String.class).contains("config.prop"));

        response.close();
    }

    /**
     * Tests that /custom-config/value returns the value from the custom config source deployed with the application
     */
    @Test
    public void testConfigSourceValue() {
        Response response =  client
            .target(deploymentURL.toString())
            .path("/custom-config/value")
            .request()
            .get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("MyCustomValue", response.readEntity(String.class));

        response.close();
    }

    /**
     * Tests that /custom-config/reloaded-value returns:
     * - 200 on first invocation and the default value `default` is returned (custom.properties file does not exist)
     * - create `custom.properties` file in the `JBOSS_HOME` directory with the content
     * `custom.provided.prop=FileSystemCustomConfigValue` and verify that /custom-config/reloaded-value returns
     * `FileSystemCustomConfigValue`
     * - change the value of `custom.provided.prop` in the `custom.properties` file and verify that it is returned
     * from /custom-config/reloaded-value
     */
    @Test
    public void testConfigSourceReloadedValue() throws IOException {
        Path customPropertiesPath = Paths.get(CustomPropertiesFileProvider.getJBossHome() + "/custom.properties");
        Files.deleteIfExists(customPropertiesPath);

        WebTarget target = client
            .target(deploymentURL.toString())
            .path("/custom-config/reloaded-value");

        Response response =  target.request().get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("default", response.readEntity(String.class));
        response.close();

        Files.write(customPropertiesPath, "custom.provided.prop=FileSystemCustomConfigValue".getBytes());

        response = target.request().get();
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("FileSystemCustomConfigValue", response.readEntity(String.class));
        response.close();

        Files.write(customPropertiesPath, "custom.provided.prop=ChangedValue".getBytes());

        response = target.request().get();
        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("ChangedValue", response.readEntity(String.class));
        response.close();

        Files.delete(customPropertiesPath);
    }

    /**
     * Tests that /converter/value returns the value from the custom converted object
     */
    @Test
    public void testConverterValue() {
        Response response =  client
            .target(deploymentURL.toString())
            .path("/converter/value")
            .request()
            .get();

        Assert.assertEquals(200, response.getStatus());
        Assert.assertEquals("MyCustomConverterValue", response.readEntity(String.class));

        response.close();
    }
}
