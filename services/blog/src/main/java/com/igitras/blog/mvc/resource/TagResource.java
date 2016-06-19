package com.igitras.blog.mvc.resource;

import static org.springframework.util.CollectionUtils.isEmpty;

import static java.util.stream.Collectors.toList;

import com.codahale.metrics.annotation.Timed;
import com.igitras.blog.domain.entity.Tag;
import com.igitras.blog.domain.repository.TagRepository;
import com.igitras.blog.mvc.dto.TagDto;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tag.
 */
@RestController
@RequestMapping("/api/tags")
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    @Autowired
    private TagRepository tagRepository;

    /**
     * POST  /tags : Create a new tag.
     *
     * @param tagDto the tagDto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tagDto, or with status 400 (Bad Request) if the tag has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagDto> createTag(@RequestBody TagDto tagDto) throws URISyntaxException {
        log.debug("REST request to save Tag : {}", tagDto);
        if (tagDto.getId() != null) {
            return ResponseEntity.badRequest()
                    .headers(HeaderUtil.createFailureAlert("tag", "idexists", "A new tag cannot already have an ID"))
                    .body(null);
        }
        Tag tag = new Tag().setName(tagDto.getName())
                .setDescription(tagDto.getDescription());
        tag = tagRepository.save(tag);
        TagDto result = new TagDto(tag);
        return ResponseEntity.created(new URI("/api/tags/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("tag", result.getId()
                        .toString()))
                .body(result);
    }

    /**
     * PUT  /tags : Updates an existing tag.
     *
     * @param tagDto the tagDto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tagDto,
     * or with status 400 (Bad Request) if the tagDto is not valid,
     * or with status 500 (Internal Server Error) if the tagDto couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/{id}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagDto> updateTag(@PathVariable("id") Long id, @RequestBody TagDto tagDto)
            throws URISyntaxException {
        log.debug("REST request to update Tag : {}", tagDto);
        if (tagDto.getId() == null) {
            return createTag(tagDto);
        }
        Tag tag = new Tag(id).setName(tagDto.getName())
                .setDescription(tagDto.getDescription());
        tag = tagRepository.save(tag);
        TagDto result = new TagDto(tag);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("tag", tagDto.getId()
                        .toString()))
                .body(result);
    }

    /**
     * GET  /tags : get all the tags.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of tags in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<TagDto>> getAllTags(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get a page of Tags");
        Page<Tag> page = tagRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/tags");
        List<TagDto> collect = new ArrayList<>();
        if (!isEmpty(page.getContent())) {
            collect = page.getContent()
                    .stream()
                    .map(TagDto::new)
                    .collect(toList());
        }
        return new ResponseEntity<>(collect, headers, HttpStatus.OK);
    }

    /**
     * GET  /tags/:id : get the "id" tag.
     *
     * @param id the id of the tagDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tagDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TagDto> getTag(@PathVariable Long id) {
        log.debug("REST request to get Tag : {}", id);
        Tag tag = tagRepository.findOne(id);
        return Optional.ofNullable(tag)
                .map(result -> new ResponseEntity<>(new TagDto(result), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tags/:id : delete the "id" tag.
     *
     * @param id the id of the tagDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        log.debug("REST request to delete Tag : {}", id);
        tagRepository.delete(id);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityDeletionAlert("tag", id.toString()))
                .build();
    }

}
