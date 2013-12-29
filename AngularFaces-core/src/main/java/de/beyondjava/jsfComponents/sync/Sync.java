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
package de.beyondjava.jsfComponents.sync;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

/**
 * Add AngularJS behaviour to a standard Primefaces InputText.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */

@FacesComponent("de.beyondjava.Sync")
public class Sync extends org.primefaces.component.inputtext.InputText {
   public static final String COMPONENT_FAMILY = "org.primefaces.component";

   @Override
   public String getFamily() {
      return COMPONENT_FAMILY;
   }

   @Override
   public String getStyle() {
      return "display:none";
   }

   /*
    * (non-Javadoc)
    * 
    * @see
    * javax.faces.component.UIInput#updateModel(javax.faces.context.FacesContext
    * )
    */
   @Override
   public void updateModel(FacesContext context) {
   }
}
