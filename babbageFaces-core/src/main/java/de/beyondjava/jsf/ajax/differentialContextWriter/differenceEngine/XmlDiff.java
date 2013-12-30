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
         List<String> changes) {
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
    * @param attributeChanges
    *           Call by reference parameter delivering the list of attributes
    *           that have to be modified.
    * @return false (aka GLOBAL_CHANGE_REQUIRED) if the parent node has to be
    *         exchanged completely via an update command.
    */
   private static boolean childHTMLTagAreEqualOrCanBeChangedLocally(List<HTMLTag> oldHTMLTags,
         List<HTMLTag> newHTMLTags, List<HTMLTag> updates, List<String> deletions, List<String> attributeChanges,
         List<String> inserts) {
      boolean needsUpdate = false;
      List<String> localDeletions = new ArrayList<>();
      List<String> localInserts = new ArrayList<>();
      List<HTMLTag> localUpdates = new ArrayList<>();
      oldHTMLTags = new ArrayList<>(oldHTMLTags); // defensive copying avoids
                                                  // side effects
      newHTMLTags = new ArrayList<>(newHTMLTags); // defensive copying avoids
                                                  // side effects
      if (oldHTMLTags.size() != newHTMLTags.size()) {
         List<HTMLTag> insertList = getRecentlyAddedHTMLTags(oldHTMLTags, newHTMLTags);
         if (insertList.size() > 0) {
            needsUpdate |= isThereANodeThatCannotBeInserted(insertList);
            if (oldHTMLTags.size() == 0) {
               // the insert command can only insert a node before or after
               // another node. If there's no such node, you can't insert
               // anything.
               needsUpdate = true;
            }
            if (!needsUpdate) {
               generateInsertCommands(newHTMLTags, localInserts, insertList, localUpdates);

            }
         }
         if (!needsUpdate) {
            List<HTMLTag> deleteList = getRecentlyAddedHTMLTags(newHTMLTags, oldHTMLTags);

            for (HTMLTag d : deleteList) {
               if (d.getId().isEmpty()) {
                  needsUpdate = true;
                  break;
               }
               else {
                  localDeletions.add(d.getId());
                  oldHTMLTags.remove(d);
               }
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
         if (!tagsAreEqualOrCanBeChangedLocally(o, newHTMLTags.get(indexNew), updates, deletions, attributeChanges,
               inserts)) {
            LOGGER.info("HTMLTags are different, require update of parent.");
            return GLOBAL_CHANGE_REQUIRED;
         }
         indexOld++;
         indexNew++;
      }
      if (localDeletions.size() > 0) {
         LOGGER.info("Adding HTMLTag deletions");
         deletions.addAll(localDeletions);
      }
      if (localInserts.size() > 0) {
         LOGGER.info("Adding HTMLTag insertions");
         inserts.addAll(localInserts);
      }
      if (localUpdates.size() > 0) {
         LOGGER.info("Adding HTMLTag update");
         updates.addAll(localUpdates);
      }

      return NO_CHANGE_REQUIRED;
   }

   /**
    * Generates the insert command of the AJAX response.
    * 
    * @param newHTMLTags
    * @param inserts
    * @param insertList
    */
   private static void generateInsertCommands(List<HTMLTag> newHTMLTags, List<String> inserts,
         List<HTMLTag> insertList, List<HTMLTag> updates) {
      for (HTMLTag insert : insertList) {
         HTMLTag parent = insert.getParent();
         for (int index = 0; index < parent.getChildren().size(); index++) {
            HTMLTag sibling = parent.getChildren().get(index);
            if (insert == sibling) {
               final String idOfNewTag = insert.getId();
               // needed to fix a Mojarra bug
               String temporaryDiv = "<div id=\"" + idOfNewTag + "\" />";
               String s;
               if (index == 0) {
                  s = "<insert id=\"" + idOfNewTag + "\"><before id=\"" + parent.getChildren().get(index + 1).getId()
                        + "\"><![CDATA[" + temporaryDiv + "]]></before></insert>";
               }
               else {
                  s = "<insert id=\"" + idOfNewTag + "\"><after id=\"" + parent.getChildren().get(index - 1).getId()
                        + "\"><![CDATA[" + temporaryDiv + "]]></after></insert>";
               }
               inserts.add(s);
               // needed to fix a Mojarra bug
               updates.add(insert);
               newHTMLTags.remove(insert);
               break;

            }
         }
      }
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
   public static List<HTMLTag> getDifferenceOfHTMLTags(HTMLTag oldHTMLTag, HTMLTag newHTMLTag, List<String> deletions,
         List<String> changes, List<String> inserts) {
      List<HTMLTag> updates = new ArrayList<>();

      if (!tagsAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag, updates, deletions, changes, inserts)) {
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
   private static List<HTMLTag> getNonEmptyHTMLTags(List<HTMLTag> childHTMLTags) {
      List<HTMLTag> nonEmpty = new ArrayList<>();
      for (int i = 0; i < childHTMLTags.size(); i++) {
         HTMLTag n = childHTMLTags.get(i);
         if (n.hasAttributes() || (n.getChildren().size() > 0)
               || ((n.getNodeName() != null) && (n.getNodeName().length() > 0))
               || (n.getInnerHTML().toString().trim().length() > 0)) {
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
   public static List<HTMLTag> getRecentlyAddedHTMLTags(List<HTMLTag> oldHTMLTags, List<HTMLTag> newHTMLTags) {
      List<HTMLTag> result = new ArrayList<>();
      Map<String, HTMLTag> alreadyThere = new HashMap<String, HTMLTag>();
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

   /**
    * Verifies that every node of a given list has an id (a neccessary
    * precondition to insert it into a DOM tree).
    * 
    * @param needsUpdate
    * @param insertList
    * @return
    */
   private static boolean isThereANodeThatCannotBeInserted(List<HTMLTag> insertList) {
      boolean needsUpdate = false;
      for (HTMLTag insert : insertList) {
         if (insert.getId().isEmpty()) {
            LOGGER.warning("TODO: add an ID to the node to be inserted");
            needsUpdate = true;
            break;
         }
      }
      return needsUpdate;
   }

   private static boolean tagsAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
         List<HTMLTag> updates, List<String> deletions, List<String> attributeChanges, List<String> inserts) {
      if (!HTMLTagNamesAreEqualsOrCanBeChangedLocally(oldHTMLTag, newHTMLTag)) {
         return false;
      }
      if (!idsAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag)) {
         return false;
      }

      if (!areStringsEqualOrCanBeChangedLocally(oldHTMLTag.getInnerHTML().toString(), newHTMLTag.getInnerHTML()
            .toString())) {
         return false;
      }
      List<String> localAttributeChanges = new ArrayList<>();
      boolean attributeChangeRequiresUpdate = (GLOBAL_CHANGE_REQUIRED == attributesAreEqualOrCanBeChangedLocally(
            oldHTMLTag, newHTMLTag, localAttributeChanges));

      if (!attributeChangeRequiresUpdate) {
         List<HTMLTag> oldHTMLTags = getNonEmptyHTMLTags(oldHTMLTag.getChildren());
         List<HTMLTag> newHTMLTags = getNonEmptyHTMLTags(newHTMLTag.getChildren());
         boolean childHTMLTagHaveChanged = !childHTMLTagAreEqualOrCanBeChangedLocally(oldHTMLTags, newHTMLTags,
               updates, deletions, attributeChanges, inserts);
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
