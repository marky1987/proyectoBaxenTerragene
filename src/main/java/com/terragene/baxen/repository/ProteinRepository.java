package com.terragene.baxen.repository;


import com.terragene.baxen.entity.ProteinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProteinRepository extends JpaRepository<ProteinEntity, Long> {
    @Query(value = "SELECT * FROM RESULTS_PROTEIN WHERE ticket_number = ?1 ", nativeQuery = true)
    List<ProteinEntity> searchByTicketNumber(String ticketNumber);
}
