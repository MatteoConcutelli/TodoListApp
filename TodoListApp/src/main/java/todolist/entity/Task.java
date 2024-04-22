package todolist.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Task {
	
	public static enum DL {
		TODAY, TOMORROW, NEXTWEEK
	}
	
	public static enum Priority {
		LOW, MEDIUM, HIGH
	}
	
	private String ownerUsername;
	private String ownerListTitle;
	
	private String name;
	private String description;
	private Priority priority;
	
	private MyDate creationDate;
	
	private boolean isDone = false;
	
	private DL dl;
	
	
	public Task(String ownerUsername, String ownerListTitle, String name, String description, Priority priority, MyDate creationDate, boolean isDone, DL dl) {
		super();
		this.ownerUsername = ownerUsername;
		this.ownerListTitle = ownerListTitle;
		
		this.name = name;
		this.description = description;
		this.priority = priority;
		
		this.creationDate = creationDate;
		
		this.isDone = isDone;
		
		this.dl = dl;
	
	}
	
	
	public void setIsDone(){
		this.isDone = true;
	}
	
	public String getOwnerUsername() {
		return ownerUsername;
	}
	
	public String getOwnerListTitle() {
		return ownerListTitle;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Priority getPriority() {
		return priority;
	}
	
	public MyDate getCreationDate() {
		return creationDate;
	}
	
	public boolean getIsDone() {
		return isDone;
	}
	
	public DL getDl() {
		return dl;
	}
	
	
	public static Task fromJSON(String jsonString) throws JSONException {
		JSONObject jsonObject = new JSONObject(jsonString);

		if(!jsonObject.has("ownerUsername") || !jsonObject.has("ownerListTitle") || !jsonObject.has("name") 
				|| !jsonObject.has("description") || !jsonObject.has("priority")
				|| !jsonObject.has("creationDate") || !jsonObject.has("isDone")
			    || !jsonObject.has("dl"))
			throw new JSONException("name and isDone format");
		
		String ownerUsername = jsonObject.getString("ownerUsername");
		String ownerListTitle = jsonObject.getString("ownerListTitle");
		String name = jsonObject.getString("name");
		String description = jsonObject.getString("description");
		Priority priority = Priority.valueOf(jsonObject.getString("priority"));
		
		MyDate creationDate = MyDate.valueOf(jsonObject.getString("creationDate"));
				
		boolean isDone = jsonObject.getBoolean("isDone");
		
		DL dl = DL.valueOf(jsonObject.getString("dl"));
		
		return new Task(ownerUsername, ownerListTitle, name, description, priority, creationDate, isDone, dl);
		
	}
	
	public String toJsonString() {
		return "{\"ownerUsername\":\"" + ownerUsername + "\",\"ownerListTitle\":\"" + ownerListTitle + "\",\"name\":\"" + name + "\",\"description\":\"" + description + 
				"\",\"priority\":\"" + priority + "\",\"creationDate\":\"" + creationDate +
				"\",\"isDone\":\"" + isDone + "\",\"dl\":\"" + dl + "\"}";
	}
	
	@Override
	public String toString() {
		return "Task [name=" + name + ", description=" + description + ", priority=" + priority + ", creationDate="
				+ creationDate + ", isDone=" + isDone + ", dl=" + dl + "]";
	}
	
}
