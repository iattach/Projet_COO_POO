package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Model.Message;
import Model.InstanceTool;
import Model.Account;
import Model.Address;



public class SocketInternalNetwork {
	protected static final String PresentServer = "https://srv-gei-tomcat.insa-toulouse.fr/EasyChat/Servlet";
	
	ConcurrentHashMap<String,Address> connectedUserList = new ConcurrentHashMap<String,Address>(); 
	
	protected final Account accountLogged;
	protected static final int MAX_CHAR = 300;
	
	DatagramSocket UDP_SEND_Socket;
	ThreadReceiverUDP UDP_RCV_Thread;
	ThreadReceiverTCP TCP_RCV_Thread;
	protected DBLocal db;
	protected UserInterface ui;
	
	ScheduledExecutorService executorService;
	
	private SocketRequestHttp srh;
	
	protected static Long ts;
	
	
	
	public SocketInternalNetwork(Account accountLogged, UserInterface ui){
		ts = 0L;//socket http
		this.accountLogged = accountLogged;
		this.connectedUserList = new ConcurrentHashMap<String,Address>();
		this.db = new DBLocal();
		this.ui = ui;
		try {
			
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
		this.srh=new SocketRequestHttp();
		this.srh.notifyConnexionServlet(userOnline);
		
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
	public void end() {
		this.sendDisconnected(accountLogged);
		this.srh=new SocketRequestHttp();
		this.srh.notifyDiscoServer(accountLogged);
		this.TCP_RCV_Thread.setStop();
		executorService.shutdownNow();
	}
	public void sendMessage(Message msg, String receiver) {
		Address res = null;

		synchronized (connectedUserList) {
			for (Map.Entry<String,Address> entry : connectedUserList.entrySet()) {
				Address tmp = entry.getValue();
				System.out.println("SocketInternalNetwork : Entry username->"+tmp.getUsername()+" nickname->"+tmp.getNickname()+" ip->"+tmp.getIp().getHostAddress());
				 if(tmp.getNickname().equals(receiver)) {
					res= tmp;
					System.out.println("SocketInternalNetwork : (Res) Entry username->"+res.getUsername()+" nickname->"+res.getNickname()+" ip->"+res.getIp().getHostAddress());
				 }
			}
		}
		String message = InstanceTool.Ident_Code.Message.toString() + "\n" +  accountLogged.getUsername() + "\n" + res.getUsername() + "\n" + msg.getTimestamp().toString() + "\n" + msg.getMessage();
		
		InetAddress addrRcv = res.getIP();
		try {
			Socket TCP_SEND_Socket = new Socket(addrRcv, InstanceTool.PortNumber.TCP_RCV_PORT.getValue());
			PrintWriter out = new PrintWriter(TCP_SEND_Socket.getOutputStream(),true);
			out.println(message);
			TCP_SEND_Socket.close();
		} catch (IOException e) {
			System.out.println("InternalSocket: Error Send message creation Socket");
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
	
	public void startReceiverThread() {
		System.out.println("InternalSocket: starting RECEIVER UDP AND TCP THREADS . . .");
		
		this.UDP_RCV_Thread = new ThreadReceiverUDP(this.connectedUserList, this.db, this.UDP_SEND_Socket, accountLogged, this.ui);
		this.TCP_RCV_Thread = new ThreadReceiverTCP(this.db, accountLogged.getUsername(),this.connectedUserList, this.ui);
		executorService = Executors.newSingleThreadScheduledExecutor();
	    executorService.scheduleWithFixedDelay(new SocketTaskHttp(this.connectedUserList , ts, accountLogged.getUsername(),ui), 0, 5, TimeUnit.SECONDS);
	    
	}
	

	public synchronized void  setUserList(ConcurrentHashMap<String,Address> ul) {
		
		this.connectedUserList = ul;
	}
	
	public void sendDisconnected(Account loggedAccount) {
		String message = InstanceTool.Ident_Code.Exit.toString() + "\n" + loggedAccount.getUsername() + "\n" + loggedAccount.getNickname()+ "\n" + (new Timestamp(System.currentTimeMillis())).toString();
		try {
			synchronized(this.UDP_SEND_Socket) {
				this.UDP_SEND_Socket.setBroadcast(true);
				List<InetAddress> listBroadAddress=this.listAllBroadcastAddresses();			
				for(int i=0; i<listBroadAddress.size();i++) {
					DatagramPacket outPacket = new DatagramPacket(message.getBytes(),message.length(),listAllBroadcastAddresses().get(i), InstanceTool.PortNumber.UDP_RCV_PORT.getValue());
					this.UDP_SEND_Socket.send(outPacket);
				}
			}
			this.UDP_SEND_Socket.close();
			this.UDP_RCV_Thread.setStop();
		} catch ( IOException e) {
			System.out.println("InternalSocket: Error in sendDisconnected");
			
			
		}
		
		

	}


}
	




	

