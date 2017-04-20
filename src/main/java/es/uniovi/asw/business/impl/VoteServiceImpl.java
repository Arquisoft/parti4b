package es.uniovi.asw.business.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uniovi.asw.business.VoteService;
import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.Vote;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private Factories factories;

	@Override
	public void save(Long idCitizen, Long idProposal) {
		Citizen citizen = factories.getServicesFactory().getCitizenService()
				.findById(idCitizen);
		Proposal proposal = factories.getServicesFactory().getProposalService()
				.findById(idProposal);
		Vote vote = new Vote(citizen, proposal);
		factories.getPersistenceFactory().newVoteRepository().save(vote);
	}

}
