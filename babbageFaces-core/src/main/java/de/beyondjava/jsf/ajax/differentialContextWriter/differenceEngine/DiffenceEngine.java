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

import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffenceEngine {
    private static boolean differentialEngineActive = true;

    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine");

    final boolean isDeveloperMode = FacesContext.getCurrentInstance().getApplication().getProjectStage() == ProjectStage.Development;

    final String LAST_KNOWN_HTML_KEY = "com.beyondEE.faces.diff.lastKnownHTML";

    /**
    * 
    */
    public DiffenceEngine() {
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
            if (null == lastKnownCorrespondingHTMLTag) {
                throw new IllegalArgumentException("Couldn't find id " + id + " in the last known DOM tree");
            }
            List<String> deletions = new ArrayList<>();
            List<String> attributeChanges = new ArrayList<>();
            List<String> inserts = new ArrayList<>();
            List<HTMLTag> updates = new ArrayList<>();
            determineNecessaryChanges(changingHTML, lastKnownCorrespondingHTMLTag, updates, deletions,
                    attributeChanges, inserts);
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
            if (null != attributeChanges) {
                for (String partialChange : attributeChanges) {
                    HTMLTag partialChangeHTMLTag = new HTMLTag(partialChange);
                    partialChanges.add(partialChangeHTMLTag);
                }
            }
            if (null != updates) {
                for (HTMLTag n : updates) {
                    while ((n.getId() == null) || (n.getId().length() == 0)) {
                        if (n.getParent() == null) {
                            LOGGER.severe("ID of update shouldn't be void - can't be fixed because parent tag is null");
                        }
                        else {
                            LOGGER.severe("ID of update shouldn't be void - using the parent instead");
                        }
                        LOGGER.fine(n.toCompactString());

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
            LOGGER.fine(change.getNodeName() + " - remains unchanged");
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
            List<HTMLTag> updates, List<String> deletions, List<String> attributeChanges, List<String> inserts) {
        if (newHTML.startsWith("<")) {
            HTMLTag newDOM = new HTMLTag(newHTML);
            XmlDiff.tagsAreEqualOrCanBeChangedLocally(lastKnownCorrespondingHTMLTag, newDOM, updates, deletions,
                    attributeChanges, inserts);
            for (HTMLTag d : updates) {
                LOGGER.fine("Updates: " + d);
                // JUnitTestCreator.generateJUnitTest(newDOM,
                // lastKnownCorrespondingHTMLTag, updates);
            }
            for (String d : deletions) {
                LOGGER.fine("Deletion: " + d);
            }
            for (String d : attributeChanges) {
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
     * @param currentResponse
     * @param domTreeToBeUpdated
     * @param change
     * @param newPartialChanges
     * @return
     */
    private String optimizeResponse(String currentResponse, HTMLTag domTreeToBeUpdated, HTMLTag change,
            List<HTMLTag> newPartialChanges) {
        String id = change.getId();
        if (!id.contains("javax.faces.ViewState")) {

            int start = currentResponse.indexOf("<update id=\"" + id + "\">");
            int end = currentResponse.indexOf("</update>", start);
            String currentResponseEnd = currentResponse.substring(end + "</update>".length());
            String tmpCurrentResponse = currentResponse.substring(0, start);
            for (HTMLTag changeDefinition : newPartialChanges) {
                // TODO: eval() zum Löschen der Attribute berücksichtigen;
                tmpCurrentResponse += changeDefinition.toCompactString();
                String idOfCurrentChange = changeDefinition.getId();
                if ((idOfCurrentChange == null) || (idOfCurrentChange.length() == 0)) {
                    LOGGER.severe("Missing HTMLTag ID");
                }
                else if (changeDefinition.getNodeName().equals("update")) {
                    final HTMLTag scriptNode = domTreeToBeUpdated.extractPrimeFacesJavascript(idOfCurrentChange);
                    if (null != scriptNode) {
                        tmpCurrentResponse = tmpCurrentResponse.substring(0, tmpCurrentResponse.length()
                                - "]]></update>".length())
                                + scriptNode.toCompactString() + "]]></update>";
                    }
                }

            }
            currentResponse = tmpCurrentResponse + currentResponseEnd;
        }
        return currentResponse;
    }

    /**
     * Retrieves the DOM tree that has been sent by the previous HTTP response (and that ought to be identical to the
     * version displayed in the browser).
     * 
     * @param sessionMap
     * @return
     */
    private HTMLTag retrieveLastKnownHTMLFromSession(Map<String, Object> sessionMap) {
        HTMLTag html = (HTMLTag) sessionMap.get(LAST_KNOWN_HTML_KEY);
        if (html == null) {
            throw new RuntimeException("HTML code is missing in the session");
        }
        return html;
    }

    /**
     * Fixes the DOM tree stored in the session by integrating the AJAX updates into the last known DOM tree.
     * 
     * @param domTreeInSession
     * @param typeOfChange
     * @param idToBeUpdated
     */
    private void updateHTMLTag(HTMLTag domTreeInSession, HTMLTag newSubtree, String idToBeUpdated) {

        HTMLTag tagToBeReplaced = domTreeInSession.findByID(idToBeUpdated);
        if (tagToBeReplaced == null) {
            if (!idToBeUpdated.contains("javax.faces.ViewState")) { // JSF omits the view state in AJAX responses
                LOGGER.severe("Wrong ID? Looking for " + idToBeUpdated
                        + ", but couldn't find the ID in the last known HTML tree");
            }
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
     * Compares the current HTML response with the last known HTML code. If it's a regular HTML response, the HTML code
     * is simply stored in the session. If it's an JSF AJAX response, the method looks at the differences and tries to
     * remove unchanged HTML from the response.
     * 
     * @param rawBuffer
     * @param sessionMap
     * @param isAJAX
     * @return
     */
    public String yieldDifferences(String currentResponse, Map<String, Object> sessionMap, boolean isAJAX) {
        int originalLength = currentResponse.length();
        final HTMLTag responseWithAdditionalIDs = new HTMLTag(currentResponse);
        if (isAJAX && differentialEngineActive) {

            HTMLTag domTreeToBeUpdated = retrieveLastKnownHTMLFromSession(sessionMap);
            List<HTMLTag> listOfChanges = extractChangesFromPartialResponse(responseWithAdditionalIDs);
            for (HTMLTag change : listOfChanges) {
                if (change.getNodeName().equals("update")) {
                    if (change.getId().equals("javax.faces.ViewRoot")) {
                        HTMLTag newDom = new HTMLTag(change.getFirstChild().getInnerHTML().toString());
                        domTreeToBeUpdated = newDom;
                        sessionMap.remove(LAST_KNOWN_HTML_KEY);
                        sessionMap.put(LAST_KNOWN_HTML_KEY, newDom);
                    }
                    else if (!change.getId().contains("javax.faces.ViewState")) {
                        List<HTMLTag> newPartialChanges = determineNecessaryChangeFromResponse(change,
                                domTreeToBeUpdated);
                        if ((null != newPartialChanges)) {
                            currentResponse = optimizeResponse(currentResponse, domTreeToBeUpdated, change,
                                    newPartialChanges);
                        }
                        updateHTMLTag(domTreeToBeUpdated, change.getFirstChild(), change.getId());
                    }

                }
                else {
                    LOGGER.severe("Unexpected JSF response (" + change.getNodeName() + ")");
                }
            }
        }
        else {
            sessionMap.remove(LAST_KNOWN_HTML_KEY);
            sessionMap.put(LAST_KNOWN_HTML_KEY, responseWithAdditionalIDs);
            String body = responseWithAdditionalIDs.getChildren().get(1).toString();
            int bodyIndex = currentResponse.indexOf("<body>");
            if (bodyIndex < 0) {
                bodyIndex = currentResponse.indexOf("<body ");
            }
            int bodyEndIndex = currentResponse.indexOf("</body>");
            if ((bodyIndex > 0) && (bodyEndIndex > 0)) {
                currentResponse = currentResponse.substring(0, bodyIndex)
                        + "\n<!-- Optimized by BabbageFaces, an AngularFaces subproject -->\n" + body
                        + currentResponse.substring(bodyEndIndex + "</body>".length());
            }
        }
        if (isDeveloperMode) {
            int optimizedLength = currentResponse.length();

            LOGGER.info("##################################################################################");
            // LOGGER.info(currentResponse);
            LOGGER.info("##################################################################################");
            LOGGER.info("#### BabbageFaces optimization result:");
            LOGGER.info("Original response:  " + originalLength + " bytes");
            LOGGER.info("Optimized response: " + optimizedLength + " bytes");
        }
        return currentResponse;
    }

}
