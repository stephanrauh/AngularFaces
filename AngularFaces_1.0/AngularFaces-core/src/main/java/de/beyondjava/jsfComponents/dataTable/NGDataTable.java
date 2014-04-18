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
package de.beyondjava.jsfComponents.dataTable;

import javax.faces.component.FacesComponent;
import javax.faces.model.DataModel;

import org.primefaces.component.datatable.DataTable;

/**
 * NGDatatable is a JSF data table with AngularJS support.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.NGDataTable")
public class NGDataTable extends DataTable {

   @Override
   public DataModel getDataModel() {
      // TODO Auto-generated method stub
      return super.getDataModel();
   }

   @Override
   public String getRendererType() {
      return "de.beyondjava.NGDataTable";
   }
}
