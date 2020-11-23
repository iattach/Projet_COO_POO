package Controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
public class Panel extends JPanel{
	private ViewPanel vresultat;	
	private TextModel tmodel;
	//private Requete r;
	private JComboBox<String> question;
	private String users;
	private String mdp;
	
	/**
	 * @param vuer
	 * 	construteur qui initialiser tous les bouttons et texts
	 */
	public Panel (ViewPanel vuer) {
		this.vresultat=vuer;
		tmodel=new TextModel();
		/*
		 * lier le vue avec le modele de text à gauche
		 */
		tmodel.addObserver(this.vresultat);
		tmodel.initJTextArea();
		/*
		 * JPanel principale qui rassemble tous les JPanel
		 */
		JPanel principale=new JPanel(new BorderLayout());
		
		/*
		 * partie pour login
		 */
		principale.add(new JLabel("Login"),BorderLayout.NORTH);
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
		
		principale.add(op,BorderLayout.CENTER);
		
		this.add(principale);
		
		
		
	}
	
	
}
