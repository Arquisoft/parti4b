package es.uniovi.asw.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class SeleniumUtilTest {
	private WebDriver driver = new HtmlUnitDriver();

	public WebDriver getDriver() {
		return driver;
	}

	public void comprobarTexto(String id, String texto) {
		WebElement element = driver.findElement(By.id(id));
		assertNotNull(element);
		assertEquals(texto, element.getText());
	}

	public void comprobarNumElemetos(String tagName, String tipoElemento,
			int numElem) {
		WebElement body = driver.findElement(By.tagName(tagName));
		assertEquals(numElem, body.findElements(By.xpath(tipoElemento)).size());
	}

	public void comprobarCabeceraAdmin() {
		comprobarNumElemetos("header", "./*", 2);
		comprobarTexto("administrador", "Administrador");
		assertTrue(isElementPresent(By.id("inicio")));
		assertTrue(isElementPresent(By.id("propuestasTramite")));
		assertTrue(isElementPresent(By.id("propuestasAceptadas")));
		assertTrue(isElementPresent(By.id("propuestasRechazadas")));
		assertTrue(isElementPresent(By.id("Salir")));
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void imprimirHijos(String tagName) {
		WebElement body = driver.findElement(By.tagName(tagName));
		for (WebElement body2 : body.findElements(By.xpath("./*"))) {
			System.err.println(body2);
		}
	}
}
