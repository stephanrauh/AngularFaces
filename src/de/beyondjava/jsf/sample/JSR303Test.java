package de.beyondjava.jsf.sample;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.*;

public class JSR303Test
{

  public static void main(String[] args) {
	  AdditionBean bean = new AdditionBean();
	  bean.setNumber1("123456789");
	  HibernateValidator h = new HibernateValidator();

	  ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<AdditionBean>> errors = validator.validate(bean);
      for (ConstraintViolation<AdditionBean> error : errors) {
          System.out.println("========");
          System.out.println(error);
          System.out.println("message=" + error.getMessage());
          System.out.println("propertyPath=" + error.getPropertyPath());
          System.out.println("value=" + error.getInvalidValue());
      }
  }
}
