package de.beyondjava.jsf.sample.election.domain;

import java.io.Serializable;
import java.util.*;

public class Election implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private List<String> parties;

    private Map<Party, Double> result = new HashMap<>();

    private List<Double> results;

    private int year;

    public List<String> getParties() {
        return parties;
    }

    /**
     * @return the result
     */
    public Map<Party, Double> getResult() {
        return this.result;
    }

    public double getResult(Long index) {
        return results.get(index.intValue());
    }

    public List<Double> getResults() {
        return results;
    }

    /**
     * @return the date
     */
    public int getYear() {
        return this.year;
    }

    public void setParties(List<String> parties) {
        this.parties = parties;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(Map<Party, Double> result) {
        this.result = result;
    }

    public void setResults(List<Double> results) {
        this.results = results;
    }

    public void setYear(int year) {
        this.year = year;

    }
}
