/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.as.quickstart.cdi.extension;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jboss.as.quickstart.cdi.extension.model.Creature;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * A simple CDI Portable Extension to "inject" values from XML into the instances of a bean.
 */
public class CreatureExtension implements Extension {
    private final Document document;
    private final Logger log = Logger.getLogger(CreatureExtension.class.getName());

    public CreatureExtension() {
        try {
            InputStream creatureDefs = CreatureExtension.class.getClassLoader().getResourceAsStream("creatures.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(creatureDefs);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Error building xml parser, aborting", e);
        } catch (SAXException e) {
            throw new RuntimeException("SAX exception while parsing xml file", e);
        } catch (IOException e) {
            throw new RuntimeException("Error reading or parsing xml file", e);
        }
    }

    /**
     * Observer to a CDI lifecycle event to correctly setup the XML backed "injection".
     * @param pit CDI lifecycle callback payload
     * @param <X> Type of the Injection to observe
     */
    <X extends Creature> void processInjectionTarget(@Observes ProcessInjectionTarget<X> pit) {
        Class<? extends Creature> klass = pit.getAnnotatedType().getJavaClass();
        log.info("Setting up injection target for " + klass);
        final Element entry = (Element) document.getElementsByTagName(klass.getSimpleName().toLowerCase()).item(0);
        pit.setInjectionTarget(new XmlBackedWrappedInjectionTarget<X>(pit.getInjectionTarget(), entry));
    }
}
