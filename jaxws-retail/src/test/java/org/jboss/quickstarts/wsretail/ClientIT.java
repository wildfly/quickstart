package org.jboss.quickstarts.wsretail;

import jakarta.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.xml.namespace.QName;

import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.Customer;
import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.DiscountRequest;
import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.DiscountResponse;
import org.jboss.quickstarts.ws.jaxws.samples.retail.profile.ProfileMgmt;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;

public class ClientIT {
    /**
     * The path of the WSDL endpoint in relation to the deployed web application.
     */
    private static final String WSDL_PATH = "ProfileMgmtService/ProfileMgmt?wsdl";

    protected static URL getHTTPEndpoint() {
        String host = getServerHost();
        if (host == null) {
            host = "http://localhost:8080";
        }
        try {
            return new URI(host + "/" + WSDL_PATH).toURL();
        } catch (URISyntaxException | MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static String getServerHost() {
        String host = System.getenv("SERVER_HOST");
        if (host == null) {
            host = System.getProperty("server.host");
        }
        return host;
    }
    private static URL deploymentUrl;

    private ProfileMgmt client;

    @BeforeClass
    public static void beforeClass() throws MalformedURLException {
        URL deploymentUrl = getHTTPEndpoint();
        // Set the deployment url
        ClientIT.deploymentUrl = deploymentUrl;
    }

    @Before
    public void setup() {
        QName serviceName = new QName("http://org.jboss.ws/samples/retail/profile", "ProfileMgmtService");
        Service service = Service.create(deploymentUrl, serviceName);
        client = service.getPort(ProfileMgmt.class);
    }

    @Test
    public void testRetail() {
        Customer customer = new Customer();
        customer.setFirstName("Jay");
        customer.setLastName("Boss");
        customer.setCreditCardDetails("newbie");
        DiscountRequest dRequest = new DiscountRequest();
        dRequest.setCustomer(customer);

        DiscountResponse dResponse = client.getCustomerDiscount(dRequest);
        Customer responseCustomer = dResponse.getCustomer();
        assertEquals(responseCustomer.getFirstName(), "Jay");
        System.out.format("%s %s\'s discount is %.2f%n", responseCustomer.getFirstName(),
                responseCustomer.getLastName(), dResponse.getDiscount());
    }
}