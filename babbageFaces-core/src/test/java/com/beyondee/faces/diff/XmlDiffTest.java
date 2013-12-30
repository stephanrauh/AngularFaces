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

import java.io.File;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.XmlDiff;
import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

public class XmlDiffTest {
   public List<String> diff(int testNr) {
      try {
         String oldHTMLString = FileUtils.readFileToString(new File("target/test-classes/test" + testNr + "-old.xml"));
         String newHTMLString = FileUtils.readFileToString(new File("target/test-classes/test" + testNr + "-new.xml"));
         HTMLTag oldHTMLTag = new HTMLTag(oldHTMLString);
         HTMLTag newHTMLTag = new HTMLTag(newHTMLString);

         List<String> deletions = new ArrayList<>();
         List<String> changes = new ArrayList<>();
         List<String> insertions = new ArrayList<>();

         List<HTMLTag> updates = XmlDiff
               .getDifferenceOfHTMLTags(oldHTMLTag, newHTMLTag, deletions, changes, insertions);
         List<String> result = new ArrayList<>();
         result.addAll(deletions);
         result.addAll(changes);
         result.addAll(insertions);
         for (HTMLTag u : updates) {
            result.add(u.toCompactString());
         }
         return result;
      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   @Test
   public void test1() {
      List<String> updates = diff(1);
      assertEquals(2, updates.size());
      assertEquals("<firstname id=\"f1\">low1</firstname>", updates.get(0));
      assertEquals("<salary id=\"s\"><test2>200000</test2></salary>", updates.get(1));
   }

   @Test
   public void test2() {
      List<String> updates = diff(2);
      assertEquals(6, updates.size());
      assertEquals("<insert id=\"s1001\"><after id=\"\"><![CDATA[<div id=\"s1001\" />]]></after></insert>",
            updates.get(0));
      assertEquals("<insert id=\"t\"><after id=\"s2001\"><![CDATA[<div id=\"t\" />]]></after></insert>", updates.get(1));
      assertEquals("<someNewTag id=\"s1001\"><test2>200000</test2></someNewTag>", updates.get(2));
      assertEquals("<firstname id=\"f1\">low1</firstname>", updates.get(3));
      assertEquals("<salary id=\"s2001\"><test2>200000</test2></salary>", updates.get(4));
      assertEquals("<someNewTag id=\"t\"><test2>200000</test2></someNewTag>", updates.get(5));

   }

}
