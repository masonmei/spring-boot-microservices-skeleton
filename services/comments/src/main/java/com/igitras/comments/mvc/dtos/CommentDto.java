package com.igitras.comments.mvc.dtos;

import java.util.Date;

/**
 * @author mason
 */
public class CommentDto {
    /**
     * The task id.
     */
    private String taskId;

    /**
     * The last name.
     */
    private String comment;

    /**
     * The completed.
     */
    private Date posted;

    /**
     * Instantiates a new task dto.
     */
    public CommentDto() {
        super();

    }

    /**
     * Instantiates a new task dto.
     *
     * @param taskId  the task id
     * @param comment the description
     */
    public CommentDto(String taskId, String comment, Date posted) {
        super();
        this.taskId = taskId;
        this.comment = comment;
        this.posted = posted;
    }

    public String getTaskId() {
        return taskId;
    }

    public CommentDto setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public CommentDto setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public Date getPosted() {
        return posted;
    }

    public CommentDto setPosted(Date posted) {
        this.posted = posted;
        return this;
    }
}
