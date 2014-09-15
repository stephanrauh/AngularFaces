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

import static org.junit.Assert.assertNotNull;

import java.io.*;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
public class PrimeFacesCheckboxTest {

    /**
     * Tests what happens when a PrimeFaces SelectBoolean has been checked.
     *
     * @throws IOException
     */
    @Test
    public void testDetermineNecessaryChanges3() throws IOException {
        final DifferenceEngine diffenceEngine = new DifferenceEngine();
        File dir = new File("src/test/resources/DifferenceEngine");

        final File partialChange = new File(dir, "partialChange3.xml");
        if (partialChange.exists()) {
            String newHTML = FileUtils.readFileToString(partialChange);
            String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html3.xml"));
            HTMLTag lastKnownCorrespondingNode = new HTMLTag(lastKnownHTML);
            List<String> deletions = new ArrayList<String>();
            List<String> attributeChanges = new ArrayList<String>();
            List<String> insertions = new ArrayList<String>();
            List<HTMLTag> updates = new ArrayList<HTMLTag>();
            diffenceEngine.determineNecessaryChanges(new HTMLTag(newHTML), lastKnownCorrespondingNode, updates,
                    deletions, attributeChanges, insertions);
            assertNotNull(updates);
            // assertEquals(0, updates.size());
            // assertEquals(1, deletions.size());
            // assertEquals(3, attributeChanges.size());
        }
    }
}
