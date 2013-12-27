package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.util.*;
import java.util.logging.Logger;

import org.w3c.dom.Element;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.*;
import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag.HTMLAttribute;

public class XmlDiff {
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff");

   private static boolean areStringsEqualOrCanBeChangedLocally(String oldString, String newString) {
      if ((oldString == null) && (newString == null)) {
         return true;
      }

      if ((oldString == null) || (newString == null)) {
         return false;
      }

      return oldString.equals(newString);
   }

   public static boolean attributesAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
         ArrayList<String> deletions, ArrayList<String> changes) {
      if (!oldHTMLTag.hasAttributes() && !newHTMLTag.hasAttributes()) {
         return true;
      }

      if (oldHTMLTag.hasAttributes() != newHTMLTag.hasAttributes()) {
         return false;
      }

      if (oldHTMLTag.getAttributes().size() != newHTMLTag.getAttributes().size()) {
         return false;
      }

      HTMLAttribute newAttribute = null;
      HTMLAttribute oldAttribute = null;
      StringBuffer changeAttributes = new StringBuffer();
      String id = null;
      for (int i = 0; i < newHTMLTag.getAttributes().size(); i++) {
         newAttribute = newHTMLTag.getAttributes().get(i);
         final String attributeName = newAttribute.name;
         oldAttribute = oldHTMLTag.getAttribute(attributeName);

         if (null == oldAttribute) {
            return false;
         }

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
         final String newString = String.valueOf(newAttribute.value);
         if (!(oldString.equals(newString))) {
            changeAttributes.append("<attribute name=\"" + attributeName + "\" value=\"" + newString + "\"/>");
         }
      }
      if (changeAttributes.length() > 0) {
         if ((null != id) && (id.length() > 0)) {
            String c = "<attributes id=\"" + id + "\">" + changeAttributes.toString() + "</attributes>";
            changes.add(c);
            return true;
         }
         else {
            return false; // this case needs an update of the parent
         }
      }

      return true;
   }

   private static boolean childHTMLTagAreEqualOrCanBeChangedLocally(ArrayList<HTMLTag> oldHTMLTags,
         ArrayList<HTMLTag> newHTMLTags, HTMLTag newParentHTMLTag, ArrayList<HTMLTag> diff,
         ArrayList<String> deletions, ArrayList<String> changes) {
      boolean needsUpdate = false;
      ArrayList<String> localDeletions = new ArrayList<String>();
      ArrayList<String> localChanges = new ArrayList<String>();
      if (oldHTMLTags.size() != newHTMLTags.size()) {
         ArrayList<HTMLTag> insertList = getRecentlyAddedHTMLTags(oldHTMLTags, newHTMLTags);
         if ((oldHTMLTags.size() < newHTMLTags.size()) && (insertList.size() == 0)) {
            // implementation error - must be debugged
            insertList = getRecentlyAddedHTMLTags(oldHTMLTags, newHTMLTags);
         }
         if (insertList.size() > 0) {
            needsUpdate = true;
         }
         if (!needsUpdate) {
            ArrayList<HTMLTag> deleteList = getRecentlyAddedHTMLTags(newHTMLTags, oldHTMLTags);

            for (HTMLTag d : deleteList) {
               if (d instanceof Element) {
                  String id = (((Element) d).getAttribute("id"));
                  if (null != id) {
                     localDeletions.add(id);
                  }
                  else {
                     localDeletions.clear();
                     needsUpdate = true;
                     break;
                  }
               }
            }
         }
         LOGGER.warning("TODO: add insert");
         if (needsUpdate) {
            LOGGER.info("HTMLTags counts are different, require update of parent. Parent HTMLTag:"
                  + newParentHTMLTag.getDescription());
            diff.add(newParentHTMLTag);
            return false;
         }
      }

      int indexOld = 0;
      int indexNew = 0;
      while (indexOld < oldHTMLTags.size()) {
         HTMLTag o = (oldHTMLTags.get(indexOld));
         if (o instanceof Element) {
            String id = ((Element) o).getAttribute("id");
            if (localDeletions.contains(id)) {
               indexOld++;
               continue;
            }
         }
         if (!tagsAreEqualOrCanBeChangedLocally(o, newHTMLTags.get(indexNew), diff, deletions, changes)) {
            LOGGER.info("HTMLTags are different, require update of parent. Parent HTMLTag:"
                  + newParentHTMLTag.getDescription());
            diff.add(newParentHTMLTag);
            return false;
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

      return true;
   }

   public static ArrayList<HTMLTag> getDifferenceOfHTMLTags(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
         ArrayList<String> deletions, ArrayList<String> changes) {
      ArrayList<HTMLTag> diff = new ArrayList<HTMLTag>();

      if (!tagsAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag, diff, deletions, changes)) {
         LOGGER.info("HTMLTags are different, require update of parent. Old HTMLTag:" + oldHTMLTag.getDescription()
               + " new HTMLTag: " + newHTMLTag.getDescription());
         diff.add(newHTMLTag);
      }

      return diff;
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
      if (!(oldHTMLTag instanceof Element) && !(newHTMLTag instanceof Element)) {
         return true;
      }

      return ((Element) oldHTMLTag).getAttribute("id").equals(((Element) newHTMLTag).getAttribute("id"));
   }

   private static boolean tagsAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
         ArrayList<HTMLTag> diff, ArrayList<String> deletions, ArrayList<String> changes) {
      if (!HTMLTagNamesAreEqualsOrCanBeChangedLocally(oldHTMLTag, newHTMLTag)) {
         return false;
      }
      if (!idsAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag)) {
         return false;
      }

      if (!areStringsEqualOrCanBeChangedLocally(oldHTMLTag.innerHTML.toString(), newHTMLTag.innerHTML.toString())) {
         return false;
      }
      ArrayList<String> localChanges = new ArrayList<String>();
      boolean attributesHaveChanged = (!attributesAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag, deletions,
            localChanges));

      ArrayList<HTMLTag> oldHTMLTags = getNonEmptyHTMLTags(oldHTMLTag.getChildren());
      ArrayList<HTMLTag> newHTMLTags = getNonEmptyHTMLTags(newHTMLTag.getChildren());
      boolean childHTMLTagHaveChanged = !childHTMLTagAreEqualOrCanBeChangedLocally(oldHTMLTags, newHTMLTags,
            newHTMLTag, diff, deletions, changes);
      if (!diff.contains(newHTMLTag)) {
         if (attributesHaveChanged) {
            LOGGER.warning("TODO: add changeAttribute");
            LOGGER.info("Attributes are different, require update of parent. Old HTMLTag:"
                  + oldHTMLTag.getDescription() + " new HTMLTag: " + newHTMLTag.getDescription());
            diff.add(newHTMLTag);
            // LOGGER.info(new DiffenceEngine().domToString(newHTMLTag));
            return true;
         }
      }
      if (localChanges.size() > 0) {
         changes.addAll(localChanges);
      }

      return true;
   }
}
