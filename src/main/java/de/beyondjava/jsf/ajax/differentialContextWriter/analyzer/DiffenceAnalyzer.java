/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter.analyzer;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffenceAnalyzer {
   private DocumentBuilder builder;

   final String LAST_KNOWN_HTML_KEY = "com.beyondEE.faces.diff.lastKnownHTML";

   /**
    * 
    */
   public DiffenceAnalyzer() {
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
    * @param partialResponseAsDOMTree
    * @return
    */
   private List<Node> extractChangesFromPartialResponse(Document partialResponseAsDOMTree) {
      // NodeList partialResponses =
      // partialResponseAsDOMTree.getElementsByTagName("partial-response");
      List<Node> partialResponses = new ArrayList<Node>();
      for (int i = 0; i < partialResponseAsDOMTree.getChildNodes().getLength(); i++) {
         Node n = partialResponseAsDOMTree.getChildNodes().item(i);
         if ("partial-response".equals(n.getNodeName())) {
            partialResponses.add(n);
         }
      }
      return partialResponses;
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
         String html = retrieveLastKnownHTMLFromSession(sessionMap);
         Document lastKnowDOMTree = stringToDOM("<original>" + html + "</original>");
         Document partialResponseAsDOMTree = stringToDOM(currentResponse);
         List<Node> listOfChanges = extractChangesFromPartialResponse(partialResponseAsDOMTree);
      }
      else {
         sessionMap.put(LAST_KNOWN_HTML_KEY, currentResponse);
      }
      return currentResponse;
   }
}
