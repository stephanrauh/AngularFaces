package de.beyondjava.jsf.sample.election.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.primefaces.component.tabview.TabView;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class MenuController implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManagedProperty("#{electionController}")
    private ElectionController electionController;

    private int activeTabIndex = 0;

    /**
     * @return the electionController
     */
    public ElectionController getElectionController() {
        return this.electionController;
    }

    /**
     * @return the tabIndex
     */
    public int getActiveTabIndex() {
        return activeTabIndex;
    }

    public String getTitle() {
        if (0 == activeTabIndex) {
            return "BF - PrimeFaces 5 example";
        }
        if (1 == activeTabIndex) {
            return "BF - Mojarra without IDs example";
        }
        if (2 == activeTabIndex) {
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
    public void setActiveTabIndex(int tabIndex) {
        this.activeTabIndex = tabIndex;
    }
    
    public void onTabChange(TabChangeEvent event) 
    {   
        TabView tabView = (TabView) event.getComponent();
        activeTabIndex= tabView.getChildren().indexOf(event.getTab());
    }
}
