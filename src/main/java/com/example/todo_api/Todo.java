package com.example.todo_api;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

// This class represents a "to-do" item in our app.This class defines the structure of our 'todo' items in the database.
// Think of it like a box that stores information about a single task.
//The @Entity tag tells Spring Boot that this class should become a database table.

@Entity
@Data // This is from Lombok: it auto-creates getters, setters, toString, equals, hashCode (saves us from writing them manually)
public class Todo {

    // This is the unique ID for each task.
    // It's like giving every to-do item a unique number so we can tell them apart.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    // This is the actual name or description of the task.
    // Example: "Buy groceries", "Finish homework"
    private String title;
    
    // This tells us if the task is done or not.
    // By default, when we create a new task, it's set to false (not completed).
    private boolean completed = false;
}

/*
This code is a Spring Boot "to-do" item model that connects Java code to a database.
 At the top, the import statements bring in tools we need: things from jakarta.persistence help map this class to a database table, and lombok. 
 Data saves us from writing boring getters/setters manually.
  The @Entity tag tells Spring Boot that this class should become a database table. 
  Inside, we have three pieces of data: an id (the unique number for each to-do, 
  marked with @Id and auto-generated using @GeneratedValue),
   a title (the actual name of the task), and completed (a true/false flag showing if the task is done, which starts as false). The @Data annotation is from Lombok, and it automatically creates all the common methods (like getTitle(), setCompleted(), toString(), etc.) so you don’t have to type them out.
*/