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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
public class DifferenceEngineTest {

    /**
     * Tests the change of a single attribute.
     *
     * @throws IOException
     */
    // @Test
    public void testDetermineNecessaryChanges1() throws IOException {
        final DifferenceEngine diffenceEngine = new DifferenceEngine();
        File dir = new File("src/test/resources/DifferenceEngine");

        final File partialChange = new File(dir, "partialChange1.xml");
        if (partialChange.exists()) {
            String newHTML = FileUtils.readFileToString(partialChange);
            String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html1.xml"));
            HTMLTag lastKnownCorrespondingNode = new HTMLTag(lastKnownHTML);
            List<String> deletions = new ArrayList<>();
            List<String> attributeChanges = new ArrayList<>();
            List<String> insertions = new ArrayList<>();
            List<HTMLTag> updates = new ArrayList<>();
            diffenceEngine.determineNecessaryChanges(new HTMLTag(newHTML), lastKnownCorrespondingNode, updates,
                    deletions, attributeChanges, insertions);
            assertNotNull(updates);
            assertEquals(0, updates.size());
            assertEquals(0, deletions.size());
            assertEquals(1, attributeChanges.size());
            String diff1 = attributeChanges.get(0);
            assertEquals(
                    "<attributes id=\"formID:cityID\"><attribute name=\"value\" value=\"Jugenheim\"/></attributes>",
                    diff1);
        }
    }

    /**
     * Tests whether insert works.
     *
     * @throws IOException
     */
    // @Test
    public void testDetermineNecessaryChanges2() throws IOException {
        final DifferenceEngine diffenceEngine = new DifferenceEngine();
        File dir = new File("src/test/resources/DifferenceEngine");

        final File partialChange = new File(dir, "partialChange2.xml");
        if (partialChange.exists()) {
            String newHTML = FileUtils.readFileToString(partialChange);
            String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html2.xml"));
            HTMLTag lastKnownCorrespondingNode = new HTMLTag(lastKnownHTML);
            List<String> deletions = new ArrayList<>();
            List<String> attributeChanges = new ArrayList<>();
            List<String> insertions = new ArrayList<>();
            List<HTMLTag> updates = new ArrayList<>();
            diffenceEngine.determineNecessaryChanges(new HTMLTag(newHTML), lastKnownCorrespondingNode, updates,
                    deletions, attributeChanges, insertions);
            assertNotNull(updates);
            assertEquals(1, updates.size());
            assertEquals(0, deletions.size());
            assertEquals(0, attributeChanges.size());
            assertEquals(1, insertions.size());
            String insertion = insertions.get(0);
            assertEquals(
                    "<insert id=\"formID:firstSection\"><after id=\"formID:controlsSection\"><![CDATA[<div id=\"formID:firstSection\" />]]></after></insert>",
                    insertion);
            String update = updates.get(0).toCompactString();
            // assertEquals(
            // "<table id=\"formID:firstSection\" border=\"0\"><tbody><tr><td><label>first name</label></td><td><input name=\"formID:j_idt12\" type=\"text\"></input></td></tr><tr><td><label>last name</label></td><td><input name=\"formID:j_idt14\" type=\"text\"></input></td></tr></tbody></table>",
            // update);

        }
    }

    /**
     * Tests the change of a single attribute.
     *
     * @throws IOException
     */
    // @Test
    public void testDetermineNecessaryChanges8() throws IOException {
        final DifferenceEngine diffenceEngine = new DifferenceEngine();
        File dir = new File("src/test/resources/DifferenceEngine");

        final File partialChange = new File(dir, "partialChange8.xml");
        if (partialChange.exists()) {
            String newHTML = FileUtils.readFileToString(partialChange);
            String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html8.xml"));
            HTMLTag lastKnownCorrespondingNode = new HTMLTag(lastKnownHTML);
            List<String> deletions = new ArrayList<>();
            List<String> changes = new ArrayList<>();
            List<String> insertions = new ArrayList<>();
            List<HTMLTag> updates = new ArrayList<>();
            diffenceEngine.determineNecessaryChanges(new HTMLTag(newHTML), lastKnownCorrespondingNode, updates,
                    deletions, changes, insertions);
            assertNotNull(updates);
            assertEquals(0, updates.size());
            assertEquals(0, deletions.size());
            assertEquals(1, changes.size());
            String diff1 = changes.get(0);
            assertEquals(
                    "<attributes id=\"formID:cityID\"><attribute name=\"value\" value=\"Oppenheim\"/></attributes>",
                    diff1);
        }
    }

}
