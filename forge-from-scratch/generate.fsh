@/* Example Forge Script - Generates a Point-of-sale Style Application */;

@/* Clear the screen */;
clear;

@/* Create a new project in the current directory */;
new-project;

@/* This means less typing. If a script is automated, or is not meant to be interactive, use this command */;
set ACCEPT_DEFAULTS true;

@/* Turn our Java project into a Web project with JSF, CDI, EJB, and JPA  */;
scaffold setup --scaffoldType faces;
persistence setup --provider HIBERNATE --container JBOSS_AS7 ;

@/* Create some JPA @Entities on which to base our application */;
entity --named Customer --package ~.domain;
field string --named firstName;
field string --named lastName;
field temporal --type DATE --named birthDate;
entity --named Item;
field string --named name;
field number --named price --type java.lang.Double;
field int --named stock;
cd ..;

@/* Create more entities, also add a relationship between Customer and their Orders */;
entity --named ProductOrder;
field manyToOne --named customer --fieldType ~.domain.Customer.java --inverseFieldName orders;
cd ../Customer.java;
entity --named Profile;
field string --named bio;
field string --named preferredName;
field string --named notes;
entity --named Address;
field string --named street;
field string --named city;
entity --named ZipCode;
field int --named code;
cd ../Address.java;

@/* Add more relationships between our @Entities */;
field manyToOne --named zip --fieldType ~.domain.ZipCode.java;
cd ..;
cd Customer.java;
field manyToMany --named addresses --fieldType ~.domain.Address.java;
cd ..;
cd Address.java;
cd ../Customer.java;
field oneToOne --named profile --fieldType ~.domain.Profile.java;
cd ..;
cd ProductOrder.java;
field manyToMany --named items --fieldType ~.domain.Item.java;
cd ..;
cd ProductOrder.java;
field manyToOne --named shippingAddress --fieldType ~.domain.Address.java;
cd ..;

@/* Generate the UI for all of our @Entities at once */;
scaffold from-entity ~.domain.* --scaffoldType faces --overwrite;
cd ~~;

@/* Setup JAX-RS, and create CRUD endpoints */;
rest setup;
rest endpoint-from-entity ~.domain.*;

@/* Build the project and disable ACCEPT_DEFAULTS */;
build;
set ACCEPT_DEFAULTS false;

@/* Return to the project root directory and leave it in your hands */;
cd ~~;
