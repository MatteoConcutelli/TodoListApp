package todolist.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todolist.dao.TodoListDAO;
import todolist.dao.TodoListDAOJDBCImpl;
import todolist.entity.Account;
import todolist.entity.TodoList;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Servlet implementation class TodoListServlet
 */
public class SharedTodoListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	private TodoListDAO dao;
	
    public SharedTodoListServlet() {
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
		
		if (req.getParameter("withMe") != null && req.getParameter("owner") != null 
				&& req.getParameter("todoListTitle") != null && req.getParameter("isDone") != null) {
			
			String ownerList = req.getParameter("owner");
			String title = req.getParameter("todoListTitle");
			boolean isDone = Boolean.valueOf(req.getParameter("isDone"));
			
			
			TodoList todoList;
			if (!isDone) {
				todoList = dao.getTodoList(ownerList, title);
			}
			else {
				todoList = dao.getDoneList(ownerList, title);
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
		else if (req.getParameter("participantsOfListTitle") != null && req.getParameter("owner") != null) {
			
			String listTitle = req.getParameter("participantsOfListTitle");
			String ownerList = req.getParameter("owner");
			
			// controllo se l'utente che fa la richiesta fa parte della lista condivisa
			if (!username.equals(ownerList)) {
				boolean isParticipant = false;
				for (String participant : dao.getParticipants(ownerList, listTitle)) {
					if (participant.equals(username)) {
						isParticipant = true;
					}
				}
				
				if (!isParticipant) {
					resp.setStatus(404);
					out.print("Non fai parte di questa lista.");
					out.flush();
					return;
				}
			}
			
			List<String> participantsList = dao.getParticipants(ownerList, listTitle);
			if (participantsList.size() == 0) {
				resp.setStatus(404);
				out.print("Non ci sono partecipanti.");
				out.flush();
				return;
			}
			
			resp.setStatus(200);
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			
			String json_string = "[";
			boolean first_cicle = true;
			for (String participant : participantsList) {
				if (first_cicle) {
					json_string += "\"" + participant + "\"";
					first_cicle = false;
				}
				else {
					json_string += "," + "\"" + participant + "\"";
				}
			}
			json_string += "]";
			
			out.print(json_string);
			out.flush();
			
			return;
			
		}
		// caso /todolists
		else if (req.getParameter("withMe") != null) {
			
			List<TodoList> todoLists = dao.getSharedWithMeTodoLists(username);
			if (todoLists.size() == 0) {
				resp.setStatus(404);
				out.print("Non esistono TodoList condivise con me.");
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
				}
				else {
					json_string = "," + todoList.toJSONString();
				}
			}
			
			json_string += "]";
			
			out.print(json_string);
			out.flush();
			
			return;
		}
		else if (req.getParameter("withMe") == null || req.getParameter("participantsOfListTitle") == null || req.getParameter("owner") == null) {
			
			List<TodoList> todoLists = dao.getSharedTodoLists(username);
			
			if (todoLists.size() == 0) {
				resp.setStatus(404);
				out.print("Non esistono TodoList condivise.");
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
	

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doPost(req, resp);
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
		
		
		// IMPORTANTE: per poter usare un file JSON in INPUT, alcuni caratteri, come { e
		// }
		// dovrebbero essere oggetto di conversione per compatibilità per protocollo
		// HTTP1.1
		// Esempio: /MusicianServlet?json=%7Bssn:10,name:"DANILO CROCE"%7D al posto di
		// /MusicianServlet?json={ssn:10,name:"DANILO CROCE"}
		
		if (req.getParameter("todoListTitle") != null && req.getParameter("newParticipant") != null) {

			String todoListTitle = req.getParameter("todoListTitle");
			String newParticipant = req.getParameter("newParticipant");
			
			if (username.equals(newParticipant)) {
				resp.setStatus(409);
				resp.setContentType("text/plain");
				resp.getWriter().append("\"" + newParticipant + "\" è il proprietario di questa lista.");
				return;
			}
			
			if (dao.getEmptyTodoList(username, todoListTitle) == null) {
				resp.setStatus(404);
				resp.setContentType("text/plain");
				resp.getWriter().append("lista \"" + todoListTitle + "\" non esiste.");
				return;
			}
			
			int res = dao.newListParticipant(username, newParticipant, todoListTitle);
			if (res >= 0) {
				resp.setStatus(201);
				return;
			}
			else if (res == -1062) {
				resp.setStatus(409);
				resp.setContentType("text/plain");
				resp.getWriter().append("\"" + newParticipant + "\" già partecipa a questa lista.");
				return;
			}
			else if (res == -1452) {
				resp.setStatus(404);
				resp.setContentType("text/plain");
				resp.getWriter().append("Utente \"" + newParticipant +"\" non esistente.");
				return;
			}
			else if (res == -1) {
				resp.setStatus(404);
				resp.setContentType("text/plain");
				resp.getWriter().append("la lista \"" + todoListTitle + "\" non è condivisa.");
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
		resp.setContentType("text/plain");
		
		if (req.getParameter("todoListTitle") != null && req.getParameter("owner") != null) {
			
			String listTitle = req.getParameter("todoListTitle");
			String ownerUsername = req.getParameter("owner");
			
			
			int res = dao.deleteParticipant(listTitle, username, ownerUsername);
			if (res > 0) {
				resp.setStatus(204);
				resp.getWriter().append("Partecipazione alla lista \"" + listTitle + "\" cancellata" );
				return;
			}
			else if (res == 0) {
				resp.setStatus(404);
				resp.getWriter().append("Non partecipi a questa lista");
				return;
			}
			else if (res == -1452) {
				resp.setStatus(404);
				resp.getWriter().append("lista \"" +listTitle + "\" non esiste.");
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
	
}
