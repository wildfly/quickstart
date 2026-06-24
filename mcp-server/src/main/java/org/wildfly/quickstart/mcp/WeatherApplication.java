package org.wildfly.quickstart.mcp;

import jakarta.json.Json;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.ext.ReaderInterceptor;
import jakarta.ws.rs.ext.ReaderInterceptorContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@ApplicationPath("/")
public class WeatherApplication extends Application {

    /**
     * Interceptor to handle "application/geo+json" content as a {@code JsonObject}.
     */
    @Provider
    public static class GeoJsonReaderInterceptor implements ReaderInterceptor {
        public static final MediaType APPLICATION_GEO_JSON_TYPE = new MediaType("application", "geo+json");
        @Override
        public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException {
            if (context.getMediaType().isCompatible(APPLICATION_GEO_JSON_TYPE)) {
                InputStreamReader reader = new InputStreamReader(context.getInputStream(), StandardCharsets.UTF_8);
                return Json.createReader(reader).readObject();
            }
            return context.proceed();
        }
    }
}