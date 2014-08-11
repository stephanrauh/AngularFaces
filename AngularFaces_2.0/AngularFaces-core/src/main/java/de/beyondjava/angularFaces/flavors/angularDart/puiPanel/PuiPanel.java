package de.beyondjava.angularFaces.flavors.angularDart.puiPanel;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesComponent("de.beyondjava.angularFaces.puiPanel.PuiPanel")
public class PuiPanel extends UIOutput {
	enum propertyKeys {
		collapsed, header, toggleable, toggleOrientation
	}

	public String getCollapsed() {
		return (String) getStateHelper().eval(propertyKeys.collapsed, null);
	}

	public String getHeader() {
		return (String) getStateHelper().eval(propertyKeys.header, null);
	}

	public String getToggleable() {
		return (String) getStateHelper().eval(propertyKeys.toggleable, null);
	}

	public String getToggleOrientation() {
		return (String) getStateHelper().eval(propertyKeys.toggleOrientation,
				null);
	}

	public void setCollapsed(String isCollapsed) {
		getStateHelper().put(propertyKeys.collapsed, isCollapsed);
	}

	public void setHeader(String closeable) {
		getStateHelper().put(propertyKeys.header, closeable);
	}

	public void setToggleable(String selected) {
		getStateHelper().put(propertyKeys.toggleable, selected);
	}

	public void setToggleOrientation(String toggleOrientation) {
		getStateHelper().put(propertyKeys.toggleOrientation, toggleOrientation);
	}

}
