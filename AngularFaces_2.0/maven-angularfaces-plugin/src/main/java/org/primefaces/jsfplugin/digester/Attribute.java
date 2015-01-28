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

import org.apache.commons.lang.StringUtils;

public class Attribute {

	private String name;
	private boolean required = false;
	private String type;
	private String defaultValue;
	private boolean ignoreInComponent = false;
	private String methodSignature;
	private boolean literal;
    private String description;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getDefaultValue() {
		if(StringUtils.isBlank(defaultValue))
			return "null";
		
		if(type.equals("java.lang.String"))
			return "\"" + defaultValue + "\"";
			
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	public boolean isIgnoreInComponent() {
		return ignoreInComponent;
	}
	public void setIgnoreInComponent(boolean ignoreInComponent) {
		this.ignoreInComponent = ignoreInComponent;
	}
	
	public String getMethodSignature() {
		return methodSignature;
	}
	public void setMethodSignature(String methodSignature) {
		this.methodSignature = methodSignature;
	}
	
	public boolean isLiteral() {
		return literal;
	}
	public void setLiteral(boolean literal) {
		this.literal = literal;
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
 
	/**
	 * Gives the short name of the attribute
	 * e.g. java.lang.String will return String
	 */
	public String getShortTypeName() {
		String[] list = type.split("\\.");
		return list[list.length-1];
	}
	
	public String getCapitalizedName() {
		return StringUtils.capitalize(name);
	}
	
	public String getCapitalizedType() {
		return StringUtils.capitalize(getShortTypeName());
	}
	
	public boolean isIgnored() {
		return ignoreInComponent;
	}
	
	public boolean isDeferredValue() {
		return getMethodSignature() == null && !isLiteral();
	}
}