package es.uniovi.asw.persistence.impl;

import es.uniovi.asw.persistence.CitizenRepository;
import es.uniovi.asw.persistence.CommentaryRepository;
import es.uniovi.asw.persistence.PersistenceFactory;
import es.uniovi.asw.persistence.ProposalRepository;
import es.uniovi.asw.persistence.VoteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;

@Service
@EnableJpaRepositories("es.uniovi.asw.persistence")
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
