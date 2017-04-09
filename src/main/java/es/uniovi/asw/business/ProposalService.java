package es.uniovi.asw.business;

import java.util.List;

import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.types.status.EstadosPropuesta;

public interface ProposalService {

	public List<Proposal> findAll();

	public Proposal findById(long id);

	public Proposal findByCitizen(String dni);

	public Proposal findByName(String name);

	public void save(Proposal propuesta);

	public void update(Proposal propuesta);

	public List<Proposal> findByStatus(EstadosPropuesta estado);

	public int count();

}
