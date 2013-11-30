package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.io.StringWriter;
import java.util.*;
import java.util.logging.Logger;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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

   public static boolean attributesAreEqualOrCanBeChangedLocally(Node oldNode, Node newNode,
         ArrayList<String> deletions, ArrayList<String> changes) {
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

   private static boolean childNodeAreEqualOrCanBeChangedLocally(NodeList oldNodes, NodeList newNodes,
         Node newParentNode, ArrayList<Node> diff, ArrayList<String> deletions, ArrayList<String> changes) {
      boolean needsUpdate = false;
      ArrayList<String> localDeletions = new ArrayList<String>();
      ArrayList<String> localChanges = new ArrayList<String>();
      if (oldNodes.getLength() != newNodes.getLength()) {
         ArrayList<Node> insertList = getAdditionalNodes(oldNodes, newNodes);
         if (insertList.size() > 0) {
            needsUpdate = true;
         }
         if (!needsUpdate) {
            ArrayList<Node> deleteList = getAdditionalNodes(newNodes, oldNodes);

            for (Node d : deleteList) {
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
         LOGGER.severe("TODO: add insert");
         if (needsUpdate) {
            LOGGER.severe("TODO: Nodes counts are different, require update of parent. Parent node:"
                  + getDescriptionOfNode(newParentNode));
            diff.add(newParentNode);
            return false;
         }
      }

      int indexOld = 0;
      int indexNew = 0;
      while (indexOld < oldNodes.getLength()) {
         Node o = (oldNodes.item(indexOld));
         if (o instanceof Element) {
            String id = ((Element) o).getAttribute("id");
            if (localDeletions.contains(id)) {
               indexOld++;
               continue;
            }
         }
         if (!nodesAreEqualOrCanBeChangedLocally(o, newNodes.item(indexNew), diff, deletions, changes)) {
            LOGGER.severe("TODO: Nodes are different, require update of parent. Parent node:"
                  + getDescriptionOfNode(newParentNode));
            diff.add(newParentNode);
            return false;
         }
         indexOld++;
         indexNew++;
      }
      if (localChanges.size() > 0) {
         LOGGER.severe("Adding attribute changes");
         changes.addAll(localChanges);
      }
      if (localDeletions.size() > 0) {
         LOGGER.severe("Adding node deletions");
         deletions.addAll(localDeletions);
      }

      return true;
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

   public static ArrayList<Node> getAdditionalNodes(NodeList oldNodes, NodeList newNodes) {
      ArrayList<Node> result = new ArrayList<Node>();
      HashMap<String, Node> alreadyThere = new HashMap<String, Node>();
      for (int i = 0; i < oldNodes.getLength(); i++) {
         Node n = oldNodes.item(i);
         String desc = getDescriptionOfNode(n);
         alreadyThere.put(desc, n);
      }
      for (int i = 0; i < newNodes.getLength(); i++) {
         Node n = newNodes.item(i);
         String desc = getDescriptionOfNode(n);
         if (!alreadyThere.containsKey(desc)) {
            result.add(n);
         }
      }
      return result;
   }

   public static String getDescriptionOfNode(Node node) {
      if (!(node instanceof Element)) {
         return domToString(node);
      }

      return "Node: id=" + ((Element) node).getAttribute("id");

   }

   public static ArrayList<Node> getDifferenceOfDocuments(Document oldDocument, Document newDocument,
         ArrayList<String> deletions, ArrayList<String> changes) {
      Node oldRootNode = oldDocument.getChildNodes().item(0);
      Node newRootNode = newDocument.getChildNodes().item(0);

      ArrayList<Node> diff = new ArrayList<Node>();

      if (!nodesAreEqualOrCanBeChangedLocally(oldRootNode, newRootNode, diff, deletions, changes)) {
         LOGGER.severe("TODO: Nodes are different, require update of parent. Old node:"
               + getDescriptionOfNode(oldRootNode) + " new node: " + getDescriptionOfNode(newRootNode));
         diff.add(newRootNode);
      }

      return diff;
   }

   public static ArrayList<Node> getDifferenceOfNodes(Node oldNode, Node newNode, ArrayList<String> deletions,
         ArrayList<String> changes) {
      ArrayList<Node> diff = new ArrayList<Node>();

      if (!nodesAreEqualOrCanBeChangedLocally(oldNode, newNode, diff, deletions, changes)) {
         LOGGER.severe("TODO: Nodes are different, require update of parent. Old node:" + getDescriptionOfNode(oldNode)
               + " new node: " + getDescriptionOfNode(newNode));
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

   private static boolean nodeNamesAreEqualsOrCanBeChangedLocally(Node oldNode, Node newNode) {
      return oldNode.getNodeName().equals(newNode.getNodeName());
   }

   private static boolean nodesAreEqualOrCanBeChangedLocally(Node oldNode, Node newNode, ArrayList<Node> diff,
         ArrayList<String> deletions, ArrayList<String> changes) {
      if (!nodeNamesAreEqualsOrCanBeChangedLocally(oldNode, newNode)) {
         return false;
      }
      if (!idsAreEqualOrCanBeChangedLocally(oldNode, newNode)) {
         return false;
      }
      boolean attributesHaveChanged = (!attributesAreEqualOrCanBeChangedLocally(oldNode, newNode, deletions, changes));

      if (!areStringsEqualOrCanBeChangedLocally(oldNode.getNodeValue(), newNode.getNodeValue())) {
         return false;
      }

      boolean childNodeHaveChanged = !childNodeAreEqualOrCanBeChangedLocally(oldNode.getChildNodes(),
            newNode.getChildNodes(), newNode, diff, deletions, changes);
      if (!diff.contains(newNode)) {
         if (attributesHaveChanged) {
            LOGGER.severe("TODO: add changeAttribute");
            LOGGER.severe("TODO: Attributes are different, require update of parent. Old node:"
                  + getDescriptionOfNode(oldNode) + " new node: " + getDescriptionOfNode(newNode));
            diff.add(newNode);
            System.out.println(new DiffenceEngine().domToString(newNode));
            return true;
         }
      }

      return true;
   }
}
