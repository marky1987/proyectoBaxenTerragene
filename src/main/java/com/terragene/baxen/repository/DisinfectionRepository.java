package com.terragene.baxen.repository;

import com.terragene.baxen.entity.DisinfectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisinfectionRepository extends JpaRepository<DisinfectionEntity, Long> {

    @Query(value = "SELECT * FROM RESULTS_DISINFECTION WHERE ticket_number = ?1", nativeQuery = true)
    List<DisinfectionEntity> searchByTicketNumber(String ticketNumber);
}
