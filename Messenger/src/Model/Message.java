package Model;

import java.sql.Timestamp; //format date (standard date SQL)

public class Message {
	Boolean isSender; // 1=Sender   0=recipients
	String message_;
	Timestamp date;
	
	public Message(Boolean isSender, String message_, Timestamp date) {

		this.isSender = isSender;
		this.message_ = message_;
		this.date = date;
	}
		
	public Boolean getIsSender() {
		return isSender;
	}

	public void setIsSender(Boolean isSender) {
		this.isSender = isSender;
	}

	public String getMessage_() {
		return message_;
	}

	public void setMessage_(String message_) {
		this.message_ = message_;
	}

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}


	
	
}
