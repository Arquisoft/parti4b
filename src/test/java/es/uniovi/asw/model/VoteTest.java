package es.uniovi.asw.model;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class VoteTest {
	@Autowired
	private Factories factories;

	private Citizen usuario;
	private Proposal proposal;
	private Long numVotes;
	private Vote vote;

	@PostConstruct
	public void inicializarTest()
			throws NoSuchAlgorithmException, CitizenException {
		Calendar c1 = GregorianCalendar.getInstance();
		c1.set(Calendar.YEAR, 1988);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1 = GregorianCalendar.getInstance();
		c1.set(Calendar.YEAR, 1988);
		c1.set(Calendar.MONTH, Calendar.JANUARY);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		usuario = new Citizen("a", "b b", "c@gmail.com",
				new Date(c1.getTimeInMillis()), "residencia", "nacionalidad",
				"dni");
		proposal = new Proposal("", "", 3);
		numVotes = factories.getPersistenceFactory().newVoteRepository()
				.count();
	}

	@Test
	public void testSaveVotes() {
		usuario = factories.getPersistenceFactory().newCitizenRepository()
				.findByID(1);
		proposal = factories.getPersistenceFactory().newProposalRepository()
				.findByID(1);
		vote = new Vote(usuario, proposal);
		factories.getPersistenceFactory().newVoteRepository().save(vote);
		assertEquals(numVotes + 1,
				factories.getPersistenceFactory().newVoteRepository().count());
		Association.Vota.unlink(vote);
		factories.getPersistenceFactory().newVoteRepository().delete(vote);
	}

	@Test
	public void testVotarPositivo() {
		assertTrue(proposal.positiveVote(usuario));
		assertFalse(proposal.negativeVote(usuario));
		proposal.getVotes().remove(vote);
	}

	@Test
	public void testNegativoPositivo() {
		assertTrue(proposal.negativeVote(usuario));
		assertFalse(proposal.positiveVote(usuario));
		proposal.getVotes().remove(vote);
	}

	@Test
	public void testModificarVotos()
			throws NoSuchAlgorithmException, CitizenException {
		vote = new Vote(usuario, proposal);
		vote.setId((long) 1);
		Calendar c1 = GregorianCalendar.getInstance();
		Citizen citizen = new Citizen("", "", "",
				new Date(c1.getTimeInMillis() - 10000), "", "", "");
		Vote vote2 = new Vote(citizen, proposal);
		vote2.setId((long) 2);
		assertFalse(vote.equals(vote2));
		assertNotEquals(vote.toString(), vote2.toString());
	}

}
