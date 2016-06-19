package com.igitras.blog.mvc.resource;

import static org.springframework.util.CollectionUtils.isEmpty;

import static java.util.stream.Collectors.toList;

import com.codahale.metrics.annotation.Timed;
import com.igitras.blog.domain.entity.Blog;
import com.igitras.blog.domain.entity.Tag;
import com.igitras.blog.domain.repository.BlogRepository;
import com.igitras.blog.mvc.dto.BlogDto;
import com.igitras.common.utils.HeaderUtil;
import com.igitras.common.utils.PaginationUtil;
import com.igitras.common.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * REST controller for managing Blog.
 */
@RestController
@RequestMapping("/api/blogs")
public class BlogResource {

    private final Logger log = LoggerFactory.getLogger(BlogResource.class);

    @Autowired
    private BlogRepository blogRepository;

    /**
     * POST  /blogs : Create a new blog.
     *
     * @param blogDto the blogDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new blogDto, or with status 400 (Bad Request) if the blog has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BlogDto> createBlog(@Valid @RequestBody BlogDto blogDto) throws URISyntaxException {
        log.debug("REST request to save Blog : {}", blogDto);
        if (blogDto.getId() != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("blog", "idexists", "A new blog cannot already have an ID"))
                    .body(null);
        }
        Blog blog = new Blog(blogDto.getId()).setAuthor(SecurityUtils.getCurrentUserLogin())
                .setTitle(blogDto.getTitle())
                .setContent(blogDto.getContent())
                .setTags(blogDto.getTags()
                        .stream()
                        .map(tagDto -> new Tag(tagDto.getId()))
                        .collect(Collectors.toSet()));
        blog = blogRepository.save(blog);
        BlogDto result = new BlogDto(blog);
        return ResponseEntity.created(new URI("/api/blogs/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("blog", result.getId()
                        .toString()))
                .body(result);
    }

    /**
     * PUT  /blogs : Updates an existing blog.
     *
     * @param blogDto the blogDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated blogDto,
     * or with status 400 (Bad Request) if the blogDto is not valid,
     * or with status 500 (Internal Server Error) if the blogDto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BlogDto> updateBlog(@PathVariable("id") Long id, @Valid @RequestBody BlogDto blogDto)
            throws URISyntaxException {
        Assert.state(id.equals(blogDto.getId()) || blogDto.getId() == null);
        log.debug("REST request to update Blog : {}", blogDto);

        Blog blog = new Blog(id).setAuthor(blogDto.getAuthor())
                .setTitle(blogDto.getTitle())
                .setContent(blogDto.getContent())
                .setTags(blogDto.getTags()
                        .stream()
                        .map(tagDto -> new Tag(tagDto.getId()))
                        .collect(Collectors.toSet()));
        blog = blogRepository.save(blog);
        BlogDto result = new BlogDto(blog);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("blog", blogDto.getId()
                        .toString()))
                .body(result);
    }

    /**
     * GET  /blogs : get all the blogs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of blogs in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BlogDto>> getAllBlogs(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Blogs");
        Page<Blog> page = blogRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/blogs");

        List<BlogDto> collect = new ArrayList<>();
        if (!isEmpty(page.getContent())) {
            collect = page.getContent()
                    .stream()
                    .map(BlogDto::new)
                    .collect(toList());
        }
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * GET  /blogs/:id : get the "id" blog.
     *
     * @param id the id of the blogDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the blogDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BlogDto> getBlog(@PathVariable Long id) {
        log.debug("REST request to get Blog : {}", id);
        Blog blog = blogRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(blog)
                .map(result -> new ResponseEntity<>(new BlogDto(result), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /blogs/:id : delete the "id" blog.
     *
     * @param id the id of the blogDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        log.debug("REST request to delete Blog : {}", id);
        blogRepository.delete(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert("blog", id.toString()))
                .build();
    }

}
