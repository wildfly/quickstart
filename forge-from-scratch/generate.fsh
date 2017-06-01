# Example Forge Script - Generates a Point-of-sale Style Application;
# NOTE: This script is compatible with Forge 3.x only and will not run in Forge 2.x

# Clear the screen;
clear;

# Create a new project in the current directory;
project-new --named forge-example --top-level-package org.example;

# Setup JPA in our project;
jpa-setup --provider Hibernate --container JBOSS_EAP7;

# Create some JPA @Entities on which to base our application;
jpa-new-entity --named Customer --target-package org.example.domain;
jpa-new-field --named firstName;
jpa-new-field --named lastName;
jpa-new-field --named birthDate --type java.util.Date;
jpa-new-entity --named Item;
jpa-new-field --type String --named name;
jpa-new-field --named price --type java.lang.Double;
jpa-new-field --named stock;
cd ..;

# Create more entities, also add a relationship between Customer and their Orders;
jpa-new-entity --named ProductOrder;
jpa-new-field --named customer --type org.example.domain.Customer --relationship-type Many-to-One --inverse-field-name orders;
cd ../Customer.java;
jpa-new-entity --named Profile;
jpa-new-field --type String --named bio;
jpa-new-field --type String --named preferredName;
jpa-new-field --type String --named notes;
jpa-new-entity --named Address;
jpa-new-field --type String --named street;
jpa-new-field --type String --named city;
jpa-new-entity --named ZipCode;
jpa-new-field --type int --named code;
cd ../Address.java;

# Add more relationships between our @Entities;
jpa-new-field --named zip --type org.example.domain.ZipCode --relationship-type Many-to-One;
cd ../Customer.java;
jpa-new-field --named addresses --type org.example.domain.Address --relationship-type Many-to-Many;
jpa-new-field --named profile --type org.example.domain.Profile --relationship-type Many-to-One;
cd ../ProductOrder.java;
jpa-new-field  --named items --type org.example.domain.Item --relationship-type Many-to-Many;
jpa-new-field  --named shippingAddress --type org.example.domain.Address --relationship-type Many-to-One;
cd ..;

#Turn our Java project into a Web project with JSF, CDI, EJB, and JPA;
scaffold-setup --provider Faces --faces-version 2.1;

# Enable CDI if not already done;
cdi-setup;

# Generate the UI for all of our @Entities at once;
scaffold-generate --provider Faces --overwrite --targets org.example.domain.*;
cd ~~;

# Setup JAX-RS, and create CRUD endpoints;
rest-setup;
rest-generate-endpoints-from-entities --targets org.example.domain.*


# Build the project and disable ACCEPT_DEFAULTS;
build;

# Return to the project root directory and leave it in your hands;
cd ~~;
