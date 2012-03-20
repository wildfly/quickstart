package org.jboss.as.quickstarts.tasks;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;

public class DefaultDeployment {

    public static WebArchive deployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                 .addPackages(true, Task.class.getPackage().getName())
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource("test-ds.xml", "test-ds.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("import.sql");
    }

}
