package com.igitras.comments.mvc.apis;

import com.igitras.comments.mvc.dtos.CommentDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mason
 */
@RestController
@RequestMapping("comments")
public class CommentController {
    private static final SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private List<CommentDto> comments = new ArrayList<>();

    public CommentController() throws ParseException {
        this.comments = Arrays.asList(new CommentDto("task11", "comment on task11", FORMATTER.parse("2015-04-23")),
                new CommentDto("task12", "comment on task12", FORMATTER.parse("2015-05-12")),
                new CommentDto("task11", "new comment on task11", FORMATTER.parse("2015-04-27")),
                new CommentDto("task21", "comment on task21", FORMATTER.parse("2015-01-15")),
                new CommentDto("task22", "comment on task22", FORMATTER.parse("2015-03-05")));
    }

    @RequestMapping(value = "/{taskId}", method = RequestMethod.GET)
    public List<CommentDto> getCommentsByTaskId(@PathVariable("taskId") String taskId) {
        List<CommentDto> commentListToReturn = new ArrayList<>();
        for (CommentDto currentComment : comments) {
            if (currentComment.getTaskId()
                    .equalsIgnoreCase(taskId)) {
                commentListToReturn.add(currentComment);
            }
        }

        return commentListToReturn;
    }
}
