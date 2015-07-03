package de.beyondjava.jsf.sample.uiinclude;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Controller2 {
	public String getHelloWorld() {
		return "¡Hola!, ¡mundo! del segundo controller";
	}
}
