package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.kafka.common.serialization.Serializer;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class TimedEntrySerializer implements Serializer<TimedEntry> {
    @Override
    public byte[] serialize(String topic, TimedEntry data) {
        if (data == null) {
            return null;
        }

        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeLong(data.getTime().getTime());
            out.writeUTF(data.getMessage());
            out.close();
            return bout.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
