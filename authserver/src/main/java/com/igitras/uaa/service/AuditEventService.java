package com.igitras.uaa.service;

import com.igitras.uaa.custom.AuditEventConverter;
import com.igitras.uaa.domain.repository.PersistentAuditEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author mason
 */
@Service
@Transactional
public class AuditEventService {


    @Autowired
    private PersistentAuditEventRepository persistentAuditEventRepository;

    @Autowired
    private AuditEventConverter auditEventConverter;

    public Page<AuditEvent> findAll(Pageable pageable) {
        return persistentAuditEventRepository.findAll(pageable)
                .map(persistentAuditEvents -> auditEventConverter.convertToAuditEvent(persistentAuditEvents));
    }

    public Page<AuditEvent> findByDates(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        return persistentAuditEventRepository.findAllByAuditEventDateBetween(fromDate, toDate, pageable)
                .map(persistentAuditEvents -> auditEventConverter.convertToAuditEvent(persistentAuditEvents));
    }

    public Optional<AuditEvent> find(Long id) {
        return Optional.ofNullable(persistentAuditEventRepository.findOne(id)).map
                (auditEventConverter::convertToAuditEvent);
    }
}
