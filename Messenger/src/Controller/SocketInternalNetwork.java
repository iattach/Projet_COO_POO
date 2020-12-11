package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Model.Message;
import Model.InstanceTool;
import Model.Account;
import Model.Address;



public class SocketInternalNetwork {
	
	ConcurrentHashMap<String,Address> connectedUserList = new ConcurrentHashMap<String,Address>(); // Need to be synchronized
	
	protected final Account accountLogged;
	protected static final int MAX_CHAR = 300;
	
	DatagramSocket UDP_SEND_Socket;
	ThreadReceiverUDP UDP_RCV_Thread;
	ThreadReceiverTCP TCP_RCV_Thread;
	protected DBLocal db;
	protected UserInterface ui;
	//protected static Long ts;
	
	
	
	public SocketInternalNetwork(Account accountLogged, UserInterface ui){
		this.accountLogged = accountLogged;
		this.connectedUserList = new ConcurrentHashMap<String,Address>();
		this.db = new DBLocal();
		this.ui = ui;
		try {
			//InstanceTool.PortNumber.UDP_SEND_PORT.getValue()
			//Integer.parseInt(accountLogged.getUsername())+1
			this.UDP_SEND_Socket = new DatagramSocket(InstanceTool.PortNumber.UDP_SEND_PORT.getValue());
		} catch (SocketException e) {
			System.out.println("Internal Socket: Error init UDP socket in constructor");
			e.printStackTrace();
		}
		this.startReceiverThread();
		this.sendConnected(accountLogged);
	}
	
	
	public void sendConnected(Account userOnline) {
		while (!UDP_RCV_Thread.isAlive() ) {
			
		}
		try {
			this.UDP_SEND_Socket.setBroadcast(true);
			
		} catch (SocketException e) {
			System.out.println("InternalSocket: Error sendConnected");
			e.printStackTrace();
		}
		System.out.println("InternalSocket: BROADCAST SENT");
		String message = InstanceTool.Ident_Code.Connected.toString() + "\n" + userOnline.getNickname() + "\n" + userOnline.getUsername() + "\n" + (new Timestamp(System.currentTimeMillis())).toString();
		try {
			/*Integer.parseInt(accountLogged.getUsername())*/
			List<InetAddress> listBroadAddress=this.listAllBroadcastAddresses();			
			for(int i=0; i<listBroadAddress.size();i++) {
				DatagramPacket outPacket = new DatagramPacket(message.getBytes(),message.length(),listAllBroadcastAddresses().get(i),InstanceTool.PortNumber.UDP_RCV_PORT.getValue());
				this.UDP_SEND_Socket.send(outPacket);
			}
			System.out.println("InternalSocket : Infomation of User account has been sent");
		} catch ( IOException e) {
			System.out.println("InternalSocket: Error dans sendConnected");
			e.printStackTrace();
		}
		
		
	}
	public void sendNewNickname(String newNickname, String oldNickname) {
		try {
			this.UDP_SEND_Socket.setBroadcast(true);
			String message = InstanceTool.Ident_Code.New_Name.toString() + "\n" + newNickname + "\n" + this.accountLogged.getUsername() + "\n" + oldNickname + "\n" + (new Timestamp(System.currentTimeMillis())).toString();
			List<InetAddress> listBroadAddress=this.listAllBroadcastAddresses();			
			for(int i=0; i<listBroadAddress.size();i++) {
				DatagramPacket outPacket = new DatagramPacket(message.getBytes(),message.length(),listAllBroadcastAddresses().get(i), InstanceTool.PortNumber.UDP_RCV_PORT.getValue());
				this.UDP_SEND_Socket.send(outPacket);
			}
			
		} catch (IOException e) {
			System.out.println("InternalSocket: Error dans sendNewPseudo");
			e.printStackTrace();
		}
		
	}
	public void sendMessage(Message msg, String receiver) {
		Address res = null;

		synchronized (connectedUserList) {
			for (Map.Entry<String,Address> entry : connectedUserList.entrySet()) {
				Address tmp = entry.getValue();
				 if(tmp.getUsername().equals(receiver)) {
					res= tmp;
				 }
			}
		}
		String message = InstanceTool.Ident_Code.Message.toString() + "\n" +  accountLogged.getUsername() + "\n" + receiver + "\n" + msg.getTimestamp().toString() + "\n" + msg.getMessage();
		//System.out.println("InternalSocket: Message envoyé : \n" + message + "\n InternalSocket: Fin du message");
		InetAddress addrRcv = res.getIP();
		try {
			Socket TCP_SEND_Socket = new Socket(addrRcv, InstanceTool.PortNumber.TCP_RCV_PORT.getValue());
			PrintWriter out = new PrintWriter(TCP_SEND_Socket.getOutputStream(),true);
			out.println(message);
			TCP_SEND_Socket.close();
		} catch (IOException e) {
			System.out.println("InternalSocket: Error Send message création Socket");
			e.printStackTrace();
		}
		
	}
	//https://www.baeldung.com/java-broadcast-multicast pour le broadcast
	public List<InetAddress> listAllBroadcastAddresses() throws SocketException {
	    List<InetAddress> broadcastList = new ArrayList<>();
	    Enumeration<NetworkInterface> interfaces 
	      = NetworkInterface.getNetworkInterfaces();
	    while (interfaces.hasMoreElements()) {
	        NetworkInterface networkInterface = interfaces.nextElement();

	        if (networkInterface.isLoopback() || !networkInterface.isUp()) {
	            continue;
	        }

	        networkInterface.getInterfaceAddresses().stream() 
	          .map(a -> a.getBroadcast())
	          .filter(Objects::nonNull)
	          .forEach(broadcastList::add);
	    }
	    return broadcastList;
	}

	public ConcurrentHashMap<String,Address> getUserList() {

		return this.connectedUserList;
	}
	/*public void updateUserList() {
		synchronized(this.listConnectedUsers) {
			this.connectedUserList.put(Username,new Address(clientAddress,Pseudo,Username ));
		}
	}*/
	public void startReceiverThread() {
		System.out.println("InternalSocket: starting RECEIVER UDP AND TCP THREADS . . .");
		//this.TCP_RCV_Thread = new ThreadReceiverTCP(this.db, accountLogged.getUsername(),this.connectedUserList, this.ui);
		this.UDP_RCV_Thread = new ThreadReceiverUDP(this.connectedUserList, this.db, this.UDP_SEND_Socket, accountLogged, this.ui);
		this.TCP_RCV_Thread = new ThreadReceiverTCP(this.db, accountLogged.getUsername(),this.connectedUserList, this.ui);
	}
	

	public synchronized void  setUserList(ConcurrentHashMap<String,Address> ul) {
		
		this.connectedUserList = ul;
	}


}
	




	

