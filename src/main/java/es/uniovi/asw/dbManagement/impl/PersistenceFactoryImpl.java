package es.uniovi.asw.dbManagement.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

import es.uniovi.asw.dbManagement.CitizenRepository;
import es.uniovi.asw.dbManagement.CommentaryRepository;
import es.uniovi.asw.dbManagement.PersistenceFactory;
import es.uniovi.asw.dbManagement.ProposalRepository;
import es.uniovi.asw.dbManagement.VoteRepository;

@Service
@EnableJpaRepositories("es.uniovi.asw.dbManagement")
public class PersistenceFactoryImpl implements PersistenceFactory {

	@Autowired
	private CitizenRepository citizenRepository;

	@Autowired
	private ProposalRepository proposalRepository;

	@Autowired
	private CommentaryRepository commentaryRepository;

	@Autowired
	private VoteRepository voteRepository;

	@Override
	public CitizenRepository newCitizenRepository() {
		return citizenRepository;
	}

	@Override
	public ProposalRepository newProposalRepository() {
		return proposalRepository;
	}

	@Override
	public CommentaryRepository newCommentaryRepository() {
		return commentaryRepository;
	}

	@Override
	public VoteRepository newVoteRepository() {
		return voteRepository;
	}
}
