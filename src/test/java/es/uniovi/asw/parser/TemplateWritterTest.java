package es.uniovi.asw.parser;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import es.uniovi.asw.model.*;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.participationSystem.parser.*;
import es.uniovi.asw.participationSystem.parser.impl.*;
import es.uniovi.asw.participationSystem.parser.writer.*;

public class TemplateWritterTest {
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private Exception exception;
	private Citizen usuario, anonimo;
	private Calendar c1;

	/**
	 * Inicializamos los parámetros que usamos en los test.
	 * 
	 * @throws NoSuchAlgorithmException
	 *             Excepción ocurrida durante la encriptación
	 * @throws CitizenException
	 *             Excepción ocurrida durante la ejecución
	 */
	@Before
	public void inicializarTest()
			throws NoSuchAlgorithmException, CitizenException {
		System.setErr(new PrintStream(errContent));
		c1 = GregorianCalendar.getInstance();
		c1.set(Calendar.YEAR, 1988);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		usuario = new Citizen("a", "b b", "c@gmail.com",
				new Date(c1.getTimeInMillis()), "residencia", "nacionalidad",
				"dni");
		anonimo = new Citizen();
	}

	/**
	 * 
	 */
	@After
	public void finalizarTest() {
		System.setErr(null);
		exception = null;
	}

	/**
	 * Test que comprueba el correcto funcionamiento de la clase PDFLetter
	 * 
	 * @throws CitizenException
	 *             Excepción ocurrida durante la ejecución
	 * @throws NoSuchAlgorithmException
	 *             Excepción ocurrida durante la encriptación
	 */
	@Test
	public void testWritterPDF()
			throws CitizenException, NoSuchAlgorithmException {
		Letter carta = new PDFLetter();
		assertNotNull(carta);
		testTemplate(carta);
		File file = new File("Letter/PDF/" + usuario.getDni() + ".pdf");
		assertTrue(file.exists());

	}

	/**
	 * Test que comprueba el correcto funcionamiento de la clase testWritterTXT
	 * 
	 * @throws CitizenException
	 *             Excepción ocurrida durante la ejecución
	 * @throws NoSuchAlgorithmException
	 *             Excepción ocurrida durante la encriptación
	 */
	@Test
	public void testWritterTXT()
			throws CitizenException, NoSuchAlgorithmException {
		Letter carta = new TXTLetter();
		assertNotNull(carta);
		testTemplate(carta);
		File file = new File("Letter/TXT/" + usuario.getDni() + ".txt");
		assertTrue(file.exists());
	}

	/**
	 * Test que comprueba el correcto funcionamiento de la clase WordLetter
	 * 
	 * @throws CitizenException
	 *             Excepción ocurrida durante la ejecución
	 * @throws NoSuchAlgorithmException
	 *             Excepción ocurrida durante la encriptación
	 */
	@Test
	public void testWritterWord()
			throws CitizenException, NoSuchAlgorithmException {
		Letter carta = new WordLetter();
		assertNotNull(carta);
		testTemplate(carta);
		File file = new File("Letter/WORD/" + usuario.getDni() + ".docx");
		assertTrue(file.exists());
	}

	/**
	 * Testea todas las acciones comunes de los objetos de tipo Letter
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testTemplate(Letter carta) {
		// Prueba con un usuario correcto
		testUsuarioPerfecto(carta);
		assertNull(exception);

		// Prueba con un usuario null
		testUsuarioNull(carta);

		// Vamos comprobando con un usuario sin parámetro pero cada vez vamos
		// añadiendo uno para ver por donde puede fallar.
		testUsuarioAnonimo(carta);

		try {
			anonimo.setResidencia("a");
			carta.generateLetter(anonimo);
			exception = null;
		} catch (Exception e) {
			exception = e;
		}
		assertNull(exception);
		testUsuarioImposible(carta);
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario null
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioNull(Letter carta) {
		try {
			carta.generateLetter(null);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("Se ha pasado un null como parámetro.",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario con todos los parámetros.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioPerfecto(Letter carta) {
		try {
			carta.generateLetter(usuario);
		} catch (Exception e) {
			exception = e;
		}
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin todos los parámetros iniciados.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioAnonimo(Letter carta) {
		testUsuarioSinCampos(carta);
		try {
			anonimo.setId((long)9);
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("No todos los campos estan inicializados",
				exception.getMessage());
		testUsuarioSinApellidos(carta);
		testUsuarioSinDNI(carta);
		testUsuarioSinEmail(carta);
		testUsuarioSinFechaNacimiento(carta);
		testUsuarioSinNacionalidad(carta);
		testUsuarioSinNombre(carta);
		testUsuarioSinPassword(carta);
		testUsuarioSinResidencia(carta);
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario con sus respectivos campos en blanco.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinCampos(Letter carta) {
		try {
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("No todos los campos estan inicializados",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin apellidos.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinApellidos(Letter carta) {
		try {
			anonimo.setApellidos("");
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("El siguiente campo del usuario esta vacío -> Apellidos",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin DNI.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinDNI(Letter carta) {
		try {
			anonimo.setApellidos("a");
			anonimo.setDni("");
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("El siguiente campo del usuario esta vacío -> DNI",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin email.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinEmail(Letter carta) {
		try {
			anonimo.setDni("a");
			anonimo.setEmail("");
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("El siguiente campo del usuario esta vacío -> Email",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin fecha de nacimiento.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinFechaNacimiento(Letter carta) {
		try {
			anonimo.setEmail("a");
			anonimo.setFechaNacimiento(null);
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("La fecha de nacimiento no puede ser null.",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin nacionalidad.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinNacionalidad(Letter carta) {
		try {
			anonimo.setFechaNacimiento(new Date(c1.getTimeInMillis()));
			anonimo.setNacionalidad("");
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals(
				"El siguiente campo del usuario esta vacío -> Nacionalidad",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin nombre.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinNombre(Letter carta) {
		try {
			anonimo.setNacionalidad("a");
			anonimo.setNombre("");
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("El siguiente campo del usuario esta vacío -> Nombre",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin password.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinPassword(Letter carta) {
		try {
			anonimo.setNombre("a");
			anonimo.setPassword(null);
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("No todos los campos estan inicializados",
				exception.getMessage());

		try {
			anonimo.setPassword("");
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("No todos los campos estan inicializados",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario sin residencia.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioSinResidencia(Letter carta) {
		try {
			anonimo.setPassword("sadf");
			anonimo.setResidencia("");
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("El siguiente campo del usuario esta vacío -> Residencia",
				exception.getMessage());
	}

	/**
	 * Test que comprueba el comportamiento al intentar crear una carta a un
	 * usuario con datos imposibles.
	 * 
	 * @param carta
	 *            Letter que queremos comprobar
	 */
	private void testUsuarioImposible(Letter carta) {
		// Prueba con un usuario con ID negativo
		// Prueba con un usuario que todavía no ha nacido
		try {
			c1.set(2056, 12, 12);
			anonimo.setFechaNacimiento(new Date(c1.getTimeInMillis()));
			carta.generateLetter(anonimo);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals("La fecha de nacimiento es posterior al dia actual.",
				exception.getMessage());
	}

}
