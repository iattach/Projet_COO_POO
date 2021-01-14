package View;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import Model.TextModel;

@SuppressWarnings({"serial","deprecation"})
public class ViewPanel extends JPanel implements Observer {
	private TextModel t;

	public ViewPanel() {
		this.t = new TextModel();
	}

	
	@Override
	public void update(Observable o, Object arg1) {
		// TODO Auto-generated method stub
		this.t = (TextModel) o;

	}

	public JTextArea getT() {
		return this.t.getJ();
	}
}