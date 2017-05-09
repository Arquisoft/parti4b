package es.uniovi.asw.dbManagement;

public interface PersistenceFactory {

	public CitizenRepository newCitizenRepository();

	public ProposalRepository newProposalRepository();

	public CommentaryRepository newCommentaryRepository();

	public VoteRepository newVoteRepository();
}
