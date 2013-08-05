package de.beyondjava.jsf.experiments;

import static org.junit.Assert.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;

import de.beyondjava.jsf.sample.additions.AdditionBean;

import groovy.util.GroovyTestCase;

class BeanValidationTest extends GroovyTestCase
{
   public void testValidation()
   {
      AdditionBean bean = new AdditionBean();
      bean.number1="123456789";

      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<AdditionBean>> errors = validator.validate(bean);

      assertTrue(errors?.size()>0)

      errors.each{ConstraintViolation<AdditionBean> error ->
         println("========");
         println(error);
         println("message= ${error.message}");
         println("propertyPath=${error.propertyPath}");
         println("value= ${error.invalidValue}");
      }
   }
}
