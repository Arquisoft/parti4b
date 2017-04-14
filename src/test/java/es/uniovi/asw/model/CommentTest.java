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
	public void testCommentsSave() {
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

}
