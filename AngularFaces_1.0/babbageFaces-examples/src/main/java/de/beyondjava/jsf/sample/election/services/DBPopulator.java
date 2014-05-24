package de.beyondjava.jsf.sample.election.services;

import java.util.*;

import de.beyondjava.jsf.sample.election.controller.ElectionController;
import de.beyondjava.jsf.sample.election.domain.*;

public class DBPopulator {

	public static final String[] PARTIES_IN_UK = { "Tories", "Labour", "SDP" };

	public static final double[][] ELECTIONS_IN_UK_PERCENTAGES = {
			{ 2010, 36.1, 29.0, 23.0 }, { 2005, 30.65, 35.24, 22.06 },
			{ 2001, 31.7, 40.7, 18.3 }, { 1997, 30.7, 43.2, 16.8 },
			{ 1992, 41.9, 34.4, 17.8 }, { 1987, 42.2, 30.8, 22.6 },
			{ 1983, 42.4, 27.6, 25.4 }, { 1979, 43.9, 36.9, 13.8 } };
	public static final double[][] ELECTIONS_IN_UK_SEATS = {
			{ 2010, 306, 258, 57 }, { 2005, 198, 356, 62 },
			{ 2001, 166, 412, 52 }, { 1997, 165, 418, 46 },
			{ 1992, 336, 271, 20 }, { 1987, 376, 229, 22 },
			{ 1983, 397, 209, 23 }, { 1979, 339, 269, 11 } };

	public static final String[] PARTIES_IN_GERMANY = { "CDU/CSU", "SPD",
			"FDP", "Grüne", "PDS", "others" };
	public static final String[] PARTY_COLORS_IN_GERMANY = { "000000", "E20019",
		"FBEE31", "1FA12D", "FF0000", "FF8800" };

	public static final double[][] ELECTIONS_IN_GERMANY = {
			{ 2013, 41.5, 25.7, 4.8, 8.4, 8.6, 11 },
			{ 2009, 33.8, 23.0, 14.6, 10.7, 11.9, 6.0 },
			{ 2005, 35.2, 34.2, 9.8, 8.1, 8.7, 4.0 },
			{ 2002, 38.5, 38.5, 7.4, 8.6, 4.0, 3.0 },
			{ 1998, 35.2, 40.9, 6.2, 6.7, 5.1, 5.9 },
			{ 1994, 41.5, 36.4, 6.9, 7.3, 4.4, 3.5 },
			{ 1990, 43.8, 33.5, 11.0, 5.0, 2.4, 4.3 },
			{ 1987, 44.3, 37.0, 9.1, 8.3, 0, 1.3 },
			{ 1983, 48.8, 38.2, 7.0, 5.6, 0, 0.4 },
			{ 1980, 44.5, 42.9, 10.6, 1.5, 0, 0.5 },
			{ 1976, 48.6, 42.6, 7.9, 0, 0, 0.9 },
			{ 1972, 44.9, 45.8, 8.4, 0, 0, 0.9 },
			{ 1969, 46.1, 42.7, 5.8, 0, 0, 5.5 },
			{ 1965, 47.6, 39.3, 9.5, 0, 0, 3.6 },
			{ 1961, 45.3, 36.2, 12.8, 0, 0, 5.7 },
			{ 1957, 50.2, 31.8, 7.7, 0, 0, 10.5 },
			{ 1953, 45.2, 28.8, 9.5, 0, 0, 16.5 },
			{ 1949, 31.0, 29.2, 11.9, 0, 0, 27.9 } };

	/**
     *
     */
	public static void populateWorld(ElectionController world) {
		List<Country> countries = new ArrayList<>();
		world.setCountries(countries);

		Country greatBritain = new Country();
		greatBritain.setName("Great Britain");
		countries.add(greatBritain);
		List<Election> electionsInUK = new ArrayList<>();
		greatBritain.setElections(electionsInUK);
		List<Party> partiesInUK = new ArrayList<>();
		greatBritain.setParties(partiesInUK);
		Party tories = new Party();
		tories.setName("Tories");
		tories.setColor("172459");
		partiesInUK.add(tories);
		Party labour = new Party();
		labour.setName("Labour");
		labour.setColor("E20019");
		partiesInUK.add(labour);

		Party sdp = new Party();
		sdp.setName("SDP");
		sdp.setColor("C43A2F");
		partiesInUK.add(sdp);

		for (int i = 0; i < ELECTIONS_IN_UK_PERCENTAGES.length; i++) {
			double[] data = ELECTIONS_IN_UK_PERCENTAGES[i];
			Election election = new Election();
			election.setYear((int) data[0]);
			Map<Party, Double> votes = new HashMap<>();
			List<Double> results = new ArrayList<>();
			election.setResults(results);
			List<String> parties = new ArrayList<>();
			election.setParties(parties);
			election.setResult(votes);
			for (int p = 1; p < data.length; p++) {
				votes.put(partiesInUK.get(p - 1), data[p]);
				results.add(data[p]);
				parties.add(partiesInUK.get(p - 1).getName());
			}
			electionsInUK.add(election);
		}

		Country germany = new Country();
		germany.setName("Germany");
		countries.add(germany);
		List<Election> electionsInGermany = new ArrayList<>();
		germany.setElections(electionsInGermany);
		List<Party> partiesInGermany = new ArrayList<>();
		germany.setParties(partiesInGermany);

		for (int i = 0; i < PARTIES_IN_GERMANY.length; i++) {
			Party p = new Party();
			p.setName(PARTIES_IN_GERMANY[i]);
			p.setColor(PARTY_COLORS_IN_GERMANY[i]);
			partiesInGermany.add(p);
		}

		for (int i = 0; i < ELECTIONS_IN_GERMANY.length; i++) {
			double[] data = ELECTIONS_IN_GERMANY[i];
			Election election = new Election();
			election.setYear((int) data[0]);
			Map<Party, Double> votes = new HashMap<>();
			List<Double> results = new ArrayList<>();
			election.setResults(results);
			List<String> parties = new ArrayList<>();
			election.setParties(parties);
			election.setResult(votes);
			for (int p = 1; p < data.length; p++) {
				votes.put(partiesInGermany.get(p - 1), data[p]);
				results.add(data[p]);
				parties.add(partiesInGermany.get(p - 1).getName());
			}
			electionsInGermany.add(election);
		}

	}

}
