package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.util.ArrayList;
import java.util.logging.Logger;

import org.w3c.dom.*;

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

   public static boolean attributesAreEqualOrCanBeChangedLocally(Node oldNode, Node newNode) {
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
         final String attributeName = newAttribute.getNodeName();
         oldAttribute = oldNode.getAttributes().getNamedItem(attributeName);

         if (null == oldAttribute) {
            return false;
         }

         String oldString = String.valueOf(oldAttribute.getNodeValue());
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
         final String newString = String.valueOf(newAttribute.getNodeValue());
         if (!(oldString.equals(newString))) {
            return false;
         }
      }

      return true;
   }

   private static boolean childNodeAreEqualOrCanBeChangedLocally(NodeList oldNodes, NodeList newNodes, Node newParentNode,
         ArrayList<Node> diff) {
      if (oldNodes.getLength() != newNodes.getLength()) {
         LOGGER.fine("TODO: add delete and insert");
         diff.add(newParentNode);
         return false;
      }

      for (int i = 0; i < oldNodes.getLength(); i++) {
         if (!nodesAreEqualOrCanBeChangedLocally(oldNodes.item(i), newNodes.item(i), diff)) {
            diff.add(newParentNode);
            return false;
         }
      }
      return true;
   }

   private static boolean nodeNamesAreEqualsOrCanBeChangedLocally(Node oldNode, Node newNode) {
      return oldNode.getNodeName().equals(newNode.getNodeName());
   }

   public static ArrayList<Node> getDifferenceOfDocuments(Document oldDocument, Document newDocument) {
      Node oldRootNode = oldDocument.getChildNodes().item(0);
      Node newRootNode = newDocument.getChildNodes().item(0);

      ArrayList<Node> diff = new ArrayList<Node>();

      if (!nodesAreEqualOrCanBeChangedLocally(oldRootNode, newRootNode, diff)) {
         diff.add(newRootNode);
      }

      return diff;
   }

   public static ArrayList<Node> getDifferenceOfNodes(Node oldNode, Node newNode) {
      ArrayList<Node> diff = new ArrayList<Node>();

      if (!nodesAreEqualOrCanBeChangedLocally(oldNode, newNode, diff)) {
         diff.add(newNode);
      }

      return diff;
   }

   public static boolean idsAreEqualOrCanBeChangedLocally(Node oldNode, Node newNode) {
      if (!(oldNode instanceof Element) && !(newNode instanceof Element)) {
         return true;
      }

      return ((Element) oldNode).getAttribute("id").equals(((Element) newNode).getAttribute("id"));
   }

   private static boolean nodesAreEqualOrCanBeChangedLocally(Node oldNode, Node newNode, ArrayList<Node> diff) {
      if (!nodeNamesAreEqualsOrCanBeChangedLocally(oldNode, newNode)) {
         return false;
      }
      if (!idsAreEqualOrCanBeChangedLocally(oldNode, newNode)) {
         return false;
      }
      boolean attributesHaveChanged = (!attributesAreEqualOrCanBeChangedLocally(oldNode, newNode));

      if (!areStringsEqualOrCanBeChangedLocally(oldNode.getNodeValue(), newNode.getNodeValue())) {
         return false;
      }

      boolean childNodeHaveChanged = !childNodeAreEqualOrCanBeChangedLocally(oldNode.getChildNodes(), newNode.getChildNodes(), newNode, diff);
      if (!diff.contains(newNode)) {
         if (attributesHaveChanged) {
            LOGGER.severe("TODO: add changeAttribute");
            diff.add(newNode);
            System.out.println(new DiffenceEngine().domToString(newNode));
            return true;
         }
      }

      return true;
   }
}
