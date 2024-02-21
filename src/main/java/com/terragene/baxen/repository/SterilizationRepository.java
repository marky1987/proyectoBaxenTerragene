package com.terragene.baxen.repository;

import com.terragene.baxen.entity.SterilizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SterilizationRepository extends JpaRepository<SterilizationEntity, Long> {

    @Query(value = "SELECT * FROM RESULTS_STERILIZATION WHERE ticket_number = ?1", nativeQuery = true)
    List<SterilizationEntity> searchByTicketNumber(String ticketNumber);
}
