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
package de.beyondjava.angularFaces.components.puiLabel;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagHandler;

public class BodyTagHandler extends AddLabelsAndMessagesHandler {
	
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.components.puiLabel.BodyTagHandler");

	
	private TagHandler defaultComponentHandler;
	
	public BodyTagHandler(javax.faces.view.facelets.ComponentConfig config) {
		super(config);
		try {
			Class<?> myFacesComponentHandler = Class.forName("org.apache.myfaces.view.facelets.tag.jsf.html.HtmlComponentHandler");
			Constructor<?> constructor = myFacesComponentHandler.getConstructor(javax.faces.view.facelets.ComponentConfig.class);
			defaultComponentHandler = (TagHandler)constructor.newInstance(config); 
		} catch (ReflectiveOperationException e) {
			try {
				Class<?> mojarraComponentHandler = Class.forName("com.sun.faces.facelets.tag.jsf.html.HtmlComponentHandler");
				Constructor<?> constructor = mojarraComponentHandler.getConstructor(javax.faces.view.facelets.ComponentConfig.class);
				defaultComponentHandler = (TagHandler)constructor.newInstance(config); 
			} catch (ReflectiveOperationException e2) {
				LOGGER.severe("AngularFaces couldn't instantiate the default component handler neither for MyFaces nor for Mojarra");
			}
		}
	}

	@Override
	public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
		defaultComponentHandler.apply(ctx, parent); // Delegate job further to first next tag in tree hierarchy.
		List<UIComponent> children = parent.getChildren();
		super.apply(ctx, children.get(children.size()-1));
	}
}
