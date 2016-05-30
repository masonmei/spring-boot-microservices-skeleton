package com.igitras.domain.search;

import com.igitras.domain.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author mason
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {
}
