package es.uniovi.asw.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.model.types.status.EstadosComentario;

@Entity
@Table(name = "TCOMMENTARIES")
public class Commentary {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private Citizen citizen;

	@ManyToOne
	private Proposal proposal;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@NotNull
	private int valoration;

	@NotNull
	private String content;

	@Enumerated(EnumType.STRING)
	private EstadosComentario status;

	Commentary() {
	}

	public Commentary(Citizen citizen, Proposal proposal, String content)
			throws CitizenException {
		if (citizen == null)
			throw new CitizenException("Citizen is null");
		if (proposal == null)
			throw new CitizenException("Proposal is null");
		Calendar calendar = GregorianCalendar.getInstance();
		setCreationDate(new Date(calendar.getTimeInMillis()));
		setContent(content);
		setValoracion(0);
		setEstado(EstadosComentario.Correcto);
		Association.Comenta.link(citizen, this, proposal);
	}

	public Long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		status = EstadosComentario.Editado;
		this.content = content;
	}

	public int getValoracion() {
		return valoration;
	}

	public void setValoracion(int valoration) {
		this.valoration = valoration;
	}

	public EstadosComentario getEstado() {
		return status;
	}

	public void setEstado(EstadosComentario status) {
		this.status = status;
	}

	public void censurarComentario() {
		setEstado(EstadosComentario.Censurado);
	}

	public Proposal getProposal() {
		return proposal;
	}

	public void setProposal(Proposal proposal) {
		this.proposal = proposal;
	}

	public Citizen getCitizen() {
		return citizen;
	}

	public void setCitizen(Citizen citizen) {
		this.citizen = citizen;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Override
	public String toString() {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String reportDate = df.format(getCreationDate());
		return "Commentary [citizen=" + getCitizen() + ", proposal="
				+ getProposal() + ", creationDate=" + reportDate
				+ ", valoration=" + getValoracion() + ", content="
				+ getContent() + ", status=" + getEstado() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((citizen == null) ? 0 : citizen.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((proposal == null) ? 0 : proposal.hashCode());
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
		Commentary other = (Commentary) obj;
		if (citizen == null) {
			if (other.citizen != null)
				return false;
		} else if (!citizen.equals(other.citizen))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (proposal == null) {
			if (other.proposal != null)
				return false;
		} else if (!proposal.equals(other.proposal))
			return false;
		return true;
	}

}
