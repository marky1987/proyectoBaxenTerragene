package com.terragene.baxen.repository;

import com.terragene.baxen.entity.SterilizerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SterilizerRepository extends JpaRepository<SterilizerEntity, Long> {
    @Query(value = "SELECT * FROM RESULTS_STERILIZER WHERE creation_test = ?1 ", nativeQuery = true)
    List<SterilizerEntity> searchByCreationTest(String creationTest);
}
