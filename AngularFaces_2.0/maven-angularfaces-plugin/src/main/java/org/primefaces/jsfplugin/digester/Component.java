/*
 * Copyright 2009 Prime Technology.
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
package org.primefaces.jsfplugin.digester;

import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;

public class Component {

	private String tag;
	private String tagClass;
	private String componentClass;
	private String componentHandlerClass;
	private String parent;
	private String componentType;
	private String componentFamily;
	private String rendererType;
	private String rendererClass;
	private Vector attributes;
	private Vector resources;
	private Vector interfaces;
    private String description;

	public Component() {
		attributes = new Vector();
		resources = new Vector();
		interfaces = new Vector();
	}
	
	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}
	
	public void addResource(Resource resource) {
		resources.add(resource);
	}
	
	public void addInterface(Interface _interface) {
		interfaces.add(_interface);
	}
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
	public String getTagClass() {
		return tagClass;
	}
	public void setTagClass(String tagClass) {
		this.tagClass = tagClass;
	}
	
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
	public Vector getAttributes() {
		return attributes;
	}
	public void setAttributes(Vector attributes) {
		this.attributes = attributes;
	}
	
	public Vector getResources() {
		return resources;
	}
	public void setResources(Vector resources) {
		this.resources = resources;
	}
	
	public String getComponentType() {
		return componentType;
	}
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getComponentFamily() {
		return componentFamily;
	}
	public void setComponentFamily(String componentFamily) {
		this.componentFamily = componentFamily;
	}

	public String getRendererType() {
		return rendererType;
	}
	public void setRendererType(String rendererType) {
		this.rendererType = rendererType;
	}
	
	public String getRendererClass() {
		return rendererClass;
	}
	public void setRendererClass(String rendererClass) {
		this.rendererClass = rendererClass;
	}

	public String getComponentClass() {
		return componentClass;
	}
	public void setComponentClass(String componentClass) {
		this.componentClass = componentClass;
	}
	
	public String getComponentHandlerClass() {
		return componentHandlerClass;
	}
	public void setComponentHandlerClass(String componentHandlerClass) {
		this.componentHandlerClass = componentHandlerClass;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
	public boolean isAjaxComponent() {
		for(Iterator iterator = getInterfaces().iterator(); iterator.hasNext();) {
			Interface _interface = (Interface) iterator.next();
			
			if(_interface.getName().equals("org.primefaces.component.api.AjaxComponent"))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Gives the short name of the component
	 * e.g. org.primefaces.component.Slider will return Slider
	 */
	public String getComponentShortName() {
		String[] list = componentClass.split("\\.");
		return list[list.length-1];
	}
	
	/**
	 * Gives the short name of the parent
	 * e.g. javax.faces.component.UIComponentBase will return UIComponentBase
	 */
	public String getParentShortName() {
		String[] list = parent.split("\\.");
		return list[list.length-1];
	}
	
	/**
	 * Returns the parent package folder
	 * e.g. org.primefaces.component.tabview.Tab will return "tabview"
	 */
	public String getParentPackagePath() {
		String[] list = getTagClass().split("\\.");
		return list[list.length-2];
	}
	
	/**
	 * Returns the parent package folder
	 * e.g. org.primefaces.component.tabview.Tab will return "org.primefaces.component.tabview"
	 */
	public String getPackage() {
		return StringUtils.substringBeforeLast(getTagClass(), ".");
	}
	
	public Vector getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(Vector interfaces) {
		this.interfaces = interfaces;
	}

    public boolean isWidget() {
        for(Iterator<Interface> iterator = getInterfaces().iterator(); iterator.hasNext();) {
				Interface _interface = iterator.next();
                if(_interface.getName().equalsIgnoreCase("org.primefaces.component.api.Widget")) {
                    return true;
            }
        }

        return false;
    }
}