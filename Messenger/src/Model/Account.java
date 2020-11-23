package Model;

public class Account {
	 
	private String nickname;
	private String username;
	private String password;
	
	public Account(String nickname, String username, String password) {
		this.nickname = nickname;
		this.username = username;
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	//A IMPLEMENTER
	public boolean verifyPassword(String password) {
		return false;
	}
	
	//A IMPLEMENTER
	public boolean verifyNickname(String password) {
		return false;
	}
	
	//A IMPLEMENTER
	public boolean verifyNewNickname(String password) {
		return false;
	}
	
	

}
