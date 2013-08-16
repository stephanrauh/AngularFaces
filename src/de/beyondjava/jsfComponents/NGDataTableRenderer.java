package de.beyondjava.jsfComponents;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;

import javax.faces.component.UIComponent;
import javax.faces.context.*;
import javax.faces.model.DataModel;
import javax.faces.render.FacesRenderer;

import org.apache.commons.beanutils.PropertyUtils;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTableRenderer;

import de.beyondjava.jsfComponents.common.ELTools;

@FacesRenderer(componentFamily = "org.primefaces.component", rendererType = "de.beyondjava.NGDataTable")
public class NGDataTableRenderer extends DataTableRenderer {
   /**
    * Finishes the ng:dataTable tag.
    * 
    * @author Stephan Rauh http://www.beyondjava.net
    */
   @Override
   public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
      ResponseWriter writer = context.getResponseWriter();
      String initJS = nggetTableDataAsJSScript(context, component);
      writer.append(initJS);
      ngencodeMarkup(context, component);
   }

   /**
    * Begin the ng:dataTable tag.
    * 
    * @author Stephan Rauh http://www.beyondjava.net
    */
   public void ngencodeMarkup(FacesContext context, UIComponent component) throws IOException {
      ResponseWriter writer = context.getResponseWriter();

      writer.append("<table role=\"grid\">\r\n");
      writer.append("<tr role=\"row\">\r\n");
      for (UIComponent c : component.getChildren()) {
         writer.append("<th>");
         if (c instanceof Column) {
            String headerText = ((Column) c).getHeaderText();
            writer.append(headerText);
         }
         writer.append("</th>\r\n");
      }
      writer.append("</tr>\r\n");
      NGDataTable table = (NGDataTable) component;
      table.setRowIndex(0);
      String array = ELTools.getNGModel(table);
      String rowVar = table.getVar();
      String ngRepeat = "ng-repeat=\"" + rowVar + " in " + array + "\"";
      writer.append("<tr " + ngRepeat + ">\r\n");

      for (UIComponent c : component.getChildren()) {
         writer.append("<td >\r\n");
         ResponseWriter myWriter = new NGResponseWriter(writer, writer.getContentType(), writer.getCharacterEncoding(),
               rowVar);
         context.setResponseWriter(myWriter);
         if (c instanceof Column) {
            ((Column) c).encodeAll(context);
         }

         writer.append("</td>\r\n");
      }
      writer.append("</tr>\r\n");
      writer.append("</table>\r\n");
   }

   public String nggetTableDataAsJSScript(FacesContext context, UIComponent table) throws IOException {
      String jsObject = "";
      boolean firstRow = true;
      int rows = ((NGDataTable) table).getRowCount();
      DataModel dataModel = ((NGDataTable) table).getDataModel();
      for (int i = 0; i < rows; i++) {
         if (!firstRow) {
            jsObject += ",\r\n";
         }
         firstRow = false;
         jsObject += "{";
         dataModel.setRowIndex(i);
         Object row = dataModel.getRowData();
         try {
            Map description = PropertyUtils.describe(row);
            boolean firstColumn = true;
            Set<Entry<?, ?>> descriptionSet = description.entrySet();
            for (Entry<?, ?> column : descriptionSet) {
               String attribute = (String) column.getKey();
               Object value = column.getValue();
               if ((value instanceof String) || (value instanceof Integer) || (value instanceof Long)
                     || (value instanceof Boolean) || (value instanceof Double)) {
                  if (!firstColumn) {
                     jsObject += ", ";
                  }
                  firstColumn = false;
                  jsObject += attribute;
                  jsObject += ": ";
                  if (value instanceof String) {
                     jsObject += "'" + value + "'";
                  }
                  else {
                     jsObject += value;
                  }
               }
            }
         }
         catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         jsObject += "}";
      }
      StringBuilder writer = new StringBuilder();
      writer.append("\r\n\r\n");
      writer.append("<script language='Javascript'>\r\n");
      String var = ELTools.getNGModel(table);
      writer.append("function initDatatable($scope) {\r\n");
      writer.append("$scope." + var + "= [\r\n");
      writer.append(jsObject);
      writer.append("];\r\n");
      writer.append("}\r\n");
      writer.append("</script>\r\n");
      writer.append("\r\n\r\n");
      return writer.toString();
   }

}
