package es.uniovi.asw.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.uniovi.asw.business.CitizenService;
import es.uniovi.asw.business.CommentaryService;
import es.uniovi.asw.business.ProposalService;
import es.uniovi.asw.business.ServicesFactory;

@Service
public class ServicesFactoryImpl implements ServicesFactory {

	@Autowired
	private CitizenServiceImpl citizenService;

	@Autowired
	private ProposalServiceImpl proposalService;

	@Autowired
	private CommentaryServiceImpl commentaryService;

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
}
