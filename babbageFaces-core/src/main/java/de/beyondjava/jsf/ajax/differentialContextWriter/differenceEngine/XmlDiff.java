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

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.*;

public class XmlDiff {
    public static final boolean ADDED_LOCAL_CHANGE = true;
    public static final boolean GLOBAL_CHANGE_REQUIRED = false;
    private static final Logger LOGGER = Logger
            .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff");

    public static final boolean NO_CHANGE_REQUIRED = true;

    private static boolean areStringsEqualOrCanBeChangedLocally(String oldString, String newString) {
        if ((oldString == null) && (newString == null)) {
            return true;
        }

        if ((oldString == null) || (newString == null)) {
            return false;
        }

        return oldString.equals(newString);
    }

    /**
     * This method is used for debugging purposes and will soon be removed. It asserts two lists of tag have the same
     * ids in the same order.
     * 
     * @param oldHTMLTags
     * @param newHTMLTags
     */
    private static void assertTagIDMatch(List<HTMLTag> oldHTMLTags, List<HTMLTag> newHTMLTags) {
        {
            int index = 0;
            while (index < oldHTMLTags.size()) {
                HTMLTag oldTag = (oldHTMLTags.get(index));
                final HTMLTag newTag = newHTMLTags.get(index);
                if (!oldTag.getId().equals(newTag.getId())) {
                    throw new IllegalArgumentException("different tag ids");
                }
                index++;
            }
        }
    }

    /**
     * @return GLOBAL_CHANGE_REQUIRED, if the entire tag has to be exchanged. ADDED_LOCAL_CHANGE, if the tags differ,
     *         but it is not necessary to exchange the entire tag. NO_CHANGE_REQUIRED, if both tags are identical.
     * 
     * @param oldHTMLTag
     * @param newHTMLTag
     * @param attributeChanges
     * @return
     */
    public static boolean attributesAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
            List<String> attributeChanges) {
        if (!oldHTMLTag.hasAttributes() && !newHTMLTag.hasAttributes()) {
            return true;
        }

        // if (oldHTMLTag.getAttributes().size() > newHTMLTag.getAttributes().size()) {
        // return GLOBAL_CHANGE_REQUIRED;
        // }

        // Have attributes been deleted?
        boolean attributeChangeMustBeFixedGlobally = hasAnAttributeBeenDeleted(oldHTMLTag, newHTMLTag, attributeChanges);
        if (attributeChangeMustBeFixedGlobally) {
            return GLOBAL_CHANGE_REQUIRED;
        }

        HTMLAttribute newAttribute = null;
        HTMLAttribute oldAttribute = null;
        StringBuffer changeAttributes = new StringBuffer();
        String id = null;
        for (int i = 0; i < newHTMLTag.getAttributes().size(); i++) {
            newAttribute = newHTMLTag.getAttributes().get(i);
            final String newValue = String.valueOf(newAttribute.value);
            final String attributeName = newAttribute.name;
            oldAttribute = oldHTMLTag.getAttribute(attributeName);

            if (null == oldAttribute) {
                if ((newValue != null) && (newValue.length() > 0)) {
                    changeAttributes.append("<attribute name=\"" + attributeName + "\" value=\"" + newValue + "\"/>");
                }
            }
            else {
                // the value of the attribute has changed
                String oldString = String.valueOf(oldAttribute.value);
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
                if (!(oldString.equals(newValue))) {
                    changeAttributes.append("<attribute name=\"" + attributeName + "\" value=\"" + newValue + "\"/>");
                }
            }
        }
        if (changeAttributes.length() > 0) {
            if ((null != id) && (id.length() > 0)) {
                String c = "<attributes id=\"" + id + "\">" + changeAttributes.toString() + "</attributes>";
                attributeChanges.add(c);
                return ADDED_LOCAL_CHANGE;
            }
            else {
                return GLOBAL_CHANGE_REQUIRED; // this case needs an update of
                // the
                // parent
            }
        }

        return NO_CHANGE_REQUIRED;
    }

    /**
     * Compares two lists of HTML tags (the new and the old version of the same DOM subtree). Returns the smallest
     * possible set of changes required to transform the old to the new DOM subtree. <br>
     * Note the similarity between the two methods childHTMLTagsAreEqualOrCanBeChangedLocally() and
     * tagsAreEqualOrCanBeChangedLocally(). There need to be two methods because the first method recognizes the
     * difference between two lists, while the latter looks at individual tags.
     * 
     * @param oldHTMLTags
     *            the old version of a DOM subtree
     * @param newHTMLTags
     *            the new version of a DOM subtree
     * @param updates
     *            Call by reference parameter delivering the list of DOM trees that have to be exchanged completely via
     *            an update command.
     * @param deletions
     *            Call by reference parameter delivering the list of DOM trees that have been deleted.
     * @param attributeChanges
     *            Call by reference parameter delivering the list of attributes that have to be modified.
     * @return GLOBAL_CHANGE_REQUIRED, if the entire tag has to be exchanged. ADDED_LOCAL_CHANGE, if the tags differ,
     *         but it is not necessary to exchange the entire tag. NO_CHANGE_REQUIRED, if both tags are identical.
     */
    private static boolean childHTMLTagsAreEqualOrCanBeChangedLocally(List<HTMLTag> oldHTMLTags,
            List<HTMLTag> newHTMLTags, List<HTMLTag> updates, List<String> deletions, List<String> attributeChanges,
            List<String> inserts) {
        boolean needsUpdate = false;
        List<String> localDeletions = new ArrayList<>();
        List<String> localInserts = new ArrayList<>();
        List<HTMLTag> localUpdates = new ArrayList<>();
        oldHTMLTags = new ArrayList<>(oldHTMLTags); // defensive copying avoids
        // side effects
        newHTMLTags = new ArrayList<>(newHTMLTags); // defensive copying avoids
        // side effects
        List<HTMLTag> insertList = getRecentlyAddedHTMLTags(oldHTMLTags, newHTMLTags);
        if (insertList.size() > 0) {
            needsUpdate |= isThereANodeThatCannotBeInserted(insertList);
            if (oldHTMLTags.size() == 0) {
                // the insert command can only insert a node before or after
                // another node. If there's no such node, you can't insert
                // anything.
                needsUpdate = true;
            }
            if (!needsUpdate) {
                needsUpdate |= generateInsertCommands(newHTMLTags, localInserts, insertList, localUpdates);

            }
        }
        if (!needsUpdate) {
            List<HTMLTag> deleteList = getRecentlyAddedHTMLTags(newHTMLTags, oldHTMLTags);

            for (HTMLTag d : deleteList) {
                if (d.getId().isEmpty()) {
                    needsUpdate = true;
                    break;
                }
                else {
                    localDeletions.add(d.getId());
                    oldHTMLTags.remove(d);
                }
            }
        }
        if (needsUpdate) {
            LOGGER.fine("HTMLTags contain different ids or descriptions, so they require update of parent.");
            return GLOBAL_CHANGE_REQUIRED;
        }

        assertTagIDMatch(oldHTMLTags, newHTMLTags);

        int indexOld = 0;
        int indexNew = 0;
        while (indexOld < oldHTMLTags.size()) {
            HTMLTag oldTag = (oldHTMLTags.get(indexOld));
            final HTMLTag newTag = newHTMLTags.get(indexNew);
            if (!tagsAreEqualOrCanBeChangedLocally(oldTag, newTag, updates, deletions, attributeChanges, inserts)) {
                LOGGER.fine("HTMLTags are different, require update of parent. Old HTMLTag:" + oldTag.getDescription()
                        + " new HTMLTag: " + newTag.getDescription());
                if ((oldTag.getId() != null) && (oldTag.getId().length() > 0)) {
                    localUpdates.add(newTag);
                }
                else {
                    return GLOBAL_CHANGE_REQUIRED;
                }
            }
            indexOld++;
            indexNew++;
        }
        if (localDeletions.size() > 0) {
            LOGGER.fine("Adding HTMLTag deletions");
            deletions.addAll(localDeletions);
        }
        if (localInserts.size() > 0) {
            LOGGER.fine("Adding HTMLTag insertions");
            inserts.addAll(localInserts);
        }
        if (localUpdates.size() > 0) {
            LOGGER.fine("Adding HTMLTag update");
            updates.addAll(localUpdates);
        }

        return NO_CHANGE_REQUIRED;
    }

    /**
     * Generates the insert command of the AJAX response.
     * 
     * @param newHTMLTags
     * @param inserts
     * @param insertList
     * @return true if a global update is needed
     */
    private static boolean generateInsertCommands(List<HTMLTag> newHTMLTags, List<String> inserts,
            List<HTMLTag> insertList, List<HTMLTag> updates) {
        for (HTMLTag insert : insertList) {
            HTMLTag parent = insert.getParent();
            for (int index = 0; index < parent.getChildren().size(); index++) {
                HTMLTag sibling = parent.getChildren().get(index);
                if (insert == sibling) {
                    final String idOfNewTag = insert.getId();
                    // needed to fix a Mojarra bug
                    String temporaryDiv = "<div id=\"" + idOfNewTag + "\" />";
                    String s = null;

                    // try to insert the new node before a node bearing an id
                    boolean isScriptNode = false;
                    int offset = 1;
                    while (((index - offset) >= 0)
                            && "script".equals(parent.getChildren().get(index - offset).getNodeName())) {
                        offset++;
                        if ((index - offset) < 0) {
                            isScriptNode = true;
                            break;
                        }
                    }
                    if (!isScriptNode) {
                        final String idOfSibling = parent.getChildren().get(index - offset).getId();
                        if ((null != idOfSibling) && (idOfSibling.length() > 0)) {
                            s = "<insert id=\"" + idOfNewTag + "\"><after id=\"" + idOfSibling + "\"><![CDATA["
                                    + temporaryDiv + "]]></after></insert>";
                        }
                    }

                    if (null == s) {
                        // try to insert the new node after a node bearing an id
                        if (index == 0) {
                            offset = 1;
                            while ("script".equals(parent.getChildren().get(index + offset).getNodeName())) {
                                offset++;
                                if ((index + offset) >= parent.getChildren().size()) {
                                    return true; // global change required
                                }
                            }
                            final String idOfSibling = parent.getChildren().get(index + offset).getId();
                            if ((null == idOfSibling) || (idOfSibling.length() == 0)) {
                                return true;
                            }
                            s = "<insert id=\"" + idOfNewTag + "\"><before id=\"" + idOfSibling + "\"><![CDATA["
                                    + temporaryDiv + "]]></before></insert>";
                        }
                    }
                    inserts.add(s);
                    // needed to fix a Mojarra bug
                    updates.add(insert);
                    newHTMLTags.remove(insert);
                    break;

                }
            }
        }
        return false;
    }

    /**
     * XML parsers tend to produce empty HTML tags. This method strips empty HTML tags from a list of HTML tags.
     * 
     * @param tags
     * @return
     */
    private static List<HTMLTag> getNonEmptyHTMLTags(List<HTMLTag> tags) {
        List<HTMLTag> nonEmpty = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            HTMLTag n = tags.get(i);
            if (n.hasAttributes() || (n.getChildren().size() > 0)
                    || ((n.getNodeName() != null) && (n.getNodeName().length() > 0))
                    || (n.getInnerHTML().toString().trim().length() > 0)) {
                if (n.hasAttributes() && (n.getAttribute("name") != null)) {
                    if (!n.getAttribute("name").value.equals("javax.faces.ViewState")) {
                        nonEmpty.add(n);
                    }
                }
                else {
                    nonEmpty.add(n);
                }
            }
            else {
                LOGGER.severe("Empty tag!");
            }
        }
        return nonEmpty;
    }

    /**
     * Does the new list of HTML tags one or more tags that weren't present in the old list of HTML tags? Note that the
     * function can return the list of deleted tags be swapping the parameters.
     * 
     * @param oldHTMLTags
     * @param newHTMLTags
     * @return The list of HTML tags that have been added.
     */
    public static List<HTMLTag> getRecentlyAddedHTMLTags(List<HTMLTag> oldHTMLTags, List<HTMLTag> newHTMLTags) {
        List<HTMLTag> result = new ArrayList<>();
        Map<String, HTMLTag> alreadyThere = new HashMap<String, HTMLTag>();
        for (int i = 0; i < oldHTMLTags.size(); i++) {
            HTMLTag n = oldHTMLTags.get(i);
            String desc = n.getDescription();
            alreadyThere.put(desc, n);
        }
        for (int i = 0; i < newHTMLTags.size(); i++) {
            HTMLTag n = newHTMLTags.get(i);
            String desc = n.getDescription();
            if (!alreadyThere.containsKey(desc)) {
                result.add(n);
            }
        }
        return result;
    }

    /**
     * Has any attribute been deleted?
     * 
     * @param oldHTMLTag
     * @param changes
     */
    private static boolean hasAnAttributeBeenDeleted(HTMLTag oldHTMLTag, HTMLTag newHTMLTag, List<String> changes) {
        {
            HTMLAttribute newAttribute = null;
            HTMLAttribute oldAttribute = null;
            for (int i = 0; i < oldHTMLTag.getAttributes().size(); i++) {
                oldAttribute = oldHTMLTag.getAttributes().get(i);
                final String attributeName = oldAttribute.name;
                newAttribute = newHTMLTag.getAttribute(attributeName);

                if (null == newAttribute) {
                    if ((newHTMLTag.getId() != null) && (newHTMLTag.getId().length() > 0)) {
                        String s = "<eval><![CDATA[document.getElementById('" + newHTMLTag.getId()
                                + "').removeAttribute('" + attributeName + "');]]></eval>";
                        changes.add(s);
                    }
                    else {
                        // we cannot fix this attribute change locally, the entire
                        // surrounding DOM node has to be replaced
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Verifies that every node of a given list has an id (a neccessary precondition to insert it into a DOM tree).
     * 
     * @param needsUpdate
     * @param insertList
     * @return
     */
    private static boolean isThereANodeThatCannotBeInserted(List<HTMLTag> insertList) {
        boolean needsUpdate = false;
        for (HTMLTag insert : insertList) {
            if (insert.getId().isEmpty()) {
                LOGGER.fine("TODO: modify the original AJAX response by adding an ID to the node to be inserted");
                needsUpdate = true;
                break;
            }
        }
        return needsUpdate;
    }

    /**
     * Compares a new and an old version of a tag. If they differ, the method tries to determine the smallest possible
     * set of changes to transform the old version to the new version of the tags. These changes are returned via the
     * call by reference parameters "updates", "deletions", "inserts" and "attributeChanges". Sometimes the
     * transformation requires the exchange of the whole tag. If so, the method returns GLOBAL_CHANGE_REQUIRED. <br>
     * Note the similarity between the two methods childHTMLTagsAreEqualOrCanBeChangedLocally() and
     * tagsAreEqualOrCanBeChangedLocally(). There need to be two methods because the first method recognizes the
     * difference between two lists, while the latter looks at individual tags.
     * 
     * 
     * @param oldHTMLTag
     * @param newHTMLTag
     * @param updates
     * @param deletions
     * @param attributeChanges
     * @param inserts
     * @return GLOBAL_CHANGE_REQUIRED, if the entire tag has to be exchanged. ADDED_LOCAL_CHANGE, if the tags differ,
     *         but it is not necessary to exchange the entire tag. NO_CHANGE_REQUIRED, if both tags are identical.
     */
    public static boolean tagsAreEqualOrCanBeChangedLocally(HTMLTag oldHTMLTag, HTMLTag newHTMLTag,
            List<HTMLTag> updates, List<String> deletions, List<String> attributeChanges, List<String> inserts) {
        if (!oldHTMLTag.getNodeName().equals(newHTMLTag.getNodeName())) {
            return GLOBAL_CHANGE_REQUIRED;
        }
        if (!oldHTMLTag.getId().equals(newHTMLTag.getId())) {
            return GLOBAL_CHANGE_REQUIRED;
        }

        if (!areStringsEqualOrCanBeChangedLocally(oldHTMLTag.getInnerHTML().toString(), newHTMLTag.getInnerHTML()
                .toString())) {
            return GLOBAL_CHANGE_REQUIRED;
        }
        List<String> localAttributeChanges = new ArrayList<>();
        if (GLOBAL_CHANGE_REQUIRED == attributesAreEqualOrCanBeChangedLocally(oldHTMLTag, newHTMLTag,
                localAttributeChanges)) {
            LOGGER.fine("Attributes are so different that they require update of parent. Old HTMLTag:"
                    + oldHTMLTag.getDescription() + " new HTMLTag: " + newHTMLTag.getDescription());
            return GLOBAL_CHANGE_REQUIRED;
        }
        List<HTMLTag> oldHTMLTags = getNonEmptyHTMLTags(oldHTMLTag.getChildren());
        List<HTMLTag> newHTMLTags = getNonEmptyHTMLTags(newHTMLTag.getChildren());
        boolean childHTMLTagHaveChanged = !childHTMLTagsAreEqualOrCanBeChangedLocally(oldHTMLTags, newHTMLTags,
                updates, deletions, attributeChanges, inserts);
        if (childHTMLTagHaveChanged) {
            if (null == newHTMLTag.getId()) {
                return GLOBAL_CHANGE_REQUIRED;
            }
            updates.add(newHTMLTag);
        }
        if (localAttributeChanges.size() > 0) {
            attributeChanges.addAll(localAttributeChanges);
        }

        return NO_CHANGE_REQUIRED;
    }
}
