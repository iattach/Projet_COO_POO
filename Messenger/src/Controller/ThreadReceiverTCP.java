package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;


import Model.Address;
import Model.InstanceTool;
import Model.Message;

public class ThreadReceiverTCP extends Thread {
	private ServerSocket receiver;
	private DBLocal dbl;
	private ConcurrentHashMap<String,Address> listConnectedUsers;
	private int n;
	private String usernameLogged;
	private UserInterface ui;
	boolean end = false;
	
	public ThreadReceiverTCP(DBLocal dbl, String usernameLogged, ConcurrentHashMap<String,Address> listConnectedUsers, UserInterface ui) {
		super();
		this.listConnectedUsers = listConnectedUsers;
		this.usernameLogged = usernameLogged;
		this.dbl = dbl;
		this.ui = ui;
		try {
			receiver = new ServerSocket(InstanceTool.PortNumber.TCP_RCV_PORT.getValue());
		} catch (IOException e) {
			System.out.println("ThreadReceiverTCP: Error constructor ServSocket");
			e.printStackTrace();
		}
		this.n = 0;
		System.out.println("ThreadReceiverTCP: Creation ServSocket");
		this.start();
		
	}
	
	
	public void setStop() {
		this.end = true;
	}
	
	public void run() {
		try {
			receiver.setSoTimeout(1000);
		} catch (SocketException e) {
			System.out.println("ThreadReceiverTCP: Error setTO");
			e.printStackTrace();
		}
		while(!end) {
			
				
				
				Socket clientSocket;
				try {
					clientSocket = receiver.accept();
					if (clientSocket != null) {
						n++;
						//System.out.println("ThreadReceiverTCP: Creation Socket fils en cours . . .");
						new ThreadSocketFils(clientSocket, n, dbl,usernameLogged, this.listConnectedUsers, this.ui);
					}
				} catch(SocketTimeoutException a) {
					
				}catch (IOException e) {
					
					e.printStackTrace();
				}
				
				
			
		}
		try {
			this.receiver.close();
			System.out.println("ThreadReceiverTCP: Closing . . .");
		} catch (IOException e) {
			System.out.println("ThreadReceiverTCP: Error close");
			e.printStackTrace();
		}
		
	}
	
	
	class ThreadSocketFils extends Thread{
		Socket socket;
		//child process number
		int n; 
		DBLocal dbl;
		ConcurrentHashMap<String,Address> listConnectedUsers;
		String usernameLogged;
		UserInterface ui;
		ThreadSocketFils(Socket socket, int a, DBLocal dbl, String usernameLogged, ConcurrentHashMap<String,Address> listConnectedUsers, UserInterface ui){
			this.usernameLogged = usernameLogged;
			//this.dbl= db_;
			this.dbl = new DBLocal();
			this.listConnectedUsers = listConnectedUsers;
			this.socket = socket;
			n =a;
			this.ui = ui;
			//System.out.println("ThreadSocketFils" + n + ": creation ThreadSocketfils . . .");
			this.start();
		}
		
		@Override
		public void run(){
			try {
				//System.out.println("ThreadSocketFils" + n + ": Succesfully created");
				//read message
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String messageText = "";
				String sender = "";
				String receiver = "";
				Timestamp ts = null;
				String temp = in.readLine();
				while(temp !=null) {
					if(temp.equals(InstanceTool.Ident_Code.Message.toString())){	
						sender = in.readLine();
						receiver = in.readLine();
						ts = Timestamp.valueOf(in.readLine());
					}else {
						messageText += temp+ "\n";
					}
					temp = in.readLine();
				}
				//System.out.println("ThreadSocketFils" + n + ": msg received: " + sender + ";\n"+ receiver + ";\n"+ message);
				//System.out.println("ThreadSocketFils" + n +":" + usernameLogged +";");
				if( usernameLogged.equals(receiver)) {
					Address temporary = this.dbl.getSpecificKnownUser(usernameLogged, sender);
					if(temporary == null) {
						synchronized(this.listConnectedUsers) {
							if (this.listConnectedUsers.containsKey(sender)) {
								Address addr = this.listConnectedUsers.get(sender);
								addr = new Address(addr.getIP(),addr.getNickname(),addr.getUsername());
								this.dbl.setKnownUser(addr, usernameLogged);
							}
						}
						
					}
					Message message = new Message(false,messageText,ts);
					this.dbl.setMessage(message,sender,receiver);
					this.ui.updateMessage(message);
				}else {
					System.out.println("ThreadSocketChild " + n +" : The receiver can not be the sender himself");
				}
				
				//System.out.println("ThreadSocketFils" + n +": Closing . . .");
				socket.close();
				
			}catch(IOException e) {
					System.out.println("ThreadSocketChild " + n + " : Error accept");
					e.printStackTrace();
			}
		}
			
		}
}

