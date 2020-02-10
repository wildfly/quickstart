package org.wildfly.quickstarts.microprofile.rest.client.model;

public class Country {

    public String name;
    public String capital;
    public String currency;

    public Country() {
    }

    public Country(String name, String capital, String currency) {
        this.name = name;
        this.capital = capital;
        this.currency = currency;
    }
}
