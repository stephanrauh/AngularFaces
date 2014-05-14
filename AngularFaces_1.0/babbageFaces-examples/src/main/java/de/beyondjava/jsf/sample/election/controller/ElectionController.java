package de.beyondjava.jsf.sample.election.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.*;

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

    private void hideEveryDialog() {
        countryEditorVisible = false;
        electionEditorVisible = false;
        electionHistoryChartVisible = false;
        partyEditorVisible = false;
        singleElectionChartVisible = false;
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
