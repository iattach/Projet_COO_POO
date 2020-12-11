package Model;

import java.sql.Timestamp; //format date (standard date SQL)

public class Message {
	Boolean isSender; // 1=Sender   0=recipients
	String message;
	Timestamp date;
	
	public Message(Boolean isSender, String message, Timestamp date) {

		this.isSender = isSender;
		this.message = message;
		this.date = date;
	}
		
	public Boolean getIsSender() {
		return isSender;
	}

	public void setIsSender(Boolean isSender) {
		this.isSender = isSender;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public Timestamp getTimestamp() {
		return date;
	}
	
	
}
