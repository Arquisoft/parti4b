package es.uniovi.asw.model;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.uniovi.asw.Application;
import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.util.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CitizenTest {
	@Autowired
	private Factories factory;

	private Citizen usuario;
	private Calendar c1;
	private int numCitizens;

	@Before
	public void inicializarTest()
			throws NoSuchAlgorithmException, CitizenException {
		c1 = GregorianCalendar.getInstance();
		c1.set(Calendar.YEAR, 1988);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		usuario = new Citizen("a", "b b", "c@gmail.com",
				new Date(c1.getTimeInMillis()), "residencia", "nacionalidad",
				"dni");
		numCitizens = (int) factory.getPersistenceFactory()
				.newCitizenRepository().count();
	}

	@Test
	public void testModificarCitizen()
			throws CitizenException, NoSuchAlgorithmException {
		assertFalse(usuario.isAdmin());
		usuario.setAdmin(true);
		assertTrue(usuario.isAdmin());

		Long id = usuario.getId();
		usuario.setId((long) 852);
		assertNotEquals(id, usuario.getId());

		String aux = usuario.getNombre();
		usuario.setNombre("asdas");
		assertNotEquals(aux, usuario.getNombre());

		aux = usuario.getApellidos();
		usuario.setApellidos("asdas");
		assertNotEquals(aux, usuario.getApellidos());

		aux = usuario.getEmail();
		usuario.setEmail("asdas");
		assertNotEquals(aux, usuario.getEmail());

		c1.set(Calendar.YEAR, 1998);
		Date fecha = new Date(c1.getTimeInMillis());
		usuario.setFechaNacimiento(fecha);
		assertNotEquals(aux, usuario.getFechaNacimiento());

		aux = usuario.getResidencia();
		usuario.setResidencia("asdas");
		assertNotEquals(aux, usuario.getResidencia());

		aux = usuario.getNacionalidad();
		usuario.setNacionalidad("asdas");
		assertNotEquals(aux, usuario.getNacionalidad());

		aux = usuario.getDni();
		usuario.setDni("asdas");
		assertNotEquals(aux, usuario.getDni());

		aux = usuario.getPassword();
		usuario.setPassword("asdas");
		assertNotEquals(aux, usuario.getPassword());
	}

	@Test
	public void testCitizensAll() {
		List<Citizen> citizens = factory.getServicesFactory()
				.getCitizenService().findAll();
		assertEquals(501, citizens.size());
		for (Citizen c : citizens) {
			assertNotNull(c.getApellidos());
			assertNotNull(c.getDni());
			assertNotNull(c.getEmail());
			assertNotNull(c.getFechaNacimiento());
			assertNotNull(c.getId());
			assertNotNull(c.getNacionalidad());
			assertNotNull(c.getNombre());
			assertNotNull(c.getPassword());
			assertNotNull(c.getResidencia());
		}
	}

	@Test
	public void testCitizensFindById() {
		for (int i = 1; i < 10; i++) {
			int id = Util.generarAleatorio(numCitizens);
			Citizen c = factory.getServicesFactory().getCitizenService()
					.findById(id);
			assertNotNull(c.getApellidos());
			assertNotNull(c.getDni());
			assertNotNull(c.getEmail());
			assertNotNull(c.getFechaNacimiento());
			assertNotNull(c.getId());
			assertNotNull(c.getNacionalidad());
			assertNotNull(c.getNombre());
			assertNotNull(c.getPassword());
			assertNotNull(c.getResidencia());
		}
	}

	@Test
	public void testCitizensFindByDni() {
		Citizen c = factory.getServicesFactory().getCitizenService()
				.findById(1);
		String dni = c.getDni();
		c = factory.getServicesFactory().getCitizenService().findByDni(dni);
		assertNotNull(c.getApellidos());
		assertNotNull(c.getDni());
		assertNotNull(c.getEmail());
		assertNotNull(c.getFechaNacimiento());
		assertNotNull(c.getId());
		assertNotNull(c.getNacionalidad());
		assertNotNull(c.getNombre());
		assertNotNull(c.getPassword());
		assertNotNull(c.getResidencia());
	}

	@Test
	public void testCitizensSave() {
		factory.getServicesFactory().getCitizenService().save(usuario);
		assertEquals(numCitizens + 1,
				factory.getPersistenceFactory().newCitizenRepository().count());
		Citizen c = factory.getServicesFactory().getCitizenService()
				.findByDni(usuario.getDni());
		assertEquals(usuario, c);
		c = factory.getServicesFactory().getCitizenService()
				.findById(usuario.getId());
		assertEquals(usuario, c);
		factory.getPersistenceFactory().newCitizenRepository().delete(usuario);
	}

	@Test
	public void testCitizensFindLoggableUser() {
		factory.getServicesFactory().getCitizenService().save(usuario);
		assertEquals(usuario, factory.getServicesFactory().getCitizenService()
				.findLoggableUser(usuario.getDni(), usuario.getPassword()));
		factory.getPersistenceFactory().newCitizenRepository().delete(usuario);
	}

}
