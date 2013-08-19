package de.beyondjava.jsfComponents.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;

import javax.el.*;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ELTools {
   private static Map<String, NGBeanAttributeInfo> beanAttributeInfos = new HashMap<>();

   /** Caching */
   private static Map<String, Field> fields = new HashMap<>();

   /**
    * Evaluates an EL expression into an object.
    * 
    * @param p_expression
    * @return
    */
   public static Object evalAsObject(String p_expression) {
      FacesContext context = FacesContext.getCurrentInstance();
      ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
      ELContext elContext = context.getELContext();
      ValueExpression vex = expressionFactory.createValueExpression(elContext, p_expression, Object.class);
      Object result = vex.getValue(elContext);
      return result;
   }

   /**
    * Evaluates an EL expression into a string.
    * 
    * @param p_expression
    * @return
    */
   public static String evalAsString(String p_expression) {
      FacesContext context = FacesContext.getCurrentInstance();
      ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
      ELContext elContext = context.getELContext();
      ValueExpression vex = expressionFactory.createValueExpression(elContext, p_expression, String.class);
      String result = (String) vex.getValue(elContext);
      return result;
   }

   public static NGBeanAttributeInfo getBeanAttributeInfos(UIComponent c) {
      String core = getCoreValueExpression(c);
      synchronized (beanAttributeInfos) {
         if (beanAttributeInfos.containsKey(c)) {
            return beanAttributeInfos.get(c);
         }
      }
      NGBeanAttributeInfo info = new NGBeanAttributeInfo(c);
      beanAttributeInfos.put(core, info);
      return info;
   }

   public static String getCoreValueExpression(UIComponent component) {
      ValueExpression valueExpression = component.getValueExpression("value");
      String v = valueExpression.getExpressionString();
      if (null != v) {
         String model = v.replace("#{", "").replace("}", "");
         return model;
      }
      return null;
   }

   private static Field getField(String p_expression) {
      synchronized (fields) {
         if (fields.containsKey(p_expression)) {
            return fields.get(p_expression);
         }
      }

      if (p_expression.startsWith("#{") && p_expression.endsWith("}")) {
         int delimiterPos = p_expression.lastIndexOf('.');
         String beanExp = p_expression.substring(0, delimiterPos) + "}";
         String fieldName = p_expression.substring(delimiterPos + 1, p_expression.length() - 1);
         Object container = evalAsObject(beanExp);
         Class<? extends Object> c = container.getClass();
         while (c != null) {
            Field declaredField;
            try {
               declaredField = c.getDeclaredField(fieldName);
               synchronized (fields) {
                  fields.put(p_expression, declaredField);
               }
               return declaredField;
            }
            catch (NoSuchFieldException e) {
               // let's try with the super class
               c = c.getSuperclass();
            }
            catch (SecurityException e) {
               System.err.println("Unable to access a field");
               e.printStackTrace();
               return null;
            }
         }
      }
      return null;
   }

   public static String getNGModel(UIComponent p_component) {
      String id = ELTools.getCoreValueExpression(p_component);
      if (id.contains(".")) {
         int index = id.lastIndexOf(".");
         id = id.substring(index + 1);
      }
      return id;
   }

   /**
    * Yields the type of the variable given by an expression.
    * 
    * @param p_expression
    * @return
    */
   public static Class<?> getType(String p_expression) {
      Field declaredField = getField(p_expression);
      if (null != declaredField) {
         return declaredField.getType();
      }
      return null;
   }

   /**
    * Yields the type of the variable displayed by a component.
    * 
    * @param p_component
    *           the UIComponent
    * @return
    */
   public static Class<?> getType(UIComponent p_component) {
      ValueExpression valueExpression = p_component.getValueExpression("value");
      if (valueExpression != null) {
         return getType(valueExpression.getExpressionString());
      }
      return null;
   }

   public static boolean hasValueExpression(UIComponent component) {
      ValueExpression valueExpression = component.getValueExpression("value");
      return null != valueExpression;
   }

   /**
    * Which annotations are given to an object described by an EL expression?
    * 
    * @param p_component
    * @return null if there are no annotations, or if they cannot be accessed
    */
   private static Annotation[] readAnnotations(String p_expression) {
      Field declaredField = getField(p_expression);
      if (null != declaredField) {
         return declaredField.getAnnotations();
      }
      return null;
   }

   /**
    * Which annotations are given to an object displayed by a JSF component?
    * 
    * @param p_component
    * @return null if there are no annotations, or if they cannot be accessed
    */
   public static Annotation[] readAnnotations(UIComponent p_component) {
      ValueExpression valueExpression = p_component.getValueExpression("value");
      if (valueExpression != null) {
         return readAnnotations(valueExpression.getExpressionString());
      }
      return null;
   }

}
