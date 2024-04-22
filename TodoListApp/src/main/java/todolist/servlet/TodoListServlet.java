package todolist.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todolist.dao.TodoListDAO;
import todolist.dao.TodoListDAOJDBCImpl;
import todolist.entity.Account;
import todolist.entity.Task;
import todolist.entity.TodoList;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.json.JSONException;

/**
 * Servlet implementation class TodoListServlet
 */
public class TodoListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	private TodoListDAO dao;
	
    public TodoListServlet() {
        super();
    }

    public void init() {
    	String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");
		
		dao = new TodoListDAOJDBCImpl(ip, port, dbName, userName, password);
		System.out.println("connessione con il DB stabilita.\n");
    }
	
    @Override
	public void destroy() {
		dao.closeConnection();
	}
    
    // get TodoList
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String encoded_cred = req.getHeader("Authorization");
		if ( encoded_cred == null ) {
			resp.setStatus(401);
			return;
		}
		/*
		byte[] decodedBytes = Base64.getDecoder().decode(encoded_cred);
		String cred = new String(decodedBytes);
		
		System.out.println(cred);
		*/
		
		String[] cred = encoded_cred.split(":");
		String username = cred[0];
		String password = cred[1];
		
		if (dao.verifyAccount(new Account(username, password)) < 0) {
			resp.setStatus(401);
			return;
		}
			
		/*_____________________________________________________________________*/
		
		
		PrintWriter out = resp.getWriter();
		
		
		if (req.getParameter("todoListTitle") != null && req.getParameter("isDone") != null) {
			
			String title = req.getParameter("todoListTitle");
			boolean isDone = Boolean.valueOf(req.getParameter("isDone"));
			
			
			TodoList todoList;
			if (!isDone) {
				todoList = dao.getTodoList(username, title);
			}
			else {
				todoList = dao.getDoneList(username, title);
			}
			
			
			if (todoList == null) {
				resp.setStatus(404);
				resp.getWriter().append("Non ci sono task o la lista non è stata trovata");
				return;
			}
			
			resp.setStatus(200);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			
			out.print(todoList.toJSONString());
			out.flush();
			return;
		}
		else if (req.getParameter("todoListTitle") == null && req.getParameter("isDone") == null) {
			
			List<TodoList> todoLists = dao.getTodoLists(username);
			
			if (todoLists.size() == 0) {
				resp.setStatus(404);
				out.print("Non esistono TodoList.");
				out.flush();
				return;
			}
			
			resp.setStatus(200);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			
			String json_string = "[";
			boolean first_cicle = true;
			for (TodoList todoList : todoLists) {
				if (first_cicle) {
					json_string += todoList.toJSONString();
					first_cicle = false;
				}
				else {
					json_string += "," + todoList.toJSONString();
				}
			}
			json_string += "]";
			
			out.print(json_string);
			out.flush();
			
			return;
		}
		
		resp.setStatus(500);
		return;
	}
	
	// check task
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String encoded_cred = req.getHeader("Authorization");
		if ( encoded_cred == null ) {
			resp.setStatus(401);
			return;
		}
		/*
		byte[] decodedBytes = Base64.getDecoder().decode(encoded_cred);
		String cred = new String(decodedBytes);
		
		System.out.println(cred);
		*/
		
		String[] cred = encoded_cred.split(":");
		String username = cred[0];
		String password = cred[1];
		
		if (dao.verifyAccount(new Account(username, password)) < 0) {
			resp.setStatus(401);
			return;
		}
			
		/*_____________________________________________________________________*/
		
		if (req.getParameter("ownerUsername") != null && req.getParameter("todoListTitle") != null && req.getParameter("taskName") != null) {
			
			String owner = req.getParameter("ownerUsername");
			String listTitle = req.getParameter("todoListTitle");
			String taskName = req.getParameter("taskName");
			
			// controllo se l'utente che fa la richiesta può checkare un task
			if (!username.equals(owner)) {
				boolean isParticipant = false;
				for (String participant : dao.getParticipants(owner, listTitle)) {
					if (participant.equals(username)) {
						isParticipant = true;
					}
				}
				
				if (!isParticipant) {
					resp.setStatus(404);
					resp.getWriter().print("Non fai parte di questa lista.");
					resp.getWriter().flush();
					return;
				}
			}
			
			
			int res = dao.checkTask(owner, listTitle, taskName);
			if (res >= 0) {
				resp.setStatus(200);	
				resp.setContentType("text/plain");
				resp.getWriter().append("Task completato");
				return;
			}
			
		}
		
		resp.setStatus(500);
		return;
		
	}
	
	
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String encoded_cred = req.getHeader("Authorization");
		if ( encoded_cred == null ) {
			resp.setStatus(401);
			return;
		}
		/*
		byte[] decodedBytes = Base64.getDecoder().decode(encoded_cred);
		String cred = new String(decodedBytes);
		
		System.out.println(cred);
		*/
		
		String[] cred = encoded_cred.split(":");
		String username = cred[0];
		String password = cred[1];
		
		if (dao.verifyAccount(new Account(username, password)) < 0) {
			resp.setStatus(401);
			return;
		}
			
		/*_____________________________________________________________________*/
		
		
		resp.setContentType("text/plain");
		
		Task newTask = null;
		TodoList newTodoList = null;
		
		
		// IMPORTANTE: per poter usare un file JSON in INPUT, alcuni caratteri, come { e
		// }
		// dovrebbero essere oggetto di conversione per compatibilità per protocollo
		// HTTP1.1
		// Esempio: /MusicianServlet?json=%7Bssn:10,name:"DANILO CROCE"%7D al posto di
		// /MusicianServlet?json={ssn:10,name:"DANILO CROCE"}
		
		if (req.getParameter("todoListTitle") != null && req.getParameter("shared") != null) {

			String todoListTitle = req.getParameter("todoListTitle");
			boolean shared = Boolean.valueOf(req.getParameter("shared"));

			newTodoList = new TodoList(username, todoListTitle, shared);
			
			int res = dao.newTodoList(newTodoList);
			if (res >= 0) {
				resp.setStatus(201);	
				resp.setContentType("application/json");
				resp.getWriter().append(newTodoList.toJSONString());
				return;
			}
			else if (res == -1062) {
				resp.setStatus(409);
				resp.setContentType("text/plain");
				resp.getWriter().append("TodoList \"" + todoListTitle + "\" già esistente.");
				return;
			}
			else if (res == -1452) {
				resp.setStatus(404);
				resp.setContentType("text/plain");
				resp.getWriter().append("TodoList \"" + todoListTitle + "\" non esiste.");
				return;
			}
			
		}
		else if (req.getParameter("jsonTask") != null) {
			String jsonString = req.getParameter("jsonTask");
			
			try {
				newTask = Task.fromJSON(jsonString);
				
				if (!username.equals(newTask.getOwnerUsername())) {
					resp.setStatus(401);
					return;
				}
				
				int res = dao.newTask(newTask); 
				if ( res >= 0) {
					resp.setStatus(201);	
					resp.setContentType("application/json");
					resp.getWriter().append(newTask.toJsonString());
				
					return;	
				}
				else if (res == -1062) {
					resp.setStatus(409);
					resp.setContentType("text/plain");
					resp.getWriter().append("Task \"" + newTask.getName() + "\" già esistente.");
					return;
				}
				else if (res == -1452) {
					resp.setStatus(404);
					resp.setContentType("text/plain");
					resp.getWriter().append("TodoList \"" + newTask.getOwnerListTitle() + "\" non esiste.");
					return;
				}
				
			} catch (JSONException e) {
				resp.setStatus(422);
				resp.getWriter().append("Errore lettura json");
				return;
			}				
		}
		else {
			resp.getWriter().append("Errore formato richiesta");
			resp.setStatus(422);
			return;
		}
		
		resp.setStatus(500);
		return;
	}

	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String encoded_cred = req.getHeader("Authorization");
		if ( encoded_cred == null ) {
			resp.setStatus(401);
			return;
		}
		/*
		byte[] decodedBytes = Base64.getDecoder().decode(encoded_cred);
		String cred = new String(decodedBytes);
		
		System.out.println(cred);
		*/
		
		String[] cred = encoded_cred.split(":");
		String username = cred[0];
		String password = cred[1];
		
		if (dao.verifyAccount(new Account(username, password)) < 0) {
			resp.setStatus(401);
			return;
		}
			
		/*_____________________________________________________________________*/
		
		
		if (req.getParameter("todoListTitle") != null) {
			
			String todoListTitle = req.getParameter("todoListTitle");
			
			// se specificato il task, viene cancellato quest'ultimo
			if (req.getParameter("taskName") != null) {
				
				String taskName = req.getParameter("taskName");
				
				int res = dao.deleteTask(username, todoListTitle, taskName);
				if (res > 0) {
					resp.getWriter().append("{}");
					resp.setStatus(200);
					return;
				}
				else if (res == 0 || res == -1452) {
					resp.setStatus(404);
					resp.getWriter().append("lista \"" + todoListTitle + "\" oppure task \"" + taskName + "\" non esistente");
					return;
				}
				
			}
			//altrimenti vengono cancellati tutti i task
			else if (req.getParameter("taskName") == null){
				
				int res = dao.deleteTodoList(username, todoListTitle);
				
				if (res > 0) {
					// risposta vuota per Volley JSONObjectRequest
					resp.getWriter().append("{}");
					resp.setStatus(200);	
					return;
				}
				else if (res == 0 || res == -1452) {
					resp.setContentType("text/plain");
					resp.setStatus(404);
					resp.getWriter().append("lista \"" + todoListTitle + "\" non esistente");
					return;
				}
			}
		}
		else {
			resp.getWriter().append("Errore formato richiesta");
			resp.setStatus(422);
			return;
		}
		
		resp.setStatus(500);
		return;
	
	}	
}
