package com.igitras.mvc.dtos;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.util.Date;

/**
 * @author mason
 */
public class CommentDto {
    /** The task id. */
    private String taskId;

    /** The last name. */
    private String comment;

    /** The completed. */
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
     * @param taskId
     *            the task id
     * @param comment
     *            the description
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

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getPosted() {
        return posted;
    }

    public void setPosted(Date posted) {
        this.posted = posted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommentDto that = (CommentDto) o;
        return com.google.common.base.Objects.equal(taskId, that.taskId) &&
                com.google.common.base.Objects.equal(comment, that.comment) &&
                com.google.common.base.Objects.equal(posted, that.posted);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taskId, comment, posted);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("taskId", taskId)
                .add("comment", comment)
                .add("posted", posted)
                .toString();
    }
}
