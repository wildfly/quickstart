/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.mail;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.mail.Address;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.IOException;
import java.io.Serializable;

/**
 * <p>
 * {@link Email} contains all the business logic for the application, and also serves as the controller for the JSF view.
 * </p>
 * <p>
 * It contains address, subject, and content for the <code>email</code> to be sent.
 * </p>
 * <p>
 * The {@link #send()} method provides the business logic to send the email.
 * The {@link #retrievePop3()} method retrieves all the emails sent to a specific email account using POP3.
 * The {@link #retrieveImap()} method retrieves all the emails sent to an account whose credentials are configured in
 * the Mail subsystem using IMAP.
 * </p>
 *
 * @author Joel Tosi
 */

@Named
@SessionScoped
public class Email implements Serializable {

    private static final long serialVersionUID = 1544680932114626710L;

    /**
     * Resource for sending the email. The mail subsystem is defined in either standalone.xml or domain.xml in your respective
     * configuration directory.
     */
    @Resource(mappedName = "java:jboss/mail/MyOtherMail")
    private Session mySession;

    private String to = "user02@mail.local";

    private String from = "user01@mail.local";

    private String subject;

    private String body;

    private String pop3User = "user02@mail.local";

    private String pop3Password = "1234";

    private String pop3Emails;

    private String imapEmails;

    /**
     * Method to send the email based upon values entered in the JSF view. Exception should be handled in a production usage but
     * is not handled in this example.
     */
    public void send() {
        try {
            Message message = new MimeMessage(mySession);
            message.setFrom(new InternetAddress(from));
            Address toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
            message.setSubject(subject);
            message.setContent(body, "text/plain");
            Transport.send(message);

            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Email sent to " + to);
            context.addMessage(null, facesMessage);
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error sending the Email. " + e.getMessage());
            context.addMessage(null, facesMessage);
        }
    }

    public void resetSmtp() {
        from = "user01@mail.local";
        to = "user02@mail.local";
        subject = null;
        body = null;
    }

    public void retrievePop3() throws Exception {
        try {
            pop3Emails = retrieveEmails("pop3", pop3User, pop3Password);
            if (pop3Emails == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage facesMessage = new FacesMessage("No message found.");
                context.addMessage(null, facesMessage);
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error retrieving emails using POP3. " + e.getMessage());
            context.addMessage(null, facesMessage);
        }
    }

    public void resetPop3() {
        pop3User = "user02@mail.local";
        pop3Password = "1234";
        pop3Emails = null;
    }

    public void retrieveImap() throws Exception {
        try {
            imapEmails = retrieveEmails("imap");
            if (imapEmails == null) {
                FacesContext context = FacesContext.getCurrentInstance();
                FacesMessage facesMessage = new FacesMessage("No message found.");
                context.addMessage(null, facesMessage);
            }
        } catch (Exception e) {
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage facesMessage = new FacesMessage("Error retrieving emails using IMAP. " + e.getMessage());
            context.addMessage(null, facesMessage);
        }
    }

    public void resetImap() {
        imapEmails = null;
    }

    private String retrieveEmails(String protocol) throws MessagingException, IOException {
        return retrieveEmails(protocol, null, null);
    }

    private String retrieveEmails(String protocol, String user, String password) throws MessagingException, IOException {
        Store store = mySession.getStore(protocol);
        if (user != null && !user.trim().isEmpty()) {
            store.connect(user, password);
        } else {
            // Users the credentials configured in the Mail Subsystem
            store.connect();
        }

        Folder inbox = store.getFolder("Inbox");
        inbox.open(Folder.READ_ONLY);

        // get the list of inbox messages
        Message[] messages = inbox.getMessages();

        if (messages.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder("Emails retrieved via ").append(protocol).append("\n");
        for (int i = 0; i < messages.length; i++) {
            // stop after listing ten messages
            if (i > 10) {
                inbox.close(true);
                store.close();
            }

            sb.append("Message ").append((i + 1)).append("\n");
            sb.append("From : ").append(messages[i].getFrom()[0]).append("\n");
            sb.append("Subject : ").append(messages[i].getSubject()).append("\n");
            sb.append("Body : ").append(messages[i].getContent().toString()).append("\n");
            sb.append("Sent Date : ").append(messages[i].getSentDate()).append("\n");
            sb.append("----------------------------------").append("\n");
        }

        inbox.close(true);
        store.close();

        return sb.toString();
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPop3User() {
        return pop3User;
    }

    public void setPop3User(String pop3User) {
        this.pop3User = pop3User;
    }

    public String getPop3Password() {
        return pop3Password;
    }

    public void setPop3Password(String pop3Password) {
        this.pop3Password = pop3Password;
    }

    public String getPop3Emails() {
        return pop3Emails;
    }

    public void setPop3Emails(String pop3Emails) {
        this.pop3Emails = pop3Emails;
    }

    public String getImapEmails() {
        return imapEmails;
    }

    public void setImapEmails(String imapEmails) {
        this.imapEmails = imapEmails;
    }
}
