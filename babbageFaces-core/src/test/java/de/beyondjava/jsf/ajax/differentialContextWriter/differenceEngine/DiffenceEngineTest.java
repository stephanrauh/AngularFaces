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
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * 
 */
public class DiffenceEngineTest {

   /**
    * Test method for
    * {@link de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.DiffenceEngine#determineNecessaryChanges(java.lang.String, org.w3c.dom.Node)}
    * .
    * 
    * @throws IOException
    */
   @Test
   public void testDetermineNecessaryChanges1() throws IOException {
      final DiffenceEngine diffenceEngine = new DiffenceEngine();
      File dir = new File("src/test/resources/DifferenceEngine");

      final File partialChange = new File(dir, "partialChange1.xml");
      if (partialChange.exists()) {
         String newHTML = FileUtils.readFileToString(partialChange);
         String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html1.xml"));
         HTMLTag lastKnownCorrespondingNode = new HTMLTag(lastKnownHTML);
         ArrayList<String> deletions = new ArrayList<String>();
         ArrayList<String> attributeChanges = new ArrayList<String>();
         ArrayList<HTMLTag> necessaryChanges = diffenceEngine.determineNecessaryChanges(newHTML,
               lastKnownCorrespondingNode, deletions, attributeChanges);
         assertNotNull(necessaryChanges);
         assertEquals(0, necessaryChanges.size());
         assertEquals(0, deletions.size());
         assertEquals(1, attributeChanges.size());
         String diff1 = attributeChanges.get(0);
         assertEquals("<attributes id=\"formID:cityID\"><attribute name=\"value\" value=\"Jugenheim\"/></attributes>",
               diff1);
      }
   }

   @Test
   public void testDetermineNecessaryChanges8() throws IOException {
      final DiffenceEngine diffenceEngine = new DiffenceEngine();
      File dir = new File("src/test/resources/DifferenceEngine");

      final File partialChange = new File(dir, "partialChange8.xml");
      if (partialChange.exists()) {
         String newHTML = FileUtils.readFileToString(partialChange);
         String lastKnownHTML = FileUtils.readFileToString(new File(dir, "html8.xml"));
         HTMLTag lastKnownCorrespondingNode = new HTMLTag(lastKnownHTML);
         ArrayList<String> deletions = new ArrayList<String>();
         ArrayList<String> changes = new ArrayList<String>();
         ArrayList<HTMLTag> necessaryChanges = diffenceEngine.determineNecessaryChanges(newHTML,
               lastKnownCorrespondingNode, deletions, changes);
         assertNotNull(necessaryChanges);
         assertEquals(0, necessaryChanges.size());
         assertEquals(0, deletions.size());
         assertEquals(1, changes.size());
         String diff1 = changes.get(0);
         assertEquals("<attributes id=\"formID:cityID\"><attribute name=\"value\" value=\"Oppenheim\"/></attributes>",
               diff1);
      }
   }

}
