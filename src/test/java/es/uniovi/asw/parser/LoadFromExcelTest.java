package es.uniovi.asw.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import es.uniovi.asw.model.*;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.participationSystem.parser.*;

public class LoadFromExcelTest {
	private List<Citizen> citizens;
	private Exception exception;
	private Parser parser = new LoadFromExcel();

	@After
	public void finalizarTest() {
		citizens = null;
		exception = null;
	}

	/**
	 * Test que comprueba el correcto funcionamiento de la clase LoadFromExcel.
	 * 
	 * @throws CitizenException
	 *             Excepción ocurrida durante la ejecución
	 */
	@Test
	public void testExcelRutaCorrecta() throws CitizenException {
		try {
			citizens = parser.loadUsers(new File("archivosExcel/test.xlsx"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(citizens);
		assertNotEquals(citizens.size(), 0);
		assertNull(exception);
	}

	@Test
	public void testExcelRutaIncorrecta() throws CitizenException {
		try {
			citizens = parser.loadUsers(new File("archivosExcel/tet.xlsx"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals(CitizenException.class, exception.getClass());
		assertEquals("Fichero no encontrado", exception.getMessage());
		assertNull(citizens);
	}

	@Test
	public void testExcelRutaVacia() throws CitizenException {
		try {
			citizens = parser.loadUsers(new File(""));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertEquals(CitizenException.class, exception.getClass());
		assertEquals("Fichero no encontrado", exception.getMessage());
		assertNull(citizens);
	}
}
