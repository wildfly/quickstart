package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Timestamp;

import org.apache.kafka.common.serialization.Deserializer;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class TimedEntryDeserializer implements Deserializer<TimedEntry> {

    @Override
    public TimedEntry deserialize(String topic, byte[] data) {
        if (data == null) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data))){
            Timestamp time = new Timestamp(in.readLong());
            String message = in.readUTF();
            return new TimedEntry(time, message);
        } catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
