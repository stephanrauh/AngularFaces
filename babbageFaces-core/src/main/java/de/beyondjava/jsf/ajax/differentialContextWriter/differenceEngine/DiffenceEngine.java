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

package de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine;

import java.util.*;
import java.util.logging.Logger;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffenceEngine {
    private static boolean differentialEngineActive = true;

    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine");

    final String LAST_KNOWN_HTML_KEY = "com.beyondEE.faces.diff.lastKnownHTML";

    /**
    * 
    */
    public DiffenceEngine() {
    }

    /**
     * Fixes the DOM tree stored in the session by integrating the AJAX
     * deletions into the last known DOM tree.
     * 
     * @param domTreeInSession
     * @param htmlTagid
     */
    private void deleteHTMLTag(HTMLTag domTreeInSession, String htmlTagid) {
        HTMLTag tagToBeRemoved = domTreeInSession.findByID(htmlTagid);
        if (tagToBeRemoved == null) {
            LOGGER.severe("Wrong ID? Looking for " + htmlTagid
                    + ", but couldn't find the ID in the last known HTML tree");
        }
        else {
            HTMLTag parentHTMLTag = tagToBeRemoved.getParent();
            parentHTMLTag.removeChild(tagToBeRemoved);
        }
    }

    /**
     * @param change
     * @param lastKnownDOMTree
     */
    private List<HTMLTag> determineNecessaryChangeFromResponse(HTMLTag change, HTMLTag lastKnownDOMTree) {

        if (change.getNodeName().equals("update")) {
            String id = change.getId();
            String changingHTML = change.getFirstChild().getInnerHTML().toString().trim();

            HTMLTag lastKnownCorrespondingHTMLTag = lastKnownDOMTree.findByID(id);
            List<String> deletions = new ArrayList<>();
            List<String> changes = new ArrayList<>();
            List<String> inserts = new ArrayList<>();
            List<HTMLTag> updates = new ArrayList<>();
            determineNecessaryChanges(changingHTML, lastKnownCorrespondingHTMLTag, updates, deletions, changes, inserts);
            List<HTMLTag> partialChanges = new ArrayList<>();
            if ((null != inserts) && (inserts.size() > 0)) {
                for (String insert : inserts) {
                    partialChanges.add(new HTMLTag(insert));
                }
            }

            if (null != deletions) {
                for (String partialID : deletions) {
                    String partialChange = "<delete id=\"" + partialID + "\"/>";
                    HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
                    partialChanges.add(partialChangeHTMLTag);
                }
            }
            if (null != changes) {
                for (String partialChange : changes) {
                    HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
                    partialChanges.add(partialChangeHTMLTag);
                }
            }
            if (null != updates) {
                for (HTMLTag n : updates) {
                    while ((n.getId() == null) || (n.getId().length() == 0)) {
                        LOGGER.severe("ID of update shouldn't be void");
                        if (n.getParent() == null) {
                            LOGGER.severe("  and parent tag is null");
                        }
                        n = n.getParent();
                    }
                    String partialUpdate = n.toCompactString();
                    String partialID = n.getId();
                    String partialChange = "<update id=\"" + partialID + "\"><![CDATA[" + partialUpdate
                            + "]]></update>";

                    HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
                    partialChanges.add(partialChangeHTMLTag);
                }
            }

            return partialChanges;
        }
        else {
            LOGGER.info(change.getNodeName() + " - remains unchanged");
        }
        return null;

    }

    /**
     * @param changingHTML
     * @param lastKnownCorrespondingHTMLTag
     * @param deletions2
     * @param changes2
     */
    protected List<HTMLTag> determineNecessaryChanges(String newHTML, HTMLTag lastKnownCorrespondingHTMLTag,
            List<HTMLTag> updates, List<String> deletions, List<String> changes, List<String> inserts) {
        if (newHTML.startsWith("<")) {
            HTMLTag newDOM = new HTMLTag(newHTML);
            XmlDiff.tagsAreEqualOrCanBeChangedLocally(lastKnownCorrespondingHTMLTag, newDOM, updates, deletions,
                    changes, inserts);
            for (HTMLTag d : updates) {
                LOGGER.fine("Updates: " + d);
                // JUnitTestCreator.generateJUnitTest(newDOM,
                // lastKnownCorrespondingHTMLTag, updates);
            }
            for (String d : deletions) {
                LOGGER.fine("Deletion: " + d);
            }
            for (String d : changes) {
                LOGGER.fine("Change: " + d);
            }
            for (String d : inserts) {
                LOGGER.fine("Insertion: " + d);
            }
            return updates;
        }
        else {
            return null;
        }

    }

    /**
     * @param partialResponseAsDOMTree
     * @return
     */
    private List<HTMLTag> extractChangesFromPartialResponse(HTMLTag partialResponseAsDOMTree) {
        // HTMLTagList partialResponses =
        // partialResponseAsDOMTree.getElementsByTagName("partial-response");
        List<HTMLTag> partialResponses = new ArrayList<>();
        HTMLTag partialHTMLTag = partialResponseAsDOMTree.getFirstChild();
        // HTMLTag changes = partialHTMLTag.getFirstChild();
        for (int i = 0; i < partialHTMLTag.getChildren().size(); i++) {
            HTMLTag n = partialHTMLTag.getChildren().get(i);
            partialResponses.add(n);
        }
        return partialResponses;
    }

    /**
     * @param sessionMap
     * @return
     */
    private HTMLTag getLastKnownHTMLBodyAsDomTree(Map<String, Object> sessionMap) {
        String html = retrieveLastKnownHTMLFromSession(sessionMap);
        int start = html.indexOf("<body");
        int end = html.lastIndexOf("</body>");
        if ((start >= 0) && (end > start)) {
            html = html.substring(start, end + "</body>".length());
        }
        // add an artificial umbrella tag (<original>) because HTML code often
        // consists of more than one root (<head> and <body>) and
        // XML parsers can't cope with that
        HTMLTag lastKnownDOMTree = new HTMLTag("<original>" + html + "</original>");
        return lastKnownDOMTree.getFirstChild(); // drop the <original> tag
    }

    /**
     * Fixes the DOM tree stored in the session by integrating the AJAX inserts
     * into the last known DOM tree.
     * 
     * @param domTreeInSession
     * @param children
     *            list of insert command (more precisely: of after and before
     *            subcommands).
     */
    private void insertHTMLTag(HTMLTag domTreeInSession, List<HTMLTag> children) {
        for (HTMLTag afterOrBefore : children) {
            String id = afterOrBefore.getId();
            HTMLTag sibling = domTreeInSession.findByID(id);
            HTMLTag parent = sibling.getParent();
            for (int i = 0; i < parent.getChildren().size(); i++) {
                if (parent.getChildren().get(i) == sibling) {
                    final HTMLTag elementToBeInserted = new HTMLTag(afterOrBefore.getFirstChild().getInnerHTML()
                            .toString());
                    elementToBeInserted.setParent(parent);
                    if ("after".equals(afterOrBefore.getNodeName())) {
                        parent.getChildren().add(i + 1, elementToBeInserted);
                    }
                    else {
                        parent.getChildren().add(i, elementToBeInserted);
                    }
                    break;
                }
            }
        }

    }

    /**
     * @param currentResponse
     * @param domTreeToBeUpdated
     * @param change
     * @param newPartialChanges
     * @return
     */
    private String optimizeResponse(String currentResponse, HTMLTag domTreeToBeUpdated, HTMLTag change,
            List<HTMLTag> newPartialChanges) {
        String id = change.getId();

        int start = currentResponse.indexOf("<update id=\"" + id + "\">");
        int end = currentResponse.indexOf("</update>", start);
        String currentResponseEnd = currentResponse.substring(end + "</update>".length());
        String tmpCurrentResponse = currentResponse.substring(0, start);
        for (HTMLTag changeDefinition : newPartialChanges) {
            tmpCurrentResponse += changeDefinition.toCompactString();
            String idOfCurrentChange = changeDefinition.getId();
            if ((idOfCurrentChange == null) || (idOfCurrentChange.length() == 0)) {
                LOGGER.severe("Missing HTMLTag ID");
            }
            else if (changeDefinition.getNodeName().equals("update")) {
                final HTMLTag scriptNode = domTreeToBeUpdated.extractPrimeFacesJavascript(idOfCurrentChange);
                if (null != scriptNode) {
                    tmpCurrentResponse = tmpCurrentResponse.substring(0,
                            tmpCurrentResponse.length() - "]]></update>".length())
                            + scriptNode.toCompactString() + "]]></update>";
                }
            }

        }
        currentResponse = tmpCurrentResponse + currentResponseEnd;
        return currentResponse;
    }

    /**
     * Retrieves the DOM tree that has been sent by the previous HTTP response
     * (and that ought to be identical to the version displayed in the browser).
     * 
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

    /**
     * Fixes the DOM tree stored in the session by integrating the AJAX
     * attribute changes into the last known DOM tree.
     * 
     * @param domTreeInSession
     * @param HTMLTagid
     * @param c
     */
    private void updateAttributes(HTMLTag domTreeInSession, String HTMLTagid, String c) {
        HTMLTag tagToBeReplaced = domTreeInSession.findByID(HTMLTagid);
        String attributes[] = c.split("<attribute name=\\\"");

        for (int i = 1; i < attributes.length; i++) {
            String a = attributes[i];
            String p[] = a.split("\" value=\"");
            String name = p[0];
            String value = p[1];
            if (value.endsWith("\"/>")) {
                value = value.substring(0, value.length() - 3);
            }
            else {
                value = value.substring(0, value.length() - "\"/></attributes>".length());
            }
            tagToBeReplaced.setAttribute(name, value);
        }

    }

    /**
     * @param domTreeToBeUpdated
     * @param newPartialChanges
     */
    private void updateDOMTreeInSession(HTMLTag domTreeToBeUpdated, List<HTMLTag> newPartialChanges) {
        for (HTMLTag changeDefinition : newPartialChanges) {
            String idOfCurrentChange = changeDefinition.getId();
            if (changeDefinition.getNodeName().equals("insert")) {
                insertHTMLTag(domTreeToBeUpdated, changeDefinition.getChildren());
            }
            else if ((idOfCurrentChange == null) || (idOfCurrentChange.length() == 0)) {
                LOGGER.severe("Missing HTMLTag ID");
            }
            else if (changeDefinition.getNodeName().equals("update")) {
                updateHTMLTag(domTreeToBeUpdated, changeDefinition.getFirstChild(), idOfCurrentChange);
            }
            else if (changeDefinition.getNodeName().equals("delete")) {
                deleteHTMLTag(domTreeToBeUpdated, idOfCurrentChange);
            }
            else if (changeDefinition.getNodeName().equals("attributes")) {
                updateAttributes(domTreeToBeUpdated, idOfCurrentChange, changeDefinition.toCompactString());
            }

        }
    }

    /**
     * Fixes the DOM tree stored in the session by integrating the AJAX updates
     * into the last known DOM tree.
     * 
     * @param domTreeInSession
     * @param typeOfChange
     * @param idToBeUpdated
     */
    private void updateHTMLTag(HTMLTag domTreeInSession, HTMLTag newSubtree, String idToBeUpdated) {

        HTMLTag tagToBeReplaced = domTreeInSession.findByID(idToBeUpdated);
        if (tagToBeReplaced == null) {
            LOGGER.severe("Wrong ID? Looking for " + idToBeUpdated
                    + ", but couldn't find the ID in the last known HTML tree");
        }
        else {
            if (newSubtree.isCDATANode()) {
                newSubtree = new HTMLTag(newSubtree.getInnerHTML().toString());
            }
            HTMLTag parentHTMLTag = tagToBeReplaced.getParent();
            if (parentHTMLTag == null) {
                LOGGER.severe("parent HTML tag = null: did you try to replace the whole tree?"
                        + tagToBeReplaced.getId());
            }
            else {
                parentHTMLTag.replaceChild(newSubtree, tagToBeReplaced);
            }
        }
    }

    /**
     * Compares the current HTML response with the last known HTML code. If it's
     * a regular HTML response, the HTML code is simply stored in the session.
     * If it's an JSF AJAX response, the method looks at the differences and
     * tries to remove unchanged HTML from the response.
     * 
     * @param rawBuffer
     * @param sessionMap
     * @param isAJAX
     * @return
     */
    public String yieldDifferences(String currentResponse, Map<String, Object> sessionMap, boolean isAJAX) {
        if (isAJAX && differentialEngineActive) {
            boolean atAll = currentResponse.contains("<head"); // update="@all"
            if (atAll) {
                LOGGER.severe("@all is not compatible with AngularFaces differential engine. Optimized AJAX is switched off.");
                differentialEngineActive = false;
            }
            else {
                HTMLTag domTreeToBeUpdated = getLastKnownHTMLBodyAsDomTree(sessionMap);
                HTMLTag partialResponseAsDOMTree = new HTMLTag(currentResponse);
                List<HTMLTag> listOfChanges = extractChangesFromPartialResponse(partialResponseAsDOMTree);
                for (HTMLTag change : listOfChanges) {
                    List<HTMLTag> newPartialChanges = determineNecessaryChangeFromResponse(change, domTreeToBeUpdated);
                    if ((null != newPartialChanges)) {
                        currentResponse = optimizeResponse(currentResponse, domTreeToBeUpdated, change,
                                newPartialChanges);

                        updateDOMTreeInSession(domTreeToBeUpdated, newPartialChanges);

                    }
                    String newHTML = domTreeToBeUpdated.toCompactString();
                    if (newHTML.startsWith("<original>")) {
                        newHTML = newHTML.substring("<original>".length());
                        newHTML = newHTML.substring(0, newHTML.length() - "</original>".length());
                    }
                    sessionMap.remove(LAST_KNOWN_HTML_KEY);
                    sessionMap.put(LAST_KNOWN_HTML_KEY, newHTML);
                }
            }
        }
        else {
            sessionMap.remove(LAST_KNOWN_HTML_KEY);
            sessionMap.put(LAST_KNOWN_HTML_KEY, currentResponse);
        }
        return currentResponse;
    }

}
