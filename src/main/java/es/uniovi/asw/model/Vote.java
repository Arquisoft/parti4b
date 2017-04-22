package es.uniovi.asw.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Darkwind
 *
 */
@Entity
@Table(name = "TVOTES")
public class Vote {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private Citizen citizen;

	@ManyToOne
	private Proposal proposal;

	@Temporal(TemporalType.TIMESTAMP)
	private Date votingDate;

	Vote() {
	}

	public Vote(Citizen citizen, Proposal proposal) {
		Association.Vota.link(citizen, this, proposal);
		Calendar calendar = GregorianCalendar.getInstance();
		setVotingDate(new Date(calendar.getTimeInMillis()));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String reportDate = df.format(votingDate);
		return "Vote [citizen=" + citizen + ", proposal=" + proposal
				+ ", votingDate=" + reportDate + "]";
	}

}
