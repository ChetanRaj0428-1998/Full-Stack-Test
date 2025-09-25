package com.example.todo_api;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// This class is the "traffic cop" of your app.
// It listens for HTTP requests (like GET, POST, PUT, DELETE) and decides what to do.
// Example: when the user opens "/api/todos", this class knows how to fetch tasks.
@RestController
@RequestMapping("/api/todos") // All routes will start with /api/todos
@CrossOrigin(origins = "*") // Allows requests from anywhere (useful when frontend is separate, like React)
public class TodoController {

    // Spring Boot automatically gives us an instance of TodoRepository to use here.
    // This is like saying "Hey, bring me my database helper tool."
    @Autowired
    private TodoRepository todoRepository;

    // GET /api/todos
    // Fetch all tasks from the database.
    @GetMapping
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }

    // POST /api/todos
    // Create a new task. The task info comes in the request body (JSON).
    @PostMapping
    public Todo createTodo(@RequestBody Todo todo) {
        return todoRepository.save(todo);
    }

    // PUT /api/todos/{id}
    // Update an existing task (title or completed status).
    // If the task exists, update it. If not, return "not found".
    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todo.setTitle(todoDetails.getTitle());
                    todo.setCompleted(todoDetails.isCompleted());
                    Todo updatedTodo = todoRepository.save(todo);
                    return ResponseEntity.ok(updatedTodo); // Return 200 OK with updated data
                }).orElse(ResponseEntity.notFound().build()); // Return 404 if not found
    }

    // DELETE /api/todos/{id}
    // Delete a task by ID. If task exists, delete it; else return "not found".
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id) {
        return todoRepository.findById(id)
                .map(todo -> {
                    todoRepository.delete(todo);
                    return ResponseEntity.ok().build(); // Return 200 OK (no content)
                }).orElse(ResponseEntity.notFound().build()); // Return 404 if not found
    }
}
