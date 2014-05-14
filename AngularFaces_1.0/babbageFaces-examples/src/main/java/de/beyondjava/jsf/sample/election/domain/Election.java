package de.beyondjava.jsf.sample.election.domain;

import java.util.*;

public class Election {

    private Calendar date;

    private Map<Party, Double> result = new HashMap<>();

    /**
     * @return the date
     */
    public Calendar getDate() {
        return this.date;
    }

    /**
     * @return the result
     */
    public Map<Party, Double> getResult() {
        return this.result;
    }

    /**
     * @return the year
     */
    public int getYear() {
        if (null == date) {
            return 0;
        }
        return date.get(Calendar.YEAR);
    }

    /**
     * @param date
     *            the date to set
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(Map<Party, Double> result) {
        this.result = result;
    }

}
