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
