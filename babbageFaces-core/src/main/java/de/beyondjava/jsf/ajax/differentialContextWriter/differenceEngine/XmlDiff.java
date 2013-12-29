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

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.*;

public class XmlDiff {
   public static final boolean ADDED_LOCAL_CHANGE = true;
   public static final boolean GLOBAL_CHANGE_REQUIRED = false;
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff");

   public static final boolean NO_CHANGE_REQUIRED = true;

   private static boolean areStringsEqualOrCanBeChangedLocally(String oldString, String newString) {
      if ((oldString == null) && (newString == null)) {
         return true;
      }

      if ((oldString == null) || (newString == null)) {
         return false;
      }

      return oldString.equals(newString);
   }

   /**
    * @param oldHTMLTag
    */
   private static boolean attributeHasBeenDeleted(HTMLTag oldHTMLTag) {
      {
         HTMLAttribute newAttribute = null;
         HTMLAttribute oldAttribute = null;
         String id = null;
         for (int i = 0; i < oldHTMLTag.getAttributes().size(); i++) {
            oldAttribute = oldHTMLTag.getAttributes().get(i);
            final String attributeName = oldAttribute.name;
            newAttribute = oldHTMLTag.getAttribute(attributeName);

            if (null == newAttribute) {
               // we cannot fix this attribute change locally, the entire DOM
               // node has to be replaced
               return true;
            }
         }
      }
      return false;
   }

   public static boolean attributesAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
         ArrayList<String> changes) {
      if (!oldHTMLTag.hasAttributes() && !newHTMLTag.hasAttributes()) {
         return true;
      }

      if (oldHTMLTag.getAttributes().size() > newHTMLTag.getAttributes().size()) {
         return GLOBAL_CHANGE_REQUIRED;
      }

      // Have attributes been deleted?
      boolean attributeChangeMustBeFixedGlobally = attributeHasBeenDeleted(oldHTMLTag);
      if (attributeChangeMustBeFixedGlobally) {
         return GLOBAL_CHANGE_REQUIRED;
      }

      HTMLAttribute newAttribute = null;
      HTMLAttribute oldAttribute = null;
      StringBuffer changeAttributes = new StringBuffer();
      String id = null;
      for (int i = 0; i < newHTMLTag.getAttributes().size(); i++) {
         newAttribute = newHTMLTag.getAttributes().get(i);
         final String newString = String.valueOf(newAttribute.value);
         final String attributeName = newAttribute.name;
         oldAttribute = oldHTMLTag.getAttribute(attributeName);

         if (null == oldAttribute) {
            changeAttributes.append("<attribute name=\"" + attributeName + "\" value=\"" + newString + "\"/>");
         }
         else {
            // the value of the attribute has changed
            String oldString = String.valueOf(oldAttribute.value);
            if (attributeName.equals("id")) {
               id = oldString;
            }
            if (attributeName.equals("action") && oldString.contains("jsessionid")) {
               int start = oldString.indexOf(";jsessionid");
               int end = oldString.indexOf(";", start + 1);
               if (end > 0) {
                  oldString = oldString.substring(0, start) + oldString.substring(end);
               }
               else {
                  oldString = oldString.substring(0, start);
               }
            }
            if (!(oldString.equals(newString))) {
               changeAttributes.append("<attribute name=\"" + attributeName + "\" value=\"" + newString + "\"/>");
            }
         }
      }
      if (changeAttributes.length() > 0) {
         if ((null != id) && (id.length() > 0)) {
            String c = "<attributes id=\"" + id + "\">" + changeAttributes.toString() + "</attributes>";
            changes.add(c);
            return ADDED_LOCAL_CHANGE;
         }
         else {
            return GLOBAL_CHANGE_REQUIRED; // this case needs an update of the
                                           // parent
         }
      }

      return NO_CHANGE_REQUIRED;
   }

   /**
    * Compares to lists of HTML tags (the new and the old version of the same
    * DOM subtree). Returns the smallest possible set of changes required to
    * transform the old to the new DOM subtree.
    * 
    * @param oldHTMLTags
    *           the old version of a DOM subtree
    * @param newHTMLTags
    *           the new version of a DOM subtree
    * @param updates
    *           Call by reference parameter delivering the list of DOM trees
    *           that have to be exchanged completely via an update command.
    * @param deletions
    *           Call by reference parameter delivering the list of DOM trees
    *           that have been deleted.
    * @param changes
    *           Call by reference parameter delivering the list of attributes
    *           that have to be modified.
    * @return false (aka GLOBAL_CHANGE_REQUIRED) if the parent node has to be
    *         exchanged completely via an update command.
    */
   private static boolean childHTMLTagAreEqualOrCanBeChangedLocally(ArrayList<HTMLTag> oldHTMLTags,
         ArrayList<HTMLTag> newHTMLTags, ArrayList<HTMLTag> updates, ArrayList<String> deletions,
         ArrayList<String> changes) {
      boolean needsUpdate = false;
      ArrayList<String> localDeletions = new ArrayList<String>();
      ArrayList<String> localChanges = new ArrayList<String>();
      if (oldHTMLTags.size() != newHTMLTags.size()) {
         ArrayList<HTMLTag> insertList = getRecentlyAddedHTMLTags(oldHTMLTags, newHTMLTags);
         if (insertList.size() > 0) {
            LOGGER.warning("TODO: add insert");
            needsUpdate = true;
         }
         if (!needsUpdate) {
            ArrayList<HTMLTag> deleteList = getRecentlyAddedHTMLTags(newHTMLTags, oldHTMLTags);

            for (HTMLTag d : deleteList) {
               localDeletions.add(d.getId());
               oldHTMLTags.remove(d);
            }
         }
         if (needsUpdate) {
            LOGGER.info("HTMLTags counts are different, require update of parent.");
            return GLOBAL_CHANGE_REQUIRED;
         }
      }

      int indexOld = 0;
      int indexNew = 0;
      while (indexOld < oldHTMLTags.size()) {
         HTMLTag o = (oldHTMLTags.get(indexOld));
         if (!tagsAreEqualOrCanBeChangedLocally(o, newHTMLTags.get(indexNew), updates, deletions, changes)) {
            LOGGER.info("HTMLTags are different, require update of parent.");
            return GLOBAL_CHANGE_REQUIRED;
         }
         indexOld++;
         indexNew++;
      }
      if (localChanges.size() > 0) {
         LOGGER.info("Adding attribute changes");
         changes.addAll(localChanges);
      }
      if (localDeletions.size() > 0) {
         LOGGER.info("Adding HTMLTag deletions");
         deletions.addAll(localDeletions);
      }

      return NO_CHANGE_REQUIRED;
   }

   /**
    * Evaluates the most simple changes to transform the old HTML tag to the new
    * HTML tag.
    * 
    * @param oldHTMLTag
    * @param newHTMLTag
    * @param deletions
    * @param changes
    * @return
    */
   public static ArrayList<HTMLTag> getDifferenceOfHTMLTags(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
         ArrayList<String> deletions, ArrayList<String> changes) {
      ArrayList<HTMLTag> updates = new ArrayList<HTMLTag>();

      if (!tagsAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag, updates, deletions, changes)) {
         LOGGER.info("HTMLTags are different, require update of parent. Old HTMLTag:" + oldHTMLTag.getDescription()
               + " new HTMLTag: " + newHTMLTag.getDescription());
         updates.add(newHTMLTag);
      }

      return updates;
   }

   /**
    * @param childHTMLTags
    * @return
    */
   private static ArrayList<HTMLTag> getNonEmptyHTMLTags(List<HTMLTag> childHTMLTags) {
      ArrayList<HTMLTag> nonEmpty = new ArrayList<HTMLTag>();
      for (int i = 0; i < childHTMLTags.size(); i++) {
         HTMLTag n = childHTMLTags.get(i);
         if (n.hasAttributes() || (n.children.size() > 0)
               || ((n.getNodeName() != null) && (n.getNodeName().length() > 0))
               || (n.innerHTML.toString().trim().length() > 0)) {
            if (n.hasAttributes() && (n.getAttribute("name") != null)) {
               if (!n.getAttribute("name").value.equals("javax.faces.ViewState")) {
                  nonEmpty.add(n);
               }
            }
            else {
               nonEmpty.add(n);
            }
         }
      }
      return nonEmpty;
   }

   /**
    * Does the new list of HTML tags one or more tags that weren't present in
    * the old list of HTML tags? Note that the function can return the list of
    * deleted tags be swapping the parameters.
    * 
    * @param oldHTMLTags
    * @param newHTMLTags
    * @return The list of HTML tags that have been added.
    */
   public static ArrayList<HTMLTag> getRecentlyAddedHTMLTags(ArrayList<HTMLTag> oldHTMLTags,
         ArrayList<HTMLTag> newHTMLTags) {
      ArrayList<HTMLTag> result = new ArrayList<HTMLTag>();
      HashMap<String, HTMLTag> alreadyThere = new HashMap<String, HTMLTag>();
      for (int i = 0; i < oldHTMLTags.size(); i++) {
         HTMLTag n = oldHTMLTags.get(i);
         String desc = n.getDescription();
         alreadyThere.put(desc, n);
      }
      for (int i = 0; i < newHTMLTags.size(); i++) {
         HTMLTag n = newHTMLTags.get(i);
         String desc = n.getDescription();
         if (!alreadyThere.containsKey(desc)) {
            result.add(n);
         }
      }
      return result;
   }

   private static boolean HTMLTagNamesAreEqualsOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag) {
      return oldHTMLTag.getNodeName().equals(newHTMLTag.getNodeName());
   }

   public static boolean idsAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag) {
      return oldHTMLTag.getId().equals(newHTMLTag.getId());
   }

   private static boolean tagsAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
         ArrayList<HTMLTag> updates, ArrayList<String> deletions, ArrayList<String> attributeChanges) {
      if (!HTMLTagNamesAreEqualsOrCanBeChangedLocally(oldHTMLTag, newHTMLTag)) {
         return false;
      }
      if (!idsAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag)) {
         return false;
      }

      if (!areStringsEqualOrCanBeChangedLocally(oldHTMLTag.innerHTML.toString(), newHTMLTag.innerHTML.toString())) {
         return false;
      }
      ArrayList<String> localAttributeChanges = new ArrayList<String>();
      boolean attributeChangeRequiresUpdate = (GLOBAL_CHANGE_REQUIRED == attributesAreEqualOrCanBeChangedLocally(
            oldHTMLTag, newHTMLTag, localAttributeChanges));

      if (!attributeChangeRequiresUpdate) {
         ArrayList<HTMLTag> oldHTMLTags = getNonEmptyHTMLTags(oldHTMLTag.getChildren());
         ArrayList<HTMLTag> newHTMLTags = getNonEmptyHTMLTags(newHTMLTag.getChildren());
         boolean childHTMLTagHaveChanged = !childHTMLTagAreEqualOrCanBeChangedLocally(oldHTMLTags, newHTMLTags,
               updates, deletions, attributeChanges);
         if (childHTMLTagHaveChanged) {
            updates.add(newHTMLTag);
         }
      }
      if (!updates.contains(newHTMLTag)) {
         if (attributeChangeRequiresUpdate) {
            LOGGER.info("Attributes are different, require update of parent. Old HTMLTag:"
                  + oldHTMLTag.getDescription() + " new HTMLTag: " + newHTMLTag.getDescription());
            updates.add(newHTMLTag);
            return true;
         }
      }
      if (localAttributeChanges.size() > 0) {
         attributeChanges.addAll(localAttributeChanges);
      }

      return true;
   }
}
