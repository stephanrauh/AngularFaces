package de.beyondjava.jsf.sample.election.controller;

import java.io.Serializable;

import javax.faces.bean.*;

@ManagedBean
@SessionScoped
public class MenuController implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{electionController}")
    private ElectionController electionController;

    private int tabIndex = 0;

    /**
     * @return the electionController
     */
    public ElectionController getElectionController() {
        return this.electionController;
    }

    /**
     * @return the tabIndex
     */
    public int getTabIndex() {
        return tabIndex;
    }

    public String getTitle() {
        if (0 == tabIndex) {
            return "BF - PrimeFaces 5 example";
        }
        if (1 == tabIndex) {
            return "BF - Mojarra without IDs example";
        }
        if (2 == tabIndex) {
            return "BF - Mojarra with IDs example";
        }
        return electionController.getTitle();

    }

    /**
     * @param electionController
     *            the electionController to set
     */
    public void setElectionController(ElectionController electionController) {
        this.electionController = electionController;
    }

    /**
     * @param tabIndex
     *            the tabIndex to set
     */
    public void setTabIndex(int tabIndex) {
        this.tabIndex = tabIndex;
    }
}
