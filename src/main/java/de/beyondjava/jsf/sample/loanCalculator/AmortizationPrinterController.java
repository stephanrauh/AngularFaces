/**
 *  (C) Stephan Rauh http://www.beyondjava.net
 */

/**
 * @author Stephan Rauh http://www.beyondjava.net
 *
 */
package de.beyondjava.jsf.sample.loanCalculator;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;

import org.primefaces.context.RequestContext;

@ManagedBean
@RequestScoped
public class AmortizationPrinterController {
   private AmortizationPrinterBean amortizationPrinterBean = new AmortizationPrinterBean();

   public void generateAmortizationPlan() {
      FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Yet to be implemented",
            "This demo is work in progress.<br>Please be patient.");
      RequestContext.getCurrentInstance().showMessageInDialog(message);
   }

   public AmortizationPrinterBean getAmortizationPrinterBean() {
      return amortizationPrinterBean;
   }

   public void setAmortizationPrinterBean(AmortizationPrinterBean amortizaionPrinterBean) {
      this.amortizationPrinterBean = amortizaionPrinterBean;
   }

}
