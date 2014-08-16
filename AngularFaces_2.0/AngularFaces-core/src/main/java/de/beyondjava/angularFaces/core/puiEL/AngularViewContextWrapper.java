package de.beyondjava.angularFaces.core.puiEL;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialResponseWriter;
import javax.faces.context.PartialViewContext;
import javax.faces.context.PartialViewContextWrapper;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.lifecycle.ClientWindow;
import javax.faces.render.ResponseStateManager;
import javax.rmi.CORBA.Util;

import de.beyondjava.angularFaces.puiModelSync.PuiModelSync;

public class AngularViewContextWrapper extends PartialViewContextWrapper {

	private static final String ORIGINAL_WRITER = "com.sun.faces.ORIGINAL_WRITER";

	private PartialViewContext wrapped;

	public AngularViewContextWrapper(PartialViewContext wrapped) {
		this.wrapped = wrapped;

	}
	
	/**
	 * Copied from com.sun.faces.util.Util, Mojarra 2.2.7
	 */
	public static String getViewStateId(FacesContext context) {
        String result = null;
        final String viewStateCounterKey = "com.sun.faces.util.ViewStateCounterKey";
        Map<Object, Object> contextAttrs = context.getAttributes();
        Integer counter = (Integer) contextAttrs.get(viewStateCounterKey);
        if (null == counter) {
            counter = Integer.valueOf(0);
        }
        
        char sep = UINamingContainer.getSeparatorChar(context);
        UIViewRoot root = context.getViewRoot();
        result = root.getContainerClientId(context) + sep + 
                ResponseStateManager.VIEW_STATE_PARAM + sep +
                + counter;
        contextAttrs.put(viewStateCounterKey, ++counter);
        
        return result;
    }

	/**
	 * Copied from com.sun.faces.util.Util, Mojarra 2.2.7
	 */
    public static String getClientWindowId(FacesContext context) {
        String result = null;
        final String clientWindowIdCounterKey = "com.sun.faces.util.ClientWindowCounterKey";
        Map<Object, Object> contextAttrs = context.getAttributes();
        Integer counter = (Integer) contextAttrs.get(clientWindowIdCounterKey);
        if (null == counter) {
            counter = Integer.valueOf(0);
        }
        
        char sep = UINamingContainer.getSeparatorChar(context);
        result = context.getViewRoot().getContainerClientId(context) + sep + 
                ResponseStateManager.CLIENT_WINDOW_PARAM + sep + counter;
        contextAttrs.put(clientWindowIdCounterKey, ++counter);
        
        return result;
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
			UIViewRoot viewRoot = ctx.getViewRoot();
//			PuiELTransformer.eliminateDuplicatePuiModelSyncTags(viewRoot);
			
			if (isAjaxRequest()) {
				Object isAngularRequest = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("de.beyondjava.angularFaces.angularRequest");
				ctx.getExternalContext().getRequestParameterMap();
				if (null != isAngularRequest || myRenderIds.contains("angular")) {
					renderAngularResponse();
					FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("de.beyondjava.angularFaces.angularRequest");
					return;
				}
			}
		}
		if (phaseId==PhaseId.APPLY_REQUEST_VALUES) {
			UIViewRoot viewRoot = ctx.getViewRoot();
			PuiELTransformer.processEverything(viewRoot);
		}
		getWrapped().processPartial(phaseId);
		if (phaseId == PhaseId.RENDER_RESPONSE) {
			FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("de.beyondjava.angularFaces.angularRequest");
		}
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
			PuiModelSync body = (PuiModelSync) findPuiBody(viewRoot);
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
			if (kid instanceof PuiModelSync) {
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
		String viewStateId = getViewStateId(context);

		writer.startUpdate(viewStateId);
		String state = context.getApplication().getStateManager().getViewState(context);
		writer.write(state);
		writer.endUpdate();

		ClientWindow window = context.getExternalContext().getClientWindow();
		if (null != window) {
			String clientWindowId = getClientWindowId(context);
			writer.startUpdate(clientWindowId);
			writer.write(window.getId());
			writer.endUpdate();
		}
	}

	public void encodeAngularScript(ResponseWriter writer, FacesContext context, PuiModelSync component) throws IOException {
		String json = component.getFacesModel();
//		writer.startElement("script", component);
		writer.writeText("var facesBeans = " + json + ";", null);
		writer.writeText("injectJSonIntoScope(facesBeans,window.jsfScope);", null);
//		writer.endElement("script");
	}
}
