package de.beyondjava.angularFaces.core.puiEL;

import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

/**
 * Converts EL expressions to Angular expressions
 */
public class PuiELTransformer implements SystemEventListener {

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiEL.PuiELTransformer");

	static {
		LOGGER.info("AngularFaces utility class PuiELTransformer ready for use.");
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		Object source = event.getSource();
		if (source instanceof UIViewRoot) {
			FindNGControllerCallback findNGControllerCallback = new FindNGControllerCallback();
			((UIViewRoot) source).visitTree(new FullVisitContext(FacesContext.getCurrentInstance()), findNGControllerCallback);
			boolean ajaxRequest = FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest();
			boolean postback = FacesContext.getCurrentInstance().isPostback();
			PuiModelSync c = findNGControllerCallback.getPuiModelSync();
			if (true) {
				// if ((!ajaxRequest) && (!postback)) {
				// processUIComponent(source);
				AddLabelCallback labelDecorator = new AddLabelCallback();
				((UIViewRoot) source).visitTree(new FullVisitContext(FacesContext.getCurrentInstance()), labelDecorator);
				System.out.println("AJAX: " + ajaxRequest + " Postback: "+ postback + " duplicate Labels: " +labelDecorator.duplicateLabels );
				((UIViewRoot) source).visitTree(new FullVisitContext(FacesContext.getCurrentInstance()), new ProcessAngularExpressionsCallback());
				((UIViewRoot) source).visitTree(new FullVisitContext(FacesContext.getCurrentInstance()), new ProcessValueCallback());
				((UIViewRoot) source).visitTree(new FullVisitContext(FacesContext.getCurrentInstance()), new AddTypeCallback());
			}
		}
	}

	@Override
	public boolean isListenerForSource(Object source) {
		if (source instanceof UIComponent) {
			return true;
		}
		return false;
	}

	private static PuiModelSync findModelSyncTagInParentNode(Object source) {
		UIComponent c = (UIComponent) source;
		while ((c != null) && !(c.getAttributes().containsKey("ng-controller")))
			c = c.getParent();
		if (null != c && c.getParent() != null) {
			UIComponent maybe = c.getParent().getChildren().get(c.getParent().getChildCount() - 1);
			if (maybe instanceof PuiModelSync)
				return (PuiModelSync) maybe;
		}
		return null;
	}

	// public static void eliminateDuplicatePuiModelSyncTags(UIViewRoot viewRoot) {
	// PuiModelSync first = findModelSyncTag(viewRoot);
	// if (null!=first) {
	// List<UIComponent> siblings= first.getParent().getChildren();
	// for (int i = siblings.size(); i>=0;i--) {
	// if (siblings.get(i) instanceof PuiModelSync)
	// if (siblings.get(i)!=first) {
	// System.out.println("gotcha");
	// }
	// }
	//
	// }
	//
	// }
}
