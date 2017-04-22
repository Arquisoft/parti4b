package es.uniovi.asw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import es.uniovi.asw.model.types.status.EstadosPropuesta;

@Entity
@Table(name = "TPROPOSAL")
public class Proposal {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	private String content;

	@NotNull
	private int valoration;

	@NotNull
	private int minVotes;

	@Enumerated(EnumType.STRING)
	private EstadosPropuesta status;

	@OneToMany(cascade = { CascadeType.REFRESH,
			CascadeType.REMOVE }, mappedBy = "proposal", fetch = FetchType.EAGER)
	private List<Commentary> comments = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.REFRESH,
			CascadeType.REMOVE }, mappedBy = "proposal", fetch = FetchType.EAGER)
	private Set<Vote> votes = new HashSet<Vote>();

	public Proposal() {
	}

	public Proposal(String name, String content, int minVotes) {
		setName(name);
		setContent(content);
		setMinVotes(minVotes);
		setStatus(EstadosPropuesta.EnTramite);
		setValoration(0);
	}

	public EstadosPropuesta getStatus() {
		return status;
	}

	public void setStatus(EstadosPropuesta status) {
		this.status = status;
	}

	public void restoreEstadoPropuesta() {
		status = EstadosPropuesta.EnTramite;
	}

	public void refuseProposal() {
		status = EstadosPropuesta.Rechazada;
	}

	public void acceptProposal() {
		status = EstadosPropuesta.Aceptada;
	}

	public void cancelProposal() {
		status = EstadosPropuesta.Anulada;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Commentary> getComments() {
		return comments;
	}

	public int getValoration() {
		return valoration;
	}

	public int getMinVotes() {
		return minVotes;
	}

	public void setMinVotes(int minVotes) {
		this.minVotes = minVotes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean positiveVote(Citizen citizen) {
		if (comprobarVotacion(citizen))
			return false;
		this.valoration++;
		return true;
	}

	public boolean negativeVote(Citizen citizen) {
		if (comprobarVotacion(citizen))
			return false;
		this.valoration--;
		return true;
	}

	private boolean comprobarVotacion(Citizen citizen) {
		for (Vote v : votes)
			if (v.getCitizen().equals(citizen))
				return true;
		votes.add(new Vote(citizen, this));
		return false;
	}

	public Set<Vote> getVotes() {
		return votes;
	}

	public void setValoration(int valoration) {
		this.valoration = valoration;
	}

	@Override
	public String toString() {
		String cadena = "La propuesta: '" + getName() + "' tiene un total de "
				+ getValoration() + " votos y " + getComments().size()
				+ " comments.\n";
		return cadena;
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
		Proposal other = (Proposal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
