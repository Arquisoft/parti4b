package es.uniovi.asw.model;

public class Association {
	public static class Comenta {
		public static void link(Citizen citizen, Commentary commentary,
				Proposal proposal) {
			commentary.setCitizen(citizen);
			commentary.setProposal(proposal);
			citizen.getComentarios().add(commentary);
			proposal.getComments().add(commentary);
		}

		public static void unlink(Commentary commentary) {
			commentary.getCitizen().getComentarios().remove(commentary);
			commentary.getProposal().getComments().remove(commentary);
			commentary.setCitizen(null);
			commentary.setProposal(null);
		}
	}
	
	public static class Vota {
		public static void link(Citizen citizen, Vote vote, Proposal proposal) {
			vote.setCitizen(citizen);
			vote.setProposal(proposal);
			citizen._getVotes().add(vote);
			proposal._getVotes().add(vote);
		}
		
		public static void unlink(Vote vote) {
			vote.getCitizen()._getVotes().remove(vote);
			vote.getProposal()._getVotes().remove(vote);
			vote.setCitizen(null);
			vote.setProposal(null);
		}

	}

}
