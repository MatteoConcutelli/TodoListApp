package todolist.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


import todolist.entity.Account;
import todolist.entity.MyDate;
import todolist.entity.Task;
import todolist.entity.TodoList;

public class TodoListDAOJDBCImpl implements TodoListDAO {
	
	private Connection conn;
	
	public TodoListDAOJDBCImpl(String ip, String port, String dbName, String userName, String pwd) {
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");

			//conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + dbName, userName, pwd);

			// IT COULD BE NECESSARY TO USE THIS FORM
			conn = DriverManager.getConnection(
					"jdbc:mysql://" + ip + ":" + port + "/" + dbName
							+ "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
					userName, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 

	// ACCOUNT
	
	@Override
	public int newAccount(Account account) {
		
		String SQL = "INSERT INTO accounts_ VALUES(\"" + account.getUsername() + "\", \"" + account.getPassword() + "\");";
		
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;

		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return -1 * e.getErrorCode();
		}	
	}
	
	@Override
	public int verifyAccount(Account account) {
		
		String SQL = "SELECT username, password FROM accounts_ WHERE username =\"" + account.getUsername() + "\" and "
				+ "password =\"" + account.getPassword() + "\";";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			if (!rset.next()) {
				return -1;
			}
			
			rset.close();
			stmt.close();
			
			return 1; 
					
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return -1 * e.getErrorCode();
		}
	}
	
	public int deleteAccount(Account account) {
		
		if (verifyAccount(account) < 0) {
			return -1;
		}
		
		String SQL = "DELETE FROM accounts_ WHERE username =\"" + account.getUsername() + "\";";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;
		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return -1 * e.getErrorCode();
		}
		
	}
	
	 

	// GET ------------------------------------------------------------------
	
	@Override
	public List<TodoList> getTodoLists(String username) {
		
		String SQL = "SELECT title, username FROM lists_ WHERE username =\"" + username + "\" and shared=false;";
		
		List<TodoList> todoLists = new ArrayList<>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			while (rset.next()) {
				String title = rset.getString(1);
				String ownerUsername = rset.getString(2);
				
				TodoList todoList = new TodoList(ownerUsername, title, false);
				todoLists.add(todoList);
			}
			
			rset.close();
			stmt.close();
			
			return todoLists;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public List<TodoList> getSharedTodoLists(String username) {
		
		String SQL = "SELECT title, username FROM lists_ WHERE username =\"" + username + "\" and shared=true;";
		
		List<TodoList> todoLists = new ArrayList<>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			while (rset.next()) {
				String title = rset.getString(1);
				String ownerUsername = rset.getString(2);
				
				TodoList todoList = new TodoList(ownerUsername, title, true);
				todoLists.add(todoList);
			}
			
			rset.close();
			stmt.close();
			
			return todoLists;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public List<TodoList> getSharedWithMeTodoLists(String username) {
		
		String SQL = "SELECT title, owner FROM sharedTo_ WHERE username =\"" + username + "\";";
		
		List<TodoList> todoLists = new ArrayList<>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			while (rset.next()) {
				String title = rset.getString(1);
				String ownerUsername = rset.getString(2);
				
				TodoList todoList = new TodoList(ownerUsername, title, true);
				todoLists.add(todoList);
			}
			
			rset.close();
			stmt.close();
			
			return todoLists;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public TodoList getEmptyTodoList(String username, String listTitle) {
		
		String SQL = "SELECT shared FROM lists_ WHERE title=\"" + listTitle + "\" and username = \"" + username +"\";";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			TodoList res = null;

			while (rset.next()) {
				res = new TodoList(username, listTitle, rset.getBoolean(1));
			}
			
			rset.close();
			stmt.close();
			
			return res;
		}
		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return null;
		}
	}
	
	@Override
	public TodoList getTodoList(String username, String listTitle) {
		
		if (getEmptyTodoList(username, listTitle) == null) {
			return null;
		}
		
		String SQL = "SELECT name, description, priority, date, dl FROM tasks_ where "
				+ "username = \"" + username + "\" and title = \"" + listTitle + "\" and isDone = false;";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			TodoList res = new TodoList(username, listTitle, false);

			while (rset.next()) {
			
				String name = rset.getString(1);
				String description = rset.getString(2);
				Task.Priority priority = Task.Priority.valueOf(rset.getString(3));
				MyDate date = MyDate.valueOf(rset.getString(4));
				boolean isDone = false;
				Task.DL dl = Task.DL.valueOf(rset.getString(5));
				
				//System.out.println(name + " " + description + " " + priority + " " + date + " " + isDone);
				
				Task item = new Task(username, listTitle, name, description, priority, date, isDone, dl);
				res.add(item);
			}
			
			rset.close();
			stmt.close();
			
			return res;
		}
		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return null;
		}
	}
	
	@Override
	public TodoList getDoneList(String username, String listTitle) {
		if (getEmptyTodoList(username, listTitle) == null) {
			return null;
		}
		
		String SQL = "SELECT name, description, priority, date, dl FROM tasks_ where "
				+ "username = \"" + username + "\" and title = \"" + listTitle + "\" and isDone = true;";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			TodoList res = new TodoList(username, listTitle, false);

			while (rset.next()) {
			
				String name = rset.getString(1);
				String description = rset.getString(2);
				Task.Priority priority = Task.Priority.valueOf(rset.getString(3));
				MyDate date = MyDate.valueOf(rset.getString(4));
				boolean isDone = true;
				Task.DL dl = Task.DL.valueOf(rset.getString(5));
				
				//System.out.println(name + " " + description + " " + priority + " " + date + " " + isDone);
				
				Task item = new Task(username, listTitle, name, description, priority, date, isDone, dl);
				res.add(item);
			}
			
			rset.close();
			stmt.close();
			
			return res;
		}
		catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return null;
		}
	}
	
	@Override
	public List<String> getParticipants(String ownerUsername, String listTitle) {
		
		String SQL = "SELECT username FROM sharedTo_ WHERE owner =\"" + ownerUsername + "\" and title=\"" + listTitle + "\";";
		
		List<String> participants = new ArrayList<>();
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			while (rset.next()) {
				participants.add(rset.getString(1));
			}
			
			rset.close();
			stmt.close();
			
			return participants;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}

	
	// INSERT ------------------------------------------------------------
	
	@Override
	public int newTask(Task newTask) {
		
		String SQL = "INSERT INTO tasks_ " + "VALUES(?,?,?,?,?,?,?,?)";

		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, newTask.getName());
			pstmt.setString(2, newTask.getDescription());
			pstmt.setString(3, newTask.getPriority().toString());
			pstmt.setString(4, newTask.getCreationDate().toString());
			pstmt.setBoolean(5, newTask.getIsDone());
			pstmt.setString(6, newTask.getOwnerUsername());
			pstmt.setString(7, newTask.getOwnerListTitle());
			pstmt.setString(8, newTask.getDl().toString());

			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;

		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return -1 * e.getErrorCode();
		}
	}
	
	
	@Override
	public int newTodoList(TodoList newTodoList) {
		
		String SQL = "INSERT INTO lists_ VALUES(\"" + newTodoList.getTitle() + "\"," + newTodoList.getShared() + ",\"" + newTodoList.getOwnerUsername() + "\")";	
		
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			int affectedRows = pstmt.executeUpdate();
			
			// nel caso la TodoList ha qualche elmento questo deve essere caricato
			if ( newTodoList.size() > 0) {
				for (Task task : newTodoList) {
					affectedRows += newTask(task);
				}
			}
			
			pstmt.close();
			return affectedRows;

		} catch (SQLException e) {
			// 1050 se la tabella già esiste
			System.out.println(e.getErrorCode());
			return -1 * e.getErrorCode();
		}
		
	}
	
	public int newListParticipant(String ownerUsername, String newParticipantUsername, String listTitle) {
		
		if (!isShared(ownerUsername, listTitle)) {
			return -1;
		}
		
		String SQL = "INSERT INTO sharedTo_ " + "VALUES(?,?,?)";
		try {
			
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			
			pstmt.setString(1, listTitle);
			pstmt.setString(2, newParticipantUsername);
			pstmt.setString(3, ownerUsername);

			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;

		} catch (SQLException e) {
			// 1050 se la tabella già esiste
			System.out.println(e.getErrorCode());
			return -1 * e.getErrorCode();
		}
	}
	
	private boolean isShared(String username, String listTitle) {
		
		String SQL = "SELECT shared FROM lists_ WHERE username =\"" + username + "\" and title=\"" + listTitle + "\";";
		
		try {
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(SQL);
			
			boolean shared = false;
			while (rset.next()) {
				shared = rset.getBoolean(1);
			}
			
			rset.close();
			stmt.close();
			
			return shared;
		}
		catch (SQLException e) {
			e.printStackTrace();
			return false;
		}	
	}
	
	    // DELETE ------------------------------------------
	
	
	@Override
	public int deleteTask(String username, String todoListTitle, String taskName) {
		
		String SQL = "DELETE FROM tasks_ WHERE username = ? and title = ? and name = ?;";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, username);
			pstmt.setString(2, todoListTitle);
			pstmt.setString(3, taskName);
			
			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;

		} catch (SQLException e) {
			System.out.println(e.getErrorCode());
			return -1 * e.getErrorCode();
		}
	}
	
	private int deleteTasks(String username, String todoListTitle) {
		
		String SQL = "DELETE FROM tasks_ WHERE username = ? and title = ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, username);
			pstmt.setString(2, todoListTitle);
			
			int affectedRows = pstmt.executeUpdate();
		
			pstmt.close();
			return affectedRows;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1 * e.getErrorCode();
		}
	}
	
	private int deleteSharing(String listTitle, String owner) {
		
		String SQL = "DELETE FROM sharedTo_ WHERE owner = ? and title = ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);

			pstmt.setString(1, owner);
			pstmt.setString(2, listTitle);
			
			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;

		} catch (SQLException e) {
			e.printStackTrace();
			return -1 * e.getErrorCode();
		}
	}
	
	@Override
	public int deleteTodoList(String username, String title) {
		
		deleteTasks(username, title);
		deleteSharing(title, username);
		
		String SQL = "DELETE FROM lists_ WHERE title = \"" + title + "\" and username = \"" + username + "\";";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1 * e.getErrorCode();
		}
	}
	
	public int deleteParticipant(String title, String participantUsername, String owner) {
		
		String SQL = "DELETE FROM sharedTo_ WHERE title = \"" + title + "\" and username = \"" + participantUsername + "\" and owner = \"" + owner + "\";";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			int affectedRows = pstmt.executeUpdate();
			
			pstmt.close();
			return affectedRows;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1 * e.getErrorCode();
		}
	}
	
	
	// UPDATE ------------
	
	@Override
	public int checkTask(String ownerUsername, String listTitle, String taskName) {
		
		Task task = getTask(ownerUsername, listTitle, taskName);
		
		if (task != null) {
			int res = deleteTask(ownerUsername, listTitle, taskName);
			
			if (res >= 0) {
				task.setIsDone();
				return res = newTask(task);
			}
		}
		
		return -1;
	}
	
	public Task getTask(String owner, String listTitle, String taskName) {
		  
		  String query = "SELECT name, description, priority, date, isDone, username, title, dl FROM tasks_ WHERE username = \"" + owner + "\"" 
				  + " and title = \"" + listTitle + "\" and name = \"" + taskName + "\";";
		  
		  try { 
			  Statement stmt = conn.createStatement(); 
			  ResultSet rset = stmt.executeQuery(query);
		  
		      Task res = null;
		  
		      while (rset.next()) { 
		    	  
		    	  String name = rset.getString(1);
		    	  String description = rset.getString(2);
		    	  Task.Priority priority = Task.Priority.valueOf(rset.getString(3));
		    	  MyDate date = MyDate.valueOf(rset.getString(4));
		    	  boolean isDone = false;
		    	  Task.DL dl = Task.DL.valueOf(rset.getString(8));
				
		    	  res = new Task(owner, listTitle, name, description, priority, date, isDone, dl);
		    	  break; 
		    	  
		      }
			  
			  rset.close(); 
			  stmt.close();
			  
			  return res;
		  
		  } catch (SQLException e) { e.printStackTrace(); return null; }
	  
	  }
	
	
	
	@Override
	public void closeConnection() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			Enumeration<Driver> enumDrivers = DriverManager.getDrivers();
			while (enumDrivers.hasMoreElements()) {
				Driver driver = enumDrivers.nextElement();
				DriverManager.deregisterDriver(driver);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
