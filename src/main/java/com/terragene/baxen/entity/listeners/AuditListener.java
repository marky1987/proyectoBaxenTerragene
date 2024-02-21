package com.terragene.baxen.entity.listeners;


import com.terragene.baxen.entity.AuditEntity;
import com.terragene.baxen.helper.BeanUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class AuditListener {
    @PrePersist
    public void prePersist(Object object) {
        if (object instanceof Auditable) {
            saveAudit(object, "INSERT");
        }
    }

    private void saveAudit(Object object, String action) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        AuditEntity auditLog = new AuditEntity();
        auditLog.setEntityName(((Auditable) object).getEntity());
        auditLog.setAction(action);
        auditLog.setActionDate(now.format(formatter));
        auditLog.setModelo(((Auditable) object).getModelName());
        auditLog.setNroSerie(((Auditable) object).getSerialNumber());
        auditLog.setUsername(((Auditable) object).getUserName());

        EntityManager em = BeanUtil.getBean(EntityManager.class);
        em.persist(auditLog);
    }
}
