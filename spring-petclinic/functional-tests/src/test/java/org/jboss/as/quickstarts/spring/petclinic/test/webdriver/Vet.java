/**
 *
 */
package org.jboss.as.quickstarts.spring.petclinic.test.webdriver;

/**
 * @author mjobanek
 *
 */
public class Vet {

    private String firstName;

    private String lastName;

    private String[] specialties;

    /**
     * @param firstName
     * @param lastName
     * @param specialties
     */
    public Vet(String firstName, String lastName, String[] specialties) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialties = specialties;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the specialties
     */
    public String[] getSpecialties() {
        return specialties;
    }

    /**
     * @param specialties the specialties to set
     */
    public void setSpecialties(String[] specialties) {
        this.specialties = specialties;
    }

}
