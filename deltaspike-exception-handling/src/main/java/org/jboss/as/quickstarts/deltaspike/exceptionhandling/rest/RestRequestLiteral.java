package org.jboss.as.quickstarts.deltaspike.exceptionhandling.rest;

import javax.enterprise.util.AnnotationLiteral;

public class RestRequestLiteral extends AnnotationLiteral<RestRequest> implements RestRequest {

    private static final long serialVersionUID = -6229036106752634991L;

    public static final RestRequest INSTANCE = new RestRequestLiteral();

    private RestRequestLiteral() {
    }

}
