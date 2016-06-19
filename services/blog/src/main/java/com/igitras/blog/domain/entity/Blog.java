package com.igitras.blog.domain.entity;


import com.google.common.base.MoreObjects;
import com.igitras.common.jpa.domain.entity.AuditingEntity;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Blog.
 */
@Entity
@Table(name = "blog")
public class Blog extends AuditingEntity<Long> {

    private static final long serialVersionUID = 566185494216983353L;

    @NotNull
    @Size(min = 5)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "content")
    private String content;

    @ManyToMany
    @JoinTable(name = "blog_tags",
            joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    public Blog() {
    }

    public Blog(Long id) {
        setId(id);
    }

    public String getTitle() {
        return title;
    }

    public Blog setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Blog setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Blog setContent(String content) {
        this.content = content;
        return this;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Blog setTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("author", author)
                .add("content", content)
                .add("tags", tags)
                .toString();
    }
}
