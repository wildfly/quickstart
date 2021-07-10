package org.wildfly.quickstarts.microprofile.config;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RootResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public String getHomePage() {
        return "<html>\n" +
                "<body>\n" +
                "<h1>MicroProfile Config Quickstart</h1>\n" +
                "\n" +
                "<h2>List of endpoints</h2>\n" +
                "<ul>\n" +
                "    <li><a href=\"/config/value\">/config/value</a></li>\n" +
                "    <li><a href=\"/config/required\">/config/required</a></li>\n" +
                "    <li><a href=\"/config/optional\">/config/optional</a></li>\n" +
                "    <li><a href=\"/config/all-props\">/config/all-props</a></li>\n" +
                "    <li><a href=\"/converter/value\">/converter/value</a></li>\n" +
                "    <li><a href=\"/custom-config/value\">/custom-config/value</a></li>\n" +
                "    <li><a href=\"/custom-config/reloaded-value\">/custom-config/reloaded-value</a></li>\n" +
                "</ul>\n" +
                "</body>\n" +
                "</html>\n";
    }
}
