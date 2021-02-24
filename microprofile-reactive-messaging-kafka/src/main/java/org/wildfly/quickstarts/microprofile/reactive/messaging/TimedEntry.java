package org.wildfly.quickstarts.microprofile.reactive.messaging;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
@Entity
public class TimedEntry {
    private Long id;
    private Timestamp time;
    private String message;

    public TimedEntry() {

    }

    public TimedEntry(Timestamp time, String message) {
        this.time = time;
        this.message = message;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        String s = "TimedEntry{";
        if (id != null) {
            s += "id=" + id + ", ";
        }
        s += "time=" + time +
                ", message='" + message + '\'' +
                '}';
        return s;
    }
}
