package org.jboss.as.quickstarts.spring.petclinic.test.webdriver;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="kpiwko@redhat.com>Karel Piwko</a>
 * @author <a href="rfalhar@redhat.com>Radek Falhar</a>
 */
@RunWith(Arquillian.class)
public class PetclinicTest {

    private final CloseableHttpClient httpClient = HttpClientBuilder.create().build();

    @FindByJQuery("h2:contains('Pets and Visits')")
    WebElement PETS_AND_VISITS;

    @FindByJQuery("a:contains('Find owners')")
    WebElement FIND_OWNER_LINK;

    @FindByJQuery("input#lastName")
    WebElement LAST_NAME_INPUT;

    @FindByJQuery("button:contains('Find Owner')")
    WebElement LAST_NAME_SUBMIT;

    @FindByJQuery("a:contains('Add Visit')")
    List<WebElement> ADD_VISIT_LINK;

    @FindByJQuery("table:contains('Samantha') a:contains('Add Visit')")
    WebElement SAMANTHA_ADD_VISIT_LINK;

    @FindByJQuery("table:contains('Samantha') table tr:eq(3)")
    WebElement SAMANTHA_THIRD_VISIT;

    @FindBy(id = "date")
    WebElement VISIT_DATE;

    @FindBy(id = "description")
    WebElement VISIT_DESCRIPTION;

    @FindByJQuery("button:contains('Add Visit')")
    WebElement VISIT_SUBMIT;

    @FindByJQuery("a:contains('Edit Pet')")
    List<WebElement> EDIT_PET_LINK;

    @FindBy(id = "name")
    WebElement PET_NAME;

    @FindBy(id = "birthDate")
    WebElement PET_BIRTH;

    @FindByJQuery("#type option[value = 'hamster']")
    WebElement HAMSTER;

    @FindByJQuery("table tr dl:contains('Maxous')")
    WebElement HAMSTER_MAXOUS;

    @FindByJQuery("button:contains('Update Pet')")
    WebElement PET_SUBMIT;

    @FindByJQuery("input[value='Delete Pet']")
    WebElement DELETE_PET_SUBMIT;

    @FindByJQuery("span:contains('invalid date')")
    WebElement DATE_ERROR;

    @FindByJQuery("span:contains('numeric value out of bounds')")
    WebElement NUMERIC_ERROR;

    @FindByJQuery("span:contains('is required')")
    WebElement IS_REQUIRED_ERROR;

    @FindByJQuery("a:contains('Add Owner')")
    WebElement ADD_OWNER_LINK;

    @FindByJQuery("#add-owner-form input")
    List<WebElement> NEW_OWNER_FORM;

    @FindByJQuery("button:contains('Add Owner')")
    WebElement ADD_OWNER_BUTTON;

    @FindByJQuery("table:contains('Telephone')")
    WebElement OWNER_FORM;

    @FindByJQuery("a:contains('Add New Pet')")
    WebElement ADD_NEW_PET_LINK;

    @FindByJQuery("button:contains('Add Pet')")
    WebElement ADD_PET_BUTTON;

    @FindByJQuery("form#pet input")
    List<WebElement> NEW_PET_FORM;

    @FindByJQuery("#type option[value = 'snake']")
    WebElement SNAKE;

    @FindByJQuery("table tr dl:contains('Dean')")
    WebElement SNAKE_DEAN;

    @FindByJQuery("a:contains('Veterinarians')")
    WebElement VETERINARIANS_LINK;

    @FindByJQuery("table#vets tbody tr")
    List<WebElement> VETS_TABLE_ROWS;

    @FindByJQuery("label:contains('Search') input")
    WebElement SEARCH_INPUT;

    @FindByJQuery("a:contains('Error')")
    WebElement ERROR_LINK;

    @FindByJQuery("h2:contains('Something happened...')")
    WebElement ERROR_LABEL;

    @Drone
    protected WebDriver driver;

    @ArquillianResource
    URL contextPath;

    @Deployment(testable = false)
    public static Archive<?> archive() {
        return Deployments.archive();
    }

    @Test
    @InSequence(1)
    public void findOwner() {
        driver.get(contextPath.toString());
        waitModel().withMessage("Waiting for owner search link").until().element(FIND_OWNER_LINK).is().visible();
        guardHttp(FIND_OWNER_LINK).click();

        LAST_NAME_INPUT.sendKeys("Coleman");
        guardHttp(LAST_NAME_SUBMIT).click();

        waitModel().withMessage("Pets and visits page should be loaded").until().element(PETS_AND_VISITS).is().visible();
        Assert.assertEquals("There should be two pet which can have a visit added.", 2, ADD_VISIT_LINK.size());
    }

    @Test
    @InSequence(2)
    public void addVisit() {
        guardHttp(SAMANTHA_ADD_VISIT_LINK).click();

        VISIT_DATE.clear();
        VISIT_DATE.sendKeys("2009/03/21");
        VISIT_DESCRIPTION.sendKeys("Check paws!");

        guardHttp(VISIT_SUBMIT).click();

        waitModel().withMessage("Pets and visits page should be loaded").until().element(PETS_AND_VISITS).is().visible();
        // TODO: fix the date
        Assert.assertTrue(SAMANTHA_THIRD_VISIT.getText().contains("Check paws!") &&
            SAMANTHA_THIRD_VISIT.getText().contains("2009-03-2"));
    }

    @Test
    @InSequence(3)
    public void editPet() {
        Assert.assertEquals("There are two pet which can be edited.", 2, EDIT_PET_LINK.size());

        guardHttp(EDIT_PET_LINK.get(0)).click();

        waitGui().withMessage("Name input is not present").until().element(PET_NAME).is().visible();

        PET_NAME.clear();
        PET_NAME.sendKeys("Maxous");
        PET_BIRTH.clear();
        PET_BIRTH.sendKeys("1995/03/33");
        guardHttp(PET_SUBMIT).click();

        waitGui().withMessage("The date is incorrect").until().element(DATE_ERROR).is().visible();

        PET_BIRTH.clear();
        PET_BIRTH.sendKeys("1995/03/09");

        HAMSTER.click();

        guardHttp(PET_SUBMIT).click();

        waitGui().withMessage("Pets and visits page should be loaded").until().element(HAMSTER_MAXOUS).is().visible();
        Assert.assertTrue(HAMSTER_MAXOUS.getText().contains("hamster"));
        // TODO: fix the date
        Assert.assertTrue(HAMSTER_MAXOUS.getText().contains("1995-03-0"));
        Assert.assertTrue(HAMSTER_MAXOUS.getText().contains("hamster"));
    }

    @Test
    @InSequence(4)
    public void addOwner() {
        waitModel().withMessage("Waiting for owner search link").until().element(FIND_OWNER_LINK).is().visible();
        guardHttp(FIND_OWNER_LINK).click();

        waitGui().until().element(ADD_OWNER_LINK).is().visible();
        guardHttp(ADD_OWNER_LINK).click();

        String[] newOwner = new String[] { "Sal", "Paradise", "Market Street 1", "Denver", "telephone" };
        for (int i = 0; i < newOwner.length; i++) {
            NEW_OWNER_FORM.get(i).clear();
            NEW_OWNER_FORM.get(i).sendKeys(newOwner[i]);
        }
        guardHttp(ADD_OWNER_BUTTON).click();

        waitModel().until().element(NUMERIC_ERROR).is().visible();
        NEW_OWNER_FORM.get(4).clear();
        NEW_OWNER_FORM.get(4).sendKeys("486351729");
        guardHttp(ADD_OWNER_BUTTON).click();

        waitModel().until().element(OWNER_FORM).is().visible();
        Assert.assertEquals(newOwner[0] + " " + newOwner[1],
            OWNER_FORM.findElement(ByJQuery.selector("tr:contains('Name') td b")).getText());
        Assert.assertEquals(newOwner[2], OWNER_FORM.findElement(ByJQuery.selector("tr:contains('Address') td")).getText());
        Assert.assertEquals(newOwner[3], OWNER_FORM.findElement(ByJQuery.selector("tr:contains('City') td")).getText());
        Assert.assertEquals("486351729", OWNER_FORM.findElement(ByJQuery.selector("tr:contains('Telephone') td")).getText());

    }

    @Test
    @InSequence(5)
    public void addPet() {
        waitGui().until().element(ADD_NEW_PET_LINK).is().visible();
        guardHttp(ADD_NEW_PET_LINK).click();

        waitGui().until().element(ADD_PET_BUTTON).is().visible();
        NEW_PET_FORM.get(1).clear();
        NEW_PET_FORM.get(1).sendKeys("Dean");
        NEW_PET_FORM.get(2).clear();
        NEW_PET_FORM.get(2).sendKeys("2013/09/11");
        guardHttp(ADD_PET_BUTTON).click();

        waitGui().until().element(IS_REQUIRED_ERROR).is().visible();
        SNAKE.click();
        guardHttp(ADD_PET_BUTTON).click();

        waitModel().until().element(SNAKE_DEAN).is().visible();
        // TODO: fix the date
        Assert.assertTrue(SNAKE_DEAN.getText().contains("2013-09-1"));
        Assert.assertTrue(SNAKE_DEAN.getText().contains("snake"));
    }

    @Test
    @InSequence(6)
    public void findVet() {
        openVetsPage();

        SEARCH_INPUT.sendKeys("ra");
        Assert.assertThat(VETS_TABLE_ROWS.size(), is(equalTo(3)));

        SEARCH_INPUT.sendKeys("d");
        Assert.assertThat(VETS_TABLE_ROWS.size(), is(equalTo(2)));

        SEARCH_INPUT.clear();
        SEARCH_INPUT.sendKeys("sha");
        Assert.assertThat(VETS_TABLE_ROWS.size(), is(equalTo(1)));

        SEARCH_INPUT.sendKeys("x");
        Assert.assertThat(VETS_TABLE_ROWS.size(), is(equalTo(1)));
        VETS_TABLE_ROWS.get(0).getText().contains("No matching records found");
    }

    @Test
    @InSequence(7)
    public void checkXmlAndFeedOfVets() throws UnsupportedEncodingException, IOException, ValidityException, ParsingException {
        openVetsPage();

        List<Vet> vets = new ArrayList<>();
        for (WebElement vetRow : VETS_TABLE_ROWS) {
            List<WebElement> vetAttributes = vetRow.findElements(ByJQuery.selector("td"));
            String[] vetName = vetAttributes.get(0).getText().split(" ");
            String[] vetSpecialties = vetAttributes.get(1).getText().split(" ");
            vets.add(new Vet(vetName[0], vetName[1], vetSpecialties));
        }

        URL url = new URL(driver.getCurrentUrl().replace("vets.html", "vets.xml"));
        InputStreamReader is = new InputStreamReader(url.openStream(), "UTF-8");
        checkXml(vets, new Builder().build(is));

        checkJson(vets, driver.getCurrentUrl().replace("vets.html", "vets.json"));
    }

    @Test
    @InSequence(8)
    public void throwException() {
        waitGui().until().element(ERROR_LINK).is().visible();
        guardHttp(ERROR_LINK).click();
        waitAjax().until().element(ERROR_LABEL).is().visible();
    }

    private void openVetsPage() {
        waitGui().until().element(VETERINARIANS_LINK).is().visible();
        guardHttp(VETERINARIANS_LINK).click();

        waitModel().until().element(SEARCH_INPUT).is().visible();
        Assert.assertThat(VETS_TABLE_ROWS.size(), is(equalTo(6)));
    }

    private void checkJson(List<Vet> vets, String url) throws ClientProtocolException, IOException {

        HttpResponse response = httpClient.execute(new HttpGet(url));

        Assert.assertEquals(200, response.getStatusLine().getStatusCode());

        String responseBody = EntityUtils.toString(response.getEntity());
        JSONArray vetArr = new JSONObject(responseBody).getJSONArray("vetList");

        List<Vet> vetsFromJson = createListOfVetsFromJson(vetArr);

        // TODO more complex check whether the lists of Vets are equal than just checking the size
        Assert.assertEquals(vets.size(), vetsFromJson.size());
    }

    private List<Vet> createListOfVetsFromJson(JSONArray vetArr) {
        List<Vet> vetsFromJson = new ArrayList<>();
        for (int i = 0; i < vetArr.length(); i++) {
            JSONObject jsonVet = vetArr.getJSONObject(i);
            JSONArray specialtiesArr = jsonVet.getJSONArray("specialties");

            String firstName = jsonVet.getString("firstName");
            String lastName = jsonVet.getString("lastName");
            String[] specialties = new String[specialtiesArr.length() == 0 ? 1 : specialtiesArr.length()];
            if (specialtiesArr.length() == 0) {
                specialtiesArr.put(new JSONObject("{\"name\":none}"));
            }
            for (int j = 0; j < specialtiesArr.length(); j++) {
                specialties[j] = specialtiesArr.getJSONObject(j).getString("name");
            }
            vetsFromJson.add(new Vet(firstName, lastName, specialties));
        }

        return vetsFromJson;
    }

    private void checkXml(List<Vet> vets, Document document) {
        Assert.assertThat(document.query("//vetList").size(), is(equalTo(6)));
        String queryTemplate = "//vetList/firstName[text()='%1$s' and ../lastName[text()='%2$s']%3$s]/../id";

        for (Vet vet : vets) {
            String query = "";
            int numberOfSpecs = 0;

            if (vet.getSpecialties().length > 0 && !vet.getSpecialties()[0].equals("none")) {
                StringBuilder specQuery = new StringBuilder("");
                numberOfSpecs = vet.getSpecialties().length;

                for (int i = 1; i <= numberOfSpecs; i++) {
                    specQuery.append(" and ../specialties/name[text()='%" + i + "$s']");
                }
                query = String.format(queryTemplate, vet.getFirstName(), vet.getLastName(), specQuery.toString());
                query = String.format(query, (Object[]) vet.getSpecialties());

            } else {
                query = String.format(queryTemplate, vet.getFirstName(), vet.getLastName(), "");
            }

            Nodes nodes = document.query(query);
            Assert.assertThat(nodes.size(), is(equalTo(1)));
            Node vetId = nodes.get(0);
            Assert.assertThat(vetId.query("../firstName").size(), is(equalTo(1)));
            Assert.assertThat(vetId.query("../lastName").size(), is(equalTo(1)));
            Assert.assertThat(vetId.query("../specialties").size(), is(equalTo(numberOfSpecs)));
        }
    }
}
