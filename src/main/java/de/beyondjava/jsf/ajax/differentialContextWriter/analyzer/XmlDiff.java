package de.beyondjava.jsf.ajax.differentialContextWriter.analyzer;

import java.util.ArrayList;

import org.w3c.dom.*;

public class XmlDiff {
   public static boolean compareAttributes(Node oldNode, Node newNode) {
      if (!oldNode.hasAttributes() && !newNode.hasAttributes()) {
         return true;
      }

      if (oldNode.hasAttributes() != newNode.hasAttributes()) {
         return false;
      }

      if (oldNode.getAttributes().getLength() != newNode.getAttributes().getLength()) {
         return false;
      }

      Node newAttribute = null;
      Node oldAttribute = null;
      for (int i = 0; i < newNode.getAttributes().getLength(); i++) {
         newAttribute = newNode.getAttributes().item(i);
         oldAttribute = oldNode.getAttributes().getNamedItem(newAttribute.getNodeName());

         if (null == oldAttribute) {
            return false;
         }

         if (!(String.valueOf(oldAttribute.getNodeValue()).equals(String.valueOf(newAttribute.getNodeValue())))) {
            return false;
         }
      }

      return true;
   }

   private static void compareChildNodes(NodeList oldNodes, NodeList newNodes, Node newParentNode, ArrayList<Node> diff) {
      if (oldNodes.getLength() != newNodes.getLength()) {
         diff.add(newParentNode);
         return;
      }

      for (int i = 0; i < oldNodes.getLength(); i++) {
         if (!compareNode(oldNodes.item(i), newNodes.item(i), diff)) {
            diff.add(newParentNode);
            return;
         }
      }
   }

   public static boolean compareIds(Node oldNode, Node newNode) {
      if (!(oldNode instanceof Element) && !(newNode instanceof Element)) {
         return true;
      }

      return ((Element) oldNode).getAttribute("id").equals(((Element) newNode).getAttribute("id"));
   }

   private static boolean compareNode(Node oldNode, Node newNode, ArrayList<Node> diff) {
      if (!compareNodeName(oldNode, newNode)) {
         return false;
      }
      if (!compareIds(oldNode, newNode)) {
         return false;
      }
      if (!compareAttributes(oldNode, newNode)) {
         return false;
      }
      if (!compareStrings(oldNode.getNodeValue(), newNode.getNodeValue())) {
         return false;
      }

      compareChildNodes(oldNode.getChildNodes(), newNode.getChildNodes(), newNode, diff);

      return true;
   }

   private static boolean compareNodeName(Node oldNode, Node newNode) {
      return oldNode.getNodeName().equals(newNode.getNodeName());
   }

   private static boolean compareStrings(String oldString, String newString) {
      if ((oldString == null) && (newString == null)) {
         return true;
      }

      if ((oldString == null) || (newString == null)) {
         return false;
      }

      return oldString.equals(newString);
   }

   public static ArrayList<Node> diff(Document oldDocument, Document newDocument) {
      Node oldRootNode = oldDocument.getChildNodes().item(0);
      Node newRootNode = newDocument.getChildNodes().item(0);

      ArrayList<Node> diff = new ArrayList<Node>();

      compareNode(oldRootNode, newRootNode, diff);

      return diff;
   }
}
