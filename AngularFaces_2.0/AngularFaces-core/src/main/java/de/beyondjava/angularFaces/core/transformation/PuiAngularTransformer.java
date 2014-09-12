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
package de.beyondjava.angularFaces.core.transformation;

import java.util.Map;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;
import de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator;

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

			if (!AngularTagDecorator.isActive()) {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_FATAL, "Configuration error: " +
										"Add javax.faces.FACELETS_DECORATORS=de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator to the context init parameters",
										"Configuration error: " +
												"Add javax.faces.FACELETS_DECORATORS=de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator to the context init parameters"));
				System.out
						.println("Add javax.faces.FACELETS_DECORATORS=de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator to the context init parameters");
			} else {
				final UIViewRoot root = (UIViewRoot) source;
				FindNGControllerCallback ngControllerCallback = new FindNGControllerCallback();
				System.out.println(((System.nanoTime() - timer) / 1000) / 1000.0d + " ms find NGControllerCallback");
				final FacesContext context = FacesContext.getCurrentInstance();
				root.visitTree(new FullVisitContext(context), ngControllerCallback);
				boolean ajaxRequest = context.getPartialViewContext().isAjaxRequest();
				boolean postback = context.isPostback();
				boolean isProduction = context.isProjectStage(ProjectStage.Production);
				PuiModelSync c = ngControllerCallback.getPuiModelSync();
				if (true) {
					addJavascript(root, context, isProduction);
					// time("extract AngularJS expressions", new Runnable(){public void run(){ root.visitTree(new FullVisitContext(context),
					// new ProcessAngularExpressionsCallback());}});
					time("add NGModel", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), new AddNGModelAndIDCallback());
						}
					});
					if (ngControllerCallback.isAddLabels()) {
					final AddLabelCallback labelDecorator = new AddLabelCallback();
					time("add labels", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), labelDecorator);
						}
					});
					System.out.println("AJAX: " + ajaxRequest + " Postback: " + postback + " duplicate Labels: "
							+ labelDecorator.duplicateLabels);
					}
					time("add type information", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), new AddTypeInformationCallback());
						}
					});
					if (ngControllerCallback.isAddMessages()) {
					time("add messages", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), new AddMessagesCallback());
						}
					});
					}
					if (!ajaxRequest) {
						time("internationalization", new Runnable() {
							public void run() {
								root.visitTree(new FullVisitContext(context), new TranslationCallback());
							}
						});
					}
				}
			}
			long time = System.nanoTime() - timer;
			System.out.println((time / 1000) / 1000.0d + " ms");
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
		long time = System.nanoTime() - timer;
		System.out.println((time / 1000) / 1000.0d + " ms " + description);
	}
}
