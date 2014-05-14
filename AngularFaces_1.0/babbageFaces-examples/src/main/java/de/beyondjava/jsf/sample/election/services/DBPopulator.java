package de.beyondjava.jsf.sample.election.services;

import java.util.*;

import de.beyondjava.jsf.sample.election.controller.ElectionController;
import de.beyondjava.jsf.sample.election.domain.*;

public class DBPopulator {

    /**
     *
     */
    public static void populateWorld(ElectionController world) {
        List<Country> countries = new ArrayList<>();
        world.setCountries(countries);

        Country greatBritain = new Country();
        greatBritain.setName("Great Britain");
        countries.add(greatBritain);
        List<Election> electionsInGB = new ArrayList<>();
        greatBritain.setElections(electionsInGB);
        List<Party> partiesInGB = new ArrayList<>();
        greatBritain.setParties(partiesInGB);
        Party tories = new Party();
        tories.setName("Tories");
        partiesInGB.add(tories);
        Party labour = new Party();
        labour.setName("Labour");
        partiesInGB.add(labour);

        Country germany = new Country();
        germany.setName("Germany");
        countries.add(germany);
        List<Election> electionsInGermany = new ArrayList<>();
        germany.setElections(electionsInGermany);
        List<Party> partiesInGermany = new ArrayList<>();
        germany.setParties(partiesInGermany);
        Party spd = new Party();
        spd.setName("SPD");
        partiesInGermany.add(spd);

        Party cdu = new Party();
        cdu.setName("CDU");
        partiesInGermany.add(cdu);

        Party fdp = new Party();
        fdp.setName("FDP");
        partiesInGermany.add(fdp);

        Party gruene = new Party();
        gruene.setName("Die Grünen");
        partiesInGermany.add(gruene);

    }

}
