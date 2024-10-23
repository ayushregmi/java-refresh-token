package com.backend.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.entity.Employee;
import com.backend.entity.Status;
import com.backend.entity.Tasks;
import com.backend.repository.EmployeeRepository;
import com.backend.repository.TasksRepository;

@Service
public class TasksService {

	@Autowired
	private TasksRepository tasksRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Tasks> getAllTasks() {
		return tasksRepository.findAll();
	}

	public List<Tasks> getTaskByEmployeeId(int userId) {

		Employee emp = employeeRepository.findById(userId).orElse(null);

		if (emp == null) {
			return null;
		}
		return emp.getTasks();
	}

	public Tasks getTaskById(int taskId) {
		return tasksRepository.findById(taskId).orElse(null);
	}

	public void addTask(Tasks task) {

		task.setCreatedAt(new Date());
		task.setStatus(Status.PENDING);

		tasksRepository.save(task);
	}

	public Tasks onProgress(int taskId) {
		Tasks task = tasksRepository.findById(taskId).orElse(null);

		if (task != null) {
			task.setStatus(Status.IN_PROGRESS);
			tasksRepository.save(task);
		}
		return task;
	}

	public Tasks completed(int taskId) {
		Tasks task = tasksRepository.findById(taskId).orElse(null);

		if (task != null) {
			task.setStatus(Status.COMPLETED);
			tasksRepository.save(task);
		}
		return task;
	}

	public Tasks pending(int taskId) {
		Tasks task = tasksRepository.findById(taskId).orElse(null);

		if (task != null) {
			task.setStatus(Status.PENDING);
			tasksRepository.save(task);
		}
		return task;
	}

}
