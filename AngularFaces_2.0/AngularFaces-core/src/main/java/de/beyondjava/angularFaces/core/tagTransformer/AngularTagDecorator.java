package de.beyondjava.angularFaces.core.tagTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;
import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;
import javax.faces.view.facelets.TagDecorator;

public class AngularTagDecorator implements TagDecorator {
	private static boolean active = false;
	private static final String PASS_THROUGH_NAMESPACE = "http://xmlns.jcp.org/jsf/passthrough";
	private static final String MOJARRA_NAMESPACE = "http://xmlns.jcp.org/jsf/html";
	private final static Pattern angularExpressionPattern = Pattern.compile("\\{\\{(\\w+\\.)+(\\w+)\\}\\}");
	private final RelaxedTagDecorator relaxedDecorator = new RelaxedTagDecorator();

	@Override
	public Tag decorate(Tag tag) {
		TagAttributes modifiedAttributes = extractAngularAttributes(tag);
		// Apache MyFaces converts HTML tag with jsf: namespace, but missing an attribute, into jsf:element tag. We'll fix this
		// for the special case of input fields.

		if ("view".equals(tag.getLocalName())) {
			active = true;
		}

		if ("element".equals(tag.getLocalName())) {
			TagAttribute tagAttribute = modifiedAttributes.get(PASS_THROUGH_NAMESPACE, "elementName");
			if ("input".equals(tagAttribute.getValue())) {
				return convertElementToInputText(tag, modifiedAttributes);
			}
		}

		if (!isHTMLNamespace(tag.getNamespace())) {
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

	private Tag convertElementToInputText(Tag tag, TagAttributes modifiedAttributes) {
		TagAttribute[] attributes = modifiedAttributes.getAll();
		TagAttribute[] lessAttributes = Arrays.copyOf(attributes, attributes.length - 1);
		TagAttributes less = new AFTagAttributes(lessAttributes);
		Tag t = new Tag(tag.getLocation(), MOJARRA_NAMESPACE, "inputText", "h:inputText", less);
		return t;
	}

	public static boolean isActive() {
		return active;
	}

	private boolean isHTMLNamespace(String ns) {
		return "".equals(ns) || "http://www.w3.org/1999/xhtml".equals(ns);
	}

	private Tag convertToInputText(Tag tag, TagAttributes modifiedAttributes) {
		TagAttribute[] attributes = modifiedAttributes.getAll();
		TagAttribute[] moreAttributes = attributes;
		TagAttributes more = new AFTagAttributes(moreAttributes);
		Tag t = new Tag(tag.getLocation(), MOJARRA_NAMESPACE, "inputText", "h:inputText", more);
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
					TagAttribute modifiedAttribute = TagAttributeUtilities.createTagAttribute(a.getLocation(), PASS_THROUGH_NAMESPACE,
							"ng-model", "p:ng-model", exp.substring(2, exp.length() - 2));
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
				modifiedAttribute = TagAttributeUtilities.createTagAttribute(a.getLocation(), PASS_THROUGH_NAMESPACE, a.getLocalName(),
						a.getLocalName(), modifiedValue);
				hasChanges = true;
			} else {
				modifiedAttribute = TagAttributeUtilities.createTagAttribute(a.getLocation(), a.getNamespace(), a.getLocalName(),
						a.getQName(), modifiedValue);
			}
			modified.add(modifiedAttribute);
		}
		if (hasChanges) {
			if (angularExpressions.length() > 0) {
				TagAttribute af = TagAttributeUtilities.createTagAttribute(tag.getLocation(), PASS_THROUGH_NAMESPACE,
						"angularfacesattributes", "p:angularfacesattribute", angularExpressions.substring(1));
				modified.add(0, af);
			}
			TagAttribute[] modifiedAttributeList = new TagAttribute[modified.size()];
			for (int i = 0; i < modified.size(); i++) {
				modifiedAttributeList[i] = modified.get(i);
			}
			return new AFTagAttributes((TagAttribute[]) modifiedAttributeList);
		}
		return tag.getAttributes();
	}
}
