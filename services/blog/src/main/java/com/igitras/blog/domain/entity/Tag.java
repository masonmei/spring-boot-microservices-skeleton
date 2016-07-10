package com.igitras.blog.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
public class Tag extends AbstractPersistable<Long> {
    private static final long serialVersionUID = 1584436018024186949L;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private Set<Post> posts = new HashSet<>();

    public Tag() {
    }

    public Tag(Long id){
        setId(id);
    }

    public String getName() {
        return name;
    }

    public Tag setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Tag setDescription(String description) {
        this.description = description;
        return this;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public Tag setPosts(Set<Post> posts) {
        this.posts = posts;
        return this;
    }

    @Override
    protected void setId(Long id) {
        super.setId(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("description", description)
                .add("posts", posts)
                .toString();
    }
}
