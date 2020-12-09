package Controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Model.*;
import Model.Account;
import Model.TextModel;
import View.ViewPanel;

/**
 *Controleur pour controler tous les JPanel au gauche
 *
 */
@SuppressWarnings("serial")
public class Panel extends JPanel {
	private ViewPanel vresult;	
	private TextModel tmodel;
	private JPanel principle;
	private DBLocal db;
	private Application app;
	private String users;
	private String mdp;
	private String displayName;	
	private JTextField username;
	private JTextField nickName;
	private JPasswordField pass;
	/*change nickname*/
	private JTextField nickName1;
	private JTextField nickName2;
	/**
	 * @param vuer
	 * 	construteur qui initialiser tous les bouttons et texts
	 */
	@SuppressWarnings("deprecation")
	public Panel (ViewPanel view,DBLocal db, Application app) {
		this.app=app;
		this.vresult=view;
		this.db=db;
		tmodel=new TextModel();
		/*
		 * lier le vue avec le modele de text
		 */
		tmodel.addObserver(this.vresult);
		tmodel.initJTextArea();
		
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
		if(users!=null) {
			tmodel.setJ("Attention !!! Your are changing your display name.");
			this.remove(principle);
			
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
			op.add(new JLabel("Display Name : "+"    ",JLabel.LEFT));
			nickName1=new JTextField(10);
			op.add(nickName1);
			
			op.add(new JLabel("Confirmed : "+"    ",JLabel.LEFT));
			nickName2=new JTextField(10);
			op.add(nickName2);
			
			
			JButton p = new JButton("Changer");
			p.addActionListener(new ActionListener() {
				@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent e) {
					tmodel.setJ("Login");
					users=username.getText();
					mdp=pass.getText();
					username.setText("");
					pass.setText("");
					
				}
			});
			op.add(p);
			
			JButton p1 = new JButton("Sign Up");
			p1.addActionListener(new ActionListener() {
				@SuppressWarnings("deprecation")
				public void actionPerformed(ActionEvent e) {
					createAccount();
					
				}
			});
			op.add(p1);
			
			principle.add(op,BorderLayout.CENTER);
			
			this.add(principle);
			
			this.revalidate();
		}else {
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
		JTextField nickName=new JTextField(10);
		op.add(nickName);
		
		JButton p = new JButton("Create account");
		p.addActionListener(new creationAccountHandler());
		op.add(p);
		
		principle.add(op,BorderLayout.CENTER);
		
		this.add(principle);
		
		this.revalidate();
	}
	
	
	public void update(String s) {
		tmodel.setJ(tmodel.getJ().getText()+s);
	}
	public void closeApplicaiton(){
		System.exit(0);
	}
	
	private class connectHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String user = username.getText();
			String password = pass.getText();
			String nickname= "test";
			Address add = new Address( nickname,user);
			Account acc =  new Account(user,password,nickname,add);
			
			
			app.setSocket(new SocketInternalNetwork(acc,app.getUI()));
			
			
		}
		
	}
	private class creationAccountHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			boolean unique_u = true;//DBCentrale.checkUsername(username_);
			boolean unique_p = true;//DBCentrale.checkPseudo(pseudo_);
			
			if (unique_u && unique_p) {
				Address add = new Address( displayName,users);
				Account acc =  new Account(users,mdp,displayName,add);
				
				//db.setAccount(acc);
				//DBCentrale.addAccount(acc);
				//db.setKnownUser(add,co.getLoggedAccount().getUsername());
				//db.setKnownUser(add,acc.getUsername()); //il se connait lui-même
				tmodel.setJ("Your account "+displayName+" has been created successfully !!!");
				connect();
			}
			else {
				if (!unique_u && !unique_p) {
					tmodel.setJ("Error : account already exists !!!");
				}
				if (!unique_u) {
					tmodel.setJ("Error : username already exists !!!");
				}
				if (!unique_p) {
					tmodel.setJ("Error : password already exists !!!");
				}

			}
			
		}
		
	}
	private class modifyNicknameHandler implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(nickName1.equals(nickName2)) {
				String nickName1String = nickName1.getText();
				boolean unique = true;//DBCentrale.checkPseudo(pseudo_);
				if (unique) {
					/*String old_psdo = co.getLoggedAccount().getPseudo();
					//rzo
					co.getSocket().sendNewPseudo(pseudo_, old_psdo);
					//logged account
					Address add=new Address(pseudo_,co.getLoggedAccount().getUsername());
					Account acc=new Account(co.getLoggedAccount().getUsername(),co.getLoggedAccount().getPassword(),pseudo_,add);
					co.setLoggedAccount(acc);
					//db
					db.updatePseudo(pseudo_, co.getLoggedAccount().getUsername()); //modifier le pseudo de son compte dans knownUsers (car on se connait soi-meme)
					DBCentrale dbc = new DBCentrale(co.getLoggedAccount().getUsername());
					dbc.changePseudo(co.getLoggedAccount().getUsername(), pseudo_);
					db.updatePseudoAccount(co.getLoggedAccount().getUsername(), pseudo_);
					*/
					
					tmodel.setJ("Your account's nickname has been changed successfully .");
					//changerpseudopage.updateUI();
				}
				else {
					tmodel.setJ("Error : the nickname already exists !!!");
					changeDisplayName();

				}
			}else {
				tmodel.setJ("Error : the nickname should be the same !!!");
				changeDisplayName();
			}
			
		}
		
	}
}
