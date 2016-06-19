package com.igitras.blog.mvc.dto;

import com.igitras.blog.domain.entity.Tag;

import java.io.Serializable;


/**
 * A DTO for the Tag entity.
 */
public class TagDto implements Serializable {

    private static final long serialVersionUID = -8551498102107198534L;

    private Long id;

    private String name;

    private String description;

    public TagDto() {
    }

    public TagDto(Tag tag) {
        this(tag.getId(), tag.getName(), tag.getDescription());
    }

    public TagDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public TagDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public TagDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TagDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
