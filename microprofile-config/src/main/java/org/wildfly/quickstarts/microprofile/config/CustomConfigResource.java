package org.wildfly.quickstarts.microprofile.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;
import jakarta.inject.Provider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/custom-config")
public class CustomConfigResource {

    @Inject
    @ConfigProperty(name = "custom.config.source.prop")
    private String customConfigSourceProp;

    @GET
    @Path("/value")
    public String getCustomConfigSourceProp() {
        return customConfigSourceProp;
    }

    @Inject
    @ConfigProperty(name = "custom.provided.prop", defaultValue = "default")
    private Provider<String> providedCustomProp;

    @GET
    @Path("/reloaded-value")
    public String providedCustomProp() {
        return providedCustomProp.get();
    }
}
