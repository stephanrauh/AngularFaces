/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents.dataTable;

import javax.faces.component.FacesComponent;
import javax.faces.model.DataModel;

import org.primefaces.component.datatable.DataTable;

/**
 * NGDatatable is a JSF data table with AngularJS support.
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
