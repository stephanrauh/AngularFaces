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

import junit.framework.TestCase;

public class ComponentTest extends TestCase {

	public void testGetShortName() {
		Component component = new Component();
		component.setComponentClass("org.primefaces.component.slider.Slider");
		assertEquals("Slider", component.getComponentShortName());
	}
	
	public void testGetParentShortName() {
		Component component = new Component();
		component.setParent("javax.faces.component.UIComponentBase");
		assertEquals("UIComponentBase", component.getParentShortName());
	}
	
	public void testGetParentPackagePath() {
		Component component = new Component();
		component.setTagClass("org.primefaces.component.tabview.TabTag");
		assertEquals("tabview", component.getParentPackagePath());
	}
	
	public void testGetPackage() {
		Component component = new Component();
		component.setTagClass("org.primefaces.component.tabview.TabTag");
		assertEquals("org.primefaces.component.tabview", component.getPackage());
	}
}
