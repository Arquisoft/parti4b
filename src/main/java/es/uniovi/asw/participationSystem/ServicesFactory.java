package es.uniovi.asw.participationSystem;

import org.springframework.stereotype.Service;

@Service
public interface ServicesFactory {
	public CitizenService getCitizenService();

	public ProposalService getProposalService();

	public CommentaryService getCommentaryService();

	public VoteService getVoteService();
}
