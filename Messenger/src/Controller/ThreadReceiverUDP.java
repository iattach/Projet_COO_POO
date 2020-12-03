package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.concurrent.ConcurrentHashMap;

import Model.Account;
import Model.Address;
import Model.InstanceTool;


public class ThreadReceiverUDP extends Thread {      
	
	private DatagramSocket receiver;
	private DatagramSocket sender;
	private ConcurrentHashMap<String,Address> listConnectedUsers;
	private DBLocal db;
	boolean termine = false;
	private Account onlineUser;
	private UserInterface ui;
	
	public ThreadReceiverUDP(ConcurrentHashMap<String,Address> listConnectedUsers, DBLocal db, DatagramSocket receiver, Account onlineUser,UserInterface ui) {
		super();
		this.sender = receiver;
		this.onlineUser = onlineUser;
		this.db = db;
		this.listConnectedUsers = listConnectedUsers;
		this.ui=ui;
		System.out.println("ThreadReceiver: starting . . .");
		try {
			/*InstanceTool.PortNumber.UDP_RCV_PORT.getValue()*/
			this.receiver = new DatagramSocket(Integer.parseInt(onlineUser.getUsername()));
		} catch (SocketException e) {
			System.out.println("ThreadReceiverUDP: Error creatino socket");
			e.printStackTrace();
		}
		this.start();
		
	}
	/*
	public void setStop() {
		this.termine = true;
	}
	
	private void sendSpecificConnected(InetAddress addr, String Pseudo, String Username) {
		
		String message = InstanceTool.Msg_Code.Con_Ack + "\n" + onlineUser.getPseudo() + "\n" + onlineUser.getUsername() + "\n" + (new Timestamp(System.currentTimeMillis())).toString();
		//System.out.println("ThreadReceiverUDP: sendSpecificConnected " + message + "\n\n" + addr.getAddress()[0] + + addr.getAddress()[1] + + addr.getAddress()[2] + addr.getAddress()[3]);
		try {
			DatagramPacket outPacket = new DatagramPacket(message.getBytes(),message.length(),addr, InstanceTool.PortNumber.UDP_RCV.getValue());
			this.sender.send(outPacket);
		} catch ( IOException e) {
			System.out.println("ThreadReceiverUDP: Error dans sendConnected");
			e.printStackTrace();
		}
	}
	*/
	@Override
	public void run(){
		System.out.println("ThreadReceiverUDP: running . . .");
		try {
			receiver.setSoTimeout(1000);
		} catch (SocketException e1) {
			System.out.println("ThreadReceiverUDP: Error setSoTimeout");
			e1.printStackTrace();
		}
		while(!termine) {
			byte[] buffer = new byte[SocketInternalNetwork.MAX_CHAR];
			DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
			try {
				
				receiver.receive(inPacket);
				if (inPacket != null) {
					//System.out.println("ThreadReceiverUDP: msg recu");
					InetAddress clientAddress = inPacket.getAddress();
					String message = new String (inPacket.getData(), 0, inPacket.getLength());
					BufferedReader reader = new BufferedReader(new StringReader(message));
					String line = reader.readLine();
					if (true/*line.contains(InstanceTool.Msg_Code.Connected.toString())*/) {
						//System.out.println("ThreadReceiverUDP: Connected received: " + message +" from " + clientAddress.getAddress()[0] + "." + clientAddress.getAddress()[1] + "." + clientAddress.getAddress()[2] + "." + clientAddress.getAddress()[3]);
						String Pseudo = reader.readLine();
						String Username = reader.readLine();
						if (!Username.equals(onlineUser.getUsername())) {
							synchronized(this.listConnectedUsers) {
								
								this.listConnectedUsers.put(Username,new Address(clientAddress,Pseudo,Username ));
								
								//this.sendSpecificConnected(clientAddress, Pseudo, Username);
							}
						}
						ui.update(clientAddress+" "+Pseudo+" "+Username+"\n");
	
					}
					/*else if (line.contains(InstanceTool.Msg_Code.Disconnected.toString())) {
						//System.out.println("ThreadReceiverUDP: Disconnected received: " + message);
						synchronized(this.listConnectedUsers) {
							String Username = reader.readLine();
							this.listConnectedUsers.remove(Username);
							
							
							
						}
						
						ui.update(s);
						//
					}else if (line.contains(InstanceTool.Msg_Code.New_Pseudo.toString())){
						//System.out.println("ThreadReceiverUDP: New_Pseudo received: " + message);
						
						synchronized(this.listConnectedUsers) {
							String new_pseudo = reader.readLine();
							String username = reader.readLine();
							reader.readLine();
							if (!username.equals(onlineUser.getUsername())) {
								this.listConnectedUsers.put(username,new Address(InetAddress.getByAddress(clientAddress.getAddress()),new_pseudo, username));
								
							//	this.db.updatePseudo(new_pseudo, username);
							}
							
						}
						ui.update(s);
					}else if(line.contains(InstanceTool.Msg_Code.Con_Ack.toString())) {
						//System.out.println("ThreadReceiverUDP: Connected_ACK received: " + message);
						synchronized(this.listConnectedUsers) {
							String Pseudo = reader.readLine();
							String Username = reader.readLine();
							this.listConnectedUsers.put(Username,new Address(clientAddress,Pseudo,Username ));
						}
						ui.update(s);
					}
					
					else {
						System.out.println("ThreadReceiverUDP: Unknown message received: " + message);
					}*/
				}
				
				
				
			}catch(SocketTimeoutException e) {
				System.out.println("ThreadReceiverUDP: No socket received ...");
				
			}catch (IOException e) {
				System.out.println("ThreadReceiverUDP: Error IOException thread");
				e.printStackTrace();
			}
		}
		this.receiver.close();
		System.out.println("ThreadReceiverUDP: Closing . . .");
		
		
	}
}