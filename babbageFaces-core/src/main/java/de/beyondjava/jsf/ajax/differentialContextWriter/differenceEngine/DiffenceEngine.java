/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Element;

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
   private ArrayList<HTMLTag> determineNecessaryChangeFromResponse(HTMLTag change, HTMLTag lastKnowDOMTree,
         ArrayList<String> deletions, ArrayList<String> changes) {
      if (change.getNodeName().equals("update")) {
         // DOMUtils.domToString(partialChangeHTMLTag);
         String id = change.getId();
         String HTMLTagValue = change.getFirstChild().getInnerHTML().toString();
         String changingHTML = HTMLTagValue.toString().trim();
         HTMLTag lastKnownCorrespondingHTMLTag = findHTMLTagWithID(id, lastKnowDOMTree);
         ArrayList<HTMLTag> necessaryChanges = determineNecessaryChanges(changingHTML, lastKnownCorrespondingHTMLTag,
               deletions, changes);
         ArrayList<HTMLTag> partialChanges = new ArrayList<HTMLTag>();
         if (null != necessaryChanges) {
            // Todo create an array of partial changes
            for (HTMLTag n : necessaryChanges) {
               String partialUpdate = n.toString();
               String partialID = ((Element) n).getAttribute("id");
               if ((partialID == null) || (partialID.length() == 0)) {
                  LOGGER.severe("ID of update shouldn't be void");
               }
               String partialChange = "<update id=\"" + partialID + "\"><![CDATA[" + partialUpdate + "]]></update>";

               HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
               partialChanges.add(partialChangeHTMLTag);
            }
         }
         if (null != deletions) {
            // Todo create an array of partial changes
            for (String partialID : deletions) {
               String partialChange = "<delete id=\"" + partialID + "\"/>";
               HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
               partialChanges.add(partialChangeHTMLTag);
            }
         }
         if (null != changes) {
            // Todo create an array of partial changes
            for (String partialChange : changes) {
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
   protected ArrayList<HTMLTag> determineNecessaryChanges(String newHTML, HTMLTag lastKnownCorrespondingHTMLTag,
         ArrayList<String> deletions, ArrayList<String> changes) {
      if (newHTML.startsWith("<")) {
         HTMLTag newDOM = new HTMLTag(newHTML);
         ArrayList<HTMLTag> differences = XHtmlDiff.getDifferenceOfHTMLTags(lastKnownCorrespondingHTMLTag, newDOM,
               deletions, changes);
         for (HTMLTag d : differences) {
            LOGGER.fine("Difference: " + d);
         }
         for (String d : deletions) {
            LOGGER.fine("Deletion: " + d);
         }
         for (String d : changes) {
            LOGGER.fine("Change: " + d);
         }
         // generateJUnitTest(newHTML, lastKnownCorrespondingHTMLTag,
         // differences);
         return differences;

      }
      else {
         return null;
      }

   }

   String domToString(HTMLTag tag) {
      return tag.toString().trim();
      // try {
      // TransformerFactory tf = TransformerFactory.newInstance();
      // Transformer transformer = tf.newTransformer();
      // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      // StringWriter writer = new StringWriter();
      // transformer.transform(new DOMSource(node), new StreamResult(writer));
      // return writer.toString();
      // }
      // catch (TransformerException te) {
      // return "(TransformerException)";
      // }
   }

   /**
    * @param partialResponseAsDOMTree
    * @return
    */
   private List<HTMLTag> extractChangesFromPartialResponse(HTMLTag partialResponseAsDOMTree) {
      // HTMLTagList partialResponses =
      // partialResponseAsDOMTree.getElementsByTagName("partial-response");
      List<HTMLTag> partialResponses = new ArrayList<HTMLTag>();
      HTMLTag partialHTMLTag = partialResponseAsDOMTree.getFirstChild();
      HTMLTag changes = partialHTMLTag.getFirstChild();
      for (int i = 0; i < changes.getChildren().size(); i++) {
         HTMLTag n = changes.getChildren().get(i);
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
    * @param newHTML
    * @param lastKnownCorrespondingHTMLTag
    * @param differences
    */
   private void generateJUnitTest(String newHTML, HTMLTag lastKnownCorrespondingHTMLTag, ArrayList<HTMLTag> differences) {
      File dir = new File("E:/this/AngularFaces/src/test/resources/DifferenceEngine");
      if (dir.exists()) {
         int freeNumber = 0;
         File testcase;
         File partialChange;
         do {
            freeNumber++;
            testcase = new File(dir, "html" + freeNumber + ".xml");
            partialChange = new File(dir, "partialChange" + freeNumber + ".xml");

         } while (testcase.exists());
         try {
            FileUtils.write(testcase, domToString(lastKnownCorrespondingHTMLTag));
            FileUtils.write(partialChange, newHTML);
            int index = 1;
            for (HTMLTag d : differences) {
               File differenceFile = new File(dir, "difference" + freeNumber + "_" + index + ".xml");
               FileUtils.write(differenceFile, domToString(d));
            }
         }
         catch (IOException e) {
            LOGGER.severe("Couldn't create JUnit test case");
         }

      }

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
      HTMLTag lastKnowDOMTree = new HTMLTag("<original>" + html + "</original>");
      return lastKnowDOMTree;
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
         ((Element) tagToBeReplaced).setAttribute(name, value);
      }

   }

   /**
    * @param lastKnowDOMTree
    * @param typeOfChange
    * @param HTMLTagid
    */
   private void updateHTMLTag(HTMLTag lastKnowDOMTree, final HTMLTag newSubtree, String HTMLTagid) {

      HTMLTag tagToBeReplaced = findHTMLTagWithID(HTMLTagid, lastKnowDOMTree);
      if (tagToBeReplaced == null) {
         LOGGER.severe("Wrong ID? Looking for " + HTMLTagid + ", but couldn't find the ID in the last known HTML tree");
      }
      else {
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
            HTMLTag lastKnowDOMTree = getLastKnownHTMLBodyAsDomTree(sessionMap);
            HTMLTag partialResponseAsDOMTree = new HTMLTag(currentResponse);
            List<HTMLTag> listOfChanges = extractChangesFromPartialResponse(partialResponseAsDOMTree);
            for (HTMLTag change : listOfChanges) {
               ArrayList<String> deletions = new ArrayList<String>();
               ArrayList<String> changes = new ArrayList<String>();
               ArrayList<HTMLTag> newPartialChanges = determineNecessaryChangeFromResponse(change, lastKnowDOMTree,
                     deletions, changes);
               if ((null != newPartialChanges) && (newPartialChanges.size() > 0)) {
                  String id = ((Element) change).getAttribute("id");
                  int start = currentResponse.indexOf("<update id=\"" + id + "\">");
                  int end = currentResponse.indexOf("</update>", start);
                  String currentResponseEnd = currentResponse.substring(end + "</update>".length());
                  try {
                     String tmpCurrentResponse = currentResponse.substring(0, start);
                     for (HTMLTag n : newPartialChanges) {
                        String c = domToString(n);
                        tmpCurrentResponse += c;
                        final HTMLTag changeDefinition = n.getFirstChild();
                        String idOfCurrentChange = ((Element) changeDefinition).getAttribute("id");
                        if ((idOfCurrentChange == null) || (idOfCurrentChange.length() == 0)) {
                           LOGGER.severe("Missing HTMLTag ID");
                        }
                        else {
                           if (changeDefinition.getNodeName().equals("update")) {
                              updateHTMLTag(lastKnowDOMTree, changeDefinition.getFirstChild(), idOfCurrentChange);
                           }
                           else if (changeDefinition.getNodeName().equals("delete")) {
                              deleteHTMLTag(lastKnowDOMTree, idOfCurrentChange);
                           }
                           else if (changeDefinition.getNodeName().equals("attributes")) {
                              updateAttributes(lastKnowDOMTree, idOfCurrentChange, c);
                           }

                        }
                     }
                     currentResponse = tmpCurrentResponse + currentResponseEnd;
                  }
                  catch (Exception e) {
                     LOGGER.log(Level.SEVERE, "An exception occured while trying to replace the response", e);
                  }
               }
               String newHTML = lastKnowDOMTree.getFirstChild().toString();
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
