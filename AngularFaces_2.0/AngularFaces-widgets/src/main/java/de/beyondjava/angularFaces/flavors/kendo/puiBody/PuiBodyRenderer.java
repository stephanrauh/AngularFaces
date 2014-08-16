package de.beyondjava.angularFaces.flavors.kendo.puiBody;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.application.*;
import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.BodyRenderer;
import com.sun.faces.renderkit.html_basic.ScriptRenderer;

import de.beyondjava.angularFaces.core.RendererUtils;

/**
 * PuiBody is an HtmlBody that activates the AngularDart framework.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.kendoFaces.puiBody.PuiBody")
public class PuiBodyRenderer extends BodyRenderer implements RendererUtils {
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.kendoFaces.puiButton.PuiBodyRenderer");

	@Override
	public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
		super.encodeBegin(context, component);
		ResponseWriter writer = context.getResponseWriter();
		String ngApp = (String) component.getAttributes().get("ng-app");
		if (null != ngApp) {
			writer.writeAttribute("ng-app", ngApp, null);
		} else {
			writer.append(" ng-app ");
		}
		String ngController = (String) component.getAttributes().get("ng-controller");
		renderNonEmptyAttribute(writer, "ng-controller", ngController);
//		writer.writeAttribute("onload", "restoreValues();", null);
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
		if (!ajaxRequest)
			super.encodeChildren(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String json = ((PuiBody) component).getFacesModel();
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n");}
		writer.startElement("script", component);
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n");}
		writer.writeText("window.jsfScope=null;", null);
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n");}
		writer.writeText("function initJSFScope($scope){", null);
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n  ");}
		writer.writeText("window.jsfScope=$scope;", null);
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n  ");}
		writer.writeText("var jsf = " + json + ";", null);
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n  ");}
		writer.writeText("injectJSonIntoScope(jsf,$scope);", null);
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n");}
		writer.writeText("}", null);
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n");}
		writer.endElement("script");
		if (context.isProjectStage(ProjectStage.Development)) {writer.append("\r\n");}

		String main = (String) component.getAttributes().get("mainclassfile");
		if (main == null) {
			main = "main.js";
		}
		if (!main.endsWith(".js")) {
			main = main + ".js";
		}
		writer.append("<script src='" + main + "'></script>");

		PuiScriptRenderer r = new PuiScriptRenderer();
		r.encodeScript(context, component, "glue.js", "AngularFaces");

		// writer.append("<script src=\"../resources/AngularFaces/glue.js\">\r\n</script>\r\n");
//		writer.append("\r\n");
//		writer.append("  <script>storeValues();</script>");
//		writer.append("\r\n");

		super.encodeEnd(context, component);

	}
};
