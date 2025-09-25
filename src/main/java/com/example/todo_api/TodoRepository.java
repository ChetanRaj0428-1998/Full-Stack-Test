package com.example.todo_api;

import org.springframework.data.jpa.repository.JpaRepository;

// This interface is the "middleman" between our Todo class and the database.
// Instead of writing SQL queries ourselves, Spring Boot gives us this shortcut.
// By extending JpaRepository, we automatically get methods like:
// - save() to add or update tasks
// - findAll() to get all tasks
// - findById() to get a single task
// - deleteById() to remove a task
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // We don’t need to write any code here for basic operations.
    // Spring Boot generates all the database operations for us.
}


/*
This code creates a repository interface that connects your Todo class to 
the database without needing manual SQL.
 The import brings in JpaRepository, which is a built-in Spring Data helper.
  By extending JpaRepository<Todo, Long>, 
  we’re saying: 
  “This repository will manage Todo objects,
   and each one is identified by a Long ID.”
    With just this line, Spring Boot automatically gives you 
    common database methods like saving, updating, fetching,
     and deleting to-do items. You don’t need
      to write queries — Spring Boot does the
       heavy lifting.
*/