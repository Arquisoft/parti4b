package es.uniovi.asw.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.uniovi.asw.model.Proposal;
import es.uniovi.asw.model.types.status.EstadosPropuesta;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
	@Query("Select a from Proposal a where a.id=?1")
	Proposal findByID(long id);

	@Query("Select a.proposal from Commentary a where a.citizen.dni=?1")
	Proposal findByDni(String dni);

	@Query("Select p from Proposal p where p.status=?1")
	List<Proposal> findByStatus(EstadosPropuesta status);
	
	@Query("Select p from Proposal p where p.name=?1")
	Proposal findByName(String name);

}
