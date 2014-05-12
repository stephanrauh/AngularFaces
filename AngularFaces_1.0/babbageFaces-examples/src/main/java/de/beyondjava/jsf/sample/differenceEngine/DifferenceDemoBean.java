/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsf.sample.differenceEngine;

import java.io.Serializable;

import javax.faces.bean.*;
import javax.faces.event.ValueChangeEvent;
import javax.validation.constraints.Size;

/**
 * This is a simple demo showing a couple of data fields and check boxes. The checkboxes control the visibility of the
 * field groups. There is also a button that replaces the content of the field "city" by a random Rheinhessian village
 * name.
 */
@ManagedBean
@SessionScoped
public class DifferenceDemoBean implements Serializable {
    private static final long serialVersionUID = 7753826496926409414L;

    private String city = "";
    @Size(max = 30)
    private String firstName = "John";

    private boolean firstSectionVisible = true;
    @Size(max = 30)
    private String lastName = "Doe";
    private boolean secondSectionVisible = true;

    private String street;
    private boolean thirdSectionVisible = true;
    private String zipcode;

    public String changeCity() {
        String[] cities = new String[] { "Armsheim", "Biebelsheim", "Crumstadt", "Dolgesheim", "Ebersheim",
                "Framersheim", "Gundersheim", "Hahnheim", "Immesheim", "Jugenheim", "Köngernheim", "Laubenheim",
                "Monsheim", "Oppenheim", "Quirnheim", "Pfeddersheim", "Rommersheim", "Spiesheim",
                "Tauber-Bischofsheim", "Undenheim", "Vendersheim", "Weinolsheim", "Zornheim" };
        long index = (long) Math.floor(Math.random() * cities.length);
        city = cities[(int) index];
        return null;
    }

    public String doNothing() {
        return null;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @return the zipcode
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * @return the firstSectionVisible
     */
    public boolean isFirstSectionVisible() {
        return firstSectionVisible;
    }

    /**
     * @return the secondSectionVisible
     */
    public boolean isSecondSectionVisible() {
        return secondSectionVisible;
    }

    /**
     * @return the thirdSectionVisible
     */
    public boolean isThirdSectionVisible() {
        return thirdSectionVisible;
    }

    /**
     * @param city
     *            the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @param firstSectionVisible
     *            the firstSectionVisible to set
     */
    public void setFirstSectionVisible(boolean firstSectionVisible) {
        this.firstSectionVisible = firstSectionVisible;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param secondSectionVisible
     *            the secondSectionVisible to set
     */
    public void setSecondSectionVisible(boolean secondSectionVisible) {
        this.secondSectionVisible = secondSectionVisible;
    }

    /**
     * @param street
     *            the street to set
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @param thirdSectionVisible
     *            the thirdSectionVisible to set
     */
    public void setThirdSectionVisible(boolean thirdSectionVisible) {
        this.thirdSectionVisible = thirdSectionVisible;
    }

    /**
     * @param zipcode
     *            the zipcode to set
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String showErrors() {
        return "ok";
    }

    public void toggleFirstAndThirdSection(javax.faces.event.AjaxBehaviorEvent event) {
        firstSectionVisible = !firstSectionVisible;
        thirdSectionVisible = !thirdSectionVisible;
    }

    public void toggleFirstAndThirdSectionPrime() {
        thirdSectionVisible = !thirdSectionVisible;
        firstSectionVisible = !firstSectionVisible;

    }

    /** Called by the Mojarra example when the first check box is clicked. */
    public void toggleFirstSection(javax.faces.event.AjaxBehaviorEvent event) {
        firstSectionVisible = !firstSectionVisible;
    }

    public void toggleFirstSectionPrime() {
        firstSectionVisible = !firstSectionVisible;
    }

    public void toggleSecondSectionAndChangeCity() {
        secondSectionVisible = !secondSectionVisible;
        changeCity();
    }

    /** Called by the Mojarra example when the second check box is clicked. */
    public void toggleSecondSectionAndChangeCity(javax.faces.event.AjaxBehaviorEvent event) {
        secondSectionVisible = !secondSectionVisible;
        changeCity();
    }

    /** Called by the Mojarra example when the third check box is clicked. */
    public void toggleThirdSection(javax.faces.event.AjaxBehaviorEvent event) {
        thirdSectionVisible = !thirdSectionVisible;
    }

    public void valueChangeListener(ValueChangeEvent event) {

    }

}
