package com.igitras.blog.domain.repository;


import com.igitras.blog.domain.entity.Blog;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Blog entity.
 */
public interface BlogRepository extends JpaRepository<Blog,Long> {
    @Query("select distinct blog from Blog blog left join fetch blog.tags")
    List<Blog> findAllWithEagerRelationships();

    @Query("select blog from Blog blog left join fetch blog.tags where blog.id =:id")
    Blog findOneWithEagerRelationships(@Param("id") Long id);

}
