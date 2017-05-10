package es.uniovi.asw.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import es.uniovi.asw.model.*;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.participationSystem.parser.*;
import es.uniovi.asw.participationSystem.parser.impl.*;


public class RListTest {
	private ReadList rs = new RList();
	private List<Citizen> citizens;
	private Exception exception;

	@Before
	public void inicializarTest() {
		citizens = null;
		exception = null;
	}

	/**
	 * Test que comprueba el correcto funcionamiento de la clase RCitizens.
	 * 
	 * @throws CitizenException
	 *             Excepción ocurrida durante la ejecución
	 */
	@Test
	public void testRutaCorrecta() throws CitizenException {
		try {
			File file = new File("archivosExcel/test.xlsx");
			citizens = rs.readCitizens(file);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(citizens);
		assertNull(exception);
		assertNotEquals(citizens.size(), 0);
	}

	@Test
	public void testRutaIncorrecta() throws CitizenException {
		try {
			File file = new File("archivosExcel/tes.xlsx");
			citizens = rs.readCitizens(file);
		} catch (Exception e) {
			exception = e;
		}
		assertNull(citizens);
		assertNotNull(exception);
		assertEquals(CitizenException.class, exception.getClass());
		assertEquals("Fichero no encontrado", exception.getMessage());
	}

	@Test
	public void testRutaVacia() throws CitizenException {
		try {
			File file = new File("");
			citizens = rs.readCitizens(file);
		} catch (Exception e) {
			exception = e;
		}
		assertNull(citizens);
		assertNotNull(exception);
		assertEquals(CitizenException.class, exception.getClass());
		assertEquals("Error en el fichero la extensión del archivo",
				exception.getMessage());
	}

}
