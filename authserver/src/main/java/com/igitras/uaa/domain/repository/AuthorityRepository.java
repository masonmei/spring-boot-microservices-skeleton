package com.igitras.uaa.domain.repository;

import com.igitras.uaa.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mason
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
