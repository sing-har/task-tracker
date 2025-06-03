package com.example.tasktracker.controller;

import com.example.tasktracker.dto.ResponseVo;
import com.example.tasktracker.model.Task;
import com.example.tasktracker.service.TaskService;
import com.example.tasktracker.util.AppConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;

import javax.sql.DataSource;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task APIs", description = "CRUD operations for Task management")
public class TaskController {


    private final TaskService taskService;
    
    @Autowired
    private DataSource dataSource;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @PostConstruct
    public void printConnectedDB() throws SQLException {
    	System.out.println("🔍 DataSource Class: " + dataSource.getClass().getName());
        System.out.println("🔍 DB URL: " + dataSource.getConnection().getMetaData().getURL());
    }

    @Operation(summary = "Create a new task")
    @PostMapping
    public ResponseEntity<ResponseVo<Task>> createTask(@RequestBody Task task) {
        Task created = taskService.createTask(task);
        return ResponseEntity.ok(
            ResponseVo.<Task>builder()
                .statusCode(200)
                .status(AppConstants.Success)
                .message(AppConstants.TASK_CREATED)
                .data(created)
                .build()
        );
    }

    @Operation(summary = "Retrieve all tasks")
    @GetMapping
    public ResponseEntity<ResponseVo<List<Task>>> getAllTasks() {
        return ResponseEntity.ok(
            ResponseVo.<List<Task>>builder()
                .statusCode(200)
                .status(AppConstants.Success)
                .message(AppConstants.TASKS_FETCHED)
                .data(taskService.getAllTasks())
                .build()
        );
    }

    @Operation(summary = "Retrieve a task by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ResponseVo<Task>> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
            .map(task -> ResponseEntity.ok(
                ResponseVo.<Task>builder()
                    .statusCode(200)
                    .status(AppConstants.Success)
                    .message(AppConstants.TASK_FETCHED)
                    .data(task)
                    .build()
            ))
            .orElse(ResponseEntity.status(404).body(
                ResponseVo.<Task>builder()
                    .statusCode(404)
                    .status(AppConstants.Failure)
                    .message(AppConstants.TASK_NOT_FOUND)
                    .data(null)
                    .build()
            ));
    }

    @Operation(summary = "Update an existing task")
    @PutMapping("/{id}")
    public ResponseEntity<ResponseVo<Task>> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updated = taskService.updateTask(id, task);
        return ResponseEntity.ok(
            ResponseVo.<Task>builder()
                .statusCode(200)
                .status(AppConstants.Success)
                .message(AppConstants.TASK_UPDATED)
                .data(updated)
                .build()
        );
    }

    @Operation(summary = "Delete a task by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseVo<String>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok(
            ResponseVo.<String>builder()
                .statusCode(200)
                .status(AppConstants.Success)
                .message(AppConstants.TASK_DELETED)
                .data("Task ID " + id + " deleted")
                .build()
        );
    }

}
