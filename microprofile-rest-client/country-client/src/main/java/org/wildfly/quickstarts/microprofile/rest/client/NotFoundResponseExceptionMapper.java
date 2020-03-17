package org.wildfly.quickstarts.microprofile.rest.client;

import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

public class NotFoundResponseExceptionMapper implements ResponseExceptionMapper<NotFoundException> {

    @Override
    public boolean handles(int status, MultivaluedMap<String, Object> headers) {
        return status == 404;
    }

    @Override
    public NotFoundException toThrowable(Response response) {
        return new NotFoundException(response.hasEntity() ? response.readEntity(String.class) : "Resource not found");
    }
}
