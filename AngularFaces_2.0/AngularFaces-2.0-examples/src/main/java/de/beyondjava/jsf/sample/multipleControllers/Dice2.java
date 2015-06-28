package de.beyondjava.jsf.sample.multipleControllers;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Dice2 implements Serializable {
	private static final long serialVersionUID = 1L;

	public int getThrow() {
		return ((int) Math.random() * 6) + 1;
	}
}
