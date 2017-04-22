package es.uniovi.asw.model;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
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
import es.uniovi.asw.model.types.status.EstadosPropuesta;
import es.uniovi.asw.util.Util;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ProposalTest {

	@Autowired
	private Factories factory;

	private Citizen c;
	private Proposal proposal;
	private int numProposals;

	@PostConstruct
	public void inicializar()
			throws NoSuchAlgorithmException, CitizenException {
		c = factory.getServicesFactory().getCitizenService().findById(
				Util.generarAleatorio((int) factory.getPersistenceFactory()
						.newCitizenRepository().count()));
		proposal = new Proposal("Test", "ProposalTest", 10);
		numProposals = (int) factory.getPersistenceFactory()
				.newProposalRepository().count();
	}

	@Test
	public void testProposalSave() {
		factory.getServicesFactory().getProposalService().save(proposal);
		assertEquals(numProposals + 1, factory.getPersistenceFactory()
				.newProposalRepository().count());
		factory.getPersistenceFactory().newProposalRepository()
				.delete(proposal);
	}

	@Test
	public void testProposalAll() {
		List<Proposal> proposals = factory.getServicesFactory()
				.getProposalService().findAll();
		for (Proposal p : proposals) {
			assertNotNull(p.getName());
			assertNotNull(p.getContent());
			assertNotNull(p.getMinVotes());
			assertNotNull(p.getStatus());
		}
	}

	@Test
	public void testProposalFindById() {
		Proposal p = factory.getServicesFactory().getProposalService()
				.findById(1);
		assertNotNull(p.getName());
		assertNotNull(p.getContent());
		assertNotNull(p.getMinVotes());
		assertNotNull(p.getStatus());
	}

	@Test
	public void testProposalFindByStatus() {
		List<Proposal> proposals = factory.getServicesFactory()
				.getProposalService().findByStatus(EstadosPropuesta.EnTramite);
		for (Proposal p : proposals) {
			assertNotNull(p.getName());
			assertNotNull(p.getContent());
			assertNotNull(p.getMinVotes());
			assertNotNull(p.getStatus());
		}

	}

	@Test
	public void testProposalFindByCitizen() throws CitizenException {
		factory.getServicesFactory().getProposalService().save(proposal);
		Commentary com = new Commentary(c, proposal, "Prueba");
		factory.getPersistenceFactory().newCommentaryRepository().save(com);
		Proposal p = factory.getServicesFactory().getProposalService()
				.findByCitizen(c.getDni());
		assertNotNull(p.getName());
		assertNotNull(p.getContent());
		assertNotNull(p.getMinVotes());
		assertNotNull(p.getStatus());
		factory.getServicesFactory().getCommentaryService().delete(com);
		factory.getPersistenceFactory().newProposalRepository()
				.delete(proposal);
	}

	@Test
	public void testProposaldelete() {
		numProposals = (int) factory.getPersistenceFactory()
				.newProposalRepository().count();
		factory.getPersistenceFactory().newProposalRepository()
				.delete(proposal);
		assertEquals(numProposals, factory.getPersistenceFactory()
				.newProposalRepository().count());
	}

	@Test
	public void testProposalStates() throws CitizenException {
		proposal.acceptProposal();
		assertEquals(proposal.getStatus(), EstadosPropuesta.Aceptada);
		proposal.cancelProposal();
		assertEquals(proposal.getStatus(), EstadosPropuesta.Anulada);
		proposal.restoreEstadoPropuesta();
		assertEquals(proposal.getStatus(), EstadosPropuesta.EnTramite);
		proposal.refuseProposal();
		assertEquals(proposal.getStatus(), EstadosPropuesta.Rechazada);
	}

	@Test
	public void testProposalInsert() throws CitizenException {
		int count = proposal.getComments().size();
		new Commentary(c, proposal, "");
		assertEquals(count + 1, proposal.getComments().size());
	}

	@Test
	public void testProposalEquals() throws CitizenException {
		Proposal p = new Proposal("", "", 12345);
		p.setId((long) 1);
		assertNotEquals(p, proposal);
	}

}
