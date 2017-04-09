package es.uniovi.asw.business.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uniovi.asw.business.ProposalService;
import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.types.status.EstadosPropuesta;
import es.uniovi.asw.persistence.ProposalRepository;

@Service
public class ProposalServiceImpl implements ProposalService {
	@Autowired
	private ProposalRepository propuestaRepository;

	@Override
	public List<Proposal> findAll() {
		return propuestaRepository.findAll();
	}

	@Override
	public Proposal findById(long id) {
		return propuestaRepository.findByID(id);
	}

	@Override
	public Proposal findByCitizen(String dni) {
		return propuestaRepository.findByDni(dni);
	}

	@Override
	public void save(Proposal propuesta) {
		propuestaRepository.save(propuesta);
	}

	@Override
	public void update(Proposal propuesta) {
		propuestaRepository.save(propuesta);

	}

	@Override
	public List<Proposal> findByStatus(EstadosPropuesta estado) {
		return propuestaRepository.findByStatus(estado);
	}

	@Override
	public Proposal findByName(String name) {
		return propuestaRepository.findByName(name);
	}

	@Override
	public int count() {
		return (int) propuestaRepository.count();
	}

}
