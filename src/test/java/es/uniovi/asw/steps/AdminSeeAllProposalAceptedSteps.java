package es.uniovi.asw.steps;

import java.util.List;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cucumber.api.java.es.*;
import es.uniovi.asw.Application;
import es.uniovi.asw.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminSeeAllProposalAceptedSteps {
	private SeleniumUtilTest su = new SeleniumUtilTest();
	private WebDriver driver = su.getDriver();
	private String baseUrl = String.format("http://localhost:8080");

	
	@Cuando("^el administrador esta en la pantalla de inicio$")
	public void el_administrador_esta_en_la_pantalla_de_inicio() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.get(baseUrl + "/");
		su.comprobarTexto("titulo", "PARTICIPATION SYSTEM 4b");
		assertTrue(su.isElementPresent(By.id("dniLabel")));
		assertTrue(su.isElementPresent(By.id("dni")));
		assertTrue(su.isElementPresent(By.id("passwordLabel")));
		assertTrue(su.isElementPresent(By.id("password")));
		assertTrue(su.isElementPresent(By.id("login")));
		assertTrue(su.isElementPresent(By.id("limpiar")));
		su.comprobarNumElemetos("body", "./*", 3);
		su.comprobarNumElemetos("fieldset", "./*", 4);
		
		driver.findElement(By.name("dni")).clear();
		driver.findElement(By.name("dni")).sendKeys("666xxx");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("admin");
		assertTrue(su.isElementPresent(By.id("login")));
		driver.findElement(By.id("login")).click();
	}
	
	@Entonces("^puede ver el menu$")
	public void puede_ver_el_menu() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		su.comprobarNumElemetos("body", "./*", 1);
		su.comprobarCabeceraAdmin();
	}
	
	@Entonces("^puede hacer click para ver las propuestas aceptadas$")
	public void puede_hacer_click_para_ver_las_propuestas_aceptadas() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		su.comprobarCabeceraAdmin();
		driver.findElement(By.linkText("Propuestas aceptadas")).click();
	}
}