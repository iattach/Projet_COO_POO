package Controller;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Model.*;

public class Application{
	//Application Interface view
	private UserInterface ui;
	
	private Conversation conversation;
	
	private DBLocal db;
	
	private SocketInternalNetwork socket;
	/*private Account loggedAccount;
	
	//private ... server;	
	
	
	*/
	
	
	
	/**
	 * @param s
	 * @param w
	 * @param h
	 */
	public Application() {
		
		this.conversation = new Conversation(new Address(null,"test","test"));
		this.db = new DBLocal();
		//this.db = new DBLocale();
		/*//test utililisateurs connectes//
		Address valentin = new Address(null,"Valentin_p","Valentin_u");
		Address simeon = new Address(null,"simeon_p","simeon_u");
		ArrayList<Address> utilco = new ArrayList<Address>();
		utilco.add(valentin);
		utilco.add(simeon);
		.0
		//*/
		/*
		this.db = new DBLocale();
		
		this.userInterface = new UserInterface(/*this,this.db);
		this.userInterface.co = this;
		this.userInterface.db = this.db;
		DBCentrale.InitPullAccount();
		*/
		//this.dbc = new DBCentrale();
		this.ui=new UserInterface("Chat box", 300,300,db,this);
		ui.setVisible(true);

	}
	
	public void setSocket(SocketInternalNetwork so) {
		this.socket=so;
	}
	
	public UserInterface getUI() {
		return ui;
	}
	/*public InternalSocket getSocket() {
		return socket;
	}
	
	
	
	public Conversation getConversation() {
		return conversation;
	}
	
	public void setConversation(Conversation conv) {
		this.conversation=conv;
	}
	
	public Account getLoggedAccount() {
		return loggedAccount;
	}
	
	
	public void setLoggedAccount(Account acc) {
		this.loggedAccount=acc;
	}*/

}