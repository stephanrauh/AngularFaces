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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.primefaces.jsfplugin.digester.Attribute;

import org.primefaces.jsfplugin.digester.Component;

/**
 * @goal generate-facelets-taglib
 */
public class FaceletsMojo extends BaseFacesMojo{

	/**
	 * @parameter
	 */
	protected String standardFaceletsTaglib;
	
	/**
	 * @parameter
	 * @required
	 */
	protected String uri;
	
	/**
	 * @parameter
	 * @required
	 */
	protected String shortName;
	
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Generating facelets-taglib");
		
		try {
			writeFaceletsTaglib(getComponents());
			getLog().info("facelets-taglib generated successfully");
		} catch (Exception e) {
			getLog().info("Exception in generating facelets-taglib:");
			getLog().info(e.toString());
		}
	}

	private void writeFaceletsTaglib(List components) throws IOException{
		FileWriter fileWriter;
		BufferedWriter writer;
		String outputPath = project.getBuild().getOutputDirectory() + File.separator + "META-INF";
		String outputFile =  "primefaces-" + shortName + ".taglib.xml";
		
		File outputDirectory = new File(outputPath);
		if(!outputDirectory.exists())
			outputDirectory.mkdirs();
		
		fileWriter = new FileWriter(outputPath + File.separator + outputFile);	
		writer = new BufferedWriter(fileWriter);
		
		writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		
		writeXSD(writer);
		
		writer.write("\t<namespace>" + uri + "</namespace>\n\n");
		
		if(standardFaceletsTaglib != null) {
			writeStandardFaceletsTaglib(writer);
		}
		
		for(Iterator<Component> iterator = components.iterator(); iterator.hasNext();) {
			Component component = iterator.next();
			writer.write("\t<tag>\n");
			writer.write("\t\t<tag-name>");
			writer.write(component.getTag());
			writer.write("</tag-name>\n");
            writer.write("\t\t<description><![CDATA[");
            if(component.getDescription() != null) {
            	writer.write(component.getDescription());
            }
			writer.write("]]></description>\n");
			writer.write("\t\t<component>\n");
			writer.write("\t\t\t<component-type>");
			writer.write(component.getComponentType());
			writer.write("</component-type>\n");
			
			if(component.getRendererType() != null) {
				writer.write("\t\t\t<renderer-type>");
				writer.write(component.getRendererType());
				writer.write("</renderer-type>\n");
			}
			
			if(component.getComponentHandlerClass() != null) {
				writer.write("\t\t\t<handler-class>");
				writer.write(component.getComponentHandlerClass());
				writer.write("</handler-class>\n");
			}

			writer.write("\t\t</component>\n");

            Attribute attribute = null;
            for(Iterator<Attribute> attr = component.getAttributes().iterator(); attr.hasNext();) {
                attribute = attr.next();

                writer.write("\t\t<attribute>\n");

                writer.write("\t\t\t<description><![CDATA[");
                if(attribute.getDescription() != null) {
                    writer.write(attribute.getDescription());
                }
                writer.write("]]></description>\n");

                writer.write("\t\t\t<name>");
                writer.write(attribute.getName());
                writer.write("</name>\n");

                writer.write("\t\t\t<required>");
                writer.write(String.valueOf(attribute.isRequired()));
                writer.write("</required>\n");

                writer.write("\t\t\t<type>");
                writer.write(attribute.getType());
                writer.write("</type>\n");

                writer.write("\t\t</attribute>\n");
            }
            

			writer.write("\t</tag>\n");
		}
		
		writer.write("</facelet-taglib>\n");
		
		writer.close();
		fileWriter.close();
	}

	private void writeXSD(BufferedWriter writer) throws IOException {
		writer.write("<facelet-taglib xmlns=\"http://java.sun.com/xml/ns/javaee\"\n");
		writer.write("\t\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
		writer.write("\t\txsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-facelettaglibrary_2_0.xsd\"\n");
		writer.write("\t\tversion=\"2.0\">\n");
	}
	
	private void writeStandardFaceletsTaglib(BufferedWriter writer) throws IOException{
		try {
			File template = new File(project.getBasedir() + File.separator + standardFaceletsTaglib);
			FileReader fileReader = new FileReader(template);
			BufferedReader reader = new BufferedReader(fileReader);
			String line = null;
			
			while((line = reader.readLine()) != null) {
				writer.write(line);
				writer.write("\n");
			}
		}catch(FileNotFoundException fileNotFoundException) {
			return;
		}
	}
}