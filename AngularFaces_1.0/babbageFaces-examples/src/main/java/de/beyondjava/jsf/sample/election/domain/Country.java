package de.beyondjava.jsf.sample.election.domain;

import java.util.*;

public class Country {
    private List<Election> elections = new ArrayList<>();
    private String name;
    private List<Party> parties;

    /**
     * @return the elections
     */
    public List<Election> getElections() {
        return this.elections;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the parties
     */
    public List<Party> getParties() {
        return this.parties;
    }

    /**
     * @param elections
     *            the elections to set
     */
    public void setElections(List<Election> elections) {
        this.elections = elections;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param parties
     *            the parties to set
     */
    public void setParties(List<Party> parties) {
        this.parties = parties;
    }

}
