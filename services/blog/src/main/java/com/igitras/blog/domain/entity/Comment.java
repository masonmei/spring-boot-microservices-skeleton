package com.igitras.blog.domain.entity;

import com.google.common.base.MoreObjects;
import com.igitras.common.jpa.domain.entity.AuditingEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by mason on 7/10/16.
 */
@Entity
@Table(name = "comment")
public class Comment extends AuditingEntity<Long> {

    @NotNull
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "author_email", nullable = false)
    private String authorEmail;

    @Column(name = "author_url")
    private String authorUrl;

    @Column(name = "author_ip")
    private String authorIp;

    @Column(name = "content", length = 512)
    private String content;

    public Comment() {
    }

    public Comment(Long id) {
        setId(id);
    }

    public Long getPostId() {
        return postId;
    }

    public Comment setPostId(Long postId) {
        this.postId = postId;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public Comment setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public Comment setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
        return this;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public Comment setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    public String getAuthorIp() {
        return authorIp;
    }

    public Comment setAuthorIp(String authorIp) {
        this.authorIp = authorIp;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Comment setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("postId", postId)
                .add("author", author)
                .add("authorEmail", authorEmail)
                .add("authorUrl", authorUrl)
                .add("authorIp", authorIp)
                .add("content", content)
                .toString();
    }
}
