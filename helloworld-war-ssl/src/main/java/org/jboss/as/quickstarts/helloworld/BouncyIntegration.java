package org.jboss.as.quickstarts.helloworld;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

/**
 * Bouncy Castle Integration Provider
 * 
 * @author Giriraj Sharma
 */
public class BouncyIntegration {
    static {
        if (Security.getProvider("BC") == null) Security.addProvider(new BouncyCastleProvider());
    }

    public static void init() {
        // empty, the static class does it
    }
}
