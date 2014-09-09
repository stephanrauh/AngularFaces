package de.beyondjava.angularFaces.core.tagTransformer;

import java.util.Arrays;

import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;
import javax.faces.view.facelets.TagDecorator;

public class AngularTagDecorator implements TagDecorator {
	private RelaxedTagDecorator relaxedDecorator = new RelaxedTagDecorator();

	@Override
	public Tag decorate(Tag tag) {
        String ns = tag.getNamespace();
        // we only handle html tags!
        if (!("".equals(ns) || "http://www.w3.org/1999/xhtml".equals(ns))) {
        	return null;
        }
        Tag newTag = relaxedDecorator.decorate(tag);
        if (newTag != null && newTag != tag) {
        	return newTag;
        }

		if ("input".equals(tag.getLocalName())) {
			TagAttribute[] attributes = tag.getAttributes().getAll();
			TagAttribute[] moreAttributes = attributes;
//			TagAttribute[] moreAttributes = Arrays.copyOf(attributes, attributes.length + 1);
//			moreAttributes[attributes.length] = new TagAttributeImpl(tag.getLocation(), tag.getNamespace(), "type", "type", "beyond");
			TagAttributes more = new AFTagAttributes(moreAttributes);
			Tag t = new Tag(tag.getLocation(), "http://xmlns.jcp.org/jsf/html", "inputText", "h:inputText", more);
			
			String localName = tag.getLocalName();
			System.out.println(localName + (attributes == null ? "0" : attributes.length));
			return t;
		}
		return null;
	}
}
