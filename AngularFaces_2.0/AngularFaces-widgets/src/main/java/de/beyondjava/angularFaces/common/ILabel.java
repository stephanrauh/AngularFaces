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
package de.beyondjava.angularFaces.common;

import javax.faces.component.UIComponent;

import de.beyondjava.angularFaces.core.ELTools;
import de.beyondjava.angularFaces.core.NGWordUtiltites;

public interface ILabel {
	public default String getLabel() {
		String l = (String) ((UIComponent) this).getAttributes().get("label");
		if (l == null) {
			String ngModel = ELTools.getNGModel((UIComponent) this);
			l = NGWordUtiltites.labelFromCamelCase(ngModel);
		}
		return l;
	}
}
