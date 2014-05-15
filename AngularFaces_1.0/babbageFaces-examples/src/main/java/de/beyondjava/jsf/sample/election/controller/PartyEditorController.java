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
package de.beyondjava.jsf.sample.election.controller;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import de.beyondjava.jsf.sample.election.domain.*;

@ManagedBean
@ViewScoped
public class PartyEditorController implements Serializable {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.sample.election.controller.PartyEditorController");
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{electionController.countries}")
    private List<Country> countries;

    private List<Party> parties;

    private Country selectedCountry;

    private Party selectedParty;

    public PartyEditorController() {
    }

    public void editParty(Party selectedParty) {
        this.selectedParty = selectedParty;
    }

    /**
     * @return the countries
     */
    public List<Country> getCountries() {
        return this.countries;
    }

    /**
     * @return the parties
     */
    public List<Party> getParties() {
        return this.parties;
    }

    /**
     * @return the selectedCountry
     */
    public Country getSelectedCountry() {
        return this.selectedCountry;
    }

    /**
     * @return the selectedParty
     */
    public Party getSelectedParty() {
        return this.selectedParty;
    }

    @PostConstruct
    public void init() {
    }

    public void selectCountry() {
        if (null == selectedCountry) {
            parties = null;
        }
        else {
            parties = selectedCountry.getParties();
        }
        // selectedParty = null;
    }

    /**
     * @param countries
     *            the countries to set
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    /**
     * @param selectedCountry
     *            the selectedCountry to set
     */
    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    /**
     * @param selectedParty
     *            the selectedParty to set
     */
    public void setSelectedParty(Party selectedParty) {
        this.selectedParty = selectedParty;
    }
}
