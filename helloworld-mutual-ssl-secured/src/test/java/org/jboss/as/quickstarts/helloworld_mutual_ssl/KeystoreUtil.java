/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2020 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.jboss.as.quickstarts.helloworld_mutual_ssl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;

/**
 * An interface to obtain the KeyPair from a keystore file.
 *
 * @author <a href="mailto:prpaul@redhat.com">Prarthona Paul</a>
 */

public class KeystoreUtil {

    public static KeyStore createTrustStore(String serverDir, String keyStoreFile, String storePassword, String keyAlias, String keyStoreType) throws KeyStoreException {
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