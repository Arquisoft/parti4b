package es.uniovi.asw.model;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import es.uniovi.asw.Application;
import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.model.types.status.EstadosComentario;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CommentTest {

	@Autowired
	private Factories factories;

	private Citizen usuario;
	private Proposal propuesta;
	private int numComments;

	@PostConstruct
	public void inicializar() {
		usuario = factories.getPersistenceFactory().newCitizenRepository()
				.findByID(1);
		propuesta = factories.getPersistenceFactory().newProposalRepository()
				.findByID(1);
		numComments = (int) factories.getPersistenceFactory()
				.newCommentaryRepository().count();
	}

	@Test
	public void testCommentsSave() throws CitizenException {
		Commentary c = new Commentary(usuario, propuesta, "mensaje");
		factories.getPersistenceFactory().newCommentaryRepository().save(c);
		assertEquals(numComments + 1, factories.getPersistenceFactory()
				.newCommentaryRepository().count());
		factories.getServicesFactory().getCommentaryService().delete(c);
	}

	@Test
	public void testCommentsAll() {
		List<Commentary> comentarios = factories.getServicesFactory()
				.getCommentaryService().findAll();
		for (Commentary c : comentarios) {
			assertNotNull(c.getContent());
		}
	}

	@Test
	public void testCheckComment() throws CitizenException {
		Commentary commentary;
		try {
			commentary = new Commentary(null, null, "");
		} catch (Exception e) {
			assertEquals(e.getClass(), CitizenException.class);
			assertEquals(e.getMessage(), "Citizen is null");
		}

		try {
			commentary = new Commentary(new Citizen(), null, "");
		} catch (Exception e) {
			assertEquals(e.getClass(), CitizenException.class);
			assertEquals(e.getMessage(), "Proposal is null");
		}

		commentary = new Commentary(usuario, propuesta, "mensaje");
		Commentary c = new Commentary(usuario, propuesta, "");
		assertNotEquals(commentary.toString(), c.toString());
		assertEquals(commentary.getEstado(), EstadosComentario.Correcto);
		commentary.censurarComentario();
		assertEquals(commentary.getEstado(), EstadosComentario.Censurado);
	}

}
