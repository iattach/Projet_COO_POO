package Controller;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Model.TextModel;
import View.ViewPanel;

public class Menu extends JMenuBar{
	private ViewPanel vresult;
	private TextModel tmodel;
	//----------------------------
	private JMenu menuSys,menuConv,menuDebug;
	private JMenuItem menuSys1,menuSys2,menuSys3,menuSys4,menuSys5;
	private JMenuItem menuConv1,menuConv2;
	/**
	 * @param vuer
	 * 	construteur qui initialiser tous les bouttons et texts
	 */
	public Menu (ViewPanel view,Panel pannel) {
		this.vresult=view;
		//---------------------------------------------------------------
		tmodel=new TextModel();
		/*
		 * lier le vue avec le modele de text 
		 */
		tmodel.addObserver(this.vresult);
		tmodel.initJTextArea();
		//--------------------------------------------------------------
		menuSys=new JMenu("System");
		menuConv=new JMenu("Conversation");
		menuDebug=new JMenu("Debug");
		menuDebug.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.debug();
			}
		});
        //-menuSys
		menuSys1=new JMenuItem("Sign out");
		menuSys1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.signOut();
			}
		});
		menuSys2=new JMenuItem("Change account");
		menuSys2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.changeAccount();
			}
		});
		menuSys3=new JMenuItem("Change display name");
		menuSys3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.changeDisplayName();
			}
		});
		menuSys4=new JMenuItem("Sign up");
		menuSys4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.createAccount();
			}
		});
		menuSys5=new JMenuItem("Close Application");
		menuSys5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.closeApplicaiton();
			}
		});
		
		//-menuConv
		menuConv1=new JMenuItem("Conversation");
		menuConv1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.conversation();
			}
		});
		menuConv2=new JMenuItem("History");
		menuConv2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pannel.conversationHistory();
			}
		});
		
		//---------------------------------------------------------------
		menuSys.add(menuSys1);
		menuSys.add(menuSys2);
		menuSys.add(menuSys3);
		menuSys.add(menuSys4);
		menuSys.add(menuSys5);
		
		menuConv.add(menuConv1);
		menuConv.add(menuConv2);
		
		this.add(menuSys);
		this.add(menuConv);
		this.add(menuDebug);
	}
	
}