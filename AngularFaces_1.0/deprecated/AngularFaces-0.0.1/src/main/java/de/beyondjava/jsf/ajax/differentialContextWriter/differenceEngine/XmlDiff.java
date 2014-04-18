package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import static de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DOMUtils.getDescriptionOfNode;

import java.util.*;
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
      StringBuffer changeAttributes = new StringBuffer();
      String id = null;
      for (int i = 0; i < newNode.getAttributes().getLength(); i++) {
         newAttribute = newNode.getAttributes().item(i);
         final String attributeName = newAttribute.getNodeName();
         oldAttribute = oldNode.getAttributes().getNamedItem(attributeName);

         if (null == oldAttribute) {
            return false;
         }

         String oldString = String.valueOf(oldAttribute.getNodeValue());
         if (attributeName.equals("id")) {
            id = oldString;
         }
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
            changeAttributes.append("<attribute name=\"" + attributeName + "\" value=\"" + newString + "\"/>");
         }
      }
      if (changeAttributes.length() > 0) {
         if ((null != id) && (id.length() > 0)) {
            String c = "<attributes id=\"" + id + "\">" + changeAttributes.toString() + "</attributes>";
            changes.add(c);
            return true;
         }
         else {
            return false; // this case needs an update of the parent
         }
      }

      return true;
   }

   private static boolean childNodeAreEqualOrCanBeChangedLocally(ArrayList<Node> oldNodes, ArrayList<Node> newNodes,
         Node newParentNode, ArrayList<Node> diff, ArrayList<String> deletions, ArrayList<String> changes) {
      boolean needsUpdate = false;
      ArrayList<String> localDeletions = new ArrayList<String>();
      ArrayList<String> localChanges = new ArrayList<String>();
      if (oldNodes.size() != newNodes.size()) {
         ArrayList<Node> insertList = getRecentlyAddedNodes(oldNodes, newNodes);
         if ((oldNodes.size() < newNodes.size()) && (insertList.size() == 0)) {
            // implementation error - must be debugged
            insertList = getRecentlyAddedNodes(oldNodes, newNodes);
         }
         if (insertList.size() > 0) {
            needsUpdate = true;
         }
         if (!needsUpdate) {
            ArrayList<Node> deleteList = getRecentlyAddedNodes(newNodes, oldNodes);

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
         LOGGER.warning("TODO: add insert");
         if (needsUpdate) {
            LOGGER.info("Nodes counts are different, require update of parent. Parent node:"
                  + DOMUtils.getDescriptionOfNode(newParentNode));
            diff.add(newParentNode);
            return false;
         }
      }

      int indexOld = 0;
      int indexNew = 0;
      while (indexOld < oldNodes.size()) {
         Node o = (oldNodes.get(indexOld));
         if (o instanceof Element) {
            String id = ((Element) o).getAttribute("id");
            if (localDeletions.contains(id)) {
               indexOld++;
               continue;
            }
         }
         if (!nodesAreEqualOrCanBeChangedLocally(o, newNodes.get(indexNew), diff, deletions, changes)) {
            LOGGER.info("Nodes are different, require update of parent. Parent node:"
                  + DOMUtils.getDescriptionOfNode(newParentNode));
            diff.add(newParentNode);
            return false;
         }
         indexOld++;
         indexNew++;
      }
      if (localChanges.size() > 0) {
         LOGGER.info("Adding attribute changes");
         changes.addAll(localChanges);
      }
      if (localDeletions.size() > 0) {
         LOGGER.info("Adding node deletions");
         deletions.addAll(localDeletions);
      }

      return true;
   }

   public static ArrayList<Node> getDifferenceOfDocuments(Document oldDocument, Document newDocument,
         ArrayList<String> deletions, ArrayList<String> changes) {
      Node oldRootNode = oldDocument.getChildNodes().item(0);
      Node newRootNode = newDocument.getChildNodes().item(0);

      ArrayList<Node> diff = new ArrayList<Node>();

      if (!nodesAreEqualOrCanBeChangedLocally(oldRootNode, newRootNode, diff, deletions, changes)) {
         LOGGER.info("Nodes are different, require update of parent. Old node:"
               + DOMUtils.getDescriptionOfNode(oldRootNode) + " new node: "
               + DOMUtils.getDescriptionOfNode(newRootNode));
         diff.add(newRootNode);
      }

      return diff;
   }

   public static ArrayList<Node> getDifferenceOfNodes(Node oldNode, Node newNode, ArrayList<String> deletions,
         ArrayList<String> changes) {
      ArrayList<Node> diff = new ArrayList<Node>();

      if (!nodesAreEqualOrCanBeChangedLocally(oldNode, newNode, diff, deletions, changes)) {
         LOGGER.info("Nodes are different, require update of parent. Old node:" + getDescriptionOfNode(oldNode)
               + " new node: " + getDescriptionOfNode(newNode));
         diff.add(newNode);
      }

      return diff;
   }

   /**
    * @param childNodes
    * @return
    */
   private static ArrayList<Node> getNonEmptyNodes(NodeList childNodes) {
      ArrayList<Node> nonEmpty = new ArrayList<Node>();
      for (int i = 0; i < childNodes.getLength(); i++) {
         Node n = childNodes.item(i);
         if (n.hasAttributes() || n.hasChildNodes() || (n.getNodeType() != Node.TEXT_NODE)
               || (n.getNodeValue().trim().length() > 0)) {
            if (n.hasAttributes() && (n.getAttributes().getNamedItem("name") != null)) {
               if (!n.getAttributes().getNamedItem("name").getNodeValue().equals("javax.faces.ViewState")) {
                  nonEmpty.add(n);
               }
            }
            else {
               nonEmpty.add(n);
            }
         }
      }
      return nonEmpty;
   }

   public static ArrayList<Node> getRecentlyAddedNodes(ArrayList<Node> oldNodes, ArrayList<Node> newNodes) {
      ArrayList<Node> result = new ArrayList<Node>();
      HashMap<String, Node> alreadyThere = new HashMap<String, Node>();
      for (int i = 0; i < oldNodes.size(); i++) {
         Node n = oldNodes.get(i);
         String desc = getDescriptionOfNode(n);
         alreadyThere.put(desc, n);
      }
      for (int i = 0; i < newNodes.size(); i++) {
         Node n = newNodes.get(i);
         String desc = getDescriptionOfNode(n);
         if (!alreadyThere.containsKey(desc)) {
            result.add(n);
         }
      }
      return result;
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

      if (!areStringsEqualOrCanBeChangedLocally(oldNode.getNodeValue(), newNode.getNodeValue())) {
         return false;
      }
      ArrayList<String> localChanges = new ArrayList<String>();
      boolean attributesHaveChanged = (!attributesAreEqualOrCanBeChangedLocally(oldNode, newNode, deletions,
            localChanges));

      ArrayList<Node> oldNodes = getNonEmptyNodes(oldNode.getChildNodes());
      ArrayList<Node> newNodes = getNonEmptyNodes(newNode.getChildNodes());
      boolean childNodeHaveChanged = !childNodeAreEqualOrCanBeChangedLocally(oldNodes, newNodes, newNode, diff,
            deletions, changes);
      if (!diff.contains(newNode)) {
         if (attributesHaveChanged) {
            LOGGER.warning("TODO: add changeAttribute");
            LOGGER.info("Attributes are different, require update of parent. Old node:"
                  + DOMUtils.getDescriptionOfNode(oldNode) + " new node: " + DOMUtils.getDescriptionOfNode(newNode));
            diff.add(newNode);
            // LOGGER.info(new DiffenceEngine().domToString(newNode));
            return true;
         }
      }
      if (localChanges.size() > 0) {
         changes.addAll(localChanges);
      }

      return true;
   }
}
