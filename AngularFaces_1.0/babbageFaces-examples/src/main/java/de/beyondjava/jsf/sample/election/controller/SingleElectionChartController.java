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
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.*;

import org.primefaces.model.chart.*;

import de.beyondjava.jsf.sample.election.domain.*;

@ManagedBean
@ViewScoped
public class SingleElectionChartController implements Serializable {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.sample.election.controller.SingleElectionChartController");
    private static final long serialVersionUID = 1L;

    private ChartModel chart;

    @ManagedProperty("#{electionController.countries}")
    private List<Country> countries;

    @ManagedProperty("#{electionController}")
    private ElectionController electionController;

    private List<Party> parties;

    private Country selectedCountry;

    private Election selectedElection;

    public SingleElectionChartController() {
    }

    /**
     * @return the chart
     */
    public ChartModel getChart() {
        return chart;
    }

    /**
     * @return the countries
     */
    public List<Country> getCountries() {
        return this.countries;
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
     * @return the selectedElection
     */
    public Election getSelectedElection() {
        return selectedElection;
    }

    @PostConstruct
    public void init() {
    }

    public String partyTableVisible() {
        return selectedCountry == null ? "display:none" : "display:block";
    }

    public void selectCountry() {
        if (null == selectedCountry) {
            setSelectedElection(null);
        }
        else {
        }
    }

    public void selectYear() {
        if (null != selectedElection) {
            Map<String, Number> data = new HashMap<>();
            for (Entry<Party, Double> e : selectedElection.getResult().entrySet()) {
                data.put(e.getKey().getName(), e.getValue());
            }

            chart = new PieChartModel(data);
            chart.setLegendPosition("ne");
        }
        else {
            chart = null;
        }

    }

    /**
     * @param countries
     *            the countries to set
     */
    public void setCountries(List<Country> countries) {
        this.countries = countries;
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
            setSelectedElection(null);
        }
        this.selectedCountry = selectedCountry;
    }

    /**
     * @param selectedElection
     *            the selectedElection to set
     */
    public void setSelectedElection(Election selectedElection) {
        this.selectedElection = selectedElection;
    }

    public void showElection(Election election) {
        electionController.singleElectionChartAction();
        for (Country country : countries) {
            for (Election e : country.getElections()) {
                if (e == election) {
                    setSelectedCountry(country);
                    setSelectedElection(election);
                    selectYear();
                }
            }
        }
    }
}
