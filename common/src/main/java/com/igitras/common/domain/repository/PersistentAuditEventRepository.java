package com.igitras.common.domain.repository;

import com.igitras.common.domain.entity.PersistentAuditEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * @author mason
 */
public interface PersistentAuditEventRepository extends JpaRepository<PersistentAuditEvent, Long> {

    Page<PersistentAuditEvent> findByPrincipal(String principal, Pageable pageable);

    Page<PersistentAuditEvent> findByPrincipalAndAuditEventDateAfter(String principal, LocalDateTime after,
            Pageable pageable);

    Page<PersistentAuditEvent> findAllByAuditEventDateBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);
}
