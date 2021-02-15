package Controller;

import java.awt.BorderLayout;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;


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
import Model.Conversation;
import Model.Account;
import Model.TextModel;
import View.ViewPanel;

/**
 * @author machilus
 *
 */
@SuppressWarnings("serial")
public class UserInterfacePanel extends JPanel {

	// view components
	private ViewPanel vresult;
	private ViewPanel viewUsers;
	private TextModel tmodel;
	private TextModel tmodelUsers;
	private JPanel principle;
	private JTextField username;
	private JTextField nickName;
	private JPasswordField pass;
	private JTextArea message;
	// change nickname
	private JTextField nickName1;
	// message
	private JComboBox<String> onlineUsers;
	// Basic settings
	private DBLocal db;
	private Application app;

	private String users;
	private String displayName;
	private ArrayList<String> connectedUserList = new ArrayList<String>();

	/**
	 * @param vuer construteur qui initialiser tous les bouttons et texts
	 */
	@SuppressWarnings("deprecation")
	public UserInterfacePanel(ViewPanel view, ViewPanel viewUsers, DBLocal db, Application app) {
		this.app = app;
		this.db = db;

		// part showing information
		this.vresult = view;
		tmodel = new TextModel();
		// connect view with textmodel
		tmodel.addObserver(this.vresult);
		tmodel.initJTextArea();

		// show user list
		this.viewUsers = viewUsers;
		tmodelUsers = new TextModel();
		// connect view with textmodel
		tmodelUsers.addObserver(this.viewUsers);
		tmodelUsers.initJTextArea();
		tmodelUsers.setVisible(false);

		this.connect();

	}

	public void connect() {
		if (principle != null) {
			this.remove(principle);
		}

		/*
		 * JPanel principale qui rassemble tous les JPanel
		 */
		principle = new JPanel(new BorderLayout());

		/*
		 * partie pour login
		 */
		principle.add(new JLabel("Login"), BorderLayout.NORTH);
		JPanel op = new JPanel(new GridLayout(3, 2, 3, 3));
		/*
		 * pour collecter les identifiants
		 */
		op.add(new JLabel("UserName : " + "    ", JLabel.LEFT));
		username = new JTextField(10);
		op.add(username);

		op.add(new JLabel("Password : " + "    ", JLabel.LEFT));
		pass = new JPasswordField();
		op.add(pass);

		JButton p = new JButton("Sign In");
		p.addActionListener(new connectHandler());
		op.add(p);

		JButton p1 = new JButton("Sign Up");
		p1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				tmodel.setJ("Inscription");
				createAccount();

			}
		});
		op.add(p1);

		principle.add(op, BorderLayout.CENTER);

		this.add(principle);
		if(tmodelUsers != null) {
			setVisibleUsers(false);
		}
		this.revalidate();
	}

	public void signOut() {
		if (app.getLoggedAccount() != null) {
			// db
			//DBCentral dbCentral = new DBCentral(app.getLoggedAccount().getUsername());=============mark=============
			//dbCentral.pushToDBC();=============mark=============
			//dbCentral.close();
			// rzo
			app.getSocket().end();
			app.setLoggedAccount(null);

			//this.db.vanishDB();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}

		}

		tmodel.setJ("Sign out successfully !");

		// init connected userlist
		this.connectedUserList = new ArrayList<String>();

		this.remove(principle);

		this.connect();

		//this.add(principle);
		this.revalidate();
	}

	public void changeAccount() {
		tmodel.setJ("Attention !!! Your are changing your account.");
		this.remove(principle);

		this.connect();

		//this.add(principle);
		this.revalidate();
	}

	public void changeDisplayName() {
		if (app.getLoggedAccount() != null) {
			tmodel.setJ("Attention !!! Your are changing your display name.");
			this.remove(principle);

			/*
			 * JPanel principale qui rassemble tous les JPanel
			 */
			principle = new JPanel(new BorderLayout());

			/*
			 * partie pour login
			 */
			principle.add(new JLabel("Change nickname"), BorderLayout.NORTH);
			JPanel op = new JPanel(new GridLayout(3, 2, 3, 3));
			/*
			 * pour collecter les identifiants
			 */
			op.add(new JLabel("Nick Name : " + "    ", JLabel.LEFT));
			nickName1 = new JTextField(10);
			op.add(nickName1);

			JButton p = new JButton("Change");
			p.addActionListener(new changeNicknameHandler());
			op.add(p);

			principle.add(op, BorderLayout.CENTER);

			this.add(principle);
			if(tmodelUsers != null) {
				setVisibleUsers(false);
			}
			this.revalidate();
		} else {
			tmodel.setJ("Error : you should connect to your account for changing your nickname !!!");
			connect();
		}

	}

	public void createAccount() {
		if(app.getLoggedAccount()!=null) {
			//db
			//DBCentral dbc = new DBCentral(app.getLoggedAccount().getUsername());=============mark=============
			//dbc.pushToDBC();=============mark=============
			//rzo
			app.getSocket().end();
			app.setLoggedAccount(null);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//
		}
		tmodel.setJ("Inscription");
		this.remove(principle);

		/*
		 * JPanel principale qui rassemble tous les JPanel
		 */
		principle = new JPanel(new BorderLayout());

		/*
		 * partie pour login
		 */
		principle.add(new JLabel("Login"), BorderLayout.NORTH);
		JPanel op = new JPanel(new GridLayout(4, 2, 3, 3));
		/*
		 * pour collecter les identifiants
		 */
		op.add(new JLabel("UserName : " + "    ", JLabel.LEFT));
		username = new JTextField(10);
		op.add(username);

		op.add(new JLabel("Password : " + "    ", JLabel.LEFT));
		pass = new JPasswordField();
		op.add(pass);

		op.add(new JLabel("Nickname : " + "    ", JLabel.LEFT));
		nickName = new JTextField(10);
		op.add(nickName);

		JButton p = new JButton("Create account");
		p.addActionListener(new creationAccountHandler());
		op.add(p);

		principle.add(op, BorderLayout.CENTER);

		this.add(principle);
		if(tmodelUsers != null) {
			setVisibleUsers(false);
		}
		
		this.revalidate();
	}

	public void conversation() {
		if (app.getLoggedAccount() != null) {

			tmodel.setJ("Conversation : \n");
			tmodelUsers.setJ("Users Online : \n");
			this.remove(principle);

			/*
			 * JPanel principale qui rassemble tous les JPanel
			 */
			principle = new JPanel(new BorderLayout());

			/*
			 * partie pour login
			 */
			JPanel labelUsers = new JPanel(new GridLayout(1, 2, 1, 1));
			labelUsers.add(new JLabel("Message -> choose user"));
			onlineUsers = new JComboBox<String>();

			Collections.sort(connectedUserList, String.CASE_INSENSITIVE_ORDER);

			for (int i = 0; i < this.connectedUserList.size(); i++) {
				onlineUsers.addItem(connectedUserList.get(i));
			}
			this.updateUsers(connectedUserList);
			labelUsers.add(onlineUsers);
			principle.add(labelUsers, BorderLayout.NORTH);
			// JPanel op=new JPanel(new BorderLayout());
			// JPanel op=new JPanel(new GridLayout(3,1,1,1));
			/*
			 * pour collecter les identifiants
			 */

			message = new JTextArea();
			message.setLineWrap(true);
			message.setWrapStyleWord(true);
			message.setSize(250, 300);
			JScrollPane jv = new JScrollPane(message);
			jv.setSize(300, 100);
			jv.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			principle.add(jv, BorderLayout.CENTER);

			JButton p = new JButton("Send");
			p.addActionListener(new conversationHandler());

			principle.add(p, BorderLayout.SOUTH);
			principle.setSize(250, 200);

			this.add(principle);

			tmodelUsers.setVisible(true);

			this.revalidate();
		} else {
			tmodel.setJ("Error : you should connect to your account for doing conversation !!!");
			connect();
		}
	}

	public void update(String s) {
		tmodel.setJ(tmodel.getJ().getText() + s);
	}

	public void setVisibleUsers(Boolean visible) {
		this.tmodelUsers.setVisible(visible);
	}

	public void updateUsers(ArrayList<String> connectedUserList) {
		this.connectedUserList = connectedUserList;
		Collections.sort(connectedUserList, String.CASE_INSENSITIVE_ORDER);
		if(onlineUsers!=null) {
			onlineUsers.removeAllItems();
			String s = "Users Online : \n";
			for (int i = 0; i < this.connectedUserList.size(); i++) {
				s += connectedUserList.get(i) + "\n";
				onlineUsers.addItem(connectedUserList.get(i));
			}

			tmodelUsers.setJ(s);
		}

	}

	public void updateMessage(Message message,String sender,String receiver) {
		tmodel.setJ(tmodel.getJ().getText() 
				+	"From : "+sender+"\n"
				+	"To : "+receiver+"\n"
				+	"Time : "+message.getTimestamp() + "\n"
				+	"-> "+ message.getMessage()  + "\n");

	}

	public void closeApplicaiton() {
		this.signOut();
		if (app.getLoggedAccount() != null) {
			
			//DBCentral dbCentral = new DBCentral(app.getLoggedAccount().getUsername());=============mark=============
			//dbCentral.pushToDBC();=============mark=============
			//dbCentral.close();=============mark=============

		}
		System.exit(0);
	}

	public void conversationHistory() {
		if (app.getLoggedAccount() != null) {

			tmodel.setJ("Conversation history : \n");
			tmodelUsers.setVisible(false);
			this.remove(principle);

			/*
			 * JPanel principale qui rassemble tous les JPanel
			 */
			principle = new JPanel(new BorderLayout());

			/*
			 * partie pour login
			 */
			JPanel labelUsers = new JPanel(new GridLayout(3,1, 1, 1));
			labelUsers.add(new JLabel("Message history -> choose user"));
			onlineUsers = new JComboBox<String>();

			Collections.sort(connectedUserList, String.CASE_INSENSITIVE_ORDER);

			for (int i = 0; i < this.connectedUserList.size(); i++) {
				onlineUsers.addItem(connectedUserList.get(i));
			}
			labelUsers.add(onlineUsers);

			JButton p = new JButton("Search");
			p.addActionListener(new conversationHistoryHandler());

			labelUsers.add(p);

			principle.add(labelUsers, BorderLayout.CENTER);

			principle.setSize(250, 200);

			this.add(principle);

			this.revalidate();
		} else {
			tmodel.setJ("Error : you should connect to your account for get conversation history !!!");
			connect();
		}
	}

//==================================================================================
//==================== handlers ====================================================
	private class connectHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String user = username.getText();
			String password = pass.getPassword().toString();

			Account acc = db.getAccount(user, password);

			if (acc == null) {
				tmodel.setJ("Error of connection : account not found !!!");
			} else {
				// init connected userlist
				connectedUserList = new ArrayList<String>();

				acc.setAddress(new Address(acc.getNickname(), acc.getUsername())); // si on utilise getAccount2()
				// System.out.println(acc.getAddress().getIP()); //test
				app.setLoggedAccount(acc);
				//DBCentral dbc = new DBCentral(acc.getUsername());=============mark=============
				//dbc.pullDBC();=============mark=============
				
				/* while (!DBCentral.finPullDBC) { 
					 try { 
						 Thread.sleep(100); 
					 } catch(InterruptedException e1) {
						 e1.printStackTrace(); 
					 } 
				}=============mark=============
				 */
				// rzo
				app.setSocket(new SocketInternalNetwork(acc, app.getUI()));

				conversation();

			}

		}

	}

	private class creationAccountHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String user = username.getText();
			String password = pass.getPassword().toString();
			String nickname = null;
			if (!nickName.getText().isEmpty()) {
				nickname = nickName.getText();
			} else {
				nickname = user;
			}

			boolean unique_user =DBLocal.checkUsername(user);//DBCentral.checkUsername(user);// dbcentrale=============mark=============
			boolean unique_nick = DBLocal.checkNickname(nickname);//DBCentral.checkNickname(nickname);// dbcentrale=============mark=============

			if (unique_user && unique_nick) {
				Address add = null;
				Account acc = null;
				if (displayName == null) {
					add = new Address(user, user);
					acc = new Account(user, password, user, add);

				} else {
					add = new Address(nickname, user);
					acc = new Account(users, password, nickname, add);

				}

				db.setAccount(acc);
				//DBCentral.addAccount(acc);=============mark=============
				db.setKnownUser(add, acc.getUsername()); // il se connait lui-même
				tmodel.setJ("Your account " + acc.getNickname() + " has been created successfully !!!");
				connect();
			} else {
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

	private class changeNicknameHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String nickNameString = nickName1.getText();

			boolean unique = true;//DBCentral.checkNickname(nickNameString);// dbcentrale=============mark=============
			if (unique) {
				String old_name = app.getLoggedAccount().getNickname();
				// network
				app.getSocket().sendNewNickname(nickNameString, old_name);
				// logged account
				Address add = new Address(nickNameString, app.getLoggedAccount().getUsername());
				Account acc = new Account(app.getLoggedAccount().getUsername(), app.getLoggedAccount().getPassword(),
						nickNameString, add);
				app.setLoggedAccount(acc);
				// db
				// change nickname because he is himself
				db.updateNickName(nickNameString, app.getLoggedAccount().getUsername()); //
				//DBCentral dbc = new DBCentral(app.getLoggedAccount().getUsername());=============mark=============
				//dbc.changeNickname(app.getLoggedAccount().getUsername(), nickNameString);=============mark=============
				db.updateNicknameAccount(app.getLoggedAccount().getUsername(), nickNameString);

				tmodel.setJ("Your account's nickname has been changed successfully .");
				connectedUserList.remove(old_name);
				//connectedUserList.add(nickNameString);
				conversation();
				//updateUsers(connectedUserList);
			} else {
				tmodel.setJ("Error : the nickname already exists !!!");
				changeDisplayName();

			}

		}

	}

	private class conversationHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			String receiver = (String) onlineUsers.getSelectedItem();
			if(receiver!=null&&message.getText()!=null) {
				System.out.println("Panel : conversationHandler " + receiver);
				Timestamp tt = new Timestamp(System.currentTimeMillis());
				Message mes = new Message(true, message.getText(), tt);
				app.getConversation().addMessage(mes);
				app.getSocket().sendMessage(mes, receiver);
				tmodel.setJ(tmodel.getJ().getText() 
						+	"From : "+app.getLoggedAccount().getNickname()+"\n"
						+	"To : "+receiver+"\n"
						+	"Time : "+tt + "\n"
						+	"-> "+message.getText() + "\n");
				message.setText("");
			}
		}

	}

	private class conversationHistoryHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			String userSelected = (String) onlineUsers.getSelectedItem();
			ArrayList<Address> userAddrList = db.getknownUsers(app.getLoggedAccount().getUsername());
			String corresp = null;
			
			if(userSelected!=null) {
				for (int i=0;i<userAddrList.size();i++) {
					if (userSelected.equals(userAddrList.get(i).getNickname())) {
						corresp = userAddrList.get(i).getUsername();
					}
				}
				if (corresp == null) {
					
					Address add=null;
					//ConcurrentHashMap
					for (Map.Entry<String,Address> entry : app.getSocket().getUserList().entrySet()) { //ConcurrentHashMap
						 if(entry.getValue().getNickname().equals(userSelected)) {
							 add = entry.getValue();
						 }
						 
					}
					
					
					db.setKnownUser(add,app.getLoggedAccount().getUsername());
					app.setConversation(new Conversation(add));
					
					corresp=add.getUsername();
				}
				else {
					
					app.setConversation(db.getConversation(app.getLoggedAccount().getUsername(), corresp));
					//System.out.println("conversation "+co.getConversation().getDestinataire()+" "+co.getConversation().getConvSize());
				}

				Message[] m = app.getConversation().getAllMessages();
				tmodel.setJ(null);
				for (int i=0;i<app.getConversation().getConvSize();i++) {
					if (m[i].getIsSender()) {
						tmodel.setJ(tmodel.getJ().getText() 
								+	"From : "+app.getLoggedAccount().getNickname()+"\n"
								+	"To : "+userSelected+"\n"
								+	"Time : "+m[i].getTimestamp() + "\n"
								+	"-> "+m[i].getMessage() + "\n");
					}
					else {
						tmodel.setJ(tmodel.getJ().getText() 
								+	"From : "+userSelected+"\n"
								+	"To : "+app.getLoggedAccount().getNickname()+"\n"
								+	"Time : "+m[i].getTimestamp() + "\n"
								+	"-> "+m[i].getMessage() + "\n");
					}
					
				}
			}else {
				tmodel.setJ("Please choose a user before searching the messages !!!");
			}
			
						

		}

	}

	public void debug() {
		this.db.vanishDB();
		System.out.println("UserInterfacePanel : click debug detected");
	}
	public Application getApp() {
		return app;
	}

}
