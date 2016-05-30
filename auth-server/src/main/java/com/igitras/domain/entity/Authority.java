package com.igitras.domain.entity;

import com.igitras.common.domain.entity.PersistEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;

/**
 * @author mason
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Authority extends PersistEntity<String> implements GrantedAuthority {

    @Override
    public String getAuthority() {
        return getId();
    }
}
