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
