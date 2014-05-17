package de.beyondjava.jsf.sample.election.domain;

import java.io.Serializable;
import java.util.*;

public class Election implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int year;

    private Map<Party, Double> result = new HashMap<>();
    
    private List<String> parties;
    
    private List<Double> results;

    /**
     * @return the date
     */
    public int getYear() {
        return this.year;
    }

    /**
     * @return the result
     */
    public Map<Party, Double> getResult() {
        return this.result;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(Map<Party, Double> result) {
        this.result = result;
    }

	public List<String> getParties() {
		return parties;
	}

	public void setParties(List<String> parties) {
		this.parties = parties;
	}

	public List<Double> getResults() {
		return results;
	}

	public void setResults(List<Double> results) {
		this.results = results;
	}

	public void setYear(int year) {
		this.year = year;
		
	}
}
