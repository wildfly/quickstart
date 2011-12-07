/**
 * 
 */
package org.jboss.as.quickstarts.jaxrsClient;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.http.client.ClientProtocolException;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import javax.ws.rs.core.MediaType;

/**
 * @author bmincey
 * 
 */
public class JaxRsClient
{

    private static final String XML_URL = "http://localhost:8080/rs-helloworld/xml";
    private static final String JSON_URL = "http://localhost:8080/rs-helloworld/json";

    /**
     * 
     */
    public JaxRsClient()
    {
        this.runRequest(JaxRsClient.XML_URL, MediaType.APPLICATION_XML_TYPE);
        this.runRequest(JaxRsClient.JSON_URL, MediaType.APPLICATION_JSON_TYPE);
    }

    /**
     * 
     * @param url
     * @param mediaType
     */
    private void runRequest(String url, MediaType mediaType)
    {
        System.out.println("===============================================");
        System.out.println("URL: " + url);
        System.out.println("MediaType: " + mediaType.toString());
        
        try
        {
            ClientRequest request = new ClientRequest(url);
            request.accept(mediaType);

            ClientResponse<String> response = request.get(String.class);

            if (response.getStatus() != 200)
            {
                throw new RuntimeException("Failed request with HTTP status: "
                        + response.getStatus());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new ByteArrayInputStream(response.getEntity().getBytes())));

            String output;
            System.out.println("\n*** Response from Server ***\n");
            while ((output = br.readLine()) != null)
            {
                System.out.println(output);
            }
        }

        catch (ClientProtocolException cpe)
        {
            System.err.println(cpe);
        }
        catch (IOException ioe)
        {
            System.err.println(ioe);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        
        System.out.println("\n===============================================");
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        new JaxRsClient();
    }
}
