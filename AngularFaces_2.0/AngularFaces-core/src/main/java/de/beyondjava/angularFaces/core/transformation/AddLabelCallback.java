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

import java.lang.reflect.Constructor;
import java.util.List;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;

import org.primefaces.component.outputlabel.OutputLabel;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGWordUtiltites;
import de.beyondjava.angularFaces.core.i18n.I18n;

/** Adds a label to a single component (if needed). */
public class AddLabelCallback implements VisitCallback {
	I18n i18n = null;

	int duplicateLabels = 0;

	@Override
	public VisitResult visit(VisitContext arg0, UIComponent parent) {
		if (!(parent instanceof UIComponent))
			return VisitResult.ACCEPT;
		List<UIComponent> children = parent.getChildren();
		for (int index = children.size() - 1; index >= 0; index--) {
			UIComponent kid = children.get(index);
			if (kid instanceof UIInput) {

				String caption = (String) AttributeUtilities.getAttribute(kid,"label");
				if (null == caption) {
					ValueExpression vex = kid.getValueExpression("value");
					if (null != vex) {
						String core = vex.getExpressionString();
						caption = NGWordUtiltites.labelFromELExpression(core);
					}
				}
				if (null != caption) {
					for (int j = 0; j < children.size(); j++) {
						UIComponent maybe = children.get(j);
						if (maybe instanceof HtmlOutputLabel) {
							if (kid.getId().equals(((HtmlOutputLabel) maybe).getFor())) {
								duplicateLabels++;
								if (j != index - 1) {
									System.out.println("Label has been restored at the wrong position");
									children.remove(j);
									if (j < index)
										index--;
								}
								continue;
							}
						}

					}
					if (index > 0) {
						UIComponent maybe = children.get(index - 1);
						if (maybe instanceof HtmlOutputLabel) {
							if (kid.getId().equals(((HtmlOutputLabel) maybe).getFor())) {
								duplicateLabels++;
								continue;
							}
						}

					}

					HtmlOutputLabel label;
					if (kid.getClass().getName().contains("primefaces")) {
						try {
							Class labelClass=Class.forName("org.primefaces.component.outputlabel.OutputLabel");
							Constructor c = labelClass.getConstructor();
							label = (HtmlOutputLabel) c.newInstance();
						} catch (ReflectiveOperationException error) {
							System.out.println("Couldn't create PrimeFaces OutputLabel: " + error);
							label = new HtmlOutputLabel();
						}
					} else {
						label = new HtmlOutputLabel();
					}
					label.setFor(kid.getId());
					if (null != caption) {
						label.setValue(translate(caption));
					}
					children.add(index, label);
				}
			}
		}

		return VisitResult.ACCEPT;
	}

	private String translate(String caption) {
		if (null == i18n)
			i18n = (I18n) ELTools.evalAsObject("#{i18n}");
		if (null == i18n)
			return caption;
		return i18n.translate(caption);
	}

}
