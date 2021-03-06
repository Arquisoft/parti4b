package es.uniovi.asw.participationSystem.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uniovi.asw.participationSystem.CitizenService;
import es.uniovi.asw.participationSystem.CommentaryService;
import es.uniovi.asw.participationSystem.ProposalService;
import es.uniovi.asw.participationSystem.ServicesFactory;
import es.uniovi.asw.participationSystem.VoteService;

@Service
public class ServicesFactoryImpl implements ServicesFactory {

	@Autowired
	private CitizenServiceImpl citizenService;

	@Autowired
	private ProposalServiceImpl proposalService;

	@Autowired
	private CommentaryServiceImpl commentaryService;

	@Autowired
	private VoteServiceImpl voteService;

	@Override
	public CitizenService getCitizenService() {
		return citizenService;
	}

	@Override
	public ProposalService getProposalService() {
		return proposalService;
	}

	@Override
	public CommentaryService getCommentaryService() {
		return commentaryService;
	}

	@Override
	public VoteService getVoteService() {
		return voteService;
	}
}
