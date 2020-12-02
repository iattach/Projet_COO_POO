import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Model.*;

public class Main extends JFrame{
	//Application Interface view
	private ApplicationInterface app;
	
	private Conversation conversation;
	
	//private DBLocale db;
	/*private Account loggedAccount;
	
	//private ... server;
	
	private UserInterface userInterface;
	
	private InternalSocket socket;
	
	
	
	*/
	
	
	
	/**
	 * @param s
	 * @param w
	 * @param h
	 */
	public Main() {
		
		this.conversation = new Conversation(new Adress(null,"test","test"));
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
		this.app=new ApplicationInterface("Chat box", 300,300);
		app.setVisible(true);

	}
	
	public static void main(String[] args) {
		Main f=new Main();
		while(true) {}
	}
	
	/*public InternalSocket getSocket() {
		return socket;
	}
	
	public void setSocket(InternalSocket so) {
		this.socket=so;
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
	public UserInterface getUI() {
		return userInterface;
	}
	
	public void setLoggedAccount(Account acc) {
		this.loggedAccount=acc;
	}*/

}
