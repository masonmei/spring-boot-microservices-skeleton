package com.igitras.blog.mvc.dto;

import static com.igitras.blog.domain.entity.PostStatus.SAVED;

import com.igitras.blog.domain.entity.Post;
import com.igitras.blog.domain.entity.PostStatus;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * A DTO for the Post entity.
 */
public class PostDto implements Serializable {

    private static final long serialVersionUID = 8364492241300619722L;

    private Long id;

    @NotNull
    @Size(min = 5)
    private String title;
    private String author;
    private String summary;
    private String content;
    private PostStatus status = SAVED;
    private int reads = 0;
    private Set<TagDto> tags = new HashSet<>();
    private ZonedDateTime createTime;
    private ZonedDateTime updateTime;

    private Page<CommentDto> comments;

    public PostDto() {
    }

    public PostDto(Post post) {
        this(post.getId(), post.getTitle(), post.getAuthor(), post.getSummary(), post.getContent(), post.getStatus(),
                post.getReads(), post.getTags().stream().map(TagDto::new).collect(Collectors.toSet()),
                post.getCreatedDate(), post.getLastModifiedDate());
    }

    public PostDto(Long id, String title, String author, String summary, String content,
                   PostStatus status, int reads, Set<TagDto> tags, ZonedDateTime createTime,
                   ZonedDateTime updateTime) {
        setId(id).setTitle(title)
                .setAuthor(author)
                .setSummary(summary)
                .setContent(content)
                .setStatus(status)
                .setReads(reads)
                .setTags(tags)
                .setCreateTime(createTime)
                .setUpdateTime(updateTime);
    }

    public Long getId() {
        return id;
    }

    public PostDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public PostDto setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getSummary() {
        return summary;
    }

    public PostDto setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public String getContent() {
        return content;
    }

    public PostDto setContent(String content) {
        this.content = content;
        return this;
    }

    public PostStatus getStatus() {
        return status;
    }

    public PostDto setStatus(PostStatus status) {
        this.status = status;
        return this;
    }

    public int getReads() {
        return reads;
    }

    public PostDto setReads(int reads) {
        this.reads = reads;
        return this;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public PostDto setTags(Set<TagDto> tags) {
        this.tags = tags;
        return this;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public PostDto setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
        return this;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public PostDto setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public Page<CommentDto> getComments() {
        return comments;
    }

    public PostDto setComments(Page<CommentDto> comments) {
        this.comments = comments;
        return this;
    }
}
