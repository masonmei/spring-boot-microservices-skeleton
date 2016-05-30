package com.igitras.common.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author mason
 */
@MappedSuperclass
public abstract class PersistEntity<PK extends Serializable> implements Persistable<PK> {
    private static final long serialVersionUID = 6974310341999248672L;

    @Id
    private PK id;

    public PK getId() {
        return id;
    }

    @Override
    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

    public void setId(PK id) {
        this.id = id;
    }
}
