import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.Menu;
import Controller.Panel;
import View.ViewPanel;

public class Main extends JFrame{
	
	Panel panel;
	ViewPanel view;
	JMenuBar jmb;
	
	/**
	 * @param s
	 * @param w
	 * @param h
	 */
	public Main(String s, int w, int h) {
		super(s);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view=new ViewPanel();
		//panel
		JPanel contenant = new JPanel();
		contenant.setLayout(new BorderLayout());
		panel = new Panel(view);
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
	
	public static void main(String[] args) {
		
		Main f = new Main("Chat box", 300,300);
		f.setVisible(true);
	}

}
