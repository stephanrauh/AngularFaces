package de.beyondjava.jsfComponents.dataTable;

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
import org.primefaces.component.datatable.*;

import de.beyondjava.jsfComponents.common.ELTools;

/**
 * NGDatatable is a JSF data table with AngularJS support.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
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
      List<String> valueExpressions = ngencodeMarkup(context, component);
      String initJS = nggetTableDataAsJSScript(context, component);
      writer.append(initJS);
   }

   /**
    * Begins the ng:dataTable tag.
    * 
    * @author Stephan Rauh http://www.beyondjava.net
    */
   public List<String> ngencodeMarkup(FacesContext context, UIComponent component) throws IOException {
      ResponseWriter originalWriter = context.getResponseWriter();

      originalWriter.append("<div class='ui-datatable ui-widget'>\r\n");
      originalWriter.append("<div class='ui-datatable-tablewrapper'>\r\n");
      originalWriter.append("<table role=\"grid\">\r\n");
      originalWriter.append("<tr role=\"row\">\r\n");
      for (UIComponent c : component.getChildren()) {
         originalWriter.append("<th class='ui-state-default'>");
         if (c instanceof Column) {
            String headerText = ((Column) c).getHeaderText();
            originalWriter.append(headerText);
         }
         originalWriter.append("</th>\r\n");
      }
      originalWriter.append("</tr>\r\n");
      NGDataTable table = (NGDataTable) component;
      String rowVar = table.getVar();
      replaceResponseWriter(context, originalWriter, table);
      table.setRowIndex(0);
      String array = ELTools.getNGModel(table);
      String ngRepeat = "ng-repeat=\"" + rowVar + " in " + array + "\"";
      originalWriter.append("<tr " + ngRepeat + " class='ui-widget-content ui-datatable-even'>\r\n");

      for (UIComponent c : component.getChildren()) {
         originalWriter.append("<td >\r\n");
         if (c instanceof Column) {
            ((Column) c).encodeAll(context);
         }

         originalWriter.append("</td>\r\n");
      }
      originalWriter.append("</tr>\r\n");
      originalWriter.append("</table>\r\n");
      originalWriter.append("</div>\r\n");
      originalWriter.append("</div>\r\n");
      List<String> valueExpressions = ((NGDataTableResponseWriter) context.getResponseWriter()).getValueExpressions();
      // restore the responseWriter
      context.setResponseWriter(originalWriter);
      return valueExpressions;
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

   private void replaceResponseWriter(FacesContext context, ResponseWriter writer, UIComponent component) {
      DataTable table = (DataTable) component;
      String rowVar = table.getVar();

      ResponseWriter myWriter = new NGDataTableResponseWriter(writer, writer.getContentType(),
            writer.getCharacterEncoding(), rowVar);
      context.setResponseWriter(myWriter);
   }

}
