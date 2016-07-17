package com.igitras.blog.mvc.resource;

import static com.igitras.common.jpa.utils.PageUtil.firstPage;

import com.codahale.metrics.annotation.Timed;
import com.igitras.blog.domain.entity.Post;
import com.igitras.blog.domain.entity.PostStatus;
import com.igitras.blog.domain.entity.Tag;
import com.igitras.blog.domain.repository.CommentRepository;
import com.igitras.blog.domain.repository.PostRepository;
import com.igitras.blog.mvc.dto.CommentDto;
import com.igitras.blog.mvc.dto.PostDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;

/**
 * REST controller for managing Post.
 *
 * @author mason
 */
@RestController
@RequestMapping("/api/posts")
public class PostResource {

    private final Logger log = LoggerFactory.getLogger(PostResource.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Create a new blog post.
     *
     * @param postDto the postDto to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new postDto, or with status
     * 400 (Bad Request) if the blog has already an ID
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostDto> createBlog(@Valid @RequestBody PostDto postDto) throws URISyntaxException {
        log.debug("REST request to save Post : {}", postDto);
        if (postDto.getId() != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("post", "idexists", "A new post cannot already have an ID"))
                    .body(null);
        }
        Post post = new Post(postDto.getId()).setAuthor(SecurityUtils.getCurrentUserLogin())
                .setTitle(postDto.getTitle())
                .setContent(postDto.getContent())
                .setReads(postDto.getReads())
                .setStatus(postDto.getStatus())
                .setTags(postDto.getTags().stream().map(tagDto -> new Tag(tagDto.getId())).collect(Collectors.toSet()));
        post = postRepository.save(post);
        PostDto result = new PostDto(post);
        return ResponseEntity.created(new URI("/api/posts/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("post", result.getId()
                        .toString()))
                .body(result);
    }

    /**
     * Updates an existing podt.
     *
     * @param postDto the postDto to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated postDto,
     * or with status 400 (Bad Request) if the postDto is not valid,
     * or with status 500 (Internal Server Error) if the postDto could not be updated
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.PUT,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostDto> updateBlog(@PathVariable("id") Long id, @Valid @RequestBody PostDto postDto)
            throws URISyntaxException {
        Assert.state(id.equals(postDto.getId()) || postDto.getId() == null);
        log.debug("REST request to update Post : {}", postDto);

        Post post = new Post(postDto.getId()).setAuthor(SecurityUtils.getCurrentUserLogin())
                .setTitle(postDto.getTitle())
                .setContent(postDto.getContent())
                .setSummary(postDto.getSummary())
                .setReads(postDto.getReads())
                .setStatus(postDto.getStatus())
                .setTags(postDto.getTags().stream().map(tagDto -> new Tag(tagDto.getId())).collect(Collectors.toSet()));
        post = postRepository.save(post);
        PostDto result = new PostDto(post);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("post", postDto.getId().toString()))
                .body(result);
    }

    /**
     * GET  /blogs : get all the blogs.
     *
     * @param pageable the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of blogs in body
     *
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<PostDto>> getAllPosts(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Posts");
        Page<Post> page = postRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts");

        Page<PostDto> collect = page.map(PostDto::new);
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * Get the most recently posted posts.
     *
     * @param pageable pageable.
     *
     * @return most recently posted posts.
     *
     * @throws URISyntaxException
     */
    @RequestMapping(value = "mine", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<PostDto>> geAllMyPosts(Principal principal, Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of all my Posts");
        Page<Post> page = postRepository.findAllByAuthor(principal.getName(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts/mine");

        Page<PostDto> collect = page.map(PostDto::new);
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * Get the most recently posted posts.
     *
     * @param pageable pageable.
     *
     * @return most recently posted posts.
     *
     * @throws URISyntaxException
     */
    @RequestMapping(value = "latest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<PostDto>> geLatestPosts(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of most recently Posts");
        Page<Post> page = postRepository.findAllByStatusOrderByLastModifiedDateDesc(PostStatus.PUBLISHED, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts/latest");

        Page<PostDto> collect = page.map(PostDto::new);
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * Get the most populated posts.
     *
     * @param pageable pageable
     *
     * @return most populated posts.
     *
     * @throws URISyntaxException
     */
    @RequestMapping(value = "popular", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<PostDto>> getPopulatesPosts(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of populates Posts");
        Page<Post> page = postRepository.findAllByStatusOrderByReadsDesc(PostStatus.PUBLISHED, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts/populated");

        Page<PostDto> collect = page.map(PostDto::new);
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * Get the "id" post.
     *
     * @param id the id of the postDto to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the postDto, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PostDto> getBlog(@PathVariable Long id) {
        log.debug("REST request to get Post : {}", id);
        Post post = postRepository.findOne(id);
        postRepository.markViewed(id);
        return Optional.ofNullable(post)
                .map(result -> {
                    PostDto postDto = new PostDto(result);
                    postDto.setComments(commentRepository.findAllByPostId(id, firstPage()).map(CommentDto::new));
                    return new ResponseEntity<>(postDto, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * delete the "id" post.
     *
     * @param id the id of the postDto to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/{id}",
                    method = RequestMethod.DELETE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        log.debug("REST request to delete Post : {}", id);
        postRepository.delete(id);
        commentRepository.deleteByPostId(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert("post", id.toString()))
                .build();
    }

}
