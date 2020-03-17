package org.wildfly.quickstarts.microprofile.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.wildfly.quickstarts.microprofile.config.converter.type.MicroProfileCustomValue;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/converter")
public class ConverterResource {

    @Inject
    @ConfigProperty(name = "custom.converter.prop")
    private MicroProfileCustomValue microProfileCustomValue;

    @GET
    @Path("/value")
    public String customConverterProp() {
        return microProfileCustomValue.getName();
    }
}
