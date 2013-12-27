/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */
package de.beyondjava.jsf.ajax.differentialContextWriter.parser;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class HTMLTag implements Serializable {

   public class HTMLAttribute implements Serializable {
      public String name;
      public String value;

      @Override
      public String toString() {
         return name + "=" + "\"" + value + "\"";
      }
   }

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

   /**
    * @return the builder
    */
   public static DocumentBuilder getBuilder() {
      return builder;
   }

   /**
    * @param html
    */
   private static Document htmlToDocument(String html) {
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

   /**
    * @param builder
    *           the builder to set
    */
   public static void setBuilder(DocumentBuilder builder) {
      HTMLTag.builder = builder;
   }

   public List<HTMLAttribute> attributes = new ArrayList<>();

   public List<HTMLTag> children = new ArrayList<>();

   /** convenience attribute (with the side effect of better performance) */
   public String id = "";

   public List<String> idsOfChildren = new ArrayList<>();

   public StringBuffer innerHTML = new StringBuffer();

   public boolean isTextNode = false;

   public String nodeName = "";

   public HTMLTag parent = null;

   public HTMLTag(Node node, HTMLTag parent) {
      if (node.getNodeType() == Node.DOCUMENT_NODE) {
         // BabbageFaces isn't interested in the document wrapped around the
         // HTML code
         node = node.getFirstChild();
      }
      isTextNode = node.getNodeType() == Node.TEXT_NODE;
      if (isTextNode) {
         innerHTML.append(node.getNodeValue());
      }
      else {
         nodeName = node.getNodeName();
         if (null != node.getAttributes()) {
            for (int i = 0; i < node.getAttributes().getLength(); i++) {
               final Node item = node.getAttributes().item(i);
               final String attributeName = item.getNodeName();
               String attributeValue = item.getNodeValue();
               addAttribute(attributeName, attributeValue);
            }
         }
         if (null != node.getChildNodes()) {
            for (int i = 0; i < node.getChildNodes().getLength(); i++) {
               final Node item = node.getChildNodes().item(i);
               if ((item.getNodeType() != Node.TEXT_NODE)
                     || ((item.getNodeValue() != null) && (item.getNodeValue().trim().length() > 0))) {
                  HTMLTag kid = new HTMLTag(item, this);
                  children.add(kid);
                  idsOfChildren.add(kid.id);
               }
            }
         }
         if ((node.getNodeValue() != null) && (node.getNodeValue().trim().length() > 0)) {
            System.out.println("NodeValue nonempty?");
         }
      }
   }

   public HTMLTag(String html) {

      this(htmlToDocument(html), null);

   }

   public void addAttribute(String name, String value) {
      HTMLAttribute a = new HTMLAttribute();
      a.name = name;
      a.value = value;
      attributes.add(a);
      if ("id".equals(name)) {
         id = value; // convenience attribute
      }
   }

   public String attributesToString() {
      StringBuffer result = new StringBuffer();
      if ((null != id) && (id.length() > 0)) {
         result.append(' ');
         result.append("id");
         result.append('=');
         result.append('"');
         result.append(id);
         result.append('"');
      }
      if (null != attributes) {
         for (HTMLAttribute a : attributes) {
            if ("id".equals(a.name)) {
               continue;
            }
            result.append(' ');
            result.append(a.name);
            result.append('=');
            result.append('"');
            result.append(a.value);
            result.append('"');
         }
      }
      return result.toString();
   }

   /**
    * @param attributeName
    * @return
    */
   public HTMLAttribute getAttribute(String attributeName) {
      for (HTMLAttribute a : attributes) {
         if (a.name.equals(attributeName)) {
            return a;
         }
      }
      return null;
   }

   /**
    * @return the attributes
    */
   public List<HTMLAttribute> getAttributes() {
      return this.attributes;
   }

   /**
    * @return the children
    */
   public List<HTMLTag> getChildren() {
      return this.children;
   }

   public String getDescription() {
      return id;
   }

   /**
    * @return
    */
   public HTMLTag getFirstChild() {
      if (children.size() == 0) {
         return null;
      }
      return children.get(0);
   }

   /**
    * convenience attribute (with the side effect of better performance)
    * 
    * @return the id
    */
   public String getId() {
      return this.id;
   }

   /**
    * @return the idsOfChildren
    */
   public List<String> getIdsOfChildren() {
      return this.idsOfChildren;
   }

   /**
    * @return the innerHTML
    */
   public StringBuffer getInnerHTML() {
      return this.innerHTML;
   }

   /**
    * @return the nodeName
    */
   public String getNodeName() {
      return this.nodeName;
   }

   /**
    * @return the parent
    */
   public HTMLTag getParent() {
      return this.parent;
   }

   /**
    * @return
    */
   public boolean hasAttributes() {
      return !attributes.isEmpty();
   }

   /**
    * @return the isTextNode
    */
   public boolean isTextNode() {
      return this.isTextNode;
   }

   /**
    * Removes a particular HTML tag from the childrens subtree.
    * 
    * @param tagToBeRemoved
    */
   public void removeChild(HTMLTag tagToBeRemoved) {
      children.remove(tagToBeRemoved);
   }

   /**
    * @param newSubtree
    * @param tagToBeReplaced
    */
   public void replaceChild(HTMLTag newSubtree, HTMLTag tagToBeReplaced) {
      if (null != children) {
         for (int i = 0; i < children.size(); i++) {
            if (children.get(i) == tagToBeReplaced) {
               children.set(i, newSubtree);
               break;
            }
         }
      }

   }

   /**
    * @param attributes
    *           the attributes to set
    */
   public void setAttributes(List<HTMLAttribute> attributes) {
      this.attributes = attributes;
   }

   /**
    * @param children
    *           the children to set
    */
   public void setChildren(List<HTMLTag> children) {
      this.children = children;
   }

   /**
    * convenience attribute (with the side effect of better performance)
    * 
    * @param id
    *           the id to set
    */
   public void setId(String id) {
      this.id = id;
   }

   /**
    * @param idsOfChildren
    *           the idsOfChildren to set
    */
   public void setIdsOfChildren(List<String> idsOfChildren) {
      this.idsOfChildren = idsOfChildren;
   }

   /**
    * @param innerHTML
    *           the innerHTML to set
    */
   public void setInnerHTML(StringBuffer innerHTML) {
      this.innerHTML = innerHTML;
   }

   /**
    * @param nodeName
    *           the nodeName to set
    */
   public void setNodeName(String nodeName) {
      this.nodeName = nodeName;
   }

   /**
    * @param parent
    *           the parent to set
    */
   public void setParent(HTMLTag parent) {
      this.parent = parent;
   }

   /**
    * @param isTextNode
    *           the isTextNode to set
    */
   public void setTextNode(boolean isTextNode) {
      this.isTextNode = isTextNode;
   }

   @Override
   public String toString() {
      if (isTextNode) {
         return innerHTML.toString();
      }
      else {
         String result = "  <" + nodeName + attributesToString() + ">";
         for (HTMLTag kid : children) {
            String k = kid.toString().replace("  <", "    <");
            result += k;
            if (!kid.isTextNode()) {
               result += "\n";
            }
         }
         result += "  </" + nodeName + ">\n";
         return result;
      }
   }
}
