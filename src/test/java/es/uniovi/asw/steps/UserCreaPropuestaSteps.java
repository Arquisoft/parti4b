package es.uniovi.asw.steps;

import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cucumber.api.java.es.*;
import es.uniovi.asw.Application;
import es.uniovi.asw.dbManagement.ProposalRepository;
import es.uniovi.asw.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserCreaPropuestaSteps {
	private SeleniumUtilTest su = new SeleniumUtilTest();
	private WebDriver driver = su.getDriver();
	private String baseUrl = String.format("http://localhost:8080");

	@Cuando("^el usuario inicia sesion$")
	public void el_usuario_inicia_sesion() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
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
		driver.findElement(By.name("dni")).sendKeys("1236547n");
		driver.findElement(By.name("password")).clear();
		driver.findElement(By.name("password")).sendKeys("contraseña");
		assertTrue(su.isElementPresent(By.id("login")));
		driver.findElement(By.id("login")).click();
	}

	@Entonces("^puede ver las propuestas$")
	public void puede_ver_las_propuestas() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		su.comprobarCabeceraUser();
		
	}

	@Entonces("^puede hacer click en nueva propuesta$")
	public void puede_hacer_click_en_nueva_propuesta() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.id("nuevaProp")).click();
	}

	@Entonces("^rellena los datos$")
	public void rellena_los_datos() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.name("nombre")).clear();
		driver.findElement(By.name("nombre")).sendKeys("El mejor test del mundo trabajando.");
		
		driver.findElement(By.name("contenido")).clear();
		driver.findElement(By.name("contenido")).sendKeys("Pues en este test se realizo a traves de cucumber");
	}

	@Autowired
	ProposalRepository p;
	@Entonces("^se publica el comentario si es correcto$")
	public void se_publica_el_comentario_si_es_correcto() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions

		driver.findElement(By.id("comentar")).click();
	}

}