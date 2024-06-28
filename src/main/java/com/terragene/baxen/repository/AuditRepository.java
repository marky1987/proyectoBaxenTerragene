package com.terragene.baxen.repository;


import com.terragene.baxen.entity.AuditEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, Long> {

    @Query(value = "SELECT * FROM AUDIT_LOGS WHERE action_date  >  ?1 and informado = 0", nativeQuery = true)
    List<AuditEntity> findByCreatedAtAfter(LocalDateTime  timestamp);

    @Query(value = "SELECT count (*) FROM AUDIT_LOGS WHERE informado = 0 ", nativeQuery = true )
    int hasNewNotification();

    @Modifying
    @Transactional
    @Query(value = "UPDATE AUDIT_LOGS SET informado = 1 WHERE informado = 0", nativeQuery = true)
    int updateNotifications();
}
