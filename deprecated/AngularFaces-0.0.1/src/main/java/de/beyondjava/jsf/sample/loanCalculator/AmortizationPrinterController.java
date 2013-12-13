/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.loanCalculator;

import java.io.*;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;

import org.primefaces.context.RequestContext;
import org.primefaces.model.*;

@ManagedBean
@RequestScoped
public class AmortizationPrinterController {
   private AmortizationPrinterBean amortizationPrinterBean = new AmortizationPrinterBean();

   public void generateAmortizationPlan() {
      double balance = amortizationPrinterBean.getLoanAmount();
      double interestRatePM = amortizationPrinterBean.getInterestRate() / 12;
      double payments = amortizationPrinterBean.getMonthlyPayments();
      payments = Math.round(100.0d * payments) / 100.0d;
      ArrayList<AmortizationRow> plan = new ArrayList<AmortizationRow>();
      int month = 1;
      while (balance > 0) {
         AmortizationRow row = new AmortizationRow();
         row.setBalance(balance);
         row.setMonth(month++);
         double interestPaid = (balance * (interestRatePM / 100.0d));
         interestPaid = Math.round(100.0d * interestPaid) / 100.0d;
         row.setInterestPaid(interestPaid);
         double principalPaid = payments - interestPaid;
         if (balance < principalPaid) {
            principalPaid = balance;
         }
         principalPaid = Math.round(100.0d * principalPaid) / 100.0d;
         row.setPrincipalPaid(principalPaid);
         row.setMonthlyPayment(interestPaid + principalPaid);
         balance = Math.round(100.0d * balance) / 100.0d;
         balance -= principalPaid;
         balance = Math.round(100.0d * balance) / 100.0d;
         plan.add(row);
      }

      amortizationPrinterBean.setAmortizationPlan(plan);
   }

   public AmortizationPrinterBean getAmortizationPrinterBean() {
      return amortizationPrinterBean;
   }

   public StreamedContent getGenerateAmortizationPlanAsPDF() {
      generateAmortizationPlan();
      try {
         InputStream stream = AmortizationPDFPrinter.printAmortizationPlan(amortizationPrinterBean
               .getAmortizationPlan());

         DefaultStreamedContent file = new DefaultStreamedContent(stream, "application/pdf", "amortizationPlan.pdf");
         return file;

      }
      catch (IOException error) {
         FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "error",
               "A technical error occurred when generating the PDF file.");
         RequestContext.getCurrentInstance().showMessageInDialog(message);
         return null;
      }

   }

   public void setAmortizationPrinterBean(AmortizationPrinterBean amortizationPrinterBean) {
      this.amortizationPrinterBean = amortizationPrinterBean;
   }

}
