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

import org.primefaces.model.chart.*;

import de.beyondjava.jsf.sample.election.domain.*;

@ManagedBean
@SessionScoped
public class ElectionHistoryController implements Serializable {
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.sample.election.controller.ElectionHistoryController");
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{electionController.countries}")
    private List<Country> countries;
    private LineChartModel historicalLineChart;

    private Country selectedCountry;

    private void createLineModels() {
        historicalLineChart = initLinearModel();
        historicalLineChart.setTitle("Elections in " + selectedCountry.getName());
        historicalLineChart.setLegendPosition("nw");
        Axis yAxis = historicalLineChart.getAxis(AxisType.Y);
        yAxis.setLabel("Percent");
        yAxis.setMin(0);
        Axis xAxis = historicalLineChart.getAxis(AxisType.X);
        xAxis.setLabel("Year");

    }

    /**
     * @return the countries
     */
    public List<Country> getCountries() {
        return this.countries;
    }

    public LineChartModel getHistoricalLineChart() {
        return historicalLineChart;
    }

    /**
     * @return the selectedCountry
     */
    public Country getSelectedCountry() {
        return this.selectedCountry;
    }

    @PostConstruct
    public void init() {
        // selectedCountry=countries.get(1);
    }

    private LineChartModel initLinearModel() {
        List<Party> parties = selectedCountry.getParties();
        List<Election> elections = selectedCountry.getElections();
        LineChartModel model = new LineChartModel();
        for (int partyIndex = 0; partyIndex < parties.size(); partyIndex++) {
            Party party = parties.get(partyIndex);

            LineChartSeries currentLine = new LineChartSeries();
            currentLine.setLabel(party.getName());
            for (int year = 0; year < elections.size(); year++) {
                currentLine.set(elections.get(year).getYear(), elections.get(year).getResults().get(partyIndex));
            }

            model.addSeries(currentLine);
        }

        return model;
    }

    public void selectCountry() {
        createLineModels();

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
}
