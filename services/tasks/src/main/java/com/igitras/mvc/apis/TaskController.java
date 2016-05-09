package com.igitras.mvc.apis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.igitras.mvc.dtos.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * REST endpoint for the task functionality
 * 
 * @author anilallewar
 *
 */
@RestController
@RequestMapping("/")
public class TaskController {

	@Autowired
	private CommentsService commentsService;

	private List<TaskDto> tasks = Arrays.asList(new TaskDto("task11", "description11", "1"),
			new TaskDto("task12", "description12", "1"), new TaskDto("task13", "description13", "1"),
			new TaskDto("task21", "description21", "2"), new TaskDto("task22", "description22", "2"));

	/**
	 * Get all tasks
	 * 
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET, headers = "Accept=application/json")
	public List<TaskDto> getTasks() {
		return tasks;
	}

	/**
	 * Get tasks for specific taskid
	 * 
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value = "{taskId}", method = RequestMethod.GET, headers = "Accept=application/json")
	public TaskDto getTaskByTaskId(@PathVariable("taskId") String taskId) {
		TaskDto taskToReturn = null;
		for (TaskDto currentTask : tasks) {
			if (currentTask.getTaskId().equalsIgnoreCase(taskId)) {
				taskToReturn = currentTask;
				break;
			}
		}

		if (taskToReturn != null) {
			taskToReturn.setComments(this.commentsService.getCommentsForTask(taskId));
		}
		return taskToReturn;
	}

	/**
	 * Get tasks for specific user that is passed in
	 * 
	 * @param taskId
	 * @return
	 */
	@RequestMapping(value = "/usertask/{userName}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<TaskDto> getTasksByUserName(@PathVariable("userName") String userName) {
		List<TaskDto> taskListToReturn = new ArrayList<TaskDto>();
		for (TaskDto currentTask : tasks) {
			if (currentTask.getUserName().equalsIgnoreCase(userName)) {
				taskListToReturn.add(currentTask);
			}
		}

		return taskListToReturn;
	}
}
