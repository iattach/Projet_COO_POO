package Controller;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import View.ViewPanel;

public class UserInterface extends JFrame{
	
	Panel panel;
	ViewPanel view;
	JMenuBar jmb;
	
	//private DBCentrale dbc;
	
	public UserInterface(String s, int w, int h,DBLocal db,Application app) {
		super(s);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view=new ViewPanel();
		//panel
		JPanel contenant = new JPanel();
		contenant.setLayout(new BorderLayout());
		panel = new Panel(view,db,app);
		panel.setMinimumSize(new Dimension(200,150));
		contenant.add(panel, BorderLayout.NORTH);
		contenant.add(new JScrollPane(view.getT()), BorderLayout.CENTER);
		//menubar
		this.jmb=new Menu(view,panel);
		this.setJMenuBar(jmb);
		
		this.setContentPane(contenant);
		this.setPreferredSize(new Dimension(w, h));
		this.setResizable(true);
		this.pack();
		
		
	}

	
	public void update(String s) {
		this.panel.update(s);
	}
	
}
