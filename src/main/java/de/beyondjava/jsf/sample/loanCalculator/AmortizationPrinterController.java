/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.loanCalculator;

import java.util.ArrayList;

import javax.faces.bean.*;

@ManagedBean
@RequestScoped
public class AmortizationPrinterController {
   private AmortizationPrinterBean amortizationPrinterBean = new AmortizationPrinterBean();

   public void generateAmortizationPlan() {
      double balance = amortizationPrinterBean.getLoanAmount();
      double term = amortizationPrinterBean.getLoanTerm();
      double interestRate = amortizationPrinterBean.getInterestRate();
      double payments = amortizationPrinterBean.getMonthlyPayments();
      payments = Math.round(100.0d * payments) / 100.0d;
      ArrayList<AmortizationRow> plan = new ArrayList<>();
      int month = 1;
      while (balance > 0) {
         AmortizationRow row = new AmortizationRow();
         row.setBalance(balance);
         row.setMonth(month++);
         double interestPaid = (balance * (interestRate / 100.0d)) / 12.0d;
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
         balance = balance * (1 + (((interestRate / 100.0d)) / 12.0d));
         balance -= principalPaid;
         balance = Math.round(100.0d * balance) / 100.0d;
         plan.add(row);
      }

      amortizationPrinterBean.setAmortizationPlan(plan);
      // FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
      // "Yet to be implemented",
      // "This demo is work in progress.<br>Please be patient.");
      // RequestContext.getCurrentInstance().showMessageInDialog(message);
   }

   public AmortizationPrinterBean getAmortizationPrinterBean() {
      return amortizationPrinterBean;
   }

   public void setAmortizationPrinterBean(AmortizationPrinterBean amortizationPrinterBean) {
      this.amortizationPrinterBean = amortizationPrinterBean;
   }

}
