package org.jboss.as.quickstarts.kitchensink.util;

import javax.annotation.sql.DataSourceDefinition;
import javax.ejb.Stateless;


@DataSourceDefinition (
	name = "java:app/env/PrimaryDataSource",
	className = "org.h2.Driver",
	url = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
	user = "sa",
	password = "sa"
)
@Stateless
public class DataSourceDefinitions {    
	
}
