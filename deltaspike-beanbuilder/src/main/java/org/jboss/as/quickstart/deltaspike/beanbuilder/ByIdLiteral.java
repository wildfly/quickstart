package org.jboss.as.quickstart.deltaspike.beanbuilder;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

import org.apache.deltaspike.core.util.bean.BeanBuilder;

/**
 * This class represents the {@link ById} annotation with its value. It is used by the {@link BeanBuilder} to set the
 * {@link Bean} {@link Qualifier}
 * 
 */
public class ByIdLiteral extends AnnotationLiteral<ById> implements ById {

    private static final long serialVersionUID = -8702546749474134989L;
    
    private final String value;

    public ByIdLiteral(String v) {
        this.value = v;
    }

    @Override
    public String value() {
        return value;
    }

}