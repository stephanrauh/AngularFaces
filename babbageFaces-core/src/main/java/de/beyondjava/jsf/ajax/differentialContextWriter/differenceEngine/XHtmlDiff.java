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
package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.util.*;
import java.util.logging.Logger;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

public class XHtmlDiff {
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff");

   /**
    * Verifies whether one of the update tags lacks an id.
    * 
    * @param changed
    * @deprecated isn't needed - will be removed soon
    */
   @Deprecated
   private static void fix(List<HTMLTag> changed) {
      List<HTMLTag> temp = new ArrayList<>();

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

   /**
    * Evaluates the smallest possible set of changes of two HTML trees and tries
    * to fix changes without id.
    * 
    * @param oldDomTree
    * @param newDomTree
    * @param deletions
    * @param changes
    * @param inserts
    * @return
    * @deprecated every change has an id by design - method can be removed soon
    */
   @Deprecated
   public static List<HTMLTag> getDifferenceOfHTMLTags(HTMLTag oldDomTree, HTMLTag newDomTree, List<String> deletions,
         List<String> changes, List<String> inserts) {
      List<HTMLTag> updates = XmlDiff.getDifferenceOfHTMLTags(oldDomTree, newDomTree, deletions, changes, inserts);

      // 1. schauen ob die HTMLTags eine Id haben und wenn ja die Parents loopen
      // bis meine eine HTMLTag mit ID hat
      // 2. durch den ersten Schritt kann passieren das man jetzt Childs von den
      // neuen Parent bereits als updates hat
      // also sortieren wir nochmal aus
      fix(updates);

      return updates;
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
