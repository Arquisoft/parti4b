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
public class UserVoteSteps {
	private SeleniumUtilTest su = new SeleniumUtilTest();
	private WebDriver driver = su.getDriver();
	private String baseUrl = String.format("http://localhost:8080");

	@Cuando("^el usuario se logea en la aplicacion$")
	public void el_usuario_se_logea_en_la_aplicacion() throws Throwable {
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

	@Entonces("^puede ver todas las propuestas en tramite$")
	public void puede_ver_todas_las_propuestas_en_tramite() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		su.comprobarCabeceraUser();
		
	}

	@Entonces("^votara la propuesta 3 a favor$")
	public void votara_la_propuesta_3_a_favor() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.id("si3")).click();
	}
	
	@Entonces("^votara la propuesta 4 en contra$")
	public void votara_la_propuesta_4_en_contra() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.id("no4")).click();
	}
	

}