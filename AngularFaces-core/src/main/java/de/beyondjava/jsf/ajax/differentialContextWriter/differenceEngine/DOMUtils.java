/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.io.*;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DOMUtils {

   private static DocumentBuilder builder;

   static {
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

   static String domToString(Node node) {
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

   public static String getDescriptionOfNode(Node node) {
      if (!(node instanceof Element)) {
         return domToString(node);
      }

      final String id = ((Element) node).getAttribute("id");
      if ((id == null) || (id.length() == 0)) {
         return domToString(node);
      }
      return "Node: id=" + id;

   }

   public static Document stringToDOM(String html) {
      if (html.trim().startsWith("<?")) {
         int pos = html.indexOf("?>");
         if (pos > 0) {
            html = html.substring(pos + 2).trim();
         }
      }
      html = html.replace("&&", "&amp;&amp;");
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

}
