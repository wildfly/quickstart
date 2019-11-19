package org.wildfly.quickstarts.microprofile.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
