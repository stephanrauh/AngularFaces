package de.beyondjava.jsf.sample.carshop;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SettingsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean useAngularFacesAJAX = true;
	
	private boolean useHTML5Style = true;
	
	private boolean usePrimeFaces = true;

	public boolean isUsePrimeFaces() {
		return usePrimeFaces;
	}

	public void setUsePrimeFaces(boolean usePrimeFaces) {
		this.usePrimeFaces = usePrimeFaces;
	}

	public boolean isUseHTML5Style() {
		return useHTML5Style;
	}

	public void setUseHTML5Style(boolean useHTML5Style) {
		this.useHTML5Style = useHTML5Style;
	}

	public boolean isUseAngularFacesAJAX() {
		return useAngularFacesAJAX;
	}

	public void setUseAngularFacesAJAX(boolean useAngularFacesAJAX) {
		this.useAngularFacesAJAX = useAngularFacesAJAX;
	}

}
