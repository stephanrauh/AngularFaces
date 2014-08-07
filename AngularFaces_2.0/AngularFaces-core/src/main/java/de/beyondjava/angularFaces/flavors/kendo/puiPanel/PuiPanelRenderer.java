package de.beyondjava.angularFaces.flavors.kendo.puiPanel;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.render.*;

import de.beyondjava.angularFaces.core.RendererUtils;
import de.beyondjava.angularFaces.flavors.kendo.puiAccordion.PuiAccordion;

/**
 * A pui:panel is a field group with a caption.
 */
@FacesRenderer(componentFamily = "javax.faces.Output", rendererType = "de.beyondjava.kendoFaces.puiPanel.PuiPanel")
public class PuiPanelRenderer extends Renderer implements RendererUtils {
	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.kendoFaces.puiPanel.PuiPanel");

	static {
		LOGGER.info("AngularFaces renderer of 'PuiPanel' is available for use.");
	}

	public PuiPanelRenderer() {
		LOGGER.info(getClass().getName() + " is being initialized");
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		PuiPanel puiPanel = (PuiPanel) component;
		ResponseWriter writer = context.getResponseWriter();
		writer.writeAttribute("k-options", "expanded:true", "expanded");
		if (!(component.getParent() instanceof PuiAccordion)) {
			writer.startElement("ul", component);
			// writer.writeAttribute("kendo-panel-bar", "", null);
			String styleClass = puiPanel.getStyleClass();
			if (styleClass == null) {
				styleClass = "k-widget k-reset k-header k-panelbar";
				puiPanel.setStyleClass(styleClass);
			} else if (!(styleClass
					.startsWith("k-widget k-reset k-header k-panelbar"))) {
				styleClass += "k-widget k-reset k-header k-panelbar";
				puiPanel.setStyleClass(styleClass);
			}
			renderMostCommonAttributes(writer, component);
		}
		writer.startElement("li", component);
		writer.writeAttribute("class",
				"ng-scope k-item k-state-default k-first k-last", "class");
		writer.startElement("span", component);
		writer.writeAttribute("class", "k-link k-header", "class");
		writer.append(puiPanel.getHeader());
		writer.endElement("span");
		writer.startElement("div", component);
		String style = puiPanel.getContentStyle();
		if (null == style)
			style = "";
		String height = puiPanel.getHeight();
		if (null != height) {
			if (style.length() > 0)
				style += ";";
			style += "height:" + height;
		}
		String width = puiPanel.getWidth();
		if (null != width) {
			if (style.length() > 0)
				style += ";";
			style += "width:" + width;
		}
		if (style.length() > 0) {
			writer.writeAttribute("style", style, "style");
		}

		String styleClass = puiPanel.getContentStyleClass();
		if (null != styleClass) {
			styleClass = "k-content " + styleClass;
		} else
			styleClass = "k-content";
		writer.writeAttribute("class", styleClass, "class");

	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		super.encodeEnd(context, component);
		writer.endElement("div");
		writer.endElement("li");
		if (!(component.getParent() instanceof PuiAccordion)) {
			writer.endElement("ul");
		}
	}

	// <ul class="k-widget k-reset k-header k-panelbar" >
	// <li class="ng-scope k-item k-state-default k-first k-last">
	// <span class="k-link k-header">What is it?
	// </span>
};
