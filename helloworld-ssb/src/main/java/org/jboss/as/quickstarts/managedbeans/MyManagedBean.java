package org.jboss.as.quickstarts.managedbeans;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.as.quickstarts.ssb.MySingletonBean;

/**
 * 
 * @author Serge Pagop (spagop@redhat.com)
 *
 */
@Named
public class MyManagedBean implements Serializable {

	private static final long serialVersionUID = -3516561457452229769L;
	
	@Inject MySingletonBean mySingletonBean;
	
	public String getValueA(){
		return "" + mySingletonBean.incrementA();
	}
	
	public String getValueB(){
		return "" + mySingletonBean.incrementB();
	}
}
