package de.beyondjava.jsf.sample.multipleControllers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Dice2 implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int value=2;

	public void throwDice() {
		setValue(((int) (Math.random() * 6.0)) + 1);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
