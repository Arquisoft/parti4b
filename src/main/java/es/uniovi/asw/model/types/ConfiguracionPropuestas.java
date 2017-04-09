package es.uniovi.asw.model.types;

import java.util.*;

import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.types.status.EstadosPropuesta;

public class ConfiguracionPropuestas {

	public List<Proposal> proposals = new ArrayList<Proposal>();

	public List<Proposal> getPropuestas() {
		// Tiene que devolver una lista con todas las propuestas que hay en la
		// base de datos
		return null;
	}

	public void establishMinVotes(int minVotes) {
		for (Proposal proposal : proposals) {
			proposal.setMinVotes(minVotes);
		}
	}

	public void acceptProposals() {
		for (Proposal proposal : proposals) {
			proposal.acceptProposal();
		}
	}

	public void refuseProposals() {
		for (Proposal proposal : proposals) {
			proposal.refuseProposal();
		}
	}

	public void cancelProposal() {
		for (Proposal proposal : proposals) {
			proposal.cancelProposal();
		}
	}

	public void restoreEstadoPropuestas() {
		for (Proposal proposal : proposals) {
			proposal.restoreEstadoPropuesta();
		}
	}

	public List<Proposal> getPropuestasAceptadas() {
		List<Proposal> accepted = new ArrayList<Proposal>();
		for (Proposal proposal : proposals) {
			if (proposal.getStatus().equals(EstadosPropuesta.Aceptada)) {
				accepted.add(proposal);
			}
		}
		return accepted;
	}

	public List<Proposal> getPropuestasEnTramite() {
		List<Proposal> enTramite = new ArrayList<Proposal>();
		for (Proposal proposal : proposals) {
			if (proposal.getStatus().equals(EstadosPropuesta.EnTramite)) {
				enTramite.add(proposal);
			}
		}
		return enTramite;
	}

	public List<Proposal> getPropuestasRechzadas() {
		List<Proposal> rechazadas = new ArrayList<Proposal>();
		for (Proposal proposal : proposals) {
			if (proposal.getStatus().equals(EstadosPropuesta.Rechazada)) {
				rechazadas.add(proposal);
			}
		}
		return rechazadas;
	}

	public List<Proposal> getPropuestasAnuladas() {
		List<Proposal> anuladas = new ArrayList<Proposal>();
		for (Proposal proposal : proposals) {
			if (proposal.getStatus().equals(EstadosPropuesta.Anulada)) {
				anuladas.add(proposal);
			}
		}
		return anuladas;
	}
}
