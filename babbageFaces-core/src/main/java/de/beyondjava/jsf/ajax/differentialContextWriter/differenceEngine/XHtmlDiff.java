package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.util.*;
import java.util.logging.Logger;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

public class XHtmlDiff {
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff");

   private static void fix(ArrayList<HTMLTag> changed) {
      ArrayList<HTMLTag> temp = new ArrayList<HTMLTag>();

      // loop HTMLTags to removed HTMLTags without id and resolve to their
      // parent with
      // id
      Iterator<HTMLTag> HTMLTagIterator = changed.iterator();
      while (HTMLTagIterator.hasNext()) {
         HTMLTag element = HTMLTagIterator.next();
         String id = element.getId();
         if (id.isEmpty()) {
            LOGGER.info("Fixing a HTMLTag without ID");
            HTMLTag parent = element.getParent();

            while (parent != null) {
               String parentId = parent.getId();
               if (!parentId.isEmpty()) {
                  break;
               }
               parent = parent.getParent();
            }
            if (null != parent) {
               LOGGER.info("replaced HTMLTag by HTMLTag with id=" + parent.getId());
            }
            else {
               LOGGER.severe("cannot replace HTMLTag");
            }
            HTMLTagIterator.remove();
            temp.add(parent);
         }
      }

      changed.addAll(temp);
      temp.clear();

      // check if a HTMLTag is child/parent from another need
      // we only need to the render the highest one
      for (HTMLTag outer : changed) {
         for (HTMLTag inner : changed) {
            if (isParentHTMLTag(outer, inner)) {
               temp.add(inner);
            }
         }
      }

      for (HTMLTag toRemove : temp) {
         changed.remove(toRemove);
      }
   }

   public static ArrayList<HTMLTag> getDifferenceOfHTMLTags(HTMLTag oldDocument, HTMLTag newDocument,
         ArrayList<String> deletions, ArrayList<String> changes) {
      ArrayList<HTMLTag> diff = XmlDiff.getDifferenceOfHTMLTags(oldDocument, newDocument, deletions, changes);

      // 1. schauen ob die HTMLTags eine Id haben und wenn ja die Parents loopen
      // bis meine eine HTMLTag mit ID hat
      // 2. durch den ersten Schritt kann passieren das man jetzt Childs von den
      // neuen Parent bereits als DIff hat
      // also sortieren wir nochmal aus
      fix(diff);

      return diff;
   }

   private static boolean isParentHTMLTag(HTMLTag parent, HTMLTag child) {
      HTMLTag childParent = child.getParent();

      while (childParent != null) {
         if (childParent == parent) {
            return true;
         }
         childParent = childParent.getParent();
      }

      return false;
   }
}
