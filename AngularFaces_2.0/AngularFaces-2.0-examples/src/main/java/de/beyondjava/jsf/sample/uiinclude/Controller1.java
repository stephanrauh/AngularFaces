package de.beyondjava.jsf.sample.uiinclude;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class Controller1 {
	public String getHelloWorld() {
		return "Hello from Controller 1";
	}
}
