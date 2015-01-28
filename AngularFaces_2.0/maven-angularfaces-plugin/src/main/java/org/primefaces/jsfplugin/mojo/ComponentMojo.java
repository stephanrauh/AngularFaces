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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.primefaces.jsfplugin.digester.Attribute;
import org.primefaces.jsfplugin.digester.Component;
import org.primefaces.jsfplugin.digester.Interface;
import org.primefaces.jsfplugin.digester.Resource;
import org.primefaces.jsfplugin.util.FacesMojoUtils;

/**
 * @goal generate-components
 */
public class ComponentMojo extends BaseFacesMojo {
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating Components");
		
		try {
			writeComponents(getComponents());
			getLog().info("Components Generated successfully");
		} catch (Exception e) {
			getLog().info("Error occured in component generation:");
			getLog().info(e.toString());
		}
	}

	private void writeComponents(List<Component> components) throws Exception{
		
		String outputPath = getCreateOutputDirectory();
		
		for (Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
			Component component = (Component) iterator.next();
			getLog().info("Generating Component Source for:" + component.getComponentClass());
			
			String packagePath = createPackageDirectory(outputPath, component);
			
			FileWriter fileWriter = new FileWriter(packagePath + File.separator +  component.getComponentShortName() + ".java");	
			BufferedWriter writer = new BufferedWriter(fileWriter);
		
			writeComponent(writer, component);
			
			writer.close();
			fileWriter.close();
		}
	}

	private void writeComponent(BufferedWriter writer, Component component) throws IOException {
		writeLicense(writer);
		writePackage(writer, component);
		writeImports(writer, component);
		writeClassDeclaration(writer, component);
		writeComponentProperties(writer, component);
		writeAttributesDeclarations(writer, component);
		writeConstructor(writer, component);
		writeComponentFamily(writer, component);
		writeAttributes(writer, component);
		writeTemplate(writer, component);

        if(component.isWidget()) {
            writeWidgetVarResolver(writer);
        }

		writer.write("}");
	}

	private void writeImports(BufferedWriter writer, Component component) throws IOException {
		writer.write("import " + component.getParent() + ";\n");
		writer.write("import javax.faces.context.FacesContext;\n");
        writer.write("import javax.faces.component.UINamingContainer;\n");
		writer.write("import javax.faces.render.Renderer;\n");
		writer.write("import java.io.IOException;\n");
        writer.write("import javax.faces.component.UIComponent;\n");
        writer.write("import javax.faces.event.AbortProcessingException;\n");
        writer.write("import javax.faces.application.ResourceDependencies;\n");
        writer.write("import javax.faces.application.ResourceDependency;\n");
        writer.write("import javax.faces.component.FacesComponent;\n");
        writer.write("import java.util.List;\n");
        writer.write("import java.util.ArrayList;\n");
		
		String templateImports = getTemplateImports(component);
		
		if (StringUtils.isNotEmpty(templateImports)) {
			writer.write(templateImports);
		}
		writer.write("\n");
	}
	
	private void writeComponentProperties(BufferedWriter writer, Component component) throws IOException {
//		writer.write("\tpublic static final String COMPONENT_TYPE = \"" + component.getComponentType() + "\";\n");
		writer.write("\tpublic static final String COMPONENT_FAMILY = \"" + component.getComponentFamily() + "\";\n");
		
		if(component.getRendererType() != null) {
			writer.write("\tprivate static final String DEFAULT_RENDERER = \"" + component.getRendererType() + "\";\n");
        }
				
		writer.write("\n");
	}
	
	private void writeAttributesDeclarations(BufferedWriter writer, Component component) throws IOException {
		boolean firstWritten = false;
		writer.write("\tprotected enum PropertyKeys {\n");
		
		for(Iterator<Attribute> attributeIterator = component.getAttributes().iterator(); attributeIterator.hasNext();) {
			Attribute attribute = attributeIterator.next();
			if(attribute.isIgnored())
				continue;
			
			writer.write("\n");
				
            if(!firstWritten) {
                writer.write("\t\t");
                firstWritten = true;
            }
            else
                writer.write("\t\t,");

            if(attribute.getName().equals("for"))
                writer.write("forValue(\"for\")");
            else
                writer.write(attribute.getName());
		}
		
		writer.write(";\n");
			
        writer.write("\n\t\tString toString;\n\n");

        writer.write("\t\tPropertyKeys(String toString) {\n");
        writer.write("\t\t\tthis.toString = toString;\n");
        writer.write("\t\t}\n\n");

        writer.write("\t\tPropertyKeys() {}\n\n");

        writer.write("\t\tpublic String toString() {\n");
        writer.write("\t\t\treturn ((this.toString != null) ? this.toString : super.toString());");
        writer.write("\n}\n");

        writer.write("\t}\n\n");
	}
	
	private void writeClassDeclaration(BufferedWriter writer, Component component) throws IOException {
		writer.write("@ResourceDependencies({\n");  
        for (Iterator iterator = component.getResources().iterator(); iterator.hasNext();) {
            Resource resource = (Resource) iterator.next();

            writer.write("\t@ResourceDependency(library=\"angularfaces-widgets\", name=\"" + resource.getName() + "\")");

            if(iterator.hasNext())
                writer.write(",\n");
        }
        writer.write("\n})");
        writer.write("\n@FacesComponent(\"" + component.getComponentType() + "\")");
		writer.write("\npublic class " + component.getComponentShortName() + " extends " + component.getParentShortName());
		
		if(!component.getInterfaces().isEmpty()) {
			writer.write(" implements ");
			
			for(Iterator<Interface> iterator = component.getInterfaces().iterator(); iterator.hasNext();) {
				Interface _interface = iterator.next();
				writer.write(_interface.getName());
				
				if(iterator.hasNext())
					writer.write(",");
			}
		}
		
		writer.write(" {\n");
		writer.write("\n\n");
	}
	
	private void writeConstructor(BufferedWriter writer, Component component) throws IOException {
		writer.write("\tpublic " + component.getComponentShortName() + "() {\n");
		
		if(component.getRendererType() != null)
			writer.write("\t\tsetRendererType(DEFAULT_RENDERER);\n");
		else
			writer.write("\t\tsetRendererType(null);\n");
				
		writer.write("\t}");
		writer.write("\n\n");
	}
	
	private void writeComponentFamily(BufferedWriter writer, Component component) throws IOException {
		writer.write("\tpublic String getFamily() {\n");
		writer.write("\t\treturn COMPONENT_FAMILY;\n");
		writer.write("\t}");
		writer.write("\n\n");
	}
	
	private void writeAttributes(BufferedWriter writer, Component component) throws IOException {
		for (Iterator attributeIterator = component.getAttributes().iterator(); attributeIterator.hasNext();) {
			Attribute attribute = (Attribute) attributeIterator.next();
			if(attribute.isIgnored())
				continue;
			
			String propertyKeyName = attribute.getName().equalsIgnoreCase("for") ? "forValue" : attribute.getName();
            //getter
            if(FacesMojoUtils.shouldPrimitize(attribute.getType()))
                writer.write("\tpublic " + FacesMojoUtils.toPrimitive(attribute.getType()) + " " + resolveGetterPrefix(attribute) + attribute.getCapitalizedName() + "() {\n");
            else
                writer.write("\tpublic " + attribute.getType() + " " + resolveGetterPrefix(attribute) + attribute.getCapitalizedName() + "() {\n");

            if(attribute.getDefaultValue() == null)
                writer.write("\t\treturn (" + attribute.getType() + ") getStateHelper().eval(PropertyKeys." + propertyKeyName + ");\n");
            else
                writer.write("\t\treturn (" + attribute.getType() + ") getStateHelper().eval(PropertyKeys." + propertyKeyName + ", " + attribute.getDefaultValue() + ");\n");

            writer.write("\t}\n");

            //setter
            if(FacesMojoUtils.shouldPrimitize(attribute.getType()))
                writer.write("\tpublic void set" + attribute.getCapitalizedName() + "(" + FacesMojoUtils.toPrimitive(attribute.getType()) + " _" + attribute.getName() + ") {\n");
            else
                writer.write("\tpublic void set" + attribute.getCapitalizedName() + "(" + attribute.getType() + " _" + attribute.getName() + ") {\n");

            writer.write("\t\tgetStateHelper().put(PropertyKeys." + propertyKeyName + ", _" + attribute.getName() + ");\n");

            writer.write("\t}\n\n");
		}
	}

	private void writePackage(BufferedWriter writer, Component component) throws IOException {
		writer.write("package " + component.getPackage() + ";\n\n");
	}
	
	private void writeTemplate(BufferedWriter writer, Component component) throws IOException{
		try {
			File template = getTemplate(component);
			FileReader fileReader = new FileReader(template);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			
			getLog().info("Writing template for " + component.getComponentShortName());
			while((line = reader.readLine()) != null) {
				if (line.startsWith("import ")) continue;
				writer.write(line);
				writer.write("\n");
				
			}
		}catch(FileNotFoundException fileNotFoundException) {
			return;
		}
	}
	
	private String getTemplateImports(Component component) throws IOException {
		try {
			StringBuffer buf = new StringBuffer();
			File template = getTemplate(component);
			FileReader fileReader = new FileReader(template);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			
			getLog().info("Looking for template imports of " + component.getComponentShortName());
			while(((line = reader.readLine()) != null) && (line.startsWith("import "))) {
				buf.append(line).append("\n");
			}
			
			return buf.toString();
		}catch(FileNotFoundException fileNotFoundException) {
		}
		return null;
	}
	
	protected boolean isBoolean(Attribute attribute) {
		return attribute.getType().equals("java.lang.Boolean");
	}
	
	protected boolean isMethodBinding(Attribute attribute) {
		return attribute.getType().equals("javax.faces.el.MethodBinding");
	}
	
	protected String resolveGetterPrefix(Attribute attribute) {
		if(isBoolean(attribute))
			return "is";
		else
			return "get";
	}
	
	protected File getTemplate(Component component) {
		String templatePath = project.getBasedir() + File.separator + templatesDir;
		String[] packagePath = component.getPackage().split("\\.");
		String templateFileName = component.getComponentShortName() + "Template.java";
		
		for (int i = 0; i < packagePath.length; i++) {
			templatePath = templatePath + File.separator + packagePath[i];
		}
		
		return new File(templatePath + File.separator + templateFileName);
	}

    protected void writeWidgetVarResolver(BufferedWriter writer) throws IOException {
        writer.write("\tpublic String resolveWidgetVar() {\n");
        writer.write("\t\treturn ComponentUtils.resolveWidgetVar(getFacesContext(), this);\n");
        writer.write("\t}\n");
    }
}