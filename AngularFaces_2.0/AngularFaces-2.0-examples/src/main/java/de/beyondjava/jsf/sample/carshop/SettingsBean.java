package de.beyondjava.jsf.sample.carshop;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SettingsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private boolean updateImmediately  = true;
	
	private boolean useAngularFacesAJAX = true;
	
	private boolean useHTML5Style = true;
	
	private boolean usePrimeFaces = true;

	public boolean isUpdateImmediately() {
		return updateImmediately;
	}

	public boolean isUseAngularFacesAJAX() {
		return useAngularFacesAJAX;
	}

	public boolean isUseHTML5Style() {
		return useHTML5Style;
	}

	public boolean isUsePrimeFaces() {
		return usePrimeFaces;
	}

	public void setUpdateImmediately(boolean immediateUpdate) {
		this.updateImmediately = immediateUpdate;
	}

	public void setUseAngularFacesAJAX(boolean useAngularFacesAJAX) {
		this.useAngularFacesAJAX = useAngularFacesAJAX;
	}

	public void setUseHTML5Style(boolean useHTML5Style) {
		this.useHTML5Style = useHTML5Style;
	}

	public void setUsePrimeFaces(boolean usePrimeFaces) {
		this.usePrimeFaces = usePrimeFaces;
	}

}
