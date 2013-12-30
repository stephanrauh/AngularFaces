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
import java.util.logging.*;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffenceEngine {
   private static boolean differentialEngineActive = true;

   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine");

   final String LAST_KNOWN_HTML_KEY = "com.beyondEE.faces.diff.lastKnownHTML";

   /**
    * 
    */
   public DiffenceEngine() {
   }

   /**
    * @param lastKnowDOMTree
    * @param htmlTagid
    */
   private void deleteHTMLTag(HTMLTag lastKnowDOMTree, String htmlTagid) {
      HTMLTag tagToBeRemoved = findHTMLTagWithID(htmlTagid, lastKnowDOMTree);
      if (tagToBeRemoved == null) {
         LOGGER.severe("Wrong ID? Looking for " + htmlTagid + ", but couldn't find the ID in the last known HTML tree");
      }
      else {
         HTMLTag parentHTMLTag = tagToBeRemoved.getParent();
         parentHTMLTag.removeChild(tagToBeRemoved);
      }
   }

   /**
    * @param change
    * @param lastKnowDOMTree
    */
   private List<HTMLTag> determineNecessaryChangeFromResponse(HTMLTag change, HTMLTag lastKnowDOMTree) {

      if (change.getNodeName().equals("update")) {
         String id = change.getId();
         String changingHTML = change.getFirstChild().getInnerHTML().toString().trim();

         HTMLTag lastKnownCorrespondingHTMLTag = findHTMLTagWithID(id, lastKnowDOMTree);
         List<String> deletions = new ArrayList<>();
         List<String> changes = new ArrayList<>();
         List<String> inserts = new ArrayList<>();
         List<HTMLTag> necessaryChanges = determineNecessaryChanges(changingHTML, lastKnownCorrespondingHTMLTag,
               deletions, changes, inserts);
         List<HTMLTag> partialChanges = new ArrayList<>();
         if ((null != inserts) && (inserts.size() > 0)) {
            for (String insert : inserts) {
               partialChanges.add(new HTMLTag(insert));
            }
         }

         if (null != deletions) {
            for (String partialID : deletions) {
               String partialChange = "<delete id=\"" + partialID + "\"/>";
               HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
               partialChanges.add(partialChangeHTMLTag);
            }
         }
         if (null != changes) {
            for (String partialChange : changes) {
               HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
               partialChanges.add(partialChangeHTMLTag);
            }
         }
         if (null != necessaryChanges) {
            for (HTMLTag n : necessaryChanges) {
               String partialUpdate = n.toCompactString();
               String partialID = n.getId();
               if ((partialID == null) || (partialID.length() == 0)) {
                  LOGGER.severe("ID of update shouldn't be void");
               }
               String partialChange = "<update id=\"" + partialID + "\"><![CDATA[" + partialUpdate + "]]></update>";

               HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
               partialChanges.add(partialChangeHTMLTag);
            }
         }

         if (partialChanges.size() > 0) {
            return partialChanges;
         }
         else {
            return null;
         }
      }
      else {
         LOGGER.info(change.getNodeName() + " - remains unchanged");
      }
      return null;

   }

   /**
    * @param changingHTML
    * @param lastKnownCorrespondingHTMLTag
    * @param deletions2
    * @param changes2
    */
   protected List<HTMLTag> determineNecessaryChanges(String newHTML, HTMLTag lastKnownCorrespondingHTMLTag,
         List<String> deletions, List<String> changes, List<String> inserts) {
      if (newHTML.startsWith("<")) {
         HTMLTag newDOM = new HTMLTag(newHTML);
         List<HTMLTag> differences = XHtmlDiff.getDifferenceOfHTMLTags(lastKnownCorrespondingHTMLTag, newDOM,
               deletions, changes, inserts);
         for (HTMLTag d : differences) {
            LOGGER.fine("Difference: " + d);
            // JUnitTestCreator.generateJUnitTest(newDOM,
            // lastKnownCorrespondingHTMLTag, differences);
         }
         for (String d : deletions) {
            LOGGER.fine("Deletion: " + d);
         }
         for (String d : changes) {
            LOGGER.fine("Change: " + d);
         }
         for (String d : changes) {
            LOGGER.fine("Insertion: " + d);
         }
         return differences;
      }
      else {
         return null;
      }

   }

   /**
    * @param partialResponseAsDOMTree
    * @return
    */
   private List<HTMLTag> extractChangesFromPartialResponse(HTMLTag partialResponseAsDOMTree) {
      // HTMLTagList partialResponses =
      // partialResponseAsDOMTree.getElementsByTagName("partial-response");
      List<HTMLTag> partialResponses = new ArrayList<>();
      HTMLTag partialHTMLTag = partialResponseAsDOMTree.getFirstChild();
      // HTMLTag changes = partialHTMLTag.getFirstChild();
      for (int i = 0; i < partialHTMLTag.getChildren().size(); i++) {
         HTMLTag n = partialHTMLTag.getChildren().get(i);
         partialResponses.add(n);
      }
      return partialResponses;
   }

   /**
    * @param id
    * @param lastKnowDOMTree
    * @return
    */
   private HTMLTag findHTMLTagWithID(String id, HTMLTag tree) {
      if (null != tree.getAttributes()) {
         final String tagid = tree.getId();
         if (null != tagid) {
            if (id.equals(tagid)) {
               return tree;
            }
         }
      }
      int length = tree.getChildren().size();
      for (int i = 0; i < length; i++) {
         HTMLTag result = findHTMLTagWithID(id, tree.getChildren().get(i));
         if (null != result) {
            return result;
         }
      }
      return null;
   }

   /**
    * @param sessionMap
    * @return
    */
   private HTMLTag getLastKnownHTMLBodyAsDomTree(Map<String, Object> sessionMap) {
      String html = retrieveLastKnownHTMLFromSession(sessionMap);
      int start = html.indexOf("<body");
      int end = html.lastIndexOf("</body>");
      if ((start >= 0) && (end > start)) {
         html = html.substring(start, end + "</body>".length());
      }
      // add an artificial umbrella tag (<original>) because HTML code often
      // consists of more than one root (<head> and <body>) and
      // XML parsers can't cope with that
      HTMLTag lastKnowDOMTree = new HTMLTag("<original>" + html + "</original>");
      return lastKnowDOMTree.getFirstChild(); // drop the <original> tag
   }

   /**
    * @param updatedDOMTree
    * @param children
    */
   private void insertHTMLTag(HTMLTag updatedDOMTree, List<HTMLTag> children) {
      for (HTMLTag afterOrBefore : children) {
         String id = afterOrBefore.getId();
         HTMLTag sibling = findHTMLTagWithID(id, updatedDOMTree);
         HTMLTag parent = sibling.getParent();
         for (int i = 0; i < parent.getChildren().size(); i++) {
            if (parent.getChildren().get(i) == sibling) {
               final HTMLTag elementToBeInserted = new HTMLTag(afterOrBefore.getFirstChild().getInnerHTML().toString());
               elementToBeInserted.setParent(parent);
               if ("after".equals(afterOrBefore.getNodeName())) {
                  parent.getChildren().add(i + 1, elementToBeInserted);
               }
               else {
                  parent.getChildren().add(i, elementToBeInserted);
               }
               break;
            }
         }
      }

   }

   /**
    * @param sessionMap
    * @return
    */
   private String retrieveLastKnownHTMLFromSession(Map<String, Object> sessionMap) {
      String html = (String) sessionMap.get(LAST_KNOWN_HTML_KEY);
      if (html == null) {
         throw new RuntimeException("HTML code is missing in the session");
      }
      return html;
   }

   /**
    * @param lastKnowDOMTree
    * @param HTMLTagid
    * @param c
    */
   private void updateAttributes(HTMLTag lastKnowDOMTree, String HTMLTagid, String c) {
      HTMLTag tagToBeReplaced = findHTMLTagWithID(HTMLTagid, lastKnowDOMTree);
      String attributes[] = c.split("<attribute name=\\\"");

      for (int i = 1; i < attributes.length; i++) {
         String a = attributes[i];
         String p[] = a.split("\" value=\"");
         String name = p[0];
         String value = p[1];
         if (value.endsWith("\"/>")) {
            value = value.substring(0, value.length() - 3);
         }
         else {
            value = value.substring(0, value.length() - "\"/></attributes>".length());
         }
         tagToBeReplaced.setAttribute(name, value);
      }

   }

   /**
    * @param lastKnowDOMTree
    * @param typeOfChange
    * @param HTMLTagid
    */
   private void updateHTMLTag(HTMLTag lastKnowDOMTree, HTMLTag newSubtree, String HTMLTagid) {

      HTMLTag tagToBeReplaced = findHTMLTagWithID(HTMLTagid, lastKnowDOMTree);
      if (tagToBeReplaced == null) {
         LOGGER.severe("Wrong ID? Looking for " + HTMLTagid + ", but couldn't find the ID in the last known HTML tree");
      }
      else {
         if (newSubtree.isCDATANode) {
            newSubtree = new HTMLTag(newSubtree.innerHTML.toString());
         }
         HTMLTag parentHTMLTag = tagToBeReplaced.getParent();
         parentHTMLTag.replaceChild(newSubtree, tagToBeReplaced);
      }
   }

   /**
    * Compares the current HTML response with the last known HTML code. If it's
    * a regular HTML response, the HTML code is simply stored in the session. If
    * it's an JSF AJAX response, the method looks at the differences and tries
    * to remove unchanged HTML from the response.
    * 
    * @param rawBuffer
    * @param sessionMap
    * @param isAJAX
    * @return
    */
   public String yieldDifferences(String currentResponse, Map<String, Object> sessionMap, boolean isAJAX) {

      if (isAJAX && differentialEngineActive) {
         boolean atAll = currentResponse.contains("<head"); // update="@all"
         if (atAll) {
            LOGGER.severe("@all is not compatible with AngularFaces differential engine. Optimized AJAX is switched off.");
            differentialEngineActive = false;
         }
         else {
            HTMLTag updatedDOMTree = getLastKnownHTMLBodyAsDomTree(sessionMap);
            HTMLTag partialResponseAsDOMTree = new HTMLTag(currentResponse);
            List<HTMLTag> listOfChanges = extractChangesFromPartialResponse(partialResponseAsDOMTree);
            for (HTMLTag change : listOfChanges) {
               List<HTMLTag> newPartialChanges = determineNecessaryChangeFromResponse(change, updatedDOMTree);
               if ((null != newPartialChanges) && (newPartialChanges.size() > 0)) {
                  String id = change.getId();
                  int start = currentResponse.indexOf("<update id=\"" + id + "\">");
                  int end = currentResponse.indexOf("</update>", start);
                  String currentResponseEnd = currentResponse.substring(end + "</update>".length());
                  try {
                     String tmpCurrentResponse = currentResponse.substring(0, start);
                     for (HTMLTag changeDefinition : newPartialChanges) {
                        String c = changeDefinition.toCompactString();
                        tmpCurrentResponse += c;
                        String idOfCurrentChange = changeDefinition.getId();
                        if (changeDefinition.getNodeName().equals("insert")) {
                           insertHTMLTag(updatedDOMTree, changeDefinition.getChildren());
                        }
                        else if ((idOfCurrentChange == null) || (idOfCurrentChange.length() == 0)) {
                           LOGGER.severe("Missing HTMLTag ID");
                        }
                        else if (changeDefinition.getNodeName().equals("update")) {
                           updateHTMLTag(updatedDOMTree, changeDefinition.getFirstChild(), idOfCurrentChange);
                        }
                        else if (changeDefinition.getNodeName().equals("delete")) {
                           deleteHTMLTag(updatedDOMTree, idOfCurrentChange);
                        }
                        else if (changeDefinition.getNodeName().equals("attributes")) {
                           updateAttributes(updatedDOMTree, idOfCurrentChange, c);
                        }

                     }
                     currentResponse = tmpCurrentResponse + currentResponseEnd;
                  }
                  catch (Exception e) {
                     LOGGER.log(Level.SEVERE, "An exception occured while trying to replace the response", e);
                  }
               }
               String newHTML = updatedDOMTree.toCompactString();
               if (newHTML.startsWith("<original>")) {
                  newHTML = newHTML.substring("<original>".length());
                  newHTML = newHTML.substring(0, newHTML.length() - "</original>".length());
               }
               sessionMap.remove(LAST_KNOWN_HTML_KEY);
               sessionMap.put(LAST_KNOWN_HTML_KEY, newHTML);
            }
            // currentResponse = DEBUGdeleteCityHTMLTag(currentResponse);
         }
      }
      else {
         sessionMap.remove(LAST_KNOWN_HTML_KEY);
         sessionMap.put(LAST_KNOWN_HTML_KEY, currentResponse);
      }
      return currentResponse;
   }

}
