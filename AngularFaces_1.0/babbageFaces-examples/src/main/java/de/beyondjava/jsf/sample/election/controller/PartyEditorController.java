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
import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import de.beyondjava.jsf.sample.election.domain.*;

@ManagedBean
@ViewScoped
public class PartyEditorController implements Serializable {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.sample.election.controller.PartyEditorController");
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{electionController.countries}")
    private List<Country> countries;
    private Party editedParty;

    @ManagedProperty("#{electionController}")
    private ElectionController electionController;

    private List<Party> parties;

    private Country selectedCountry;

    private Party selectedParty;

    public PartyEditorController() {
    }

    public void cancelParty() {
        selectedParty = null;
        setEditedParty(null);
    }

    public void editPartiesAction(Country country) {
        getElectionController().editPartiesAction();
        setSelectedCountry(country);
        selectCountry();
    }

    public void editParty(Party selectedParty) {
        this.selectedParty = selectedParty;
        setEditedParty(new Party());
        getEditedParty().setColor(selectedParty.getColor());
        getEditedParty().setName(selectedParty.getName());
        getEditedParty().setYearOfEstablishment(selectedParty.getYearOfEstablishment());
    }

    /**
     * @return the countries
     */
    public List<Country> getCountries() {
        return this.countries;
    }

    /**
     * @return the editedParty
     */
    public Party getEditedParty() {
        return editedParty;
    }

    public ElectionController getElectionController() {
        return electionController;
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

    public String partyTableVisible() {
        return selectedCountry == null ? "display:none" : "display:block";
    }

    public void saveParty() {
        selectedParty.setColor(getEditedParty().getColor());
        selectedParty.setName(getEditedParty().getName());
        selectedParty.setYearOfEstablishment(getEditedParty().getYearOfEstablishment());
        selectedParty = null;
        setEditedParty(null);
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Your input has been saved.");
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void selectCountry() {
        if (null == selectedCountry) {
            parties = null;
            selectedParty = null;
        }
        else {
            parties = selectedCountry.getParties();
        }
    }

    /**
     * @param countries
     *            the countries to set
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    /**
     * @param editedParty
     *            the editedParty to set
     */
    public void setEditedParty(Party editedParty) {
        this.editedParty = editedParty;
    }

    public void setElectionController(ElectionController electionController) {
        this.electionController = electionController;
    }

    /**
     * @param selectedCountry
     *            the selectedCountry to set
     */
    public void setSelectedCountry(Country selectedCountry) {
        if (this.selectedCountry != selectedCountry) {
            selectedParty = null;
        }
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
