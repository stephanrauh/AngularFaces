package de.beyondjava.jsf.ajax.context;

import java.util.List;

import javax.faces.component.*;
import javax.faces.event.*;

/**
 * This class add a unique marker to each JSF DOM node which doesn't change even
 * if the JSF elements visibility (rendered attribute) changes. The marker
 * enables the DifferentialResponseWriter to recognize nodes that can be
 * inserted into the HTML tree.
 * 
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class BabbagePreRenderViewListener implements SystemEventListener {

   /**
    * @param root
    * @param parentID
    */
   private void annotateChildren(UIComponent root, String parentID) {
      int index = 0;
      List<UIComponent> children = root.getChildren();
      for (UIComponent c : children) {
         final String id = parentID + String.valueOf(index++);
         if (c.getPassThroughAttributes().get("babbageid") != null) {
            if (!c.getPassThroughAttributes().get("babbageid").equals(id)) {
               System.out.println("Error while annotating" + id);
               c.getPassThroughAttributes().remove("babbageid");
            }
         }
         c.getPassThroughAttributes().put("babbageid", id);
         annotateChildren(c, id + ".");
      }
   }

   @Override
   public boolean isListenerForSource(Object source) {
      return true;
   }

   @Override
   public void processEvent(SystemEvent event) throws AbortProcessingException {
      UIViewRoot root = (UIViewRoot) event.getSource();

      String parentID = "";
      annotateChildren(root, parentID);
   }

}