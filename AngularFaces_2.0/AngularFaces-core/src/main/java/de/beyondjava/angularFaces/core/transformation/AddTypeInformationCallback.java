package de.beyondjava.angularFaces.core.transformation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGBeanAttributeInfo;

public class AddTypeInformationCallback implements VisitCallback {

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent component) {
		if (component instanceof UIInput) {
			NGBeanAttributeInfo infos = ELTools.getBeanAttributeInfos(component);
			if (infos.isRequired()) {
				((UIInput) component).setRequired(true);
				component.getPassThroughAttributes().put("required","");
			}
			if (infos.getMax()>0) {
				((UIInput)component).getPassThroughAttributes().put("max", infos.getMax());
			}
			if (infos.getMin()>0) {
				((UIInput)component).getPassThroughAttributes().put("min", infos.getMin());
			}
			if (infos.getMaxSize()>0) {
				((UIInput)component).getPassThroughAttributes().put("ng-maxlength", infos.getMaxSize());
			}
			if (infos.getMinSize()>0) {
				((UIInput)component).getPassThroughAttributes().put("ng-minlength", infos.getMinSize());
			}
			if (infos.isNumeric()) {
				setType(component, "number");
			}
			else if (infos.isDate()) {
				setType(component, "date");
			}
		}
		return VisitResult.ACCEPT;
	}

	private void setType(UIComponent component, String type) {
		if (component.getClass().getName().equals("org.primefaces.component.inputtext.InputText")) {
			Method method;
			try {
				method = component.getClass().getMethod("setType", String.class);
				method.invoke(component, type);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// catch block required by compiler, can't happen in reality
			}
		}
		else {
			component.getPassThroughAttributes().put("type", type);
		}
	}

}
