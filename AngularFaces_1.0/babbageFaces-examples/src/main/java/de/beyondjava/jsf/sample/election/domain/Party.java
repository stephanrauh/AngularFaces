package de.beyondjava.jsf.sample.election.domain;

import java.io.Serializable;

public class Party implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String color;

    private String name;

    private int yearOfEstablishment;

    /**
     * @return the color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the yearOfEstablishment
     */
    public int getYearOfEstablishment() {
        return this.yearOfEstablishment;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param yearOfEstablishment
     *            the yearOfEstablishment to set
     */
    public void setYearOfEstablishment(int yearOfEstablishment) {
        this.yearOfEstablishment = yearOfEstablishment;
    }

}
