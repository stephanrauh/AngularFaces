/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsfComponents;

import javax.faces.component.FacesComponent;
import javax.faces.model.DataModel;

import org.primefaces.component.datatable.DataTable;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
@FacesComponent("de.beyondjava.NGDataTable")
public class NGDataTable extends DataTable {

   /*
    * (non-Javadoc)
    * 
    * @see org.primefaces.component.api.UIData#getDataModel()
    */
   @Override
   public DataModel getDataModel() {
      // TODO Auto-generated method stub
      return super.getDataModel();
   }

   @Override
   public String getRendererType() {
      // return "org.primefaces.component.DataTableRenderer";
      return "de.beyondjava.NGDataTable";
   }
}
