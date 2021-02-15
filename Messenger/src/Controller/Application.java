package Controller;


import Model.*;

public class Application{
	//Application Interface view
	private UserInterface ui;
	
	private Conversation conversation;
	
	

	private DBLocal db;
	
	private SocketInternalNetwork socket;

	private Account loggedAccount;

	
	
	/**
	 * @param s
	 * @param w
	 * @param h
	 */
	public Application() {
		
		this.conversation = new Conversation(new Address(null,"test","test"));
		this.db = new DBLocal();
		
		this.ui=new UserInterface("Chat box", 300,300,db,this);
		ui.setVisible(true);

	}
	
	public void setSocket(SocketInternalNetwork so) {
		if(this.socket==null) {
			this.socket=so;
		}	
	}
	
	public void setUserListSocket() {
		
	}
	
	public UserInterface getUI() {
		return ui;
	}
	
	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}
	
	public SocketInternalNetwork getSocket() {
		return socket;
	}
	
	public Account getLoggedAccount() {
		return loggedAccount;
	}
	
	
	public void setLoggedAccount(Account loggedAccount) {
		this.loggedAccount=loggedAccount;
	}

}
