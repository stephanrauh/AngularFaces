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
package org.primefaces.jsfplugin.mojo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import org.primefaces.jsfplugin.digester.Attribute;
import org.primefaces.jsfplugin.digester.Component;

/**
 * @goal generate-tags
 */
public class TagMojo extends BaseFacesMojo {

	public void execute() throws MojoExecutionException, MojoFailureException {
	
		getLog().info("Generating Tags");
		try{
			createTags(getComponents());
			getLog().info("Tags Generated");
		}
		catch (Exception e) {
			getLog().error("Error occured in generating tags" + e.getMessage());
		}
	}

	private void createTags(List components) throws Exception {
		String outputPath = getCreateOutputDirectory();

		for (Iterator iterator = components.iterator(); iterator.hasNext();) {
			Component component = (Component) iterator.next();

			String packagePath = createPackageDirectory(outputPath, component);
			String tagClassName = getTagClassName(component.getTagClass());
			
			FileWriter fileWriter = new FileWriter(packagePath + File.separator + tagClassName + ".java");
			BufferedWriter writer = new BufferedWriter(fileWriter);

			writeTagFile(writer, component, tagClassName, outputPath);
		}
	}

	private void writeTagFile(BufferedWriter writer, Component component, String tagClassName, String outputPath) throws IOException {
		writeLicense(writer);
		writePackageImportAndClassDefinition(writer, component, tagClassName);
		writeProperties(writer, component);
		writeReleaseMethod(writer, component);
		writePropertiesMethod(writer, component);
		writeComponentAndRenderers(writer, component);
		writePropertySetters(writer, component.getAttributes());

		writer.write("}");
		writer.close();
	}

	private void writePropertiesMethod(BufferedWriter writer, Component component) throws IOException {
		writer.write("\tprotected void setProperties(UIComponent comp){\n");
		writer.write("\t\tsuper.setProperties(comp);\n\n");
		
		writer.write("\t\t" + component.getComponentClass() + " component = null;\n");
		writer.write("\t\ttry {\n");
		writer.write("\t\t\tcomponent = (" + component.getComponentClass() + ") comp;\n");
		writer.write("\t\t} catch(ClassCastException cce) {\n");
		writer.write("\t\t\tthrow new IllegalStateException(\"Component \" + component.toString() + \" not expected type.\");\n");
		writer.write("\t\t}\n\n");

		for (Iterator iterator = component.getAttributes().iterator(); iterator.hasNext();) {
			Attribute attribute = (Attribute) iterator.next();
			if(isIgnored(attribute, uicomponentAttributes))
				continue;
			
			writePropertySetterMethod(writer, attribute);
		}
		
		writer.write("\t}\n");
		writer.write("\n");
	}
	
	private void writePropertySetterMethod(BufferedWriter writer, Attribute attribute) throws IOException {
		if(attribute.getType().equals("javax.faces.convert.Converter")) {
			writer.write("\t\tif(_ " + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tif(!_" + attribute.getName() + ".isLiteralText()) {\n");
			writer.write("\t\t\tcomponent.setValueExpression(\"converter\", _" + attribute.getName() + ");\n");
			writer.write("\t\t} else {\n");
			writer.write("\t\t\tjavax.faces.convert.Converter convert = FacesContext.getCurrentInstance().getApplication().createConverter(_" + attribute.getName() + ".getExpressionString());");
			writer.write("\t\t\tcomponent.setConverter(convert);\n");
			writer.write("\t\t}");
		} 
		else if(attribute.getType().equals("javax.faces.validator.Validator")) {
			writer.write("\t\tif(_" + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tcomponent.addValidator(new javax.faces.validator.MethodExpressionValidator(_" + attribute.getName() + "));\n");
			writer.write("\t\t}\n");
		}
		else if(attribute.getType().equals("javax.faces.event.ValueChangeListener")) {
			writer.write("\t\tif(_" + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tcomponent.addValueChangeListener(new javax.faces.event.MethodExpressionValueChangeListener(_" + attribute.getName() + "));\n");
			writer.write("\t\t}\n");
		}
		else if(attribute.getType().equals("javax.el.MethodExpression") && attribute.getName().equals("action")) {
			writer.write("\t\tif(_" + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tcomponent.setActionExpression(_" + attribute.getName() + ");\n");
			writer.write("\t\t}\n");
		}
		else if(attribute.getType().equals("javax.faces.event.ActionListener")) {
			writer.write("\t\tif(_" + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tcomponent.addActionListener(new javax.faces.event.MethodExpressionActionListener(_" + attribute.getName() + "));\n");
			writer.write("\t\t}\n");
		}
		else if(attribute.getType().equals("javax.el.MethodExpression")) {
			writer.write("\t\tif(_" + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tcomponent.set" + attribute.getName().substring(0,1).toUpperCase() + attribute.getName().substring(1)  + "(_" + attribute.getName() + ");\n");
			writer.write("\t\t}\n");
		}
		else if(attribute.isLiteral()) {
			writer.write("\t\tif(_" + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tcomponent.set" + attribute.getName().substring(0,1).toUpperCase() + attribute.getName().substring(1)  + "(_" + attribute.getName() + ");\n");
			writer.write("\t\t}\n");
		}
		else {
			writer.write("\t\tif(_" + attribute.getName() + " != null) {\n");
			writer.write("\t\t\tcomponent.setValueExpression(\"" + attribute.getName() + "\", _" + attribute.getName() + ");\n");
			writer.write("\t\t}\n");
		}
	}

	private void writeReleaseMethod(BufferedWriter writer, Component component) throws IOException {
		writer.write("\tpublic void release(){\n");
		writer.write("\t\tsuper.release();\n");
		
		for (Iterator iterator = component.getAttributes().iterator(); iterator.hasNext();) {
			Attribute attribute = (Attribute) iterator.next();
			if(isIgnored(attribute, uicomponentAttributes))
				continue;
			
			writer.write("\t\tthis._" + attribute.getName() +" = null;\n");
		}
		
		writer.write("\t}\n\n");
	}

	private void writePropertySetters(BufferedWriter writer, Vector attributes) throws IOException {
		for (Iterator iterator = attributes.iterator(); iterator.hasNext();) {
			Attribute attribute = (Attribute) iterator.next();
			if(isIgnored(attribute, uicomponentAttributes))
				continue;
			
			if(attribute.isLiteral()) {
				writer.write("\tpublic void set"+ attribute.getName().substring(0,1).toUpperCase() + attribute.getName().substring(1) + "(java.lang.String value){\n");
				writer.write("\t\tthis._"+attribute.getName() + " = value;\n");
				writer.write("\t}\n\n");
			}
			else {
				if(isMethodExpression(attribute)) {
					writer.write("\tpublic void set"+ attribute.getName().substring(0,1).toUpperCase() + attribute.getName().substring(1) + "(javax.el.MethodExpression expression){\n");
					writer.write("\t\tthis._"+attribute.getName() + " = expression;\n");
					writer.write("\t}\n\n");
				} else {
					writer.write("\tpublic void set"+ attribute.getName().substring(0,1).toUpperCase() + attribute.getName().substring(1) + "(javax.el.ValueExpression expression){\n");
					writer.write("\t\tthis._"+attribute.getName() + " = expression;\n");
					writer.write("\t}\n\n");
				}
			}	
		}
	}

	private void writeProperties(BufferedWriter writer, Component component) throws IOException {
		for (Iterator iterator = component.getAttributes().iterator(); iterator.hasNext();) {
			Attribute attribute = (Attribute) iterator.next();
			if(isIgnored(attribute, uicomponentAttributes))
				continue;
			
			if(attribute.isLiteral())
				writer.write("\tprivate java.lang.String _" + attribute.getName() +";\n");
			else {
				if(isMethodExpression(attribute))
					writer.write("\tprivate javax.el.MethodExpression _" + attribute.getName() +";\n");
				else
					writer.write("\tprivate javax.el.ValueExpression _" + attribute.getName() +";\n");
			}
		}
		writer.write("\n");
	}

	private void writeComponentAndRenderers(BufferedWriter writer, Component component) throws IOException {
		writer.write("\tpublic String getComponentType() {\n");
		writer.write("\t\treturn " + component.getComponentShortName() + ".COMPONENT_TYPE;\n");
		writer.write("\t}");
		writer.write("\n\n");
		
		writer.write("\tpublic String getRendererType() {\n");
		if(component.getRendererType() != null)
			writer.write("\t\treturn \""+ component.getRendererType() +"\";\n");
		else
			writer.write("\t\treturn null;\n");
		
		writer.write("\t}");
		writer.write("\n\n");
	}

	private void writePackageImportAndClassDefinition(BufferedWriter writer, Component component, String tagClassName) throws IOException {
		writer.write("package " + component.getPackage() + ";\n");
		writer.write("\n");

		writer.write("import javax.faces.webapp.UIComponentELTag;\n");
		writer.write("import javax.faces.component.UIComponent;\n");
		writer.write("\n");
		
		writer.write("public class " + tagClassName + " extends UIComponentELTag {\n");
		writer.write("\n");
	}

	private String getTagClassName(String dotSeparatedPackageAndTagClass) {
		int lastIndex = dotSeparatedPackageAndTagClass.lastIndexOf(".");
		
		return dotSeparatedPackageAndTagClass.substring(lastIndex+1);
	}
	
	private boolean isIgnored(Attribute attribute, String[] attributes) {
		return ArrayUtils.contains(attributes, attribute.getName());
	}
}