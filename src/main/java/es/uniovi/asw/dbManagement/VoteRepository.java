package es.uniovi.asw.dbManagement;

import es.uniovi.asw.model.Vote;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
