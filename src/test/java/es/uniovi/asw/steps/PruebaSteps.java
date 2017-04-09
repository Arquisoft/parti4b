package es.uniovi.asw.steps;

import java.util.List;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cucumber.api.java.en.*;
import es.uniovi.asw.Application;
import es.uniovi.asw.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PruebaSteps {
	private SeleniumUtilTest su = new SeleniumUtilTest();
	private WebDriver driver = su.getDriver();
	private String baseUrl = String.format("http://localhost:8080/");

	@Given("^the user is on the login page$")
	public void theUserIsOnTheLoginPage() throws Throwable {
		driver.get(baseUrl + "/");
		su.comprobarTexto("titulo", "Participation System");
		assertTrue(su.isElementPresent(By.id("dniLabel")));
		assertTrue(su.isElementPresent(By.id("dni")));
		assertTrue(su.isElementPresent(By.id("passwordLabel")));
		assertTrue(su.isElementPresent(By.id("password")));
		assertTrue(su.isElementPresent(By.id("login")));
		su.comprobarNumElemetos("body", "./*", 2);
		su.comprobarNumElemetos("form", "./*", 7);
		su.comprobarNumElemetos("form", "./input", 3);
	}

	@Given("^the user is logger on as admin$")
	public void theUserIsLoggerOnAsAdmin() throws Throwable {
		driver.findElement(By.name("dni")).clear();
		driver.findElement(By.name("dni")).sendKeys("666xxx");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		assertTrue(su.isElementPresent(By.id("login")));
		driver.findElement(By.id("login")).click();
	}

	@Given("^the user is on the admin main page$")
	public void theUserIsOnTheAdminMainPage() throws Throwable {
		su.comprobarNumElemetos("body", "./*", 1);
		su.comprobarCabeceraAdmin();
	}

	@When("^the user clicks on the button \"([^\"]*)\"$")
	public void theUserClicksOnTheButton(String arg1) throws Throwable {
		su.comprobarCabeceraAdmin();
		driver.findElement(By.linkText("Propuestas en trámite")).click();
	}

	@Then("^the proposals are in process will be shown to the user$")
	public void theProposalsAreInProcessWillBeShownToTheUser()
			throws Throwable {
		su.comprobarCabeceraAdmin();
		su.comprobarNumElemetos("body", "./*", 3);
		su.comprobarTexto("propuestas", "Propuestas en trámite");
		WebElement tabla = driver
				.findElement(By.id("tablaPropuestasEnTramite"));
		assertNotNull(tabla);
		List<WebElement> numFilas = tabla.findElements(
				By.xpath("id('tablaPropuestasEnTramite')/tbody/tr"));
		if (numFilas.size() > 0) {
			for (WebElement trElement : numFilas) {
				List<WebElement> numColum = trElement
						.findElements(By.xpath("td"));
				assertEquals(6, numColum.size());
				for (WebElement elemento : numColum) {
					assertNotNull(elemento);
				}
			}
		}
	}
}