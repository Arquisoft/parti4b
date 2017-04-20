package es.uniovi.asw.persistence;

import es.uniovi.asw.model.Vote;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
