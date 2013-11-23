package com.beyondee.faces.diff;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.*;

import de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff;

public class XmlDiffTest {
   public ArrayList<Node> diff(int testNr) {
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         factory.setNamespaceAware(false);
         factory.setValidating(false);
         factory.setFeature("http://xml.org/sax/features/namespaces", false);
         factory.setFeature("http://xml.org/sax/features/validation", false);
         factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
         factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
         DocumentBuilder builder = factory.newDocumentBuilder();

         return XmlDiff.getDifferenceOfDocuments(
               builder.parse(getClass().getResourceAsStream("/test" + testNr + "-old.xml")),
               builder.parse(getClass().getResourceAsStream("/test" + testNr + "-new.xml")));
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @SuppressWarnings("deprecation")
   @Test
   public void test1() {
      ArrayList<Node> diff = diff(1);
      assertEquals("<firstname id=\"f1\">low1</firstname>", xmlToString(diff.get(0)));
      assertEquals("<salary id=\"s\"><test2>200000</test2></salary>", xmlToString(diff.get(1)));
      assertEquals(2, diff.size());
      assertEquals("f1", ((Element) diff.get(0)).getAttribute("id"));
      assertEquals("s", ((Element) diff.get(1)).getAttribute("id"));
   }

   @SuppressWarnings("deprecation")
   @Test
   public void test2() {
      ArrayList<Node> diff = diff(2);
      assertTrue(xmlToString(diff.get(0)).startsWith("<staff id=\"1001\">"));
      assertTrue(xmlToString(diff.get(1)).startsWith("<staff id=\"2001\">"));

      assertEquals(2, diff.size());
      assertEquals("1001", ((Element) diff.get(0)).getAttribute("id"));
      assertEquals("2001", ((Element) diff.get(1)).getAttribute("id"));
   }

   private String xmlToString(Node node) {
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
}
