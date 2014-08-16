package de.beyondjava.angularFaces.core.puiEL;

import java.io.IOException;
import java.util.Collection;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.ClientWindow;

import com.sun.faces.util.Util;

import de.beyondjava.angularFaces.flavors.kendo.puiBody.PuiBody;
import de.beyondjava.angularFaces.flavors.kendo.puiBody.PuiScriptRenderer;

public class AngularViewContextWrapper extends PartialViewContextWrapper {

	private static final String ORIGINAL_WRITER = "com.sun.faces.ORIGINAL_WRITER";

	private PartialViewContext wrapped;

	public AngularViewContextWrapper(PartialViewContext wrapped) {
		this.wrapped = wrapped;

	}

	@Override
	public PartialViewContext getWrapped() {
		return wrapped;
	}

	@Override
	public void processPartial(PhaseId phaseId) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		PartialViewContext pvc = ctx.getPartialViewContext();
		Collection<String> myRenderIds = pvc.getRenderIds();

		if (phaseId == PhaseId.RENDER_RESPONSE) {
			if (isAjaxRequest()) {
				if (myRenderIds.contains("angular")) {
					renderAngularResponse();
					return;
				}
			}
		}
		if (phaseId==PhaseId.APPLY_REQUEST_VALUES) {
			UIViewRoot viewRoot = ctx.getViewRoot();
			PuiBody body = (PuiBody) findPuiBody(viewRoot);
			PuiELTransformer.processEverything(body);
		}
		getWrapped().processPartial(phaseId);

		return;
	}

	private void renderAngularResponse() {
		try {
			FacesContext ctx = FacesContext.getCurrentInstance();
			PartialViewContext pvc = ctx.getPartialViewContext();
			UIViewRoot viewRoot = ctx.getViewRoot();
			//
			// We re-enable response writing.
			//
			PartialResponseWriter writer = pvc.getPartialResponseWriter();
			ResponseWriter orig = ctx.getResponseWriter();
			ctx.getAttributes().put(ORIGINAL_WRITER, orig);

			ctx.setResponseWriter(writer);

			ExternalContext exContext = ctx.getExternalContext();
			exContext.setResponseContentType("text/xml");
			exContext.addResponseHeader("Cache-Control", "no-cache");

			String encoding = writer.getCharacterEncoding();
			if (encoding == null) {
				encoding = "UTF-8";
			}
			writer.writePreamble("<?xml version='1.0' encoding='" + encoding + "'?>\n");
			writer.startDocument();
			writer.startEval();
			PuiBody body = (PuiBody) findPuiBody(viewRoot);
//			PuiELTransformer.processEverything(body);
			encodeAngularScript(writer, ctx, body);
			writer.endEval();
			renderState(ctx);

			writer.endDocument();
		} catch (IOException ex) {
			this.cleanupAfterView();
		} catch (RuntimeException ex) {
			this.cleanupAfterView();
			// Throw the exception
			throw ex;
		}
	}

	private UIComponent findPuiBody(UIComponent parent) {
		int index = 0;
		while (index < parent.getChildCount()) {
			UIComponent kid = parent.getChildren().get(index);
			if (kid instanceof PuiBody) {
				return kid;
			}
			UIComponent grandChild = findPuiBody(kid);
			if (null != grandChild)
				return grandChild;
			index++;
		}
		return null;
	}

	/** Copied from com.sun.faces.context.PartialViewContextImpl. */
	private void cleanupAfterView() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		ResponseWriter orig = (ResponseWriter) ctx.getAttributes().get(ORIGINAL_WRITER);
		assert (null != orig);
		// move aside the PartialResponseWriter
		ctx.setResponseWriter(orig);
	}

	/**
	 * Copied from com.sun.faces.context.PartialViewContextImpl. May have to be adapted to future Mojarra or JSF versions.
	 */
	private void renderState(FacesContext context) throws IOException {
		// Get the view state and write it to the response..
		PartialViewContext pvc = context.getPartialViewContext();
		PartialResponseWriter writer = pvc.getPartialResponseWriter();
		String viewStateId = Util.getViewStateId(context);

		writer.startUpdate(viewStateId);
		String state = context.getApplication().getStateManager().getViewState(context);
		writer.write(state);
		writer.endUpdate();

		ClientWindow window = context.getExternalContext().getClientWindow();
		if (null != window) {
			String clientWindowId = Util.getClientWindowId(context);
			writer.startUpdate(clientWindowId);
			writer.write(window.getId());
			writer.endUpdate();
		}
	}

	public void encodeAngularScript(ResponseWriter writer, FacesContext context, PuiBody component) throws IOException {
		String json = component.getFacesModel();
//		writer.startElement("script", component);
		writer.writeText("var facesBeans = " + json + ";", null);
		writer.writeText("injectJSonIntoScope(facesBeans,window.jsfScope);", null);
//		writer.endElement("script");
	}
}
