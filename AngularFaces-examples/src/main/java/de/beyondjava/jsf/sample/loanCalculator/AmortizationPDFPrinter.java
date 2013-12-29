/**
 *  (C) 2013-2014 Stephan Rauh http://www.beyondjava.net
 *  PDF-table creation algorithm copied from http://fahdshariff.blogspot.de/2010/10/creating-tables-with-pdfbox.html
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
package de.beyondjava.jsf.sample.loanCalculator;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

/**
 * @author Stephan Rauh http://www.beyondjava.net
 * @see http://fahdshariff.blogspot.de/2010/10/creating-tables-with-pdfbox.html
 */
public class AmortizationPDFPrinter {
   /**
    * @param page
    * @param contentStream
    * @param y
    *           the y-coordinate of the first row
    * @param margin
    *           the padding on left and right of table
    * @param content
    *           a 2d array containing the table data
    * @throws IOException
    */
   public static void drawTable(PDPage page, PDPageContentStream contentStream, float y, float margin,
         String[][] content) throws IOException {
      final int rows = content.length;
      final int cols = content[0].length;
      final float rowHeight = 20f;
      final float tableWidth = page.findMediaBox().getWidth() - (2 * margin);
      final float tableHeight = rowHeight * rows;
      final float colWidth = tableWidth / cols;
      final float cellMargin = 5f;

      // draw the rows
      float nexty = y;
      for (int i = 0; i <= rows; i++) {
         contentStream.drawLine(margin, nexty, margin + tableWidth, nexty);
         nexty -= rowHeight;
      }

      // draw the columns
      float nextx = margin;
      for (int i = 0; i <= cols; i++) {
         contentStream.drawLine(nextx, y, nextx, y - tableHeight);
         nextx += colWidth;
      }

      // now add the text
      contentStream.setFont(PDType1Font.COURIER, 10);

      float textx = margin + cellMargin;
      float texty = y - 15;
      for (String[] element : content) {
         for (String text : element) {
            contentStream.beginText();
            contentStream.moveTextPositionByAmount(textx, texty);
            contentStream.drawString(text);
            contentStream.endText();
            textx += colWidth;
         }
         texty -= rowHeight;
         textx = margin + cellMargin;
      }
   }

   public static void main(String[] args) throws IOException, COSVisitorException {
      PDDocument doc = new PDDocument();
      PDPage page = new PDPage();
      doc.addPage(page);

      PDPageContentStream contentStream = new PDPageContentStream(doc, page);

      String[][] content = { { "a", "b", "1" }, { "c", "d", "2" }, { "e", "f", "3" }, { "g", "h", "4" },
            { "i", "j", "5" } };

      drawTable(page, contentStream, 700, 100, content);
      contentStream.close();
      doc.save("test.pdf");
   }

   // ((ServletContext)
   // FacesContext.getCurrentInstance().getExternalContext().getContext())
   // .getResourceAsStream("/images/optimusprime.jpg");

   public static InputStream printAmortizationPlan(List<AmortizationRow> amortizationPlan) throws IOException {
      try {
         PDDocument doc = new PDDocument();

         for (int p = 0; p < amortizationPlan.size();) {
            int rowsOnCurrentPage = amortizationPlan.size() - p;
            if (rowsOnCurrentPage > 30) {
               rowsOnCurrentPage = 30;
            }
            PDPage page = new PDPage();
            doc.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(doc, page);
            String[][] content = new String[1 + rowsOnCurrentPage][5];
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
            NumberFormat numberFormatter = NumberFormat.getNumberInstance();
            content[0][0] = "month";
            content[0][1] = "balance";
            content[0][2] = "payment";
            content[0][3] = "interest";
            content[0][4] = "principal";
            for (int row = p; (row < amortizationPlan.size()) && (row < (p + rowsOnCurrentPage)); row++) {
               AmortizationRow a = amortizationPlan.get(row);
               content[(1 + row) - p] = new String[5];
               content[(1 + row) - p][0] = numberFormatter.format(a.getMonth());
               content[(1 + row) - p][1] = currencyFormatter.format(a.getBalance());
               content[(1 + row) - p][2] = currencyFormatter.format(a.getMonthlyPayment());
               content[(1 + row) - p][3] = currencyFormatter.format(a.getInterestPaid());
               content[(1 + row) - p][4] = currencyFormatter.format(a.getPrincipalPaid());
            }

            drawTable(page, contentStream, 700, 100, content);
            contentStream.close();
            p += rowsOnCurrentPage;
         }
         ByteArrayOutputStream bo = new ByteArrayOutputStream();
         doc.close();
         doc.save(bo);
         byte[] pdfAsByteArray = bo.toByteArray();
         ByteArrayInputStream bi = new ByteArrayInputStream(pdfAsByteArray);
         return bi;
      }
      catch (COSVisitorException exception) {
         throw new IOException("Couldn't create amortization plan as PDF file", exception);
      }

   }
}
