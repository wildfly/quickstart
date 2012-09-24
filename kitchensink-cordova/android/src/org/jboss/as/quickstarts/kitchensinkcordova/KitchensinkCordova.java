package org.jboss.as.quickstarts.kitchensinkcordova;

import org.apache.cordova.DroidGap;

import android.os.Bundle;
import android.webkit.WebSettings;

public class KitchensinkCordova extends DroidGap {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Loads the URL with HTML application
        super.loadUrl("file:///android_asset/www/index.html");
    }

    @Override
    public void init() {
        super.init();

        WebSettings settings = this.appView.getSettings();
        settings.setUserAgentString("Kitchensink Cordova Webview Android");
    }
}
