package es.uniovi.asw.persistence;

public interface PersistenceFactory {

	public CitizenRepository newCitizenRepository();

	public ProposalRepository newProposalRepository();

	public CommentaryRepository newCommentaryRepository();

	public VoteRepository newVoteRepository();
}
