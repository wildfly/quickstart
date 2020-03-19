package org.wildfly.quickstarts.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.Optional;

@Path("/config")
@ApplicationScoped
public class ConfigResource {

    @Inject
    @ConfigProperty(name = "config.prop")
    private String configValue;

    @GET
    @Path("/value")
    public String getValue() {
        return configValue;
    }

    @Inject
    @ConfigProperty(name = "required.prop", defaultValue = "Default required prop value")
    private String requiredProp;

    @GET
    @Path("/required")
    public String getRequiredProp() {
        return requiredProp;
    }

    @Inject
    @ConfigProperty(name = "optional.prop")
    private Optional<String> optionalString;

    @GET
    @Path("/optional")
    public String getOptionalValue() {
        return optionalString.orElse("no optional value provided, use this as the default");
    }

    @Inject
    private Config config;

    @GET
    @Path("/all-props")
    public String getConfigPropertyNames() {
        return config.getPropertyNames().toString();
    }
}
