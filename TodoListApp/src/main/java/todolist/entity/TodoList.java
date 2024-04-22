package todolist.entity;

import java.util.ArrayList;


public class TodoList extends ArrayList<Task>{

	private static final long serialVersionUID = 1L;
	
	String ownerUsername;
	
	private String title;
	private boolean shared = false;
	
	
	public TodoList(String ownerUsername, String title, boolean shared) {
		super();
		this.ownerUsername = ownerUsername;
		
		this.title = title;
		this.shared = shared;
	}
	
	public String getOwnerUsername() {
		return ownerUsername;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean getShared() {
		return shared;
	}
	
	public String toJSONString() {
		String jsonString = "{\"ownerUsername\":\""+ ownerUsername + "\",\"title\":\"" + title + "\",\"shared\":" + shared + ",\"tasks\":[";
		
		boolean first_cicle = true;
		for (Task task : this) {
			if (first_cicle) {
				jsonString += task.toJsonString();
				first_cicle = false;
			}
			else {
				jsonString += "," + task.toJsonString();
			}
		}
		
		return jsonString + "]}";
	}
	
	@Override
	public String toString() {
		return ownerUsername + " " + title + " " + shared;
	}
}
