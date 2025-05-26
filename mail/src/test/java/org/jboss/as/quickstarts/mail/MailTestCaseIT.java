package org.jboss.as.quickstarts.mail;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MailTestCaseIT {

    private static final String DEFAULT_SERVER_HOST = "http://localhost:8080";

    private WebDriver driver;

    @Before
    public void testSetup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--headless");

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        String serverHost = System.getenv("SERVER_HOST");
        if (serverHost == null) {
            serverHost = System.getProperty("server.host");
        }
        if (serverHost == null) {
            serverHost = DEFAULT_SERVER_HOST;
        }

        driver.get(serverHost+"/mail");
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(800));
    }

    @After
    public void cleanUp() {
        if (driver != null) {
            driver.close();
        }
    }

    @Test
    public void a_testSMTP() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement from = driver.findElement(By.id("smtp_from"));
        WebElement to = driver.findElement(By.id("smtp_to"));
        WebElement subject = driver.findElement(By.id("smtp_subject"));
        WebElement body = driver.findElement(By.id("smtp_body"));

        from.clear();
        from.sendKeys("user01@mail.local");

        to.clear();
        to.sendKeys("user02@mail.local");

        subject.clear();
        subject.sendKeys("This is a test");

        body.clear();
        body.sendKeys("Hello user02, I've sent an email.");

        WebElement submitButton = driver.findElement(By.id("smtp_send_btn"));
        submitButton.click();

        wait.until(d -> driver.findElement(By.xpath("//ul[@id='smtp_messages']/li")).isDisplayed());

        Assert.assertEquals(
                "Unexpected result messages after sending an email via SMTP.",
                "Email sent to user02@mail.local",
                driver.findElement(By.xpath("//ul[@id='smtp_messages']/li")).getText());
    }

    @Test
    public void b_retrievePOP3() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement user = driver.findElement(By.id("pop3_user"));
        WebElement password = driver.findElement(By.id("pop3_password"));

        user.clear();
        user.sendKeys("user02@mail.local");

        password.clear();
        password.sendKeys("1234");

        WebElement submitButton = driver.findElement(By.id("pop3_get_emails_btn"));
        submitButton.click();

        wait.until(d -> {
            try {
                WebElement emails = driver.findElement(By.id("pop3_emails"));
                return !emails.getText().isEmpty();
            } catch (StaleElementReferenceException sere) {
                return false;
            }
        });

        WebElement emails = driver.findElement(By.id("pop3_emails"));
        Assert.assertTrue("Expected From not found: " + emails.getText(), emails.getText().contains("From : user01@mail.local"));
        Assert.assertTrue("Expected Subject not found: " + emails.getText(), emails.getText().contains("Subject : This is a test"));
        Assert.assertTrue("Expected Body not found : " + emails.getText(), emails.getText().contains("Body : Hello user02, I've sent an email."));
    }


    @Test
    public void c_retrieveIMAP() {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        WebElement submitButton = driver.findElement(By.id("imap_get_emails_btn"));
        submitButton.click();

        wait.until(d -> {
            try {
                WebElement emails = driver.findElement(By.id("imap_emails"));
                return !emails.getText().isEmpty();
            } catch (StaleElementReferenceException sere) {
                return false;
            }
        });

        WebElement emails = driver.findElement(By.id("imap_emails"));
        Assert.assertNotNull("IMAP No messages found.", emails.getText());
        Assert.assertTrue("Expected email not found.", emails.getText().contains("From : user01@mail.local"));
        Assert.assertTrue("Expected email not found.", emails.getText().contains("Subject : This is a test"));
        Assert.assertTrue("Expected email not found.", emails.getText().contains("Body : Hello user02, I've sent an email."));
    }
}
