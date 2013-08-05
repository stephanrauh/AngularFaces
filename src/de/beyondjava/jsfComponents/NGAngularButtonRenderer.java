/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import org.primefaces.component.commandbutton.CommandButtonRenderer;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
@FacesRenderer(componentFamily = "de.beyondjava.AngularButton", rendererType = "de.beyondjava.AngularButton")
public class NGAngularButtonRenderer extends CommandButtonRenderer
{

}
