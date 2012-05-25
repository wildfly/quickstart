package org.jboss.as.quickstarts.cluster.hasingleton.service.ejb;

public interface Scheduler {

    public abstract void initialize(String info);

    public abstract void stop();

}