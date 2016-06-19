package com.igitras.blog.domain.repository;


import com.igitras.blog.domain.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

}
