package com.igitras.blog.domain.entity;


import static com.igitras.blog.domain.entity.PostStatus.SAVED;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

import com.google.common.base.MoreObjects;
import com.igitras.common.jpa.domain.entity.AuditingEntity;
import org.springframework.context.annotation.Lazy;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Post.
 */
@Entity
@Table(name = "post")
public class Post extends AuditingEntity<Long> {

    private static final long serialVersionUID = 566185494216983353L;

    @NotNull
    @Size(min = 5)
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "summary")
    @Basic
    @Lob
    private String summary;

    @Column(name = "content")
    @Basic(fetch = LAZY)
    @Lob
    private String content;

    @ManyToMany(fetch = EAGER)
    @JoinTable(name = "blog_tags",
            joinColumns = @JoinColumn(name = "blog_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tags = new HashSet<>();

    /* Blog lifecycle */
    @Basic
    @Enumerated(value = STRING)
    @Column(name = "`status`")
    private PostStatus status = SAVED;

    /* Reading times, when the reader open the reading page and this is add 1. */
    @Column(name = "`reads`")
    private int reads;

    public Post() {
    }

    public Post(Long id) {
        setId(id);
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Post setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public Post setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Post setContent(String content) {
        this.content = content;
        return this;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Post setTags(Set<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public PostStatus getStatus() {
        return status;
    }

    public Post setStatus(PostStatus status) {
        this.status = status;
        return this;
    }

    public int getReads() {
        return reads;
    }

    public Post setReads(int reads) {
        this.reads = reads;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", title)
                .add("author", author)
                .add("summary", summary)
                .add("content", content)
                .add("tags", tags)
                .add("status", status)
                .add("reads", reads)
                .toString();
    }
}
