package org.jboss.as.quickstarts.gwthelloworld.client.local;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * When the script is loaded in the web browser, the generated GWT code creates
 * an instance of this class, then calls its {@link #onModuleLoad()} method. From
 * there, we create the UI and add it to the DOM.
 * 
 * @author Jonathan Fuerth <jfuerth@redhat.com>
 * @author Christian Sadilek <csadilek@redhat.com>
 */
public class HelloWorldApp implements EntryPoint {

  @Override
  public void onModuleLoad() {
    RootPanel.get().add(new HelloWorldClient().getElement());
  }

}
