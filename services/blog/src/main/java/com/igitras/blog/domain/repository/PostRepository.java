package com.igitras.blog.domain.repository;


import com.igitras.blog.domain.entity.Post;
import com.igitras.blog.domain.entity.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data JPA repository for the Post entity.
 */
public interface PostRepository extends JpaRepository<Post,Long> {

    @Modifying
    @Transactional
    @Query("update Post post set post.reads = post.reads + 1 where post.id = :id")
    void markViewed(@Param("id") Long id);

    Page<Post> findAllByStatusOrderByLastModifiedDateDesc(PostStatus status, Pageable pageable);

    Page<Post> findAllByStatusOrderByReadsDesc(PostStatus status, Pageable pageable);
}
