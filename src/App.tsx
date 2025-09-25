/*
  This is the main React code for our Todo List app.
  React will show the tasks on the screen and let us add, mark, or delete them.
*/
import React, { useState, useEffect, FormEvent } from 'react';

// The address where our backend (Spring Boot) is running.
// When we make fetch() calls, they go here.
const API_URL = 'http://localhost:8080/api/todos';

// This describes what a Todo looks like (id, title, completed).
interface Todo {
  id: number;
  title: string;
  completed: boolean;
}

const App: React.FC = () => {
  // State = memory for React. This is how React remembers things.
  const [todos, setTodos] = useState<Todo[]>([]); // list of tasks
  const [newTodoTitle, setNewTodoTitle] = useState(''); // the text box for a new task
  const [error, setError] = useState<string | null>(null); // store errors
  const [loading, setLoading] = useState(true); // show loading state

  // Runs when the page loads (only once).
  // It fetches tasks from our Spring Boot API.
  useEffect(() => {
    const fetchTodos = async () => {
      try {
        const response = await fetch(API_URL);
        if (!response.ok) {
          throw new Error('Failed to fetch todos');
        }
        const data: Todo[] = await response.json();
        setTodos(data); // save tasks to state
      } catch (err) {
        setError(err instanceof Error ? err.message : 'An unknown error occurred');
      } finally {
        setLoading(false); // stop showing "loading"
      }
    };
    fetchTodos();
  }, []); // empty [] means run only once (like componentDidMount)

  // Add a new task
  const handleAddTodo = async (e: FormEvent) => {
    e.preventDefault(); // stop page reload
    if (!newTodoTitle.trim()) return; // ignore empty input

    try {
      const response = await fetch(API_URL, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title: newTodoTitle, completed: false }),
      });
      if (!response.ok) throw new Error('Failed to create todo');
      const newTodo = await response.json();
      setTodos([...todos, newTodo]); // add to the list
      setNewTodoTitle(''); // clear input
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to add todo');
    }
  };
  
  // Toggle a task as done/not done
  const handleToggleComplete = async (id: number, completed: boolean) => {
      const todoToUpdate = todos.find(t => t.id === id);
      if (!todoToUpdate) return;
  
      try {
        const response = await fetch(`${API_URL}/${id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ ...todoToUpdate, completed: !completed }),
        });
        if (!response.ok) throw new Error('Failed to update todo');
        const updatedTodo = await response.json();
        // Replace the old task with the updated one
        setTodos(todos.map(t => t.id === id ? updatedTodo : t));
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to toggle todo');
      }
    };

  // Delete a task
  const handleDeleteTodo = async (id: number) => {
    try {
      const response = await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error('Failed to delete todo');
      // Remove it from state
      setTodos(todos.filter(t => t.id !== id));
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to delete todo');
    }
  };
  
  return (
    <div className="min-h-screen bg-gray-900 text-white flex items-center justify-center font-sans p-4">
      <div className="w-full max-w-lg">
        <h1 className="text-4xl font-bold text-center mb-6 text-cyan-400">Todo List</h1>
        
        {/* Show errors if any */}
        {error && <p className="text-red-500 bg-red-900/50 p-3 rounded-md mb-4">{error}</p>}

        {/* Input box + Add button */}
        <form onSubmit={handleAddTodo} className="flex gap-2 mb-6">
          <input
            type="text"
            value={newTodoTitle}
            onChange={(e) => setNewTodoTitle(e.target.value)}
            placeholder="Add a new todo..."
            className="flex-grow bg-gray-800 border border-gray-700 rounded-md p-3 focus:outline-none focus:ring-2 focus:ring-cyan-500"
          />
          <button type="submit" className="bg-cyan-600 hover:bg-cyan-700 transition-colors text-white font-semibold py-3 px-5 rounded-md">
            Add
          </button>
        </form>

        {/* Show either loading message or the todo list */}
        {loading ? (
          <p className="text-center text-gray-400">Loading...</p>
        ) : (
          <ul className="space-y-3">
            {todos.map(todo => (
              <li key={todo.id} className="flex items-center justify-between bg-gray-800 p-4 rounded-lg">
                <span
                  onClick={() => handleToggleComplete(todo.id, todo.completed)}
                  className={`cursor-pointer ${todo.completed ? 'line-through text-gray-500' : ''}`}
                >
                  {todo.title}
                </span>
                <button onClick={() => handleDeleteTodo(todo.id)} className="text-gray-500 hover:text-red-500 transition-colors">
                  {/* Trash icon */}
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                  </svg>
                </button>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
};

export default App;
