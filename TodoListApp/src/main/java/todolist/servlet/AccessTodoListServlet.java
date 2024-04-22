package todolist.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import todolist.dao.TodoListDAO;
import todolist.dao.TodoListDAOJDBCImpl;
import todolist.entity.Account;

import java.io.IOException;

/**
 * Servlet implementation class AccessTodoListServlet
 */
public class AccessTodoListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	
	private TodoListDAO dao;
    
    public AccessTodoListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");
		
		dao = new TodoListDAOJDBCImpl(ip, port, dbName, userName, password);
		System.out.println("connessione con il DB stabilita.\n");
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	// LOGIN
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if (req.getParameter("username") != null && req.getParameter("password") != null) {
			
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			Account account = new Account(username,password);
			
			int res = dao.verifyAccount(account);
			if (res >= 0) {
				resp.setStatus(200);	
				resp.setContentType("application/json");
				resp.getWriter().append(account.toJSONString());
				return;
			}
			else if (res == -1) {
				resp.setStatus(404);
				resp.setContentType("text/plain");
				resp.getWriter().append("Account \"" + username + "\" non esiste.");
				return;
			}
		}
		else if (req.getParameter("username") == null || req.getParameter("password") == null) {
			// bad request
			resp.setStatus(400);
			return;
		}
		
		resp.setStatus(500);
		return;
	}
	
	// REGISTRATION
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		if (req.getParameter("username") != null && req.getParameter("password") != null) {
			
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			
			if (password.length() < 4) {
				resp.setStatus(401);
				return;
			}
			
			Account account = new Account(username,password);
			
			int res = dao.newAccount(account);
			if (res >= 0) {
				resp.setStatus(201);	
				resp.setContentType("application/json");
				resp.getWriter().append(account.toJSONString());
				return;
			}
			else if (res == -1062) {
				resp.setStatus(409);
				resp.setContentType("text/plain");
				resp.getWriter().append("Account \"" + username + "\" già esistente.");
				return;
			}
		}
		else if (req.getParameter("username") == null || req.getParameter("password") == null) {
			// bad request
			resp.setStatus(400);
			return;
		}
		
		resp.setStatus(500);
		return;
	}
	
}
