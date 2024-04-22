import java.util.GregorianCalendar;
import java.util.List;

import org.json.JSONException;

import todolist.dao.TodoListDAOJDBCImpl;
import todolist.entity.MyDate;
import todolist.entity.Task;
import todolist.entity.TodoList;


public class Main {
	
	public static void main(String[] args) throws JSONException {
		
		
	  String ip = "127.0.0.1"; String port = "3306"; String dbName = "todolist";
	  String userName = "root"; String password = "toor";
	  
	  // PROVE ENTITY
	  /*
	  Task task = new Task("Matteo", "list1", "Task", "descrizione", Priority.MEDIUM, new MyDate(2023, 6, 22, 12, 38), false);
	  System.out.println(task.toJsonString());
	  
	  Task task2 = new Task("Matteo", "list1", "task2", "ciao", Priority.HIGH, new MyDate(2023, 6, 22, 12, 38), false);
	  System.out.println(task2.toJsonString());
	  
	  TodoList todolist = new TodoList("Matteo", "lista1", false);
	 
	  todolist.add(task);
	  todolist.add(task2);
	  
	  System.out.println(todolist.toJSONString());
	  */
	  // PROVE DAO
	  
	  TodoListDAOJDBCImpl dao = new TodoListDAOJDBCImpl(ip, port, dbName, userName,password);
	  
	  // GET -------------
	  
	  //System.out.println(dao.getTodoLists("Matteo"));
	  
	  System.out.println(dao.checkTask("Nicolas", "nuovaLista", "nuovoTask2"));
	  //System.out.println(dao.getTodoList("Matteo", "lista1").toJSONString());
	  //System.out.println(dao.getParticipants("Matteo", "lista2").toString());
	  //System.out.println(dao.getSharedTodoLists("Matteo"));
	  //System.out.println(dao.getParticipants("Nicolas", "nuovaLista"));

	  // INSERT -------------
	  //System.out.println(dao.newTask(new Task("Nicolas", "alimentazione", "Task", "descrizione", Priority.MEDIUM, new MyDate(2023, 6, 22, 12, 38), false)));
	  //System.out.println(dao.getTodoList("Nicolas", "alimentazione").toJSONString());
	  //System.out.println(dao.newTodoList(new TodoList("Nicolas", "alimentazione", false)));
	  //System.out.println(dao.newListParticipant("Matteo", "Nicolas", "lista2"));
	  
	  
	  // DELETE --------------
	  
	  //System.out.println(dao.deleteTask("Matteo", "lista1", "task1"));
	  //System.out.println(dao.deleteTodoList("Matteo", "lista2"));
	}
}
