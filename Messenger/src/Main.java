import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.Panel;
import View.ViewPanel;

public class Main extends JFrame{
	
	Panel panel;
	ViewPanel view;
	
	/**
	 * @param s
	 * @param w
	 * @param h
	 */
	public Main(String s, int w, int h) {
		super(s);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		view=new ViewPanel();
		JPanel contenant = new JPanel();
		contenant.setLayout(new BorderLayout());

		panel = new Panel(view);
		panel.setPreferredSize(new Dimension(200,120));
		contenant.add(panel, BorderLayout.NORTH);

		contenant.add(new JScrollPane(view.getT()), BorderLayout.CENTER);
		this.setContentPane(contenant);
		this.setPreferredSize(new Dimension(w, h));
		this.setResizable(false);
		this.pack();

	}
	
	public static void main(String[] args) {
		
		Main f = new Main("Chat box", 300,300);
		f.setVisible(true);

	}

}
