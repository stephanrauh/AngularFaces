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

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

import de.beyondjava.jsf.ajax.differentialContextWriter.parser.HTMLTag;

public class JUnitTestCreator {

   private static final Logger LOGGER = Logger
         .getLogger("de.beyondjava.jsf.ajax.differentialContextWriter.differenceEngine.JUnitTestCreator");

   /**
    * Stores the original HTML tree, the AJAX response send by JSF and the list
    * of differences in order to generate a JUnit test.
    * 
    * @param newHTML
    * @param lastKnownCorrespondingHTMLTag
    * @param differences
    */
   static void generateJUnitTest(HTMLTag newHTML, HTMLTag lastKnownCorrespondingHTMLTag, List<HTMLTag> differences) {
      File dir = new File("D:\\eclipse\\workspace\\AngularFaces\\babbageFaces-core\\src\\test\\resources\\DifferenceEngine");
      System.out.println("dir" + dir.exists());
      if (dir.exists()) {
         int freeNumber = 0;
         File testcase;
         File partialChange;
         do {
            freeNumber++;
            testcase = new File(dir, "html" + freeNumber + ".xml");
            partialChange = new File(dir, "partialChange" + freeNumber + ".xml");

         } while (testcase.exists());
         try {
            FileUtils.write(testcase, lastKnownCorrespondingHTMLTag.toString());
            FileUtils.write(partialChange, newHTML.toString());
            int index = 1;
            for (HTMLTag d : differences) {
               File differenceFile = new File(dir, "difference" + freeNumber + "_" + index + ".xml");
               FileUtils.write(differenceFile, d.toString());
            }
         }
         catch (IOException e) {
            LOGGER.severe("Couldn't create JUnit test case");
         }

      }

   }
}
