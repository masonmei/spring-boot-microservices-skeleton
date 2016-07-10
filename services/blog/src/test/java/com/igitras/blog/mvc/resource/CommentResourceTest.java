package com.igitras.blog.mvc.resource;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igitras.blog.Application;
import com.igitras.blog.mvc.dto.CommentDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

/**
 * Created by mason on 7/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(value = {Application.class})
@WebAppConfiguration
public class CommentResourceTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private ObjectMapper objectMapper;

    @Autowired
    private Filter springSecurityFilterChain;

    @Before
    public void setUp() throws Exception {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(springSecurityFilterChain)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown() throws Exception {
        this.mvc = null;
        this.objectMapper = null;
    }

    @Test
    public void createPostComment() throws Exception {
        CommentDto commentDto = new CommentDto();
        commentDto.setPostId(1L)
                .setAuthor("mason")
                .setAuthorEmail("mason@gmail.com")
                .setContent("## Header One \nThis is a comment from mason.");
        mvc.perform(post("api/posts/{postId}/comments", "1")
                .with(user("user").password("password"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(commentDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getAllPostComments() throws Exception {

    }

    @Test
    public void getAllComments() throws Exception {

    }

    @Test
    public void deleteBlog() throws Exception {

    }
}