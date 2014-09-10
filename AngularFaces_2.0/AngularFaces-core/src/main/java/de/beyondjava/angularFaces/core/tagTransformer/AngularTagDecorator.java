package de.beyondjava.angularFaces.core.tagTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;
import javax.faces.view.facelets.TagDecorator;

public class AngularTagDecorator implements TagDecorator {
	private final static Pattern angularExpressionPattern = Pattern.compile("\\{\\{(\\w+\\.)+(\\w+)\\}\\}");
	private RelaxedTagDecorator relaxedDecorator = new RelaxedTagDecorator();

	String[] angularAttributes = { "ng-app", "ng-bind", "ng-bindhtml", "ng-bindtemplate", "ng-blur", "ng-chang-e", "ng-checked",
			"ng-class", "ng-classeven", "ng-classodd", "ng-click", "ng-cloak", "ng-controller", "ng-copy", "ng-csp", "ng-cut",
			"ng-dblclick", "ng-disabled", "ng-focus", "ng-form", "ng-hide", "ng-href", "ng-if", "ng-include", "ng-init", "ng-keydown",
			"ng-keypress", "ng-keyup", "ng-list", "ng-model", "ng-modeloptions", "ng-mousedown", "ng-mouseenter", "ng-mouseleave",
			"ng-mousemove", "ng-mouseover", "ng-mouseup", "ng-nonbindable", "ng-open", "ng-paste", "ng-pluralize", "ng-readonly",
			"ng-repeat", "ng-selected", "ng-show", "ng-src", "ng-srcset", "ng-style", "ng-submit", "ng-switch", "ng-transclude", "ng-value" };

	@Override
	public Tag decorate(Tag tag) {
		TagAttributes modifiedAttributes = extractAngularAttributes(tag);
		// Apache MyFaces converts HTML tag with jsf: namespace, but missing an attribute, into jsf:element tag. We'll fix this
		// for the special case of input fields.
		if ("element".equals(tag.getLocalName())) {
			TagAttribute tagAttribute = modifiedAttributes.get("http://xmlns.jcp.org/jsf/passthrough", "elementName");
			if ("input".equals(tagAttribute.getValue())) {
				TagAttribute[] attributes = modifiedAttributes.getAll();
				TagAttribute[] lessAttributes = Arrays.copyOf(attributes, attributes.length - 1);
				TagAttributes less = new AFTagAttributes(lessAttributes);
				Tag t = new Tag(tag.getLocation(), "http://xmlns.jcp.org/jsf/html", "inputText", "h:inputText", less);

				String localName = tag.getLocalName();
				System.out.println(localName + (attributes == null ? "0" : attributes.length));
				return t;
			}
		}

		String ns = tag.getNamespace();
		// we only handle html tags!
		if (!("".equals(ns) || "http://www.w3.org/1999/xhtml".equals(ns))) {
			return generateTagIfNecessary(tag, modifiedAttributes);
		}
		Tag newTag = relaxedDecorator.decorate(tag);
		if (newTag != null && newTag != tag) {
			return newTag;
		}

		if ("input".equals(tag.getLocalName())) {
			return convertToInputText(tag, modifiedAttributes);
		}
		return generateTagIfNecessary(tag, modifiedAttributes);
	}

	private Tag convertToInputText(Tag tag, TagAttributes modifiedAttributes) {
		TagAttribute[] attributes = modifiedAttributes.getAll();
		TagAttribute[] moreAttributes = attributes;
		TagAttributes more = new AFTagAttributes(moreAttributes);
		Tag t = new Tag(tag.getLocation(), "http://xmlns.jcp.org/jsf/html", "inputText", "h:inputText", more);
		return t;
	}

	private Tag generateTagIfNecessary(Tag tag, TagAttributes modifiedAttributes) {
		if (modifiedAttributes != tag.getAttributes()) {
			Tag t = new Tag(tag.getLocation(), tag.getNamespace(), tag.getLocalName(), tag.getQName(), modifiedAttributes);
			return t;
		}
		return null;
	}

	private TagAttributes extractAngularAttributes(Tag tag) {
		TagAttribute[] attrs = tag.getAttributes().getAll();
		List<TagAttribute> modified = new ArrayList<TagAttribute>();
		boolean hasChanges = false;
		String angularExpressions = "";
		for (TagAttribute a : attrs) {
			String modifiedValue = a.getValue();
			boolean firstMatch = true;
			Matcher matcher = angularExpressionPattern.matcher(a.getValue());
			while (matcher.find()) {
				String exp = matcher.group();
				angularExpressions += "," + exp;
				modifiedValue = modifiedValue.replace(exp, "#" + exp.substring(1, exp.length() - 1));
				if ("value".equals(a.getLocalName())) {
					TagAttribute modifiedAttribute = TagAttributeUtilities.createTagAttribute(a.getLocation(),
							"http://xmlns.jcp.org/jsf/passthrough", "ng-model", "p:ng-model", exp.substring(2, exp.length() - 2));
					modified.add(modifiedAttribute);
					if (!firstMatch) {
						System.out.println("Tag " + tag.getQName() + " can't have multiple ng-models.");
					}
					firstMatch = false;
				}
				hasChanges = true;
			}
			TagAttribute modifiedAttribute;
			if (a.getLocalName().startsWith("ng-")) {
				// make AngularJS attributes pass-through attributes
				modifiedAttribute = TagAttributeUtilities.createTagAttribute(a.getLocation(), "http://xmlns.jcp.org/jsf/passthrough", a.getLocalName(),
						"p:" + a.getLocalName(), modifiedValue);
				hasChanges=true;
			} else {
				modifiedAttribute = TagAttributeUtilities.createTagAttribute(a.getLocation(), a.getNamespace(), a.getLocalName(),
						a.getQName(), modifiedValue);
			}
			modified.add(modifiedAttribute);
		}
		if (angularExpressions.length()>0) {
			System.out.println(angularExpressions);
			TagAttribute af = TagAttributeUtilities.createTagAttribute(tag.getLocation(), "http://xmlns.jcp.org/jsf/passthrough",
					"angularfacesattributes", "p:angularfacesattribute", angularExpressions.substring(1));
			modified.add(0, af);
			TagAttribute[] modifiedAttributeList = new TagAttribute[modified.size()];
			for (int i = 0; i < modified.size(); i++) {
				modifiedAttributeList[i] = modified.get(i);
			}
			return new AFTagAttributes((TagAttribute[]) modifiedAttributeList);
		}
		// TODO Auto-generated method stub
		return tag.getAttributes();
	}
}
