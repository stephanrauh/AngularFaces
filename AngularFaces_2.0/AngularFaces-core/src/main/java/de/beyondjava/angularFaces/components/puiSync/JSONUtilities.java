package de.beyondjava.angularFaces.components.puiSync;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class JSONUtilities {
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.components.puiSync.JSONUtilities");

	private static Object gson;
	private static Object jackson;

	static {
		try {
			Class<?> jacksonClass = Class.forName("com.fasterxml.jackson.databind.ObjectMapper");
			jackson = jacksonClass.newInstance();
		} catch (Exception e) {
			try {
				Class<?> jacksonClass = Class.forName("org.codehaus.jackson.map.ObjectMapper");
				jackson = jacksonClass.newInstance();
			} catch (Exception e2) {
				jackson = null;
			}
		}
		try {
			Class<?> gsonClass = Class.forName("com.google.gson.Gson");
			gson = gsonClass.newInstance();
		} catch (Exception e) {
			gson = null;
		}
		if (jackson == null && gson == null) {
			LOGGER.severe("Please specify a JSON serializer! Current Gson and Jackson 1 and Jackson 2 are supported.");
			LOGGER.severe("To add Jackson, simply add these lines to your pom.xml:");
			LOGGER.severe("<dependency>");
			LOGGER.severe("  <groupId>com.fasterxml.jackson.jaxrs</groupId>");
			LOGGER.severe("  <artifactId>jackson-jaxrs-json-provider</artifactId>");
			LOGGER.severe("  <version>2.4.2</version>");
			LOGGER.severe("</dependency>");

		}

	}

	@SuppressWarnings("unchecked")
	public static Object readObjectFromJSONString(String json, Class beanType) {
		if (null != jackson) {
			try {
				Method method = jackson.getClass().getMethod("readValue", String.class, Class.class);
				if (null != method) {
					return method.invoke(jackson, json, beanType);
				}
			} catch (InvocationTargetException e) {
				Throwable rootException = e.getTargetException();
				System.out.println("Jackson couldn't read the value: " + rootException);

			} catch (ReflectiveOperationException e) {
				System.out.println("Jackson couldn't read the value: " + e);
			}
		}
		if (null != gson) {
			try {
				Method method = gson.getClass().getMethod("fromJson", String.class, Class.class);
				if (null != method) {
					return method.invoke(gson, json, beanType);
				}
			} catch (ReflectiveOperationException e) {
			}
		}
		return null;
	}

	public static String writeObjectToJSONString(Object bean) {
		if (null != jackson) {
			try {
				Method method = jackson.getClass().getMethod("writeValueAsString", Object.class);
				if (null != method) {
					return (String) method.invoke(jackson, bean);
				}
			} catch (ReflectiveOperationException e) {
			}

		}
		if (null != gson) {
			try {
				Method method = gson.getClass().getMethod("toJson", Object.class);
				if (null != method) {
					return (String) method.invoke(gson, bean);
				}
			} catch (ReflectiveOperationException e) {
			}
		}
		return null;
	}
}

// Support for Jackson:
// //1. Convert Java object to JSON format
// org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
// mapper.writeValue(new File("c:\\user.json"), user);
// //2. Convert JSON to Java object
// org.codehaus.jackson.map.ObjectMapper mapper = new org.codehaus.jackson.map.ObjectMapper();
// User user = mapper.readValue(new File("c:\\user.json"), User.class);