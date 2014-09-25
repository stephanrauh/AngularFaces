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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.view.facelets.Tag;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagAttributes;
import javax.faces.view.facelets.TagDecorator;

/**
 * This is one of the most important classes of AngularFaces. It converts attributes to pass-through parameters, adds them to the list of
 * JSF bean to be synchronized with the client and implements a couple of pseudo JSF tags.
 */
public class AngularTagDecorator implements TagDecorator {
	private static boolean active = false;

	private final static Pattern angularExpressionPattern = Pattern.compile("\\{\\{(\\w+\\.)+(\\w+)\\}\\}");
	private static final String HTML_NAMESPACE = "http://www.w3.org/1999/xhtml";
	private static final Logger LOGGER = Logger.getLogger("de.beyondjava.angularFaces.core.tagTransformer.AngularTagDecorator");
	private static final String MOJARRA_NAMESPACE = "http://xmlns.jcp.org/jsf/html";
	private static final String PASS_THROUGH_NAMESPACE = "http://xmlns.jcp.org/jsf/passthrough";
	private static final String ANGULAR_FACES_CORE_NAMESPACE = "http://beyondjava.net/angularFacesCore";
	private static final String PRIMEFACES_NAMESPACE="http://primefaces.org/ui";

	public static boolean isActive() {
		return active;
	}

	private final RelaxedTagDecorator relaxedDecorator = new RelaxedTagDecorator();

	private Tag convertElementToInputText(Tag tag, TagAttributes modifiedAttributes) {
		TagAttribute[] attributes = modifiedAttributes.getAll();
		TagAttribute[] lessAttributes = Arrays.copyOf(attributes, attributes.length - 1);
		TagAttributes less = new AFTagAttributes(lessAttributes);
		Tag t = new Tag(tag.getLocation(), MOJARRA_NAMESPACE, "inputText", "h:inputText", less);
		return t;
	}

	private Tag convertToInputText(Tag tag, TagAttributes attributeList) {
		TagAttribute[] attributes = attributeList.getAll();
		TagAttributes more = new AFTagAttributes(attributes);
		Tag t = new Tag(tag.getLocation(), MOJARRA_NAMESPACE, "inputText", "inputText", more);
		return t;
	}

	private Tag convertToACBodyTag(Tag tag, TagAttributes attributeList) {
		Tag t = new Tag(tag.getLocation(), ANGULAR_FACES_CORE_NAMESPACE, tag.getLocalName(), tag.getQName(), attributeList);
		return t;
	}

	private Tag convertToNGSyncTag(Tag tag, TagAttributes attributeList) {

		TagAttribute[] attributes = attributeList.getAll();
		TagAttribute[] newAttributes = new TagAttribute[attributes.length + 1];

		int newLength = 0;
		String direction = "serverToClient";
		String angularExpression = null;
		int indexOfValueAttribute = -1;
		for (int i = 0; i < attributes.length; i++) {
			if ("value".equals(attributes[i].getLocalName())) {
				indexOfValueAttribute = i;
				angularExpression = attributes[i].getValue();
				if (angularExpression.startsWith("#{")) {
					angularExpression = "{{" + angularExpression.substring(2, angularExpression.length() - 1) + "}}";
				}
				TagAttribute af = TagAttributeUtilities.createTagAttribute(tag.getLocation(), "", "angularfacesattributes",
						"angularfacesattributes", angularExpression);
				newAttributes[newLength++] = af;

			} else if ("direction".equals(attributes[i].getLocalName())) {
				direction = attributes[i].getValue();
				newAttributes[newLength++] = attributes[i];
			} else if ("id".equals(attributes[i].getLocalName())) {
				newAttributes[newLength++] = attributes[i];
			} else if ("styleClass".equals(attributes[i].getLocalName())) {
				newAttributes[newLength++] = attributes[i];
			}
		}

		if (null == angularExpression) {
			LOGGER.severe("ngsync: Please provide the value attribute containing the bean that is to be synchronized.");
			TagAttribute value = TagAttributeUtilities.createTagAttribute(tag.getLocation(), "", "value", "value",
					"ngsync: Please provide the value attribute containing the bean that is to be synchronized.");
			newAttributes[newLength++] = value;
			newAttributes = Arrays.copyOf(newAttributes, newLength);
			TagAttributes more = new AFTagAttributes(newAttributes);
			Tag t = new Tag(tag.getLocation(), MOJARRA_NAMESPACE, "outputText", "outputText", more);
			return t;
		} else if ("serverToClient".equalsIgnoreCase(direction)) {
			TagAttribute hide = TagAttributeUtilities.createTagAttribute(tag.getLocation(), "", "rendered", "rendered", "true");
			newAttributes[newLength++] = hide;
			newAttributes = Arrays.copyOf(newAttributes, newLength);
			TagAttributes more = new AFTagAttributes(newAttributes);
			Tag t = new Tag(tag.getLocation(), "http://beyondjava.net/angularFacesCore", "sync", "sync", more);
			return t;
		} else {
			String coreExpression = angularExpression.substring(2, angularExpression.length() - 2);
			TagAttribute value = TagAttributeUtilities.createTagAttribute(tag.getLocation(), "", "value", "value", coreExpression);
			newAttributes[indexOfValueAttribute] = value;
			newAttributes = Arrays.copyOf(newAttributes, newLength);
			TagAttributes more = new AFTagAttributes(newAttributes);
			Tag t = new Tag(tag.getLocation(), "http://beyondjava.net/angularFacesCore", "sync", "sync", more);
			return t;
		}
	}

	private Tag convertToTranslateTag(Tag tag, TagAttributes modifiedAttributes) {
		TagAttribute[] attributes = modifiedAttributes.getAll();
		TagAttributes more = new AFTagAttributes(attributes);
		Tag t = new Tag(tag.getLocation(), MOJARRA_NAMESPACE, "outputText", "h:outputText", more);
		return t;
	}

	@Override
	public Tag decorate(Tag tag) {
		Tag newTag = createTags(tag);
		return newTag;
	}

	private Tag createTags(Tag tag) {
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

		if ("body".equals(tag.getLocalName())) {
			return convertToACBodyTag(tag, modifiedAttributes);
		}
		if ("messages".equals(tag.getLocalName())) {
			return convertToPuiMessagesTag(tag, modifiedAttributes);
		}
		if (!isHTMLNamespace(tag.getNamespace())) {
			return generateTagIfNecessary(tag, modifiedAttributes);
		}
		Tag newTag = relaxedDecorator.decorate(tag);
		if (newTag != null && newTag != tag) {
			return newTag;
		}

		if ("translate".equals(tag.getLocalName()) || "i18n".equals(tag.getLocalName())) {
			return convertToTranslateTag(tag, modifiedAttributes);
		}

		if ("ngsync".equals(tag.getLocalName())) {
			return convertToNGSyncTag(tag, modifiedAttributes);
		}

		if ("input".equals(tag.getLocalName())) {
			return convertToInputText(tag, modifiedAttributes);
		}
		return generateTagIfNecessary(tag, modifiedAttributes);
	}

	private Tag convertToPuiMessagesTag(Tag tag, TagAttributes attributeList) {
		if (tag.getNamespace().equals(PRIMEFACES_NAMESPACE)) {
			AFTagAttributes modifiedAttributes = new AFTagAttributes(attributeList.getAll());
			modifiedAttributes.addAttribute(tag.getLocation(), PASS_THROUGH_NAMESPACE, "primefaces", "primefaces", "true");
			Tag t = new Tag(tag.getLocation(), HTML_NAMESPACE, "puimessages", "puimessages", modifiedAttributes);
			return t;
		}
		else {
			Tag t = new Tag(tag.getLocation(), HTML_NAMESPACE, "puimessages", "puimessages", attributeList);
			return t;
		}
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
							"ng-model", "ng-model", exp.substring(2, exp.length() - 2));
					modified.add(modifiedAttribute);
					if (!firstMatch) {
						LOGGER.severe("Tag " + tag.getQName() + " can't have multiple ng-models." + tag.getLocation().toString());
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
				TagAttribute af = TagAttributeUtilities.createTagAttribute(tag.getLocation(), "", "angularfacesattributes",
						"angularfacesattributes", angularExpressions.substring(1));
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

	private Tag generateTagIfNecessary(Tag tag, TagAttributes modifiedAttributes) {
		if (modifiedAttributes != tag.getAttributes()) {
			if (tag.getLocalName().equals("div") && modifiedAttributes instanceof AFTagAttributes) {
				return generatePuiHtmlTag(tag, modifiedAttributes, "puiDiv");
			} else if (tag.getLocalName().equals("span") && modifiedAttributes instanceof AFTagAttributes) {
				return generatePuiHtmlTag(tag, modifiedAttributes, "puiSpan");
			} else
				return new Tag(tag.getLocation(), tag.getNamespace(), tag.getLocalName(), tag.getQName(), modifiedAttributes);
		}
		return null;
	}

	private Tag generatePuiHtmlTag(Tag tag, TagAttributes modifiedAttributes, final String htmlTag) {
		String keys = "";
		TagAttribute[] all = modifiedAttributes.getAll();
		for (int i = 0; i < all.length; i++) {
			TagAttribute attr = all[i];
			keys += attr.getLocalName() + ",";
			all[i] = TagAttributeUtilities.createTagAttribute(attr.getLocation(), PASS_THROUGH_NAMESPACE, attr.getLocalName(),
					attr.getQName(), attr.getValue());
		}
		if (keys.endsWith(","))
			keys = keys.substring(0, keys.length() - 1);
		((AFTagAttributes) modifiedAttributes).addAttribute(tag.getLocation(), ANGULAR_FACES_CORE_NAMESPACE, "attributeNames",
				"attributeNames", keys);
		return new Tag(tag.getLocation(), ANGULAR_FACES_CORE_NAMESPACE, htmlTag, htmlTag, modifiedAttributes);
	}

	private boolean isHTMLNamespace(String ns) {
		return "".equals(ns) || HTML_NAMESPACE.equals(ns);
	}

	// May become useful in future
	// private Tag addLabelsTag(Tag tag) {
	// if (null==tag) return null;
	// if ("inputText".equals(tag.getLocalName())) {
	// TagAttribute originalTagAttribute = TagAttributeUtilities.createTagAttribute(tag.getLocation(), tag.getNamespace(),
	// "originalTagName", tag.getQName(), tag.getLocalName());
	// final TagAttribute[] origininalAttributes = tag.getAttributes().getAll();
	// TagAttribute[] attrs = Arrays.copyOf(origininalAttributes, origininalAttributes.length+1);
	// attrs[origininalAttributes.length]=originalTagAttribute;
	// AFTagAttributes afta = new AFTagAttributes(attrs);
	// return new Tag(tag.getLocation(), "http://beyondjava.net/angularFacesCore", "addLabelAndMessage", "addLabelAndMessage", afta);
	// }
	// return tag;
	// }

}
