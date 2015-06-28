package de.beyondjava.jsf.sample.multipleControllers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Selector implements Serializable {
	private static final long serialVersionUID = 1L;
	private boolean firstControllerActive=true;

	public String getActiveController() {
		if (firstControllerActive)
			return "Dice1Controller";
		else
			return "Dice2Controller";
	}

	public boolean isFirstControllerActive() {
		return firstControllerActive;
	}

	public void setFirstControllerActive(boolean firstControllerActive) {
		this.firstControllerActive = firstControllerActive;
	}
	
	public void toggleControllers() {
		firstControllerActive = !firstControllerActive;
	}

}
