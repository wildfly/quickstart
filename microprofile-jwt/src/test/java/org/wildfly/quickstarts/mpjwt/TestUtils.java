package org.wildfly.quickstarts.mpjwt;

public class TestUtils {
    static final String DEFAULT_SERVER_HOST = "http://localhost:8080/microprofile-jwt";

    static final String ROOT_PATH = "/Sample/";
    static final String HELLO_WORLD = "helloworld";


    static String getServerHost() {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        return serverHost;
    }
}
