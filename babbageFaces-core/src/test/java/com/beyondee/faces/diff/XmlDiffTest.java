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
package com.beyondee.faces.diff;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff;
import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

public class XmlDiffTest {
   public ArrayList<HTMLTag> diff(int testNr) {
      try {
         String oldHTMLString = FileUtils.readFileToString(new File("target/test-classes/test" + testNr + "-old.xml"));
         String newHTMLString = FileUtils.readFileToString(new File("target/test-classes/test" + testNr + "-new.xml"));
         HTMLTag oldHTMLTag = new HTMLTag(oldHTMLString);
         HTMLTag newHTMLTag = new HTMLTag(newHTMLString);

         ArrayList<String> deletions = new ArrayList<String>();
         ArrayList<String> changes = new ArrayList<String>();

         return XmlDiff.getDifferenceOfHTMLTags(oldHTMLTag, newHTMLTag, deletions, changes);
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @Test
   public void test1() {
      ArrayList<HTMLTag> updates = diff(1);
      assertEquals("<firstname id=\"f1\">low1</firstname>", updates.get(0).toCompactString());
      assertEquals("<salary id=\"s\"><test2>200000</test2></salary>", updates.get(1).toCompactString());
      assertEquals(2, updates.size());
      assertEquals("f1", updates.get(0).getId());
      assertEquals("s", updates.get(1).getId());
   }

   @Test
   public void test2() {
      ArrayList<HTMLTag> updates = diff(2);
      assertTrue(updates.get(0).toCompactString().startsWith("<staff id=\"1001\">"));
      assertTrue(updates.get(1).toCompactString().startsWith("<staff id=\"2001\">"));

      assertEquals(2, updates.size());
      assertEquals("1001", updates.get(0).getId());
      assertEquals("2001", updates.get(1).getId());
   }

}
