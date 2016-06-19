package com.igitras.blog.mvc.dto;

import com.igitras.blog.domain.entity.Blog;
import com.igitras.blog.domain.entity.Tag;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * A DTO for the Blog entity.
 */
public class BlogDto implements Serializable {

    private static final long serialVersionUID = 8364492241300619722L;

    private Long id;

    @NotNull
    @Size(min = 5)
    private String title;

    @NotNull
    private String author;

    private String content;

    private Set<TagDto> tags = new HashSet<>();

    public BlogDto() {
    }

    public BlogDto(Blog blog) {
        this(blog.getId(), blog.getTitle(), blog.getAuthor(), blog.getContent(), blog.getTags()
                .stream()
                .map(TagDto::new)
                .collect(Collectors.toSet()));
    }

    public BlogDto(Long id, String title, String author, String content, Set<TagDto> tags) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public BlogDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public BlogDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public BlogDto setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public BlogDto setContent(String content) {
        this.content = content;
        return this;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public BlogDto setTags(Set<TagDto> tags) {
        this.tags = tags;
        return this;
    }
}
