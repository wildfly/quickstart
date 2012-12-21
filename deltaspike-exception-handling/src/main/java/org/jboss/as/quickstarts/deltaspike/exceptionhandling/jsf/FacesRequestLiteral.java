package org.jboss.as.quickstarts.deltaspike.exceptionhandling.jsf;

import javax.enterprise.util.AnnotationLiteral;

public class FacesRequestLiteral extends AnnotationLiteral<FacesRequest> implements FacesRequest {

    private static final long serialVersionUID = -5782459184825622680L;

    public static final FacesRequest INSTANCE = new FacesRequestLiteral();

    private FacesRequestLiteral() {
    }

}
