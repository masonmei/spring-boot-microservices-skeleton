package com.igitras.blog.mvc.resource;

import static com.igitras.common.utils.RequestUtil.getRemoteAddress;

import com.codahale.metrics.annotation.Timed;
import com.igitras.blog.domain.entity.Comment;
import com.igitras.blog.domain.repository.CommentRepository;
import com.igitras.blog.mvc.dto.CommentDto;
import com.igitras.common.utils.HeaderUtil;
import com.igitras.common.utils.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * REST controller for managing comments.
 */
@RestController
@RequestMapping("api")
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Create a new blog post.
     *
     * @param commentDto the commentDto to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new commentDto, or with status 400 (Bad Request) if the blog has already an ID
     *
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "posts/{postId}/comments",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CommentDto> createPostComment(
            @PathVariable("postId") Long postId,
            @Valid @RequestBody CommentDto commentDto, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save Blog Comment : {}", commentDto);
        if (commentDto.getId() != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("comment", "idexists", "A new comment cannot already have an ID"))
                    .body(null);
        }
        Comment comment = new Comment(commentDto.getId()).setPostId(postId)
                .setAuthor(commentDto.getAuthor())
                .setAuthorEmail(commentDto.getAuthorEmail())
                .setAuthorUrl(commentDto.getAuthorUrl())
                .setAuthorIp(getRemoteAddress(request))
                .setContent(commentDto.getContent());
        comment = commentRepository.save(comment);
        CommentDto result = new CommentDto(comment);
        return ResponseEntity.created(new URI("/api/posts/" + postId + "/comments/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("comment", result.getId()
                        .toString()))
                .body(result);
    }

    /**
     * Get all the comments of `postId` post.
     *
     * @param pageable the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and page of comments in body
     *
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "posts/{postId}/comments", method = RequestMethod.GET,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<CommentDto>> getAllPostComments(@PathVariable("postId") Long postId, Pageable pageable)
            throws URISyntaxException {
        log.debug("REST request to get a page of Comments");
        Page<Comment> page = commentRepository.findAllByPostId(postId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/posts/" + postId + "/comments");

        Page<CommentDto> collect = page.map(CommentDto::new);
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of comment in body
     *
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "comments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Page<CommentDto>> getAllComments(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Comments");
        Page<Comment> page = commentRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/comments");

        Page<CommentDto> collect = page.map(CommentDto::new);
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * DELETE  /blogs/:id : delete the "id" blog.
     *
     * @param id the id of the blogDTO to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "comments/{id}",
                    method = RequestMethod.DELETE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        commentRepository.delete(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert("comment", id.toString()))
                .build();
    }

}
