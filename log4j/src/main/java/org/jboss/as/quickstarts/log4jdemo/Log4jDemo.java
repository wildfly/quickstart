/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and/or its affiliates, and individual
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
package org.jboss.as.quickstarts.log4jdemo;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.apache.log4j.Logger;

/**
 * <p>Simplistic class to initialize logger and push value passed by user.</p>
 * <p>The {@link #text} variable is populated with content which is logged in {@link #log()} method.</p>
 * @author baranowb
 * 
 */
@SessionScoped
@Named
public class Log4jDemo implements Serializable {

	private static final Logger log4jLogger = Logger.getLogger(Log4jDemo.class);
	
	private String text;
    
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public void log()
	{
		log4jLogger.info(this.getText());
		this.setText(null);
	}
   
}
