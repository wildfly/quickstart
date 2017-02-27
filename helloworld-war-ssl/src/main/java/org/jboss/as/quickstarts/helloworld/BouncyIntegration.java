package org.jboss.as.quickstarts.helloworld;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * Bouncy Castle Integration Provider
 *
 * @author Giriraj Sharma
 */
public class BouncyIntegration {
    static {
        if (Security.getProvider("BC") == null) { Security.addProvider(new BouncyCastleProvider()); }
    }

    public static void init() {
        // empty, the static class does it
    }
}
