package com.igitras.blog.mvc.dto;

import com.google.common.base.MoreObjects;
import com.igitras.blog.domain.entity.Comment;

import java.time.ZonedDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by mason on 7/10/16.
 */
public class CommentDto {

    private Long id;
    private ZonedDateTime createTime;

    @NotNull
    private Long postId;

    @NotNull
    private String author;

    @NotNull
    private String authorEmail;

    private String authorUrl;

    private String authorIp;

    @NotNull
    @Size(min = 10)
    private String content;

    public CommentDto() {
    }

    public CommentDto(Long postId, String author, String authorEmail, String content) {
        setPostId(postId).setAuthor(author).setAuthorEmail(authorEmail).setContent(content);
    }

    public CommentDto(Long id, ZonedDateTime createTime, Long postId, String author, String authorEmail,
                      String authorUrl, String authorIp, String content) {
        setId(id).setCreateTime(createTime)
                .setPostId(postId)
                .setAuthor(author)
                .setAuthorEmail(authorEmail)
                .setAuthorUrl(authorUrl)
                .setAuthorIp(authorIp)
                .setContent(content);
    }

    public CommentDto(Comment source) {
        this(source.getId(), source.getCreatedDate(), source.getPostId(), source.getAuthor(), source.getAuthorEmail(),
                source.getAuthorUrl(), source.getAuthorIp(), source.getContent());
    }

    public Long getId() {
        return id;
    }

    public CommentDto setId(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public CommentDto setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public Long getPostId() {
        return postId;
    }

    public CommentDto setPostId(Long postId) {
        this.postId = postId;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public CommentDto setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public CommentDto setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
        return this;
    }

    public String getAuthorUrl() {
        return authorUrl;
    }

    public CommentDto setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
        return this;
    }

    public String getAuthorIp() {
        return authorIp;
    }

    public CommentDto setAuthorIp(String authorIp) {
        this.authorIp = authorIp;
        return this;
    }

    public String getContent() {
        return content;
    }

    public CommentDto setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("createTime", createTime)
                .add("postId", postId)
                .add("author", author)
                .add("authorEmail", authorEmail)
                .add("authorUrl", authorUrl)
                .add("authorIp", authorIp)
                .add("content", content)
                .toString();
    }
}
