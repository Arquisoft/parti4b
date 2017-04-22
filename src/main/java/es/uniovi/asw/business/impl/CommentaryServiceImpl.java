package es.uniovi.asw.business.impl;

import es.uniovi.asw.business.CommentaryService;
import es.uniovi.asw.conf.Factories;
import es.uniovi.asw.model.Association;
import es.uniovi.asw.model.Citizen;
import es.uniovi.asw.model.Commentary;
import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.exception.CitizenException;
import es.uniovi.asw.persistence.CommentaryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class CommentaryServiceImpl implements CommentaryService {
	@Autowired
	private CommentaryRepository comentarioRepository;

	@Autowired
	private Factories factories;

	@Override
	public List<Commentary> findAll() {
		return comentarioRepository.findAll();
	}

	@Override
	public Commentary findById(long id) {
		return comentarioRepository.findByID(id);
	}

	@Override
	public Commentary findByCitizen(String dni) {
		return comentarioRepository.findByDni(dni);
	}

	@Override
	// Métodos @Transactional debe ir en @Service
	public void save(Long idCitizen, Long idProposal, String message) throws CitizenException {
		Citizen citizen = factories.getServicesFactory().getCitizenService()
				.findById(idCitizen);
		Proposal proposal = factories.getServicesFactory().getProposalService()
				.findById(idProposal);
		Commentary comment = new Commentary(citizen, proposal, message);
		comentarioRepository.save(comment);
	}

	@Override
	public void delete(Commentary commentary) {
		Association.Comenta.unlink(commentary);
		comentarioRepository.delete(commentary);
	}

	@Override
	public List<Commentary> findByProposal(long id) {
		return comentarioRepository.findByPorposal(id);
	}

	@Override
	public Commentary findByCreationDate(Date date) {
		return comentarioRepository.findByCreationDate(date);
	}

	@Override
	public void update(Commentary comentario) {
		comentarioRepository.save(comentario);
	}

	@Override
	public List<Commentary> findByProposalId(Long idPropuesta) {
		return comentarioRepository.findByProposalId(idPropuesta);
	}
}