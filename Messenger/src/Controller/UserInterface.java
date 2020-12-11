package Controller;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Model.Address;
import Model.Message;
import View.ViewPanel;

public class UserInterface extends JFrame{
	
	Panel panel;
	ViewPanel view;
	ViewPanel viewUsers;
	JMenuBar jmb;
	JScrollPane jv;
	JScrollPane jvu;
	//private DBCentrale dbc;
	
	public UserInterface(String s, int w, int h,DBLocal db,Application app) {
		super(s);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view=new ViewPanel();
		viewUsers=new ViewPanel();
		//panel
		JPanel contenant = new JPanel();
		contenant.setLayout(new BorderLayout());
		panel = new Panel(view,viewUsers,db,app);
		panel.setMinimumSize(new Dimension(200,150));
		contenant.add(panel, BorderLayout.NORTH);
		
		jv=new JScrollPane(view.getT());
		jv.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jv.setMinimumSize(new Dimension(270,150));
		contenant.add(jv, BorderLayout.CENTER);
		
		jvu=new JScrollPane(viewUsers.getT());
		jvu.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		jvu.setMinimumSize(new Dimension(30,150));
		contenant.add(jvu, BorderLayout.EAST);
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


	public void updateUsers(ConcurrentHashMap<String, Address> listConnectedUsers) {
		ArrayList<String> connectedUserList=new ArrayList<String>();
		for (Map.Entry<String,Address> entry : listConnectedUsers.entrySet()) { 
			connectedUserList.add(entry.getValue().getNickname());
		}
		this.panel.updateUsers(connectedUserList);
	}
	
	public void updateMessage(Message message) {
		this.panel.updateMessage(message);
	}

	
}
