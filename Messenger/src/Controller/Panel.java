package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Model.Message;
import Model.Address;
import Model.Account;
import Model.TextModel;
import View.ViewPanel;

/**
 * @author machilus
 *
 */
@SuppressWarnings("serial")
public class Panel extends JPanel {
	
	//view components
	private ViewPanel vresult;	
	private ViewPanel viewUsers;
	private TextModel tmodel;
	private TextModel tmodelUsers;
	private JPanel principle;
	private JTextField username;
	private JTextField nickName;
	private JPasswordField pass;
	private JTextArea message;
	//change nickname
	private JTextField nickName1;
	//message
	private JComboBox<String> onlineUsers;
	//Basic settings
	private DBLocal db;
	private Application app;
	private String users;
	private String mdp;
	private String displayName;	
	private	ArrayList<String> connectedUserList = new ArrayList<String>();
	/**
	 * @param vuer
	 * 	construteur qui initialiser tous les bouttons et texts
	 */
	public Panel (ViewPanel view, ViewPanel viewUsers, DBLocal db, Application app) {
		this.app=app;
		this.db=db;
		
		//part showing information 
		this.vresult=view;
		tmodel=new TextModel();
		// connect view with textmodel
		tmodel.addObserver(this.vresult);
		tmodel.initJTextArea();
		
		//show user list
		this.viewUsers=viewUsers;
		tmodelUsers=new TextModel();
		// connect view with textmodel
		tmodelUsers.addObserver(this.viewUsers);
		tmodelUsers.initJTextArea();
		tmodelUsers.setVisible(false);
		
		this.connect();
		
		
	}
	public void connect() {
		if(principle!=null) {
			this.remove(principle);
		}
		
		/*
		 * JPanel principale qui rassemble tous les JPanel
		 */
		principle=new JPanel(new BorderLayout());
		
		/*
		 * partie pour login
		 */
		principle.add(new JLabel("Login"),BorderLayout.NORTH);
		JPanel op=new JPanel(new GridLayout(3,2,3,3));
		/*
		 * pour collecter les identifiants 
		 */
		op.add(new JLabel("UserName : "+"    ",JLabel.LEFT));
		username=new JTextField(10);
		op.add(username);
		
		op.add(new JLabel("Password : "+"    ",JLabel.LEFT));
		pass=new JPasswordField();
		op.add(pass);
		
		
		JButton p = new JButton("Sign In");
		p.addActionListener(new connectHandler());
		op.add(p);
		
		JButton p1 = new JButton("Sign Up");
		p1.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				
				tmodel.setJ("Inscription");
				createAccount();
				
			}
		});
		op.add(p1);
		
		principle.add(op,BorderLayout.CENTER);
		
		this.add(principle);
		this.revalidate();
	}
	public void signOut() {
		tmodel.setJ("Sign out successfully !");
		this.remove(principle);
		
		this.connect();
		
		this.add(principle);
		this.revalidate();
	}
	public void changeAccount() {
		tmodel.setJ("Attention !!! Your are changing your account.");
		this.remove(principle);
		
		this.connect();
		
		this.add(principle);
		this.revalidate();
	}
	public void changeDisplayName() {
		if(app.getLoggedAccount()!=null) {
			tmodel.setJ("Attention !!! Your are changing your display name.");
			this.remove(principle);
			
			/*
			 * JPanel principale qui rassemble tous les JPanel
			 */
			principle=new JPanel(new BorderLayout());
			
			/*
			 * partie pour login
			 */
			principle.add(new JLabel("Change nickname"),BorderLayout.NORTH);
			JPanel op=new JPanel(new GridLayout(3,2,3,3));
			/*
			 * pour collecter les identifiants 
			 */
			op.add(new JLabel("Nick Name : "+"    ",JLabel.LEFT));
			nickName1=new JTextField(10);
			op.add(nickName1);
			
			
			JButton p = new JButton("Change");
			p.addActionListener(new changeNicknameHandler());
			op.add(p);
			
			principle.add(op,BorderLayout.CENTER);
			
			this.add(principle);
			
			this.revalidate();
		}else {
			tmodel.setJ("Error : you should connect to your account for changing your nickname !!!");
			connect();
		}
		
	}
	
	public void createAccount() {
		tmodel.setJ("Inscription");
		this.remove(principle);
		
		/*
		 * JPanel principale qui rassemble tous les JPanel
		 */
		principle=new JPanel(new BorderLayout());
		
		/*
		 * partie pour login
		 */
		principle.add(new JLabel("Login"),BorderLayout.NORTH);
		JPanel op=new JPanel(new GridLayout(4,2,3,3));
		/*
		 * pour collecter les identifiants 
		 */
		op.add(new JLabel("UserName : "+"    ",JLabel.LEFT));
		username=new JTextField(10);
		op.add(username);
		
		op.add(new JLabel("Password : "+"    ",JLabel.LEFT));
		pass=new JPasswordField();
		op.add(pass);
		
		op.add(new JLabel("Display Name : "+"    ",JLabel.LEFT));
		nickName=new JTextField(10);
		op.add(nickName);
		
		JButton p = new JButton("Create account");
		p.addActionListener(new creationAccountHandler());
		op.add(p);
		
		principle.add(op,BorderLayout.CENTER);
		
		this.add(principle);
		
		this.revalidate();
	}
	public void conversation() {
		
		tmodel.setJ("Conversation history");
		tmodelUsers.setJ("Users Online : ");
		this.remove(principle);
		
		/*
		 * JPanel principale qui rassemble tous les JPanel
		 */
		principle=new JPanel(new BorderLayout());
		
		/*
		 * partie pour login
		 */
		JPanel labelUsers = new JPanel(new GridLayout(1,2,1,1));
		labelUsers.add(new JLabel("Message -> choose user"));
		onlineUsers=new JComboBox<String>();
		
		Collections.sort(connectedUserList, String.CASE_INSENSITIVE_ORDER);

		for(int i=0; i<this.connectedUserList.size();i++) {
			onlineUsers.addItem(connectedUserList.get(i));
		}
		labelUsers.add(onlineUsers);
		principle.add(labelUsers,BorderLayout.NORTH);
		//JPanel op=new JPanel(new BorderLayout());
		//JPanel op=new JPanel(new GridLayout(3,1,1,1));
		/*
		 * pour collecter les identifiants 
		 */
		       
		message=new JTextArea();
		message.setLineWrap(true);        
		message.setWrapStyleWord(true);    
		message.setSize(250, 300);
		JScrollPane jv=new JScrollPane(message);
		jv.setSize(300, 100);
		jv.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		principle.add(jv, BorderLayout.CENTER);
		
		JButton p = new JButton("Send");
		p.addActionListener(new conversationHandler());

		
		principle.add(p,BorderLayout.SOUTH);
		principle.setSize(250,200);

		this.add(principle);
		
		tmodelUsers.setVisible(true);
		
		this.revalidate();
	}
	
	
	public void update(String s) {
		tmodel.setJ(tmodel.getJ().getText()+s);
	}
	public void setVisibleUsers(Boolean visible) {
		this.tmodelUsers.setVisible(visible);
	}
	public void updateUsers(ArrayList<String> connectedUserList) {
		this.connectedUserList=connectedUserList;
		Collections.sort(connectedUserList, String.CASE_INSENSITIVE_ORDER);
		onlineUsers.removeAllItems();
		
		String s="Users Online : \n";
		for(int i=0;i<this.connectedUserList.size();i++) {
			s+=connectedUserList.get(i)+"\n";
			onlineUsers.addItem(connectedUserList.get(i));
		}
		
		tmodelUsers.setJ(s);
		
	}
	public void updateMessage(Message message) {
		
		tmodel.setJ(tmodel.getJ().getText()+"\n"+message.getMessage()+"\n"+message.getTimestamp());
		
	}
	public void closeApplicaiton(){
		System.exit(0);
	}
//==================================================================================
//==================== handlers ====================================================
	private class connectHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String user = username.getText();
			String password = pass.getPassword().toString();
			
			Account acc =  db.getAccount(user, password);
			
			if (acc == null) {
				tmodel.setJ("Error of connection : account not found !!!");
			}
			else {
				
				acc.setAddress(new Address(acc.getNickname(),acc.getUsername())); //si on utilise getAccount2()
				//System.out.println(acc.getAddress().getIP()); //test
				app.setLoggedAccount(acc);
				//DBCentrale DBc  = new DBCentrale(acc.getUsername());
				//DBc.PullDB();
				/*while (!DBCentrale.finPullDB) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						
						e1.printStackTrace();
					}
				}*/
				//rzo
				app.setSocket(new SocketInternalNetwork(acc,app.getUI()));
				
				
				conversation();

			}
			
		}
		
	}
	private class creationAccountHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String user = username.getText();
			String password = pass.getPassword().toString();
			String nickname = null;
			if(!nickName.getText().isEmpty()) {
				nickname= nickName.getText();
			}else {
				nickname=user;
			}
			
			boolean unique_user = DBLocal.checkUsername(user);//dbcentrale
			boolean unique_nick = DBLocal.checkNickname(nickname);//dbcentrale
			
			if (unique_user && unique_nick) {
				Address add = null ; 
				Account acc =  null;
				if(displayName==null) {
					add = new Address( user,user);
					acc =  new Account(user,password,user,add);
					
				}else {
					add = new Address( nickname,user);
					acc =  new Account(users,password,nickname,add);
					
				}
				
				
				db.setAccount(acc);
				//DBCentrale.addAccount(acc);
				//db.setKnownUser(add,app.getLoggedAccount().getUsername());
				db.setKnownUser(add,acc.getUsername()); //il se connait lui-même
				tmodel.setJ("Your account "+acc.getNickname()+" has been created successfully !!!");
				connect();
			}
			else {
				if (!unique_user && !unique_nick) {
					tmodel.setJ("Error : account already exists !!!");
				}
				if (!unique_user) {
					tmodel.setJ("Error : username already exists !!!");
				}
				if (!unique_nick) {
					tmodel.setJ("Error : nickname already exists !!!");
				}

			}
			
		}
		
	}
	private class changeNicknameHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {

			String nickNameString = nickName1.getText();
			
			boolean unique = DBLocal.checkNickname(nickNameString);//dbcentrale
			if (unique) {
				String old_name = app.getLoggedAccount().getNickname();
				//network
				app.getSocket().sendNewNickname(nickNameString, old_name);
				//logged account
				Address add=new Address(nickNameString,app.getLoggedAccount().getUsername());
				Account acc=new Account(app.getLoggedAccount().getUsername(),app.getLoggedAccount().getPassword(),nickNameString,add);
				app.setLoggedAccount(acc);
				//db
				//change nickname because he is himself 
				db.updateNickName(nickNameString, app.getLoggedAccount().getUsername()); //
				//DBCentrale dbc = new DBCentrale(app.getLoggedAccount().getUsername());
				//dbc.changePseudo(app.getLoggedAccount().getUsername(), nickNameString);
				db.updateNicknameAccount(app.getLoggedAccount().getUsername(), nickNameString);
				
				
				tmodel.setJ("Your account's nickname has been changed successfully .");
				connectedUserList.remove(old_name);
				connectedUserList.add(nickNameString);
				conversation();
				updateUsers(connectedUserList);
			}
			else {
				tmodel.setJ("Error : the nickname already exists !!!");
				changeDisplayName();

			}
			
			
		}
		
	}
	private class conversationHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String receiver = (String)onlineUsers.getSelectedItem();
			System.out.println("Panel : conversationHandler "+receiver);
			Message mes=new Message(true,message.getText(),new Timestamp(System.currentTimeMillis()));
			app.getConversation().addMessage(mes);
			app.getSocket().sendMessage(mes,receiver);
			tmodel.setJ(message.getText());
			message.setText("");
			
		}
		
	}
	public void debug() {
		this.db.vanishDB();
	}
	
}
