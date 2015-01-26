/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.quickstarts.servlet.util;

public class ServletUtil {
	
	public static String createHelloMessage(String name) {
		return "Hello " + name + "!";
	}

	public static String pageHeader(String title) {
		return "<html><head><title>" + title +"</title><body>";
	}
	   
	public static String pageFooter() {
		return "</body></html>";
	}
		
	public static String contentType() {
		return "text/html";
	}
	
	public static String servletReturnStr() {
		return "&#160;&#160;&#160;<a href=\"/helloworld-servlet/servlets/index.html\">Back</a>" ;
	}

}
