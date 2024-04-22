package todolist.dao;

import java.util.List;

import todolist.entity.Account;
import todolist.entity.Task;
import todolist.entity.TodoList;

public interface TodoListDAO {
	
	public int newAccount(Account account);
	public int verifyAccount(Account account);
	public int deleteAccount(Account account);
	
	// GET 
	// ottieni le la lista di todolist vuote
	public List<TodoList> getTodoLists(String username);
	public List<TodoList> getSharedTodoLists(String username);
	public List<TodoList> getSharedWithMeTodoLists(String username);
	
	// ottieni una todolist senza task (per controllo in doPut in sharedTodoListServlet)
	public TodoList getEmptyTodoList(String username, String listTitle);
	
	// ottieni la lista di task di una todolist
	public TodoList getTodoList(String username, String listTitle);
	public TodoList getDoneList(String username, String listTitle);
	public List<String> getParticipants(String ownerUsername, String listTitle);
	
	
	// INSERT
	
	public int newTask(Task newTask);
	public int newTodoList(TodoList newTodoList);
	public int newListParticipant(String ownerUsername, String newParticipantUsername, String listTitle);
	
	
	// DELETE
	
	public int deleteTask(String username, String todoListTitle, String taskName);
	public int deleteTodoList(String username, String title);
	public int deleteParticipant(String title, String participantUsername, String owner);
	
	// UPDATE
	public int checkTask(String ownerUsername, String listTitle, String taskName);

	public void closeConnection();
	
	
}
