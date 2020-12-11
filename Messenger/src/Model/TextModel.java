package Model;

import java.util.Observable;

import javax.swing.JTextArea;

public class TextModel extends Observable{
	private JTextArea j;
	/**
	 * pour les résultats qui affiche à droit 
	 */
	public TextModel() {

	}
	
	public void initJTextArea() {
		this.j=new JTextArea();
		j.setEditable(false);
		setChanged();
		notifyObservers();
		j.setLineWrap(true);
		j.setWrapStyleWord(true);
	}
	public JTextArea getJ() {
		return j;
	}
	public void setJ(String t) {
		this.j.setText(t);
		setChanged();
		notifyObservers();
	}
	
	public void setVisible(Boolean visible) {
		this.j.setVisible(visible);
		setChanged();
		notifyObservers();
	}
	
	
}