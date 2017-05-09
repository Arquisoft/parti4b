package es.uniovi.asw.dbManagement;

import es.uniovi.asw.model.Commentary;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentaryRepository extends JpaRepository<Commentary, Long> {
	@Query("Select c from Commentary c where c.id=?1")
	Commentary findByID(long id);

	@Query("Select c from Commentary c where c.citizen.dni=?1")
	Commentary findByDni(String dni);

	@Query("Select c from Commentary c where c.proposal.id=?1")
	List<Commentary> findByProposalId(long id);

	@Query("Select c from Commentary c where c.proposal.id=?1 and c.status='Correcto'")
	List<Commentary> findByPorposal(long id);

	@Query("Select c from Commentary c where c.creationDate=?1")
	Commentary findByCreationDate(Date date);
}
