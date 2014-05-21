package de.beyondjava.jsf.sample.election.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;

import de.beyondjava.jsf.sample.election.domain.Country;
import de.beyondjava.jsf.sample.election.services.DBPopulator;

@ManagedBean
@SessionScoped
public class ElectionController implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Country> countries;

    boolean countryEditorVisible = false;
    boolean electionEditorVisible = false;
    boolean electionHistoryChartVisible = false;
    private boolean missingTDVisible = false;
    boolean partyEditorVisible = false;

    boolean singleElectionChartVisible = false;

    public ElectionController() {
        DBPopulator.populateWorld(this);
    }

    public void editCountriesAction() {
        hideEveryDialog();
        countryEditorVisible = true;
    }

    public void editElectionsAction() {
        hideEveryDialog();
        electionEditorVisible = true;
    }

    public void editPartiesAction() {
        hideEveryDialog();
        partyEditorVisible = true;
    }

    public void electionHistoryChartAction() {
        hideEveryDialog();
        electionHistoryChartVisible = true;
    }

    /**
     * @return the countries
     */
    public List<Country> getCountries() {
        return this.countries;
    }

    public String getTitle() {
        if (countryEditorVisible) {
            return "Edit countries";
        }
        else if (electionEditorVisible) {
            return "Edit elections";
        }
        else if (electionHistoryChartVisible) {
            return "Historical election charts";
        }
        else if (partyEditorVisible) {
            return "Edit parties";
        }
        else if (singleElectionChartVisible) {
            return "Dive into the details of a single election";
        }
        else {
            return "BabbagesFaces election demo";
        }
    }

    private void hideEveryDialog() {
        countryEditorVisible = false;
        electionEditorVisible = false;
        electionHistoryChartVisible = false;
        partyEditorVisible = false;
        singleElectionChartVisible = false;
        setMissingTDVisible(false);
    }

    /**
     * @return the countryEditorVisible
     */
    public boolean isCountryEditorVisible() {
        return this.countryEditorVisible;
    }

    /**
     * @return the electionEditorVisible
     */
    public boolean isElectionEditorVisible() {
        return this.electionEditorVisible;
    }

    /**
     * @return the electionHistoryChartVisible
     */
    public boolean isElectionHistoryChartVisible() {
        return this.electionHistoryChartVisible;
    }

    /**
     * @return the missingTDVisible
     */
    public boolean isMissingTDVisible() {
        return missingTDVisible;
    }

    /**
     * @return the partyEditorVisible
     */
    public boolean isPartyEditorVisible() {
        return this.partyEditorVisible;
    }

    /**
     * @return the singleElectionChartVisible
     */
    public boolean isSingleElectionChartVisible() {
        return this.singleElectionChartVisible;
    }

    public void missingTDAction() {
        hideEveryDialog();
        missingTDVisible = true;
    }

    public void saveAction(Country country) {
        FacesMessage notImplementedMessage = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Saving a country's name",
                "This action hasn't been implemented yet.");
        FacesContext.getCurrentInstance().addMessage(null, notImplementedMessage);
    }

    /**
     * @param countries
     *            the countries to set
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    /**
     * @param countryEditorVisible
     *            the countryEditorVisible to set
     */
    public void setCountryEditorVisible(boolean countryEditorVisible) {
        this.countryEditorVisible = countryEditorVisible;
    }

    /**
     * @param electionEditorVisible
     *            the electionEditorVisible to set
     */
    public void setElectionEditorVisible(boolean electionEditorVisible) {
        this.electionEditorVisible = electionEditorVisible;
    }

    /**
     * @param electionHistoryChartVisible
     *            the electionHistoryChartVisible to set
     */
    public void setElectionHistoryChartVisible(boolean electionHistoryChartVisible) {
        this.electionHistoryChartVisible = electionHistoryChartVisible;
    }

    /**
     * @param missingTDVisible
     *            the missingTDVisible to set
     */
    public void setMissingTDVisible(boolean missingTDVisible) {
        this.missingTDVisible = missingTDVisible;
    }

    /**
     * @param partyEditorVisible
     *            the partyEditorVisible to set
     */
    public void setPartyEditorVisible(boolean partyEditorVisible) {
        this.partyEditorVisible = partyEditorVisible;
    }

    /**
     * @param singleElectionChartVisible
     *            the singleElectionChartVisible to set
     */
    public void setSingleElectionChartVisible(boolean singleElectionChartVisible) {
        this.singleElectionChartVisible = singleElectionChartVisible;
    }

    public void singleElectionChartAction() {
        hideEveryDialog();
        singleElectionChartVisible = true;
    }
}
