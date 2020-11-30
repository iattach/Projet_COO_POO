package Controller;

import java.awt.BorderLayout;
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
	//private Requete r;
	private String users;
	private String mdp;
	private String display;
	//private 
	private String displayName;
	private String mdp1;
	/**
	 * @param vuer
	 * 	construteur qui initialiser tous les bouttons et texts
	 */
	public Panel (ViewPanel view) {
		this.vresult=view;
		tmodel=new TextModel();
		/*
		 * lier le vue avec le modele de text
		 */
		tmodel.addObserver(this.vresult);
		tmodel.initJTextArea();
		
		this.connect();
		
		
	}
	public void connect() {
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
		JTextField username=new JTextField(10);
		op.add(username);
		
		op.add(new JLabel("Password : "+"    ",JLabel.LEFT));
		JPasswordField pass=new JPasswordField();
		op.add(pass);
		
		
		JButton p = new JButton("Sign In");
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
				tmodel.setJ("Inscription");
				users=username.getText();
				mdp=pass.getText();
				username.setText("");
				pass.setText("");
				
			}
		});
		op.add(p1);
		
		principle.add(op,BorderLayout.CENTER);
		
		this.add(principle);
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
		JTextField username=new JTextField(10);
		op.add(username);
		
		op.add(new JLabel("Confirmed : "+"    ",JLabel.LEFT));
		JPasswordField pass=new JPasswordField();
		op.add(pass);
		
		
		JButton p = new JButton("Sign In");
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
				tmodel.setJ("Inscription");
				users=username.getText();
				mdp=pass.getText();
				username.setText("");
				pass.setText("");
				
			}
		});
		op.add(p1);
		
		principle.add(op,BorderLayout.CENTER);
		
		this.add(principle);
		
		this.revalidate();
	}
	
	public void createAccount() {
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
		JPanel op=new JPanel(new GridLayout(4,2,3,3));
		/*
		 * pour collecter les identifiants 
		 */
		op.add(new JLabel("UserName : "+"    ",JLabel.LEFT));
		JTextField username=new JTextField(10);
		op.add(username);
		
		op.add(new JLabel("Password : "+"    ",JLabel.LEFT));
		JPasswordField pass=new JPasswordField();
		op.add(pass);
		
		op.add(new JLabel("Display Name : "+"    ",JLabel.LEFT));
		JTextField displayName=new JTextField(10);
		op.add(displayName);
		
		JButton p = new JButton("Create account");
		p.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tmodel.setJ("Login");
				users=username.getText();
				mdp=new String(pass.getPassword());
				display=displayName.getText();
				username.setText("");
				pass.setText("");
				displayName.setText("");
				tmodel.setJ("Create account successfully !!!");
			}
		});
		op.add(p);
		
		principle.add(op,BorderLayout.CENTER);
		
		this.add(principle);
		
		this.revalidate();
	}
	public void closeApplicaiton(){
		System.exit(0);
	}
	
	
}
