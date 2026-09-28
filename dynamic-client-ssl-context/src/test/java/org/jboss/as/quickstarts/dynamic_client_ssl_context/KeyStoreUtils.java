/*
 * Copyright The WildFly Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.jboss.as.quickstarts.dynamic_client_ssl_context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

/**
 * An interface to obtain the certificate from a keystore file and create truststore.
 *
 * @author <a href="mailto:prpaul@redhat.com">Prarthona Paul</a>
 */

public class KeyStoreUtils {

    public static KeyStore loadKeyPairFromKeyStore(String serverDir, String keyStoreFile, String storePassword, String keyAlias, String keyStoreType) throws KeyStoreException {
        FileInputStream stream = findFile(serverDir + "/" + keyStoreFile);
        try {
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(stream, storePassword.toCharArray());
            Certificate cert = keyStore.getCertificate(keyAlias);
            KeyStore trustStore = keyStore.getInstance(keyStoreType);
            trustStore.load(null, null);
            trustStore.setCertificateEntry("server", cert);
            trustStore.store(new FileOutputStream(serverDir + "/" + "client.truststore"), storePassword.toCharArray());
            return trustStore;
        } catch (Exception e) {
            throw new KeyStoreException(e.getMessage());
        }
    }

    public static FileInputStream findFile(String keystoreFile) {
        try {
            return new FileInputStream(keystoreFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
