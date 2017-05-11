package es.uniovi.asw.steps;

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
public class UserCreaComentarioSteps {
	private SeleniumUtilTest su = new SeleniumUtilTest();
	private WebDriver driver = su.getDriver();
	private String baseUrl = String.format("http://localhost:8080");

	@Cuando("^el usuario entra en sesion$")
	public void el_usuario_entra_en_sesion() throws Throwable {
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
	
	@Entonces("^ve las propuestas$")
	public void ve_las_propuestas() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		su.comprobarCabeceraUser();
		
	}

	@Entonces("^puede hacer click en ver dentro de cada propuesta$")
	public void puede_hacer_click_en_ver_dentro_de_cada_propuesta() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.id("ver5")).click();
	}

	@Entonces("^desde el nuevo menu puede hacer click en nuevo comentario$")
	public void desde_el_nuevo_menu_puede_hacer_click_en_nuevo_comentario() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.id("crearComment")).click();
	}

	@Entonces("^rellena los datos necesarios$")
	public void rellena_los_datos_necesarios() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.name("comment")).clear();
		driver.findElement(By.name("comment")).sendKeys("Pues en este comentario es de cucumber");
	
	}

	@Entonces("^crea un nuevo comentario$")
	public void crea_un_nuevo_comentario() throws Throwable {
	    // Write code here that turns the phrase above into concrete actions
		driver.findElement(By.id("comentar")).click();
	}
}