package es.uniovi.asw.participationSystem;

import es.uniovi.asw.model.Commentary;
import es.uniovi.asw.model.exception.CitizenException;

import java.util.Date;
import java.util.List;

public interface CommentaryService {

	public List<Commentary> findAll();

	public Commentary findById(long id);

	public Commentary findByCreationDate(Date date);

	public Commentary findByCitizen(String dni);

	public void save(Long idCitizen, Long idProposal, String message)
			throws CitizenException;

	public List<Commentary> findByProposal(long id);

	public void update(Commentary comentario);

	public List<Commentary> findByProposalId(Long idPropuesta);

	void delete(Commentary c);

}
