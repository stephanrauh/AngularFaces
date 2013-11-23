package com.beyondee.faces.diff;

import java.util.ArrayList;

import javax.xml.parsers.*;

import junit.framework.Assert;

import org.junit.Test;
import org.w3c.dom.*;

import de.beyondjava.jsf.ajax.differentialContextWriter.analyzer.XmlDiff;

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

         return XmlDiff.diff(builder.parse(getClass().getResourceAsStream("/test" + testNr + "-old.xml")),
               builder.parse(getClass().getResourceAsStream("/test" + testNr + "-new.xml")));
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @Test
   public void test1() {
      ArrayList<Node> diff = diff(1);

      Assert.assertEquals(2, diff.size());
      Assert.assertEquals("f1", ((Element) diff.get(0)).getAttribute("id"));
      Assert.assertEquals("s", ((Element) diff.get(1)).getAttribute("id"));
   }

   @Test
   public void test2() {
      ArrayList<Node> diff = diff(2);

      Assert.assertEquals(2, diff.size());
      Assert.assertEquals("1001", ((Element) diff.get(0)).getAttribute("id"));
      Assert.assertEquals("2001", ((Element) diff.get(1)).getAttribute("id"));
   }
}
