package com.igitras.domain.repository;

import com.igitras.domain.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author mason
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
