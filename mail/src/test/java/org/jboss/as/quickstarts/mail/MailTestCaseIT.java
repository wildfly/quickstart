package org.jboss.as.quickstarts.mail;

import java.io.IOException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MailTestCaseIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    private String serverHost;

    @Before
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
    public void a_testSMTP() throws IOException {
        try (final WebClient webClient = new WebClient()) {
            // Get the first page
            HtmlPage mailHomePage = webClient.getPage(serverHost + "/mail");

            HtmlInput from = mailHomePage.getHtmlElementById("smtp_from");
            HtmlInput to = mailHomePage.getHtmlElementById("smtp_to");
            HtmlInput subject = mailHomePage.getHtmlElementById("smtp_subject");
            HtmlTextArea body = mailHomePage.getHtmlElementById("smtp_body");
            HtmlSubmitInput submitButton = mailHomePage.getHtmlElementById("smtp_send_btn");

            from.setValue("user01@james.local");
            to.setValue("user02@james.local");
            subject.setValue("This is a test");
            body.setText("Hello user02, I've sent an email.");

            submitButton.click();
            /* will wait JavaScript to execute up to 30s */
            webClient.waitForBackgroundJavaScript(30 * 1000);

            HtmlElement message = mailHomePage.getFirstByXPath("//ul[@id=\"smtp_messages\"]");
            Assert.assertEquals("Unexpected result messages after sending an email via SMTP.", "Email sent to user02@james.local", message.asNormalizedText());
        }
    }

    @Test
    public void b_retrievePOP3() throws IOException {
        try (final WebClient webClient = new WebClient()) {
            // Get the first page
            HtmlPage mailHomePage = webClient.getPage(serverHost + "/mail");

            HtmlInput user = mailHomePage.getHtmlElementById("pop3_user");
            HtmlInput password = mailHomePage.getHtmlElementById("pop3_password");
            HtmlSubmitInput submitButton = mailHomePage.getHtmlElementById("pop3_get_emails_btn");

            user.setValue("user02@james.local");
            password.setValue("1234");
            submitButton.click();
            /* will wait JavaScript to execute up to 30s */
            webClient.waitForBackgroundJavaScript(30 * 1000);
            HtmlTextArea emails = mailHomePage.getHtmlElementById("pop3_emails");

            Assert.assertTrue("Expected From not found: " + emails.getText(), emails.getText().contains("From : user01@james.local"));
            Assert.assertTrue("Expected Subject not found: " + emails.getText(), emails.getText().contains("Subject : This is a test"));
            Assert.assertTrue("Expected Body not found : " + emails.getText(), emails.getText().contains("Body : Hello user02, I've sent an email."));
        }
    }


    @Test
    public void c_retrieveIMAP() throws IOException {
        try (final WebClient webClient = new WebClient()) {
            // Get the first page
            HtmlPage mailHomePage = webClient.getPage(serverHost + "/mail");

            HtmlSubmitInput submitButton = mailHomePage.getHtmlElementById("imap_get_emails_btn");
            submitButton.click();
            /* will wait JavaScript to execute up to 30s */
            webClient.waitForBackgroundJavaScript(30 * 1000);
            HtmlTextArea emails = mailHomePage.getHtmlElementById("imap_emails");

            Assert.assertNotNull("IMAP No messages found.", emails.getText());
            Assert.assertTrue("Expected From not found: " + emails.getText(), emails.getText().contains("From : user01@james.local"));
            Assert.assertTrue("Expected Subject not found: " + emails.getText(), emails.getText().contains("Subject : This is a test"));
            Assert.assertTrue("Expected Body not found : " + emails.getText(), emails.getText().contains("Body : Hello user02, I've sent an email."));
        }
    }
}
