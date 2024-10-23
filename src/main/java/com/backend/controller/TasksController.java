package com.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.entity.Employee;
import com.backend.entity.Employer;
import com.backend.entity.Tasks;
import com.backend.entity.User;
import com.backend.service.JwtService;
import com.backend.service.TasksService;
import com.backend.service.UserService;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private TasksService tasksService;

	@PostMapping("/assign/{employee_id}")
	@PreAuthorize("hasAuthority('EMPLOYER')")
	public ResponseEntity<?> assignTask(@RequestBody Tasks task, @PathVariable("employee_id") int assigneeId,
			@RequestHeader("Authorization") String token) {

		User u = userService.getUserById(assigneeId);

		if (u == null) {
			return new ResponseEntity<String>("Employee doesn't exists", HttpStatus.NOT_FOUND);
		}

		String email = jwtService.extractUsername(token.substring(7));

		Employer employer = new Employer(userService.getUserByEmail(email));
		Employee employee = new Employee(u);

		task.setAssignee(employee);
		task.setAssigner(employer);

		tasksService.addTask(task);

		return ResponseEntity.ok(task);
	}

	@GetMapping("/get/all")
	public ResponseEntity<?> getAllTasks() {
		return ResponseEntity.ok(tasksService.getAllTasks());
	}

	@GetMapping("/get/mytasks")
	public ResponseEntity<?> getMyTasks(@RequestHeader("Authorization") String token) {

		String email = jwtService.extractUsername(token.substring(7));
		int employeeId = userService.getUserByEmail(email).getUserId();

		List<Tasks> tasks = tasksService.getTaskByEmployeeId(employeeId);

		if (tasks == null) {
			return new ResponseEntity<String>("Employee not found", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(tasks);
	}

	@PutMapping("/onprogress/{task_id}")
	public ResponseEntity<?> setInProgress(@PathVariable("task_id") int taskId) {
		Tasks task = tasksService.onProgress(taskId);

		if (task == null) {
			return new ResponseEntity<String>("Task doesn't exist", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok("status changed");
	}

	@PutMapping("/completed/{task_id}")
	public ResponseEntity<?> completed(@PathVariable("task_id") int taskId) {
		Tasks task = tasksService.completed(taskId);

		if (task == null) {
			return new ResponseEntity<String>("Task doesn't exist", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok("status changed");
	}

	@PutMapping("/pending/{task_id}")
	public ResponseEntity<?> pending(@PathVariable("task_id") int taskId) {
		Tasks task = tasksService.pending(taskId);

		if (task == null) {
			return new ResponseEntity<String>("Task doesn't exist", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok("status changed");
	}

}
