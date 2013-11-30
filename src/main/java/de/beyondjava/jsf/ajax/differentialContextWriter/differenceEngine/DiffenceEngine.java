/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffenceEngine {
   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine");
   private DocumentBuilder builder;

   final String LAST_KNOWN_HTML_KEY = "com.beyondEE.faces.diff.lastKnownHTML";

   /**
    * 
    */
   public DiffenceEngine() {
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(false);
         factory.setValidating(false);
         factory.setFeature("http://xml.org/sax/features/namespaces", false);
         factory.setFeature("http://xml.org/sax/features/validation", false);
         factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
         factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
         builder = factory.newDocumentBuilder();
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Couldn't initialize the SAX parser.", e);
      }
   }

   /**
    * @param currentResponse
    * @return
    */
   private String DEBUGdeleteCityNode(String currentResponse) {
      int pos = currentResponse.indexOf("</changes></partial-response>");
      String test = "<delete id=\"j_idt3:zipcode\"></delete>";
      currentResponse = currentResponse.substring(0, pos) + test + currentResponse.substring(pos);
      return currentResponse;
   }

   /**
    * @param change
    * @param lastKnowDOMTree
    */
   private ArrayList<Node> determineNecessaryChangeFromResponse(Node change, Document lastKnowDOMTree,
         ArrayList<String> deletions, ArrayList<String> changes) {
      if (change.getNodeName().equals("update")) {
         String id = change.getAttributes().getNamedItem("id").getNodeValue();
         String nodeValue = change.getFirstChild().getNodeValue();
         String changingHTML = nodeValue.toString().trim();
         Node lastKnownCorrespondingNode = findNodeWithID(id, lastKnowDOMTree);
         ArrayList<Node> necessaryChanges = determineNecessaryChanges(changingHTML, lastKnownCorrespondingNode,
               deletions, changes);
         if (null != necessaryChanges) {
            ArrayList<Node> partialChanges = new ArrayList<Node>();
            // Todo create an array of partial changes
            return partialChanges;
         }
         return null;
      }
      else {
         System.out.println(change.getNodeName() + " - remains unchanged");
      }
      change.getFirstChild().getNodeName();
      return null;

   }

   /**
    * @param changingHTML
    * @param lastKnownCorrespondingNode
    * @param deletions2
    * @param changes2
    */
   protected ArrayList<Node> determineNecessaryChanges(String newHTML, Node lastKnownCorrespondingNode,
         ArrayList<String> deletions, ArrayList<String> changes) {
      if (newHTML.startsWith("<")) {
         Node newDOM = stringToDOM(newHTML).getFirstChild();
         ArrayList<Node> differences = XmlDiff.getDifferenceOfNodes(lastKnownCorrespondingNode, newDOM, deletions,
               changes);
         for (Node d : differences) {
            System.out.println("Difference: " + d);
         }
         for (String d : deletions) {
            System.out.println("Deletion: " + d);
         }
         for (String d : changes) {
            System.out.println("Change: " + d);
         }
         // generateJUnitTest(newHTML, lastKnownCorrespondingNode, differences);
         return differences;

      }
      else {
         return null;
      }

   }

   String domToString(Node node) {
      try {
         TransformerFactory tf = TransformerFactory.newInstance();
         Transformer transformer = tf.newTransformer();
         transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         StringWriter writer = new StringWriter();
         transformer.transform(new DOMSource(node), new StreamResult(writer));
         return writer.toString();
      }
      catch (TransformerException te) {
         return "(TransformerException)";
      }
   }

   /**
    * @param partialResponseAsDOMTree
    * @return
    */
   private List<Node> extractChangesFromPartialResponse(Document partialResponseAsDOMTree) {
      // NodeList partialResponses =
      // partialResponseAsDOMTree.getElementsByTagName("partial-response");
      List<Node> partialResponses = new ArrayList<Node>();
      Node partialNode = partialResponseAsDOMTree.getFirstChild();
      Node changes = partialNode.getFirstChild();
      for (int i = 0; i < changes.getChildNodes().getLength(); i++) {
         Node n = changes.getChildNodes().item(i);
         partialResponses.add(n);
      }
      return partialResponses;
   }

   /**
    * @param id
    * @param lastKnowDOMTree
    * @return
    */
   private Node findNodeWithID(String id, Node tree) {
      if (null != tree.getAttributes()) {
         final Node idNode = tree.getAttributes().getNamedItem("id");
         if (null != idNode) {
            if (id.equals(idNode.getNodeValue())) {
               return tree;
            }
         }
      }
      int length = tree.getChildNodes().getLength();
      for (int i = 0; i < length; i++) {
         Node result = findNodeWithID(id, tree.getChildNodes().item(i));
         if (null != result) {
            return result;
         }
      }
      return null;
   }

   /**
    * @param newHTML
    * @param lastKnownCorrespondingNode
    * @param differences
    */
   private void generateJUnitTest(String newHTML, Node lastKnownCorrespondingNode, ArrayList<Node> differences) {
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
            FileUtils.write(testcase, domToString(lastKnownCorrespondingNode));
            FileUtils.write(partialChange, newHTML);
            int index = 1;
            for (Node d : differences) {
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
   private String retrieveLastKnownHTMLFromSession(Map<String, Object> sessionMap) {
      String html = (String) sessionMap.get(LAST_KNOWN_HTML_KEY);
      if (html == null) {
         throw new RuntimeException("HTML code is missing in the session");
      }
      return html;
   }

   public Document stringToDOM(String html) {
      if (html.trim().startsWith("<?")) {
         int pos = html.indexOf("?>");
         if (pos > 0) {
            html = html.substring(pos + 2).trim();
         }
      }
      InputSource inputSource = new InputSource(new StringReader(html));

      try {
         Document domTree = builder.parse(inputSource);
         return domTree;
      }
      catch (SAXException e) {
         throw new RuntimeException("Couldn't parse the HTML oder XML code due to a SAXException", e);
      }
      catch (IOException e) {
         throw new RuntimeException("Couldn't parse the HTML oder XML code due to an IOException", e);
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
      if (isAJAX) {
         // System.out.println("-----------");
         // System.out.println(currentResponse);
         // System.out.println("-----------");
         String html = retrieveLastKnownHTMLFromSession(sessionMap);
         Document lastKnowDOMTree = stringToDOM("<original>" + html + "</original>");
         Document partialResponseAsDOMTree = stringToDOM(currentResponse);
         List<Node> listOfChanges = extractChangesFromPartialResponse(partialResponseAsDOMTree);
         for (Node change : listOfChanges) {
            ArrayList<String> deletions = new ArrayList<String>();
            ArrayList<String> changes = new ArrayList<String>();
            ArrayList<Node> newPartialChanges = determineNecessaryChangeFromResponse(change, lastKnowDOMTree,
                  deletions, changes);
            if (null != newPartialChanges) {
               for (Node n : newPartialChanges) {
                  LOGGER.severe("Implement new partial changes");
                  String c = domToString(n);
               }
               // Todo replace the current change with the new partial Changes
            }
         }
         // currentResponse = DEBUGdeleteCityNode(currentResponse);
      }
      else {
         sessionMap.put(LAST_KNOWN_HTML_KEY, currentResponse);
      }
      return currentResponse;
   }

}
