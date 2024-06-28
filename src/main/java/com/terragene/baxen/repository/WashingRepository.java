package com.terragene.baxen.repository;


import com.terragene.baxen.entity.WasherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WashingRepository extends JpaRepository<WasherEntity, Long> {

    /*filtrar por ticketNumber*/
    @Query(value = "SELECT * FROM RESULTS_WASHER WHERE creation_test = ?1", nativeQuery = true)
    List<WasherEntity> searchByCreationTest(String creationTest);

}
