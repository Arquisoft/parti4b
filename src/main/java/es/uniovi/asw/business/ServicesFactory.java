package es.uniovi.asw.business;

import org.springframework.stereotype.Service;

@Service
public interface ServicesFactory {
	public CitizenService getCitizenService();

	public ProposalService getProposalService();

	public CommentaryService getCommentaryService();
}
