/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.angularFaces.puiButton;

import java.util.logging.Logger;

import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

/**
 * &ltpui-button&gt; is a command button that can call Dart code (or Javascript code, albeit JS code can't interoperate
 * with Dart). &ltpui-button&gt; can contain a caption and an image that can be put in front of or behind the caption.
 */
@FacesComponent("de.beyondjava.angularFaces.puiButton.PuiButton")
public class PuiButton extends UIOutput {
    enum propertyKeys {
        actionListener, disabled, icon, iconPos, value
    }

    private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiButton.PuiButton");

    static {
        LOGGER.info("AngularFaces component 'PuiButton' is available for use.");
    }

    public PuiButton() {
        LOGGER.info(getClass().getName() + " is initialized");
        LOGGER.info(getFamily());
        Renderer renderer = getRenderer(FacesContext.getCurrentInstance());
        LOGGER.info(renderer.getClass().getName());
    }

    /**
     * optional: the name of a Dart function called when the button is clicked. Similar to ng-click (but more natural to
     * PrimeFaces programmers). Note that onClick is also a legal attribute, only it calls a Javascript function instead
     * of a Dart function!
     */
    public String getActionListener() {
        return (String) getStateHelper().eval(propertyKeys.actionListener, null);
    }

    /** optional: if set to "true", the button is disabled and doesn't react to being clicked. */
    public String getDisabled() {
        return (String) getStateHelper().eval(propertyKeys.disabled, null);
    }

    /** optional: if set to "true", the button is disabled and doesn't react to being clicked. */
    public String getIcon() {
        return (String) getStateHelper().eval(propertyKeys.icon, null);
    }

    /** optional: the button's icon's position. Legal values: "right" and "left" (=default). */
    public String getIconPos() {
        return (String) getStateHelper().eval(propertyKeys.iconPos, null);
    }

    /** The button's caption */
    @Override
    public String getValue() {
        return (String) getStateHelper().eval(propertyKeys.value, null);
    }

    /**
     * optional: the name of a Dart function called when the button is clicked. Similar to ng-click (but more natural to
     * PrimeFaces programmers). Note that onClick is also a legal attribute, only it calls a Javascript function instead
     * of a Dart function!
     */
    public void setActionListener(String ngClick) {
        getStateHelper().put(propertyKeys.actionListener, ngClick);
    }

    /** optional: if set to "true", the button is disabled and doesn't react to being clicked. */
    public void setDisabled(String disabled) {
        getStateHelper().put(propertyKeys.disabled, disabled);
    }

    /** optional: if set to "true", the button is disabled and doesn't react to being clicked. */
    public void setIcon(String icon) {
        getStateHelper().put(propertyKeys.icon, icon);
    }

    /** optional: the button's icon's position. Legal values: "right" and "left" (=default). */
    public void setIconPos(String iconPos) {
        getStateHelper().put(propertyKeys.iconPos, iconPos);
    }

    /** The button's caption */
    public void setValue(String value) {
        getStateHelper().put(propertyKeys.value, value);
    }

}
