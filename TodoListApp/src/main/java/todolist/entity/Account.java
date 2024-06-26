package todolist.entity;

import org.json.JSONObject;

public class Account {
	
	private String username;
	private String password;
	
	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String toJSONString() {
		return new JSONObject(this).toString();
	}

	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + "]";
	}
	
}
