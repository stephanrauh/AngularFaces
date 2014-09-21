package de.beyondjava.angularFaces.components.puiSync;

import com.google.gson.Gson;

public class JSONUtilities {
	@SuppressWarnings("unchecked")
	public static Object readObjectFromJSONString(String json, Class beanType) {
		return new Gson().fromJson(json, beanType);
	}
	
	public static String writeObjectToJSONString(Object bean) {
		return new Gson().toJson(bean);
	}
}
