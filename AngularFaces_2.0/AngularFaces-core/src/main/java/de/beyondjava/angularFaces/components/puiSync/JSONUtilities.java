/**
 *  (C) 2013-2015 Stephan Rauh http://www.beyondjava.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

	public static Object readObjectFromJSONString(String json, Class<?> beanType) {
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