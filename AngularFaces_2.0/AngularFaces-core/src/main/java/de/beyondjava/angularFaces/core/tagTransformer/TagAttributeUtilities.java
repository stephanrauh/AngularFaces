package de.beyondjava.angularFaces.core.tagTransformer;

import java.lang.reflect.Constructor;

import javax.faces.view.Location;
import javax.faces.view.facelets.TagAttribute;

public class TagAttributeUtilities {
    public static TagAttribute createTagAttribute(Location location, String ns, String myLocalName, String qName, String value) {
    	try {
    		Class<?> implClass;
    		try {
    	implClass = Class.forName("org.apache.myfaces.view.facelets.tag.TagAttributeImpl");
    		} catch (Exception e) {
    	implClass = Class.forName("com.sun.faces.facelets.tag.TagAttributeImpl");
    		}
    		Constructor<?> constructor = implClass.getConstructor(Location.class, String.class, String.class, String.class, String.class);
    		Object newTagAttribute = constructor.newInstance(location, ns, myLocalName, qName, value);
			return (TagAttribute) newTagAttribute;
    	} catch (ReflectiveOperationException e) {
    		throw new IllegalArgumentException("Couldn't create neither a Oracle Mojarra Tag attribute nor an Apache MyFaces TagAttribute", e);
    	}
    }

}
