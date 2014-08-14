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
package de.beyondjava.jsf.sample.additions;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.*;
import javax.faces.context.FacesContext;
import javax.validation.constraints.*;

@ManagedBean
@SessionScoped
public class CalculatorBean implements Serializable {
    String color = "#0F0";

    @Min(7L)
    @Max(50L)
    @NotNull
    int number1 = 42;

    @Max(100L)
    @Min(10L)
    @NotNull
    int number2 = 65;

    @Max(100L)
    @Min(10L)
    @NotNull
    int number3 = 33;

    int sum = 0;

    public String add() {
        sum = number1 + number2;
        String msg = "Last calculation on the server side: " + number1 + "+" + number2 + "=" + sum;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
        return null;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return this.color;
    }

    /**
     * @return the number1
     */
    public int getNumber1() {
        return this.number1;
    }

    /**
     * @return the number2
     */
    public int getNumber2() {
        return this.number2;
    }

    public int getNumber3() {
        number3++;
        return number3;
    }

    /**
     * @return the result
     */
    public int getResult() {
        return this.sum;
    }

    /**
     * @param color
     *            the color to set
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * @param number1
     *            the number1 to set
     */
    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    /**
     * @param number2
     *            the number2 to set
     */
    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public void setNumber3(int i) {
        number3 = i;
    }

    /**
     * @param result
     *            the result to set
     */
    public void setResult(int result) {
        this.sum = result;
    }
    
    public String getGridStyle(){
    	return "font-weight:bold";
    }
    public String getHeaderText(){
    	return "Header text from Angular Model. The last calculation on the server side was " + number1 + " + " + number2 + " = " + sum;
    }
}
