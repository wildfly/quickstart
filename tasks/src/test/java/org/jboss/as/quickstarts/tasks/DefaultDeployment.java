package org.jboss.as.quickstarts.tasks;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

public class DefaultDeployment {

    public static JavaArchive deployment() {
        return ShrinkWrap.create(JavaArchive.class, "test.jar").addPackages(true, "org/jboss/as/quickstarts/tasks")
                .addAsResource("persistence.xml", "META-INF/persistence.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml").addAsResource("import.sql");
    }

}
