package org.jboss.aerogear;

import android.app.Activity;
import android.os.Bundle;
import org.apache.cordova.*;
import android.webkit.WebSettings;

public class KitchensinkCordova extends DroidGap
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
    
    @Override
    public void init() {
    	super.init();
    	
    	WebSettings settings = this.appView.getSettings();
    	settings.setUserAgentString("Kitchensink Cordova Webview Android");
    }
}

