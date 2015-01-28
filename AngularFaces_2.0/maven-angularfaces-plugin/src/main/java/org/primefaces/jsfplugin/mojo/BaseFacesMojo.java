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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

import org.primefaces.jsfplugin.digester.Attribute;
import org.primefaces.jsfplugin.digester.Component;
import org.primefaces.jsfplugin.digester.Interface;
import org.primefaces.jsfplugin.digester.Resource;

/**
 * Base class for all the jsf mojos, parses the component config files and generates output directories
 * 
 * @author Latest modification by $Author: cagatay_civici $
 * @version $Revision: 1279 $ $Date: 2008-04-20 13:06:50 +0100 (Sun, 20 Apr 2008) $
 */
public abstract class BaseFacesMojo extends AbstractMojo{

	/**
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 */
	protected MavenProject project;
	
	/**
	 * @parameter
	 * @required
	 */
	protected String componentConfigsDir;
	
	/**
	 * @parameter
	 */
	protected String templatesDir;
	    
    /**
	 * @parameter
	 */
	protected String license;
	
	protected String[] uicomponentAttributes = new String[]{"id","rendered","binding"};
	
	protected String[] specialAttributes = new String[]{"value","converter","validator","valueChangeListener","immediate","required","action","actionListener"};
    
	protected File[] getResources() {
		return new File(project.getBasedir() + File.separator + componentConfigsDir).listFiles();
	}
	
	protected Digester getDigester() {
		Digester digester = new Digester();
		digester.setValidating(false);
		
		digester.addObjectCreate("component", Component.class);
		digester.addBeanPropertySetter("component/tag", "tag");
		digester.addBeanPropertySetter("component/tagClass", "tagClass");
		digester.addBeanPropertySetter("component/componentClass", "componentClass");
		digester.addBeanPropertySetter("component/componentHandlerClass", "componentHandlerClass");
		digester.addBeanPropertySetter("component/componentType", "componentType");
		digester.addBeanPropertySetter("component/componentFamily", "componentFamily");
		digester.addBeanPropertySetter("component/rendererType", "rendererType");
		digester.addBeanPropertySetter("component/rendererClass", "rendererClass");
		digester.addBeanPropertySetter("component/parent", "parent");
        digester.addBeanPropertySetter("component/description", "description");
		
		digester.addObjectCreate("component/attributes/attribute", Attribute.class);
		digester.addBeanPropertySetter("component/attributes/attribute/name","name");
		digester.addBeanPropertySetter("component/attributes/attribute/required","required");
		digester.addBeanPropertySetter("component/attributes/attribute/type","type");
		digester.addBeanPropertySetter("component/attributes/attribute/defaultValue","defaultValue");
		digester.addBeanPropertySetter("component/attributes/attribute/ignoreInComponent","ignoreInComponent");
		digester.addBeanPropertySetter("component/attributes/attribute/method-signature","methodSignature");
		digester.addBeanPropertySetter("component/attributes/attribute/literal","literal");
        digester.addBeanPropertySetter("component/attributes/attribute/description","description");
		digester.addSetNext("component/attributes/attribute", "addAttribute");
		
		digester.addObjectCreate("component/resources/resource", Resource.class);
		digester.addBeanPropertySetter("component/resources/resource/name","name");
		digester.addSetNext("component/resources/resource", "addResource");
		
		digester.addObjectCreate("component/interfaces/interface", Interface.class);
		digester.addBeanPropertySetter("component/interfaces/interface/name","name");
		digester.addSetNext("component/interfaces/interface", "addInterface");
		
		return digester;
	}
	
	protected List getComponents() {
		File[] resources = getResources();
		Digester digester = getDigester();
		List components = new ArrayList();

		for (int i = 0; i < resources.length; i++) {
			try {
				
				File resource = resources[i];
				if(resource.getName().endsWith(".xml")) {
					components.add( digester.parse( resources[i]));
				}
				
			} catch (Exception e) {
				getLog().info(e.getMessage());
				getLog().info("Error in generation");
				return null;
			}
		}
		
		return components;
	}

	protected String getCreateOutputDirectory() {
		
		String outputPath = project.getBuild().getDirectory()
				+ File.separator + "generated-sources"
                                + File.separator + "maven-jsf-plugin" + File.separator;

		File componentsDirectory = new File(outputPath);
		
		if(!componentsDirectory.exists())
			componentsDirectory.mkdirs();

		return outputPath;
	}
	
	protected String createPackageDirectory(String outputPath, Component component) {
		String packagePath = outputPath + File.separator;
		String[] packageFolders = component.getPackage().split("\\.");
		
		for (String folder : packageFolders) {
			packagePath = packagePath + File.separator + folder;
		}
		
		File packageDirectory = new File(packagePath);
		if(!packageDirectory.exists())
			packageDirectory.mkdirs();
		
		return packagePath;
	}
	
	protected String getLicense() {
        boolean elite = (license != null && license.equals("elite"));
        String license = null;
        
        if(elite) {
            license = "/*\n * Generated, Do Not Modify\n */\n" +
                        "/*\n"+
						" * Copyright 2009-2013 PrimeTek.\n" +
						" *\n" + 
						" * Licensed under PrimeFaces Commercial License, Version 1.0 (the \"License\");\n"+
						" * you may not use this file except in compliance with the License.\n" +
						" * You may obtain a copy of the License at\n" +
 						" *\n" +
 						" * http://www.primefaces.org/elite/license.xhtml\n" +
 						" *\n" + 
 						" * Unless required by applicable law or agreed to in writing, software\n" +
 						" * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
 						" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
 						" * See the License for the specific language governing permissions and\n" + 
 						" * limitations under the License.\n" +
 						" */\n";
        }
        else {
            license = "/*\n * Generated, Do Not Modify\n */\n" +
                        "/*\n"+
						" * Copyright 2009-2013 PrimeTek.\n" +
						" *\n" + 
						" * Licensed under the Apache License, Version 2.0 (the \"License\");\n"+
						" * you may not use this file except in compliance with the License.\n" +
						" * You may obtain a copy of the License at\n" +
 						" *\n" +
 						" * http://www.apache.org/licenses/LICENSE-2.0\n" +
 						" *\n" + 
 						" * Unless required by applicable law or agreed to in writing, software\n" +
 						" * distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
 						" * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
 						" * See the License for the specific language governing permissions and\n" + 
 						" * limitations under the License.\n" +
 						" */\n";
        }
        
		return license;
	}
	
	protected void writeLicense(BufferedWriter writer) throws IOException{
		writer.write(getLicense());
	}
	
	protected void writeResourceHolderGetter(BufferedWriter writer) throws IOException{
		writer.write("\n\tprotected ResourceHolder getResourceHolder() {\n");
		writer.write("\t\tFacesContext facesContext = getFacesContext();\n");
		writer.write("\t\tif(facesContext == null)\n");
		writer.write("\t\t\treturn null;\n\n");
		writer.write("\t\tValueExpression ve = facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), \"#{primeFacesResourceHolder}\", ResourceHolder.class);\n");
		writer.write("\n\t\treturn (ResourceHolder) ve.getValue(facesContext.getELContext());");
		writer.write("\n\t}\n");
	}
	
	protected boolean isMethodExpression(Attribute attribute) {
		String type = attribute.getType();
		
		if(type.equals("javax.faces.validator.Validator") || type.equals("javax.faces.event.ValueChangeListener")
				|| type.equals("javax.el.MethodExpression") || type.equals("javax.faces.event.ActionListener"))
			return true;
		else
			return false;		
	}
 }