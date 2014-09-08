package de.beyondjava.angularFaces.core.transformation;

import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;

/**
 * Converts EL expressions to Angular expressions
 */
public class PuiAngularTransformer implements SystemEventListener {

	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.puiEL.PuiELTransformer");

	static {
		LOGGER.info("AngularFaces utility class PuiELTransformer ready for use.");
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		Object source = event.getSource();
		if (source instanceof UIViewRoot) {
			long timer = System.nanoTime();
			PuiModelSync.initJSFAttributesTable();
			UIViewRoot root = (UIViewRoot) source;
			FindNGControllerCallback findNGControllerCallback = new FindNGControllerCallback();
			FacesContext context = FacesContext.getCurrentInstance();
			root.visitTree(new FullVisitContext(context), findNGControllerCallback);
			boolean ajaxRequest = context.getPartialViewContext().isAjaxRequest();
			boolean postback = context.isPostback();
			boolean isProduction = context.isProjectStage(ProjectStage.Production);
			PuiModelSync c = findNGControllerCallback.getPuiModelSync();
			if (true) {
				// if ((!ajaxRequest) && (!postback)) {
				addJavascript(root, context, isProduction);
				time("extract AngularJS expressions", () -> root.visitTree(new FullVisitContext(context), new ProcessAngularExpressionsCallback()));
				time("add NGModel", () -> root.visitTree(new FullVisitContext(context), new AddNGModelAndIDCallback()));
				time("add ng* attributes", () -> root.visitTree(new FullVisitContext(context), new AddNGPassThroughAttributesCallback()));
//				if (!ajaxRequest) {
					AddLabelCallback labelDecorator = new AddLabelCallback();
					time("add labels", () -> root.visitTree(new FullVisitContext(context), labelDecorator));
					System.out.println("AJAX: " + ajaxRequest + " Postback: " + postback + " duplicate Labels: "
							+ labelDecorator.duplicateLabels);
//				}
				time("add type information", () -> root.visitTree(new FullVisitContext(context), new AddTypeInformationCallback()));
				time("add messages", () -> root.visitTree(new FullVisitContext(context), new AddMessagesCallback()));
				if (!ajaxRequest) {
					time("internationalization", () -> root.visitTree(new FullVisitContext(context), new TranslationCallback()));
				}
			}
			long time=System.nanoTime()-timer;
			System.out.println((time/1000)/1000.0d + " ms");
		}
	}

	private void addJavascript(UIViewRoot root, FacesContext context, boolean isProduction) {
		UIOutput output = new UIOutput();
		output.setRendererType("javax.faces.resource.Script");
		if (isProduction)
			output.getAttributes().put("name", "jquery.min-1.11.1.js");
		else
			output.getAttributes().put("name", "jquery-1.11.1.js");
		output.getAttributes().put("library", "jQuery");
		root.addComponentResource(context, output, "head");

		output = new UIOutput();
		output.setRendererType("javax.faces.resource.Script");
		if (isProduction)
			output.getAttributes().put("name", "angular.min-1.2.22.js");
		else
			output.getAttributes().put("name", "angular-1.2.22.js");
		output.getAttributes().put("library", "AngularJS");
		root.addComponentResource(context, output, "head");
	}

	@Override
	public boolean isListenerForSource(Object source) {
		if (source instanceof UIComponent) {
			return true;
		}
		return false;
	}
	
	private void time(String description, Runnable runnable) {
		long timer = System.nanoTime();
		runnable.run();
		long time=System.nanoTime()-timer;
		System.out.println((time/1000)/1000.0d + " ms " + description);
	}
}
