/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.beyondjava.jsf.ajax.differentialContextWriter.parser;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.xml.parsers.*;

import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
public class HTMLTag implements Serializable {

    private transient static DocumentBuilder builder;

    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine");

    private static final long serialVersionUID = 8958589454536115945L;

    static {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            factory.setFeature("http://xml.org/sax/features/namespaces", false);
            factory.setFeature("http://xml.org/sax/features/validation", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setValidating(false);
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) {
            throw new RuntimeException("Couldn't initialize the SAX parser.", e);
        }

    }

    /**
     * As it seems we can't stop the DOM parsers from resolving entities in attributes. So we have to escape them to
     * receive the original HTML code after converting string to XML and back to strings. Nonetheless the entire method
     * just seems wrong - after all, its unique purpose is to do nothing (more precisely, to prevent SAX from doing
     * weird things). Feel free to drop me a note if you know how to do it right.
     *
     * @param html
     * @return a version of <code>html</code> with every ampersand replaced by the corresponding XML entity.
     */
    private static String escapeXmlEntities(String html) {
        return html.replace("&", "AmPeRsAnD");
        // StringBuffer result = new StringBuffer();
        // if (html.indexOf("<body") > 0) {
        // int pos = html.indexOf("<body") + "<body".length();
        // result.append(html.substring(0, pos));
        // html = html.substring(pos);
        //
        // }
        // boolean isInString = false;
        // boolean isBeingEscaped = false;
        // boolean isInCDATA = false;
        // char[] charArray = html.toCharArray();
        // final int len = charArray.length;
        // for (int pos = 0; pos < len; pos++) {
        // char c = charArray[pos];
        // if (!isBeingEscaped) {
        // if (c == '\\') {
        // isBeingEscaped = true;
        // continue;
        // }
        // if (c == '"') {
        // isInString = !isInString;
        // }
        // if ((!isInString) && (!isInCDATA)) {
        // final String CDATA = "<![CDATA[";
        // if ((c == '<') && ((pos + CDATA.length()) < len)) {
        // boolean match = true;
        // for (int i = 1; i < CDATA.length(); i++) {
        // if (charArray[pos + i] == CDATA.charAt(i)) {
        // continue;
        // }
        // else {
        // match = false;
        // break;
        // }
        // }
        // if (match) {
        // isInCDATA = true;
        // }
        // }
        // }
        // if ((!isInString) && (!isInCDATA)) {
        // final String CDATAEND = "]]>";
        // if ((c == ']') && ((pos + CDATAEND.length()) < len)) {
        // boolean match = true;
        // for (int i = 1; i < CDATAEND.length(); i++) {
        // if (charArray[pos + i] == CDATAEND.charAt(i)) {
        // continue;
        // }
        // else {
        // match = false;
        // break;
        // }
        // }
        // if (match) {
        // isInCDATA = false;
        // }
        // }
        // }
        // }
        // isBeingEscaped = false;
        // if ((c == '&') && (isInString) && (!isInCDATA)) {
        // result.append("&amp;");
        // }
        // else {
        // result.append(c);
        // }
        // }
        //
        // return result.toString();
    }

    /**
     * Removes the document node from the XML tree.
     *
     * @param html
     * @return
     */
    private static Node getXMLRootNode(Node document) {
        if (document.getNodeType() == Node.DOCUMENT_NODE) {
            document = document.getFirstChild();
            while (((document.getNodeType() == Node.DOCUMENT_TYPE_NODE) || (document.getNodeType() == Node.COMMENT_NODE))
                    && (document.getNextSibling() != null)) {
                // sometimes additional headers are added, such as <!DOCTYPE composition>
                document = document.getNextSibling();
            }
        }

        return document;
    }

    /**
     * @param html
     */
    private static Document htmlToDocument(String html) {
        long DEBUG_startTime = System.nanoTime();
        if (html.trim().startsWith("<?")) {
            int pos = html.indexOf("?>");
            if (pos > 0) {
                html = html.substring(pos + 2).trim();
            }
        }
        if (html.startsWith("<head>")) {
            // PrimeFaces delivers code without the surroundings html tag.
            // If we don't add it, the SAX parser refuses to parse the document
            html = "<html>" + html + "</html>";
        }
        html = escapeXmlEntities(html);
        InputSource inputSource = new InputSource(new StringReader(html));

        try {
            Document domTree = builder.parse(inputSource);
            long DEBUG_time = System.nanoTime() - DEBUG_startTime;
            if ((null != FacesContext.getCurrentInstance())
                    && (FacesContext.getCurrentInstance().getApplication().getProjectStage() == ProjectStage.Development)) {
                // LOGGER.info("HTML Parser took " + (((DEBUG_time) / 1000) / 1000.0d) + " ms. " + html.substring(0,
                // 20));
            }
            return domTree;
        }
        catch (SAXException e) {
            if (!e.getMessage().contains("]]>")) {
                LOGGER.severe("Couldn't parse the HTML oder XML code due to a SAXException. This may have been caused by an application exception.");
            }
            else {
                LOGGER.severe("Couldn't parse the HTML oder XML code due to a SAXException. The HTML code is:");
                LOGGER.severe(html);
            }

            throw new RuntimeException("Couldn't parse the HTML oder XML code due to a SAXException", e);
        }
        catch (IOException e) {
            throw new RuntimeException("Couldn't parse the HTML oder XML code due to an IOException", e);
        }
    }

    /**
     * As it seems we can't stop the DOM parsers from resolving entities in attributes. So we have to escape them to
     * receive the original HTML code after converting string to XML and back to strings. Nonetheless the entire method
     * just seems wrong - after all, its unique purpose is to do nothing (more precisely, to prevent SAX from doing
     * weird things). Feel free to drop me a note if you know how to do it right.
     *
     * @param html
     * @return a version of <code>html</code> with every ampersand replaced by the corresponding XML entity.
     */
    private static String unescapeXmlEntities(String html) {
        return html.replace("AmPeRsAnD", "&");
    }

    private List<HTMLAttribute> attributes = new ArrayList<>();

    private List<HTMLTag> children = new ArrayList<>();

    /** convenience attribute (with the side effect of better performance) */
    private String id = "";

    private StringBuffer innerHTML = new StringBuffer();
    private boolean isCDATANode = false;

    /**
     * Currently BabbageFaces ignores comments for the sake of saving network bandwith. Should I ever decide to use
     * comment, this flag is going set to true, and the comment is put in the innerHTML.
     */
    private boolean isCommentNode = false;

    private boolean isTextNode = false;

    private String nodeName = "";

    private HTMLTag parent = null;

    /**
     * Converts a SAX DOM tree to a more simple HTMLTag.
     *
     * @param node
     *            the SAX DOM tree to be converted to the simplified version.
     * @param parent
     *            the parent HTMLTag (if any).
     */
    public HTMLTag(Node node, HTMLTag parent) {
        this.parent = parent;
        isTextNode = node.getNodeType() == Node.TEXT_NODE;
        if (isTextNode) {
            innerHTML.append(unescapeXmlEntities(node.getNodeValue().trim()));
        }
        else if (node.getNodeType() == Node.CDATA_SECTION_NODE) {
            isTextNode = true;
            isCDATANode = true;
            innerHTML.append(unescapeXmlEntities(node.getNodeValue().trim()));
        }
        else if (node.getNodeType() == Node.COMMENT_NODE) {
            isCommentNode = true;
            innerHTML.append(unescapeXmlEntities(node.getNodeValue().trim()));
        }
        else {
            nodeName = node.getNodeName();
            if (null != node.getAttributes()) {
                for (int i = 0; i < node.getAttributes().getLength(); i++) {
                    final Node item = node.getAttributes().item(i);
                    addAttribute(item.getNodeName(), unescapeXmlEntities(item.getNodeValue()));
                }
                if (null != parent) {
                    if ((id == null) || (id.length() == 0)) {
                        // "div".equals(nodeName) excluded because SelectOneMenus don't work if updated partially
                        if ("span".equals(nodeName) || "input".equals(nodeName) || "a".equals(nodeName)
                                || "tr".equals(nodeName) || "td".equals(nodeName) || "option".equals(nodeName)) {
                            if ((parent.getId() != null) && (parent.getId().length() > 0)) {
                                addAttribute("id", parent.getId() + ":" + nodeName + parent.getChildren().size());
                            }
                            else if (parent.getNodeName().equals("body")) {
                                addAttribute("id", parent.getNodeName() + ":" + nodeName + parent.getChildren().size());
                            }
                        }
                    }
                }
            }
            if (null != node.getChildNodes()) {
                for (int i = 0; i < node.getChildNodes().getLength(); i++) {
                    final Node item = node.getChildNodes().item(i);

                    final short type = item.getNodeType();
                    if (type == Node.COMMENT_NODE) {
                        // removes comment nodes to save network bandwith
                        continue;
                    }
                    if ((type != Node.TEXT_NODE)
                            || ((item.getNodeValue() != null) && (item.getNodeValue().trim().length() > 0))) {
                        HTMLTag kid = new HTMLTag(item, this);
                        children.add(kid);
                    }
                }
            }

            if ((node.getNodeValue() != null) && (node.getNodeValue().trim().length() > 0)) {
                LOGGER.warning("NodeValue nonempty?");
            }
        }
    }

    /**
     * Create an HTMLTag tree structure from html source code.
     *
     * @param html
     */
    public HTMLTag(String html) {

        this(getXMLRootNode(htmlToDocument(html)), null);

    }

    public HTMLTag(String tag, String id, String cdata) {
        this.nodeName = tag;
        this.id = id;
        if (null != cdata) {
            HTMLTag inner = new HTMLTag(null, null, null);
            inner.innerHTML.append(cdata.trim());
            inner.isTextNode = true;
            inner.isCDATANode = true;
            inner.parent = this;
            this.children.add(inner);
        }
    }

    /**
     * Adds an attribute to an HTML tag.
     *
     * @param name
     * @param value
     */
    public void addAttribute(String name, String value) {
        HTMLAttribute a = new HTMLAttribute();
        a.name = name;
        a.value = value;
        attributes.add(a);
        if ("id".equals(name)) {
            id = value; // convenience attribute
        }
    }

    /** Returns a textual representation of the attributes. */
    private String attributesToString() {
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
     * Returns a hashmap of every script in the current HTML tag.
     *
     * @return a hash map (never null). The keys are the names of the scripts (with the trailing "_s"). The objects are
     *         the HTML tags containing the scripts.
     */
    public Map<String, HTMLTag> collectScripts() {
        Map<String, HTMLTag> scripts = new HashMap<>();
        for (HTMLTag candidate : children) {
            if ("script".equals(candidate.getNodeName())) {
                String scriptID = candidate.getId();
                if ((null != scriptID) && (scriptID.length() > 0)) {
                    if (scriptID.endsWith("_s")) {
                        scriptID = scriptID.substring(0, scriptID.length() - 2);
                    }
                    if (!scripts.containsKey(scriptID)) {
                        scripts.put(scriptID, candidate);
                    }
                }
            }
            else {
                Map<String, HTMLTag> collectScripts = candidate.collectScripts();
                if (!collectScripts.isEmpty()) {
                    for (String key : collectScripts.keySet()) {
                        if (!scripts.containsKey(key)) {
                            scripts.put(key, collectScripts.get(key));
                        }
                    }
                }
            }
        }
        return scripts;
    }

    /**
     * Returns a list of scripts that have to be executed again to re-initialize the components in this HTML tag.
     *
     * @param idOfCurrentChange
     * @return null or the Java script node
     */
    public Collection<HTMLTag> extractPrimeFacesJavascript(Map<String, HTMLTag> availableScripts) {
        Map<String, HTMLTag> requiredScripts = new HashMap<>();
        if ((id != null) && (id.length() > 0)) {
            for (Entry<String, HTMLTag> script : availableScripts.entrySet()) {
                if (id.startsWith(script.getKey())) {
                    requiredScripts.put(script.getKey(), script.getValue());
                }
            }
        }
        final List<HTMLTag> kids = getChildren();
        for (int i = 0; i < kids.size(); i++) {
            HTMLTag child = kids.get(i);
            Collection<HTMLTag> list = child.extractPrimeFacesJavascript(availableScripts);
            for (HTMLTag t : list) {
                if (!requiredScripts.containsKey(t.id)) {
                    requiredScripts.put(t.id, t);
                }
            }
        }

        return requiredScripts.values();
    }

    /**
     * Looks for a subtree bearing a particular id.
     *
     * @param id
     * @return null, if neither this nor any subtree of this bear the id
     */
    public HTMLTag findByID(String id) {
        if (getId().equals(id)) {
            return this;
        }
        for (HTMLTag child : getChildren()) {
            HTMLTag result = child.findByID(id);
            if (null != result) {
                return result;
            }
        }
        return null;
    }

    /** Looks for a certain tag (non-recursive). */
    public HTMLTag findTag(String tagName) {
        if (getNodeName().equals(tagName)) {
            return this;
        }
        if (null != children) {
            for (HTMLTag c : children) {
                if (c.getNodeName().equals(tagName)) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * returns a certain attribute, or null if there is no attribute with the name asked for.
     *
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
     * Return the list of attributes.
     *
     * @return The list of attributes is never null (even if its empty).
     */
    public List<HTMLAttribute> getAttributes() {
        return this.attributes;
    }

    /**
     * Return the list of children (aka HTML or DOM subtrees).
     *
     * @return The list of children is never null (even if its empty).
     */
    public List<HTMLTag> getChildren() {
        return this.children;
    }

    public String getDescription() {
        if ((id == null) || (id.length() == 0)) {
            if (isCDATANode) {
                return "<![CDATA[" + innerHTML.toString() + "]]>";
            }
            else if (isTextNode) {
                return innerHTML.toString();
            }
            String result = "<" + nodeName + attributesToString();
            result += ">";
            for (HTMLTag kid : children) {
                if (kid.isTextNode()) {
                    result += kid.innerHTML.toString();
                }
                else if (kid.isCDATANode) {
                    result += "<![CDATA[" + kid.innerHTML.toString() + "]]>";
                }
            }
            result += "</" + nodeName + ">";
            return result;
        }
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
     * @return the isCDATANode
     */
    public boolean isCDATANode() {
        return this.isCDATANode;
    }

    /**
     * Currently BabbageFaces ignores comments for the sake of saving network bandwith. Should I ever decide to use
     * comment, this flag is going set to true, and the comment is put in the innerHTML.
     *
     * @return the isCommentNode
     */
    public boolean isCommentNode() {
        return this.isCommentNode;
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
                    newSubtree.setParent(this);
                    break;
                }
            }
        }

    }

    /**
     * @param name
     * @param value
     */
    public void setAttribute(String name, String value) {
        for (int i = 0; i < attributes.size(); i++) {
            HTMLAttribute a = attributes.get(i);
            if (a.name.equals(name)) {
                if (value == null) {
                    attributes.remove(i);
                }
                else {
                    a.value = value;
                }
                return;
            }
        }
        final HTMLAttribute newAttribute = new HTMLAttribute();
        newAttribute.name = name;
        newAttribute.value = value;
        attributes.add(newAttribute);
    }

    /**
     * @param attributes
     *            the attributes to set
     */
    public void setAttributes(List<HTMLAttribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @param children
     *            the children to set
     */
    public void setChildren(List<HTMLTag> children) {
        this.children = children;
    }

    /**
     * Currently BabbageFaces ignores comments for the sake of saving network bandwith. Should I ever decide to use
     * comment, this flag is going set to true, and the comment is put in the innerHTML.
     *
     * @param isCommentNode
     *            the isCommentNode to set
     */
    public void setCommentNode(boolean isCommentNode) {
        this.isCommentNode = isCommentNode;
    }

    /**
     * convenience attribute (with the side effect of better performance)
     *
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @param innerHTML
     *            the innerHTML to set
     */
    public void setInnerHTML(StringBuffer innerHTML) {
        this.innerHTML = innerHTML;
    }

    /**
     * @param nodeName
     *            the nodeName to set
     */
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(HTMLTag parent) {
        this.parent = parent;
    }

    /**
     * @param isTextNode
     *            the isTextNode to set
     */
    public void setTextNode(boolean isTextNode) {
        this.isTextNode = isTextNode;
    }

    /**
     * Returns a formatted representation of the HTML tag suited ideally for runtime.
     *
     * @return the HTML code as a single line
     */
    public String toCompactString() {
        StringBuffer s = new StringBuffer();
        return toCompactString(s).toString();
    }

    /**
     * Returns a formatted representation of the HTML tag suited ideally for runtime.
     *
     * @return the HTML code as a single line
     */
    public StringBuffer toCompactString(StringBuffer result) {

        if (isCDATANode) {
            result.append("<![CDATA[");
            result.append(innerHTML);
            result.append("]]>");
            return result;
        }
        else if (isTextNode) {
            result.append(innerHTML);
            return result;
        }
        else {
            result.append("<");
            result.append(nodeName);
            result.append(attributesToString());
            if ((children.size() == 0) && (innerHTML.length() == 0)) {
                result.append("></");
                result.append(nodeName);
                result.append(">");
            }
            else {
                result.append(">");
                for (HTMLTag kid : children) {
                    if (!kid.isTextNode()) {
                        kid.toCompactString(result);
                    }
                    else if (kid.isCDATANode) {
                        result.append("<![CDATA[");
                        result.append(kid.innerHTML);
                        result.append("]]>");
                    }
                    else {
                        result.append(kid.innerHTML);
                    }
                }
                result.append("</");
                result.append(nodeName);
                result.append('>');
            }
            return result;
        }
    }

    /**
     * Returns a formatted representation of the HTML tag suited ideally for debugging purposes.
     *
     * @return multi-line, indented HTML code
     */
    @Override
    public String toString() {
        return toStringIntern().replace("  </", "</");
    }

    /**
     * Returns an almost correctly formatted representation of the HTML tag suited ideally for debugging purposes.
     *
     * @return multi-line, indented HTML code
     */
    private String toStringIntern() {
        if (isCDATANode) {
            return "<![CDATA[" + innerHTML.toString() + "]]>";
        }
        else if (isTextNode) {
            return innerHTML.toString();
        }
        else {
            String result = "<" + nodeName + attributesToString();
            if ((children.size() == 0) && (innerHTML.length() == 0)) {
                result += "></" + nodeName + ">";
            }
            else {
                result += ">";
                boolean newLineRequired = false;
                for (HTMLTag kid : children) {
                    if (!kid.isTextNode()) {
                        String k = kid.toStringIntern().replace("  <", "    <");
                        result += "\n";
                        newLineRequired = true;
                        result += "  " + k;
                    }
                    else if (kid.isCDATANode) {
                        return "<![CDATA[" + kid.innerHTML.toString() + "]]>";
                    }
                    else {
                        result += kid.innerHTML.toString();
                    }
                }
                if (newLineRequired) {
                    if (!result.endsWith("\n")) {
                        result += "\n";
                    }
                }
                if (result.endsWith("\n")) {
                    result += "  ";
                }
                result += "</" + nodeName + ">\n";
            }
            return result;
        }
    }

}
