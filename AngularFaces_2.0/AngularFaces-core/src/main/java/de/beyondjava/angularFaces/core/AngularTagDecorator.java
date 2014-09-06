package de.beyondjava.angularFaces.core;

import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttributes;
import javax.faces.view.facelets.TagDecorator;


public class AngularTagDecorator implements TagDecorator {

	@Override
	public Tag decorate(Tag tag) {
		TagAttributes attributes = tag.getAttributes();
		String localName = tag.getLocalName();
		System.out.println(localName + (attributes==null?"0":attributes.getAll().length));
		return tag;
	}
}
