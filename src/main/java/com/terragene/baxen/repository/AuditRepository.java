package com.terragene.baxen.repository;


import com.terragene.baxen.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, Long> {

    @Query(value = "SELECT * FROM AUDIT_LOGS WHERE action_date  >  ?1", nativeQuery = true)
    List<AuditEntity> findByCreatedAtAfter(LocalDateTime  timestamp);
}
