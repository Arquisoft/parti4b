package es.uniovi.asw.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.uniovi.asw.model.types.keys.VotesKey;

/**
 * @author Darkwind
 *
 */
@Entity
@IdClass(VotesKey.class)
@Table(name = "TVOTES")
public class Vote {

	@Id
	@ManyToOne
	private Citizen citizen;

	@Id
	@ManyToOne
	private Proposal proposal;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date votingDate;
	
	Vote() {}
	
	public Vote(Citizen citizen, Proposal proposal) {
		Association.Vota.link(citizen, this, proposal);
		Calendar calendar = GregorianCalendar.getInstance();
		this.votingDate = new Date(calendar.getTimeInMillis());
	}

	public Citizen getCitizen() {
		return citizen;
	}

	public void setCitizen(Citizen citizen) {
		this.citizen = citizen;
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Date getVotingDate() {
		return votingDate;
	}

	public void setVotingDate(Date votingDate) {
		this.votingDate = votingDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((citizen == null) ? 0 : citizen.hashCode());
		result = prime * result + ((proposal == null) ? 0 : proposal.hashCode());
		result = prime * result + ((votingDate == null) ? 0 : votingDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vote other = (Vote) obj;
		if (citizen == null) {
			if (other.citizen != null)
				return false;
		} else if (!citizen.equals(other.citizen))
			return false;
		if (proposal == null) {
			if (other.proposal != null)
				return false;
		} else if (!proposal.equals(other.proposal))
			return false;
		if (votingDate == null) {
			if (other.votingDate != null)
				return false;
		} else if (!votingDate.equals(other.votingDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Vote [citizen=" + citizen + ", proposal=" + proposal + ", votingDate=" + votingDate + "]";
	}
	
	

	
	
}
