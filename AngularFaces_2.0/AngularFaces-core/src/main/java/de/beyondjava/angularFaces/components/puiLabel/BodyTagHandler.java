package de.beyondjava.angularFaces.components.puiLabel;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.logging.Logger;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIMessage;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;

import de.beyondjava.angularFaces.components.puiMessage.PuiMessage;
import de.beyondjava.angularFaces.components.puiSync.PuiSync;
import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGWordUtiltites;
import de.beyondjava.angularFaces.core.i18n.I18n;
import de.beyondjava.angularFaces.core.transformation.AttributeUtilities;

public class BodyTagHandler extends AddLabelsAndMessagesHandler {
	
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator");

	
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
