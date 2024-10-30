package org.wildfly.quickstarts.microprofile.reactive.messaging.test;

public class TestUtils {
    static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    static String getServerHost() {
        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
        return serverHost+"/microprofile-reactive-messaging-kafka";
    }
}
