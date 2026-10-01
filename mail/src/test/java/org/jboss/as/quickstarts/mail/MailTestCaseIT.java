package org.jboss.as.quickstarts.mail;

import java.io.IOException;

import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSubmitInput;
import org.htmlunit.html.HtmlTextArea;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MailTestCaseIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    private String serverHost;

    @BeforeEach
    public void testSetup() {
        serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }
    }

    @Test
    public void testSendAndRetrieveEmail() throws IOException, InterruptedException {
        try (final WebClient webClient = new WebClient()) {
            // Get the home page
            HtmlPage mailHomePage = webClient.getPage(serverHost + "/mail");
            // Send an email
            sendEmailBySMTP(webClient, mailHomePage);
            // give to mail server extra time to save mail to a mailbox
            Thread.sleep(2000);
            // retrieve an email by POP3 and IMAP
            retrieveEmailByPOP3(webClient, mailHomePage);
            retrieveEmailByIMAP(webClient, mailHomePage);
        }
    }

    private void sendEmailBySMTP(WebClient webClient, HtmlPage mailHomePage) throws IOException {
        HtmlInput from = mailHomePage.getHtmlElementById("smtp_from");
        HtmlInput to = mailHomePage.getHtmlElementById("smtp_to");
        HtmlInput subject = mailHomePage.getHtmlElementById("smtp_subject");
        HtmlTextArea body = mailHomePage.getHtmlElementById("smtp_body");
        HtmlSubmitInput submitButton = mailHomePage.getHtmlElementById("smtp_send_btn");

        from.setValue("user01@mail.local");
        to.setValue("user02@mail.local");
        subject.setValue("This is a test");
        body.setText("Hello user02, I've sent an email.");

        submitButton.click();
        /* will wait JavaScript to execute up to 30s */
        webClient.waitForBackgroundJavaScript(30 * 1000);

        HtmlElement message = mailHomePage.getFirstByXPath("//ul[@id='smtp_messages']/li");
        Assertions.assertEquals("Email sent to user02@mail.local", message.asNormalizedText(), "Unexpected result messages after sending an email via SMTP.");
    }

    private void retrieveEmailByPOP3(WebClient webClient, HtmlPage mailHomePage) throws IOException {
        HtmlInput user = mailHomePage.getHtmlElementById("pop3_user");
        HtmlInput password = mailHomePage.getHtmlElementById("pop3_password");
        HtmlSubmitInput submitButton = mailHomePage.getHtmlElementById("pop3_get_emails_btn");

        user.setValue("user02@mail.local");
        password.setValue("1234");
        submitButton.click();
        /* will wait JavaScript to execute up to 30s */
        webClient.waitForBackgroundJavaScript(30 * 1000);
        HtmlTextArea emails = mailHomePage.getHtmlElementById("pop3_emails");

        Assertions.assertTrue(emails.getText().contains("From : user01@mail.local"), "Expected From not found: " + emails.getText());
        Assertions.assertTrue(emails.getText().contains("Subject : This is a test"), "Expected Subject not found: " + emails.getText());
        Assertions.assertTrue(emails.getText().contains("Body : Hello user02, I've sent an email."), "Expected Body not found : " + emails.getText());
    }

    private void retrieveEmailByIMAP(WebClient webClient, HtmlPage mailHomePage) throws IOException {
        HtmlSubmitInput submitButton = mailHomePage.getHtmlElementById("imap_get_emails_btn");
        submitButton.click();
        /* will wait JavaScript to execute up to 30s */
        webClient.waitForBackgroundJavaScript(30 * 1000);
        HtmlTextArea emails = mailHomePage.getHtmlElementById("imap_emails");

        Assertions.assertNotNull(emails.getText(), "IMAP No messages found.");
        Assertions.assertTrue(emails.getText().contains("From : user01@mail.local"), "Expected From not found: " + emails.getText());
        Assertions.assertTrue(emails.getText().contains("Subject : This is a test"), "Expected Subject not found: " + emails.getText());
        Assertions.assertTrue(emails.getText().contains("Body : Hello user02, I've sent an email."), "Expected Body not found : " + emails.getText());
    }
}
