

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Controller.Menu;
import Controller.Panel;
import View.ViewPanel;

public class ApplicationInterface extends JFrame{
	
	Panel panel;
	ViewPanel view;
	JMenuBar jmb;

	
	//private DBCentrale dbc;
	
	public ApplicationInterface(String s, int w, int h) {
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

	

	/*@SuppressWarnings("unused")
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Controller co = new Controller();
		
		/*System.out.println(Tools.getPcIP()[0]);
		System.out.println(Tools.getPcIP()[1]);
		System.out.println(Tools.getPcIP()[2]);
		System.out.println(Tools.getPcIP()[3]);
		
		co.db.printAllTable();*/
		
		/*java.util.ArrayList<String> m = co.getUI().cutMsg("aa aa dsf sd fsd fsdfsdfds sdfs df sdfsdfs sdfs dsdfs sdfsd fsdfs dfsdfsd sdfsdf sdfsdf sdfsdfs fsdfsd fsdf sdfsdf sdfsdfs dfsdfsd fsdf sfs dfs dfs dfs dfsd aa aa dsf sd fsd fsdfsdfds sdfs df sdfsdfs sdfs dsdfs sdfsd fsdfs dfsdfsd sdfsdf sdfsdf sdfsdfs fsdfsd fsdf sdfsdf sdfsdfs dfsdfsd fsdf sfs dfs dfs dfs dfsd aa aa dsf sd fsd fsdfsdfds sdfs df sdfsdfs sdfs dsdfs sdfsd fsdfs dfsdfsd sdfsdf sdfsdf sdfsdfs fsdfsd fsdf sdfsdf sdfsdfs dfsdfsd fsdf sfs dfs dfs dfs dfsd aa aa dsf sd fsd fsdfsdfds sdfs df sdfsdfs sdfs dsdfs sdfsd fsdfs dfsdfsd sdfsdf sdfsdf sdfsdfs fsdfsd fsdf sdfsdf sdfsdfs dfsdfsd fsdf sfs dfs dfs dfs dfsd ");
		for (int i=0;i<m.size();i++) {
			System.out.println(m.get(i));
		}*/
		
		/*try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Address simeon = new Address(null,"simeon_p","simeon_u");
		Address kevin = new Address(null,"kevin_p","kevin_u");
		Address yuyuan = new Address(null,"yuyuan_p","yuyuan_u");
		co.getSocket().getUserList().put("simeon_u", simeon);
		co.getSocket().getUserList().put("kevin_u", kevin);
		co.getSocket().getUserList().put("yuyuan_u", yuyuan);
		
		for (Map.Entry<String,Address> entry : co.getSocket().getUserList().entrySet()) {
			 System.out.println(entry.getValue().getPseudo());
			 
		}
		
		System.out.println(co.getSocket().getUserList().size());*/
		
		
		//test utililisateurs connectes//
		/*Address valentin = new Address(null,"Valentin","Valentin_u");
		Address simeon = new Address(null,"simeon","simeon_u");
		Address kevin = new Address(null,"kevin","kevin_u");
		Address yuyuan = new Address(null,"yuyuan","yuyuan_u");
		Address ds = new Address("D4rk-Sasuk3","ds_u");
		Address j = new Address(null,"jean","jean_u");
		Address t = new Address(null,"tom","tom_u");
		Address l = new Address(null,"leo","leo_u");
		Address y = new Address(null,"yan","yan_u");
		Address le = new Address(null,"lea","lea_u");
		Address em = new Address(null,"emma","emma_u");
		Address a = new Address(null,"a","a_u");
		Address b = new Address(null,"b","b_u");
		Address c = new Address(null,"c","c_u");
		Address d = new Address(null,"d","d_u");
		Address d1 = new Address(null,"d1","d1_u");
		Address d2 = new Address(null,"d2","d2_u");
		Address d3 = new Address(null,"d3","d3_u");
		Address d4 = new Address(null,"d4","d4_u");
		Address d5 = new Address(null,"d5","d5_u");
		Address d6 = new Address(null,"d6","d6_u");
		Address d7 = new Address(null,"d7","d7_u");
		ArrayList<Address> utilco = new ArrayList<Address>();
		ArrayList<Address> utilnco = new ArrayList<Address>();
		utilco.add(valentin);
		utilco.add(simeon);
		utilco.add(kevin);
		utilco.add(yuyuan);
		utilco.add(ds);
		utilco.add(j);
		utilco.add(t);
		utilco.add(l);
		utilco.add(y);
		utilco.add(le);
		utilco.add(em);
		utilco.add(a);
		utilco.add(b);
		utilco.add(c);
		utilnco.add(c);
		utilnco.add(d);
		utilnco.add(d1);
		utilnco.add(d2);
		utilnco.add(d3);
		utilnco.add(d4);
		utilnco.add(d5);
		utilnco.add(d6);
		utilnco.add(d7);
		co.userInterface.connectedUserList = utilco;
		co.userInterface.conversation_nc = utilnco;
		
		//test conversation
		co.conversation.setDestinataire(ds);
		co.conversation.addMessage(new Message(true, "bonjour",true));
		co.conversation.addMessage(new Message(false, "yo",true));
		co.conversation.addMessage(new Message(true, "ma phrase est très long exprès pour tester l'affichage des messages\n plutot long pas besoin de la lire jsuqu'au bout ça n'a pas trop d'interet hahahahahahah ahaha hahahah ahahahah ahahahahahahahahahahahaha",true));
		co.conversation.addMessage(new Message(false, "ma phrase aussi est très long exprès pour tester l'affichage des messages plutot long pas besoin de la lire jsuqu'au bout ça n'a pas trop d'interet hahahahahahah ahaha hahahah ahahahah ahahahahahahahahahahahaha",true));
		co.conversation.addMessage(new Message(true, "ok",true));
		//co.conversation.addMessage(new Message(true, "1234567890123456789012345678901234567890123456789012345678901234567890123456789",true));
		//co.conversation.addMessage(new Message(false, "1234567890123456789012345678901234567890123456789012345678901234567890123456789",true));
		//
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test1");
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test2");
		
		Address neww = new Address(null,"new","new_u");
		utilco.add(neww);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test3");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		co.userInterface.recevoirmessageUI(new Message(false,"je suis la",true));
		////
		
	*/	
		/*while (co.getLoggedAccount() == null) {
		try {
			Thread.sleep(10000);
			System.out.println("acc null");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		System.out.println("user not null");
		Address bbb = new Address("bbb_p","bbb_u");
		co.db.setKnownUser(bbb,co.getLoggedAccount().getUsername());
		Address ds = new Address("ds_p","ds_u");
		co.db.setKnownUser(ds,co.getLoggedAccount().getUsername());
		co.db.setMessage(new Message(false,"bonjour",true), "bbb_u", "123");
		co.db.setMessage(new Message(false,"hello",true), "ds_u", "123");
		ArrayList<Address> adr = co.db.getknownUsers(co.getLoggedAccount().getUsername());
		for (int i=0;i<adr.size();i++) {
			System.out.println(adr.get(i).getPseudo());
		}
		Address aaa = new Address("aaa_p","aaa_u");
		//co.userInterface.connectedUserList.add(aaa);
		try {
			Thread.sleep(20000);
			System.out.println("DONE");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		co.userInterface.connectedUserList.add(aaa);
		Message msg = new Message(false,"hahaha",true);
		co.userInterface.recevoirmessageUI(msg,aaa);
		//co.db.setMessage(msg, "aaa_u", "123");
		co.userInterface.connectedUserList.remove(aaa);*/
		/*try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Address valentin = new Address("Valentin","Valentin_u");
		Address simeon = new Address("simeon","simeon_u");
		Address kevin = new Address("kevin","kevin_u");
		Address yuyuan = new Address("yuyuan","yuyuan_u");
		Address ds = new Address("D4rk-Sasuk3","ds_u");
		Address j = new Address("jean","jean_u");
		Address t = new Address("tom","tom_u");
		Address l = new Address("leo","leo_u");
		Address y = new Address("yan","yan_u");
		Address le = new Address("lea","lea_u");
		Address em = new Address("emma","emma_u");
		Address bot = new Address("LeBotlan","bot");
		Address ia = new Address("iop","iop_u");
		Address st = new Address("steak","steak_u");
		Address fl = new Address("flop","flop_u");
		
		co.getSocket().connectedUserList.put("Valentin_u",valentin);
		co.getSocket().connectedUserList.put("simeon_u",simeon);
		//co.getSocket().connectedUserList.put("bot",bot);
		co.getSocket().connectedUserList.put("kevin_u",kevin);
		co.getSocket().connectedUserList.put("yuyuan_u",yuyuan);
		co.getSocket().connectedUserList.put("ds_u",ds);
		co.getSocket().connectedUserList.put("bot",bot);
		co.db.setKnownUser(j, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", j.getUsername());
		co.db.setKnownUser(t, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", t.getUsername());
		co.db.setKnownUser(l, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", l.getUsername());
		co.db.setKnownUser(y, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", y.getUsername());
		co.db.setKnownUser(le, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", le.getUsername());
		co.db.setKnownUser(em, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", em.getUsername());
		co.db.setKnownUser(ia, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", ia.getUsername());
		co.db.setKnownUser(st, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", st.getUsername());
		co.db.setKnownUser(fl, "sim");
		co.db.setMessage(new Message(true,"r"), "sim", fl.getUsername());
		co.getUI().UserMsgNonLu.add(ds);*/
		/*while(true) {}
	}*/
}
