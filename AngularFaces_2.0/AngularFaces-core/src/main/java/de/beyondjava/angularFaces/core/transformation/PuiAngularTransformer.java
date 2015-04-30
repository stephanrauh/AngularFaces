/**
 *  (C) 2013-2015 Stephan Rauh http://www.beyondjava.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.beyondjava.angularFaces.core.transformation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.ProjectStage;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import de.beyondjava.angularFaces.components.puiModelSync.PuiModelSync;
import de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator;

/**
 * Converts EL expressions to Angular expressions
 */
public class PuiAngularTransformer implements SystemEventListener {

	private static final Logger LOGGER = Logger
			.getLogger("de.beyondjava.angularFaces.core.transformation.PuiAngularTransformer");

	private static final String RESOURCE_KEY = "de.beyondjava.angularFaces.core.ResourceFiles";

	static {
		LOGGER.info("AngularFaces utility class PuiELTransformer ready for use.");
	}

	@Override
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		Object source = event.getSource();
		if (source instanceof UIViewRoot) {
			long timer = System.nanoTime();

			final FacesContext context = FacesContext.getCurrentInstance();
			boolean isProduction = context.isProjectStage(ProjectStage.Production);
			if ((!isProduction) && (!AngularTagDecorator.isActive())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
						"Configuration error: ",
						"Add javax.faces.FACELETS_DECORATORS=de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator to the context init parameters in the web.xml"));
				LOGGER.severe(
						"Add javax.faces.FACELETS_DECORATORS=de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator to the context init parameters in the web.xml");
			} else {
				final UIViewRoot root = (UIViewRoot) source;
				final boolean ajaxRequest = context.getPartialViewContext().isAjaxRequest();
				boolean angularFacesRequest = ajaxRequest && isAngularFacesRequest();
				if (!angularFacesRequest || PuiModelSync.isJSFAttributesTableEmpty()) {
					PuiModelSync.initJSFAttributesTable();
					FindNGControllerCallback ngControllerCallback = new FindNGControllerCallback();
					LOGGER.fine(((System.nanoTime() - timer) / 1000) / 1000.0d + " ms find NGControllerCallback");
					root.visitTree(new FullVisitContext(context), ngControllerCallback);
					if (!angularFacesRequest) {
						addJavascript(root, context, isProduction);
					}
					time("add NGModel", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), new AddNGModelAndIDCallback());
						}
					});
					time("add type information", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), new AddTypeInformationCallback());
						}
					});
					time("convert options to f:selectItem", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), new PuiSelectItemTagHandler());
						}
					});
					time("internationalization", new Runnable() {
						public void run() {
							root.visitTree(new FullVisitContext(context), new TranslationCallback(ajaxRequest));
						}
					});
				}
			}
			long time = System.nanoTime() - timer;
			LOGGER.fine((time / 1000) / 1000.0d + " ms");
		}
	}

	private void addJavascript(UIViewRoot root, FacesContext context, boolean isProduction) {
		boolean loadJQuery = true;
		boolean loadJQueryUI = true;
		boolean loadAngularJS = true;
		boolean loadAngularMessages = true;
		List<UIComponent> availableResources = root.getComponentResources(context, "head");
		for (UIComponent ava : availableResources) {
			String name = (String) ava.getAttributes().get("name");
			if (null != name)
				if (name.contains("angular") && name.endsWith(".js")) {
					loadAngularJS = false;
				} else if (name.toLowerCase().contains("angular-messages") && name.toLowerCase().endsWith(".js")) {
					loadAngularMessages = false;
				} else if (name.toLowerCase().contains("jquery-ui") && name.toLowerCase().endsWith(".js")) {
					loadJQueryUI = false;
				} else if (name.toLowerCase().contains("jquery") && name.toLowerCase().endsWith(".js")) {
					loadJQuery = false;
				}
		}

		if (loadJQuery) {
			UIOutput output = new UIOutput();
			output.setRendererType("javax.faces.resource.Script");
			if (isProduction)
				output.getAttributes().put("name", "jquery-1.11.2.min.js");
			else
				output.getAttributes().put("name", "jquery-1.11.2.js");
			output.getAttributes().put("library", "jQuery");
			root.addComponentResource(context, output, "head");
		}
		if (loadJQueryUI) {
			UIOutput output = new UIOutput();
			output.setRendererType("javax.faces.resource.Script");
			if (isProduction)
				output.getAttributes().put("name", "jquery-ui-1.11.3.min.js");
			else
				output.getAttributes().put("name", "jquery-ui-1.11.3.min.js");
			output.getAttributes().put("library", "jQuery");
			root.addComponentResource(context, output, "head");
		}

		if (loadAngularJS) {
			UIOutput output = new UIOutput();
			output.setRendererType("javax.faces.resource.Script");
			if (isProduction) {
				output.getAttributes().put("name", "angular.min.js");
			} else {
				output.getAttributes().put("name", "angular.js");
			}
			output.getAttributes().put("library", "AngularJS");

			root.addComponentResource(context, output, "head");
		}
		{
			UIOutput output = new UIOutput();
			output.setRendererType("javax.faces.resource.Script");
			if (isProduction) {
				output.getAttributes().put("name", "jua-0.1.0.min.js");
			} else {
				output.getAttributes().put("name", "jua-0.1.0.js");
			}
			output.getAttributes().put("library", "AngularFaces");
			root.addComponentResource(context, output, "head");
		}

		if (loadAngularMessages) {
			UIOutput output = new UIOutput();
			output.setRendererType("javax.faces.resource.Script");
			if (isProduction) {
				output.getAttributes().put("name", "angular-messages.min.js");
			} else {
				output.getAttributes().put("name", "angular-messages.js");
			}
			output.getAttributes().put("library", "AngularJS");

			root.addComponentResource(context, output, "head");
		}
		{
			UIOutput output = new UIOutput();
			output.setRendererType("javax.faces.resource.Script");
			if (isProduction) {
				output.getAttributes().put("name", "angularfaces-core.js");
			} else {
				output.getAttributes().put("name", "angularfaces-core.js");
			}
			output.getAttributes().put("library", "AngularFaces");
			root.addComponentResource(context, output, "head");
		}
		{
			UIOutput output = new UIOutput();
			output.setRendererType("javax.faces.resource.Script");
			if (isProduction) {
				output.getAttributes().put("name", "angularfaces-directives.js");
			} else {
				output.getAttributes().put("name", "angularfaces-directives.js");
			}
			output.getAttributes().put("library", "AngularFaces");
			root.addComponentResource(context, output, "head");
		}

		{
			boolean hasLocalizedBundle = false;
			Application app = context.getApplication();
			ResourceHandler rh = app.getResourceHandler();
			Resource rdp;
			Iterator<Locale> preferredLanguages = context.getExternalContext().getRequestLocales();
			while (preferredLanguages.hasNext()) {
				final String messageBundleFile = "messages_" + preferredLanguages.next().getLanguage() + ".js";
				rdp = rh.createResource(messageBundleFile, "AngularFaces");
				if (rdp != null) { // rdp is null if the language .js is not
									// present in jar
					UIOutput output = new UIOutput();
					output.setRendererType("javax.faces.resource.Script");
					output.getAttributes().put("name", messageBundleFile);
					output.getAttributes().put("library", "AngularFaces");
					root.addComponentResource(context, output, "head");
					hasLocalizedBundle = true;
					break;
				}

			}
			if (!hasLocalizedBundle) {
				UIOutput output = new UIOutput();
				output.setRendererType("javax.faces.resource.Script");
				output.getAttributes().put("name", "messages_en.js");
				output.getAttributes().put("library", "AngularFaces");
				root.addComponentResource(context, output, "head");
			}
		}

		Map<String, Object> viewMap = root.getViewMap();
		Map<String, String> resourceMap = (Map<String, String>) viewMap.get(RESOURCE_KEY);
		if (null != resourceMap) {
			for (Entry<String, String> entry : resourceMap.entrySet()) {
				String file = entry.getValue();
				String library = entry.getKey().substring(0, entry.getKey().length() - file.length() - 1);
				UIOutput output = new UIOutput();
				output.setRendererType("javax.faces.resource.Script");
				output.getAttributes().put("name", file);
				output.getAttributes().put("library", library);
				root.addComponentResource(context, output, "head");

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

	private void time(String description, Runnable runnable) {
		long timer = System.nanoTime();
		runnable.run();
		long time = System.nanoTime() - timer;
		LOGGER.fine((time / 1000) / 1000.0d + " ms " + description);
	}

	private boolean isAngularFacesRequest() {
		FacesContext ctx = FacesContext.getCurrentInstance();
		PartialViewContext pvc = ctx.getPartialViewContext();
		Collection<String> myRenderIds = pvc.getRenderIds();
		boolean isAngularFacesRequest = false;
		if (null != myRenderIds) {
			if (myRenderIds.contains("angular")) {
				isAngularFacesRequest = true;
			} else {
				for (Object id : myRenderIds) {
					if (id instanceof String) {
						if (((String) id).endsWith(":angular")) {
							isAngularFacesRequest = true;
							break;
						}
					}
				}
			}
		}
		return isAngularFacesRequest;
	}

	/**
	 * Registers a JS file that needs to be include in the header of the HTML
	 * file, but after jQuery and AngularJS.
	 * 
	 * @param library
	 *            The name of the sub-folder of the resources folder.
	 * @param resource
	 *            The name of the resource file within the library folder.
	 */
	public static void addResourceAfterAngularJS(String library, String resource) {
		FacesContext ctx = FacesContext.getCurrentInstance();
		UIViewRoot v = ctx.getViewRoot();
		Map<String, Object> viewMap = v.getViewMap();
		@SuppressWarnings("unchecked")
		Map<String, String> resourceMap = (Map<String, String>) viewMap.get(RESOURCE_KEY);
		if (null == resourceMap) {
			resourceMap = new HashMap<String, String>();
			viewMap.put(RESOURCE_KEY, resourceMap);
		}
		String key = library + "#" + resource;
		if (!resourceMap.containsKey(key)) {
			resourceMap.put(key, resource);
		}
	}

}
