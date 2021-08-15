package com.jinu.interview.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jinu.interview.model.Candidate;
@Repository
public interface CandidateRepository extends JpaRepository<Candidate, Long> {
	@Query("SELECT c FROM Candidate c WHERE c.status = :status")
    public List<Candidate> getCandidateByStatus(@Param("status") String status);
}
