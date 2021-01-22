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
	boolean end = false;
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
			/*Integer.parseInt(onlineUser.getUsername())*/
			this.receiver = new DatagramSocket(InstanceTool.PortNumber.UDP_RCV_PORT.getValue());
		} catch (SocketException e) {
			System.out.println("ThreadReceiverUDP: Error creation socket");
			e.printStackTrace();
		}
		this.start();
		
	}
	
	public void setStop() {
		this.end = true;
	}
	
	//UDP returns a message which contains the current user logged , like  ACK for SYN, so the other uses know that this user is already connected
	private void sendSYNACKConnected(InetAddress addr, String nickname, String username) {
		
		String message = InstanceTool.Ident_Code.Con_SYN_Ack+ "\n" + nickname + "\n" + username + "\n" + (new Timestamp(System.currentTimeMillis())).toString();
		//System.out.println("ThreadReceiverUDP: sendACKConnected " + message + "\n\n" + addr.getAddress()[0] + + addr.getAddress()[1] + + addr.getAddress()[2] + addr.getAddress()[3]);
		try {
			DatagramPacket outPacket = new DatagramPacket(message.getBytes(),message.length(),addr, InstanceTool.PortNumber.UDP_RCV_PORT.getValue());
			this.sender.send(outPacket);
		} catch ( IOException e) {
			System.out.println("ThreadReceiverUDP: Error in sendSYNACKConnected");
			e.printStackTrace();
		}
	}
	
	private void sendACKConnected(InetAddress addr, String nickname, String username) {
		
		String message = InstanceTool.Ident_Code.Con_Ack+"\n"+addr.getHostAddress()+ "\n"  +addr.getHostName()+ "\n" + nickname + "\n" + username + "\n" + (new Timestamp(System.currentTimeMillis())).toString();
		//System.out.println("ThreadReceiverUDP: sendACKConnected " + message + "\n\n" + addr.getAddress()[0] + + addr.getAddress()[1] + + addr.getAddress()[2] + addr.getAddress()[3]);
		try {
			DatagramPacket outPacket = new DatagramPacket(message.getBytes(),message.length(),addr, InstanceTool.PortNumber.UDP_RCV_PORT.getValue());
			this.sender.send(outPacket);
		} catch ( IOException e) {
			System.out.println("ThreadReceiverUDP: Error in sendACKConnected");
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		System.out.println("ThreadReceiverUDP: running . . .");
		try {
			receiver.setSoTimeout(1000);
		} catch (SocketException e1) {
			System.out.println("ThreadReceiverUDP: Error setSoTimeout");
			e1.printStackTrace();
		}
		while(!end) {
			byte[] buffer = new byte[SocketInternalNetwork.MAX_CHAR];
			DatagramPacket inPacket = new DatagramPacket(buffer,buffer.length);
			try {
				
				receiver.receive(inPacket);
				if (inPacket != null) {
					System.out.println("ThreadReceiverUDP: Socket received");
					//System.out.println("ThreadReceiverUDP: msg recu");
					InetAddress clientAddress = inPacket.getAddress();
					String message = new String (inPacket.getData(), 0, inPacket.getLength());
					BufferedReader reader = new BufferedReader(new StringReader(message));
					String line = reader.readLine();
					//System.out.println("ThreadReceiverUDP: line -> "+line);
					if (line.contains(InstanceTool.Ident_Code.Connected.toString())) {
						System.out.println("ThreadReceiverUDP: Socket received -> Type : Connected");
						System.out.println("ThreadReceiverUDP: Connected received: " + message +" from " + clientAddress.getHostAddress());
						String nickname = reader.readLine();
						String username = reader.readLine();
						if (!username.equals(onlineUser.getUsername())&&!this.listConnectedUsers.containsKey(username)) {
							synchronized(this.listConnectedUsers) {
								
								this.listConnectedUsers.put(username,new Address(clientAddress,nickname,username ));
								
								this.sendSYNACKConnected(clientAddress,onlineUser.getNickname(), onlineUser.getUsername());
							}
						}
	
					}else if (line.contains(InstanceTool.Ident_Code.Exit.toString())) {
						System.out.println("ThreadReceiverUDP: Exit received: " + message);
						synchronized(this.listConnectedUsers) {
							
							String username = reader.readLine();
							this.listConnectedUsers.remove(username);
							
							
						}
						
						
						
					}else if (line.contains(InstanceTool.Ident_Code.New_Name.toString())){
						System.out.println("ThreadReceiverUDP: New_Name received: " + message);
						
						synchronized(this.listConnectedUsers) {
							String newNickname = reader.readLine();
							String username = reader.readLine();
							reader.readLine();
							if (!username.equals(onlineUser.getUsername())&&!this.listConnectedUsers.containsKey(username)) {
								this.listConnectedUsers.put(username,new Address(InetAddress.getByAddress(clientAddress.getAddress()),newNickname, username));
								
								this.db.updateNickName(newNickname, username);
								
								
							}
						}
						ui.updateUsers(this.listConnectedUsers);
						
					}else if(line.contains(InstanceTool.Ident_Code.Con_SYN_Ack.toString())) {
						System.out.println("ThreadReceiverUDP: Connected_SYN_ACK received: " + message);
						synchronized(this.listConnectedUsers) {
							String nickname = reader.readLine();
							String username = reader.readLine();
							if (!username.equals(onlineUser.getUsername())&&!this.listConnectedUsers.containsKey(username)) {
							
								this.listConnectedUsers.put(username,new Address(clientAddress,nickname,username ));
								
								this.sendACKConnected(clientAddress,nickname,username);
								
								
							}
						}
						
					}else if(line.contains(InstanceTool.Ident_Code.Con_Ack.toString())) {
						System.out.println("ThreadReceiverUDP: Connected_ACK received: " + message);
						synchronized(this.listConnectedUsers) {
							String addr = reader.readLine();
							String nickname = reader.readLine();
							String username = reader.readLine();
				
							if (!username.equals(onlineUser.getUsername())&&!this.listConnectedUsers.containsKey(username)) {
								this.listConnectedUsers.replace(username,new Address(InetAddress.getByName(addr),nickname,username ));
								
								
							}
						}
						
					}
					
					else {
						System.out.println("ThreadReceiverUDP: Unknown message received: " + message);
					}
					
				}
				
				ui.updateUsers(this.listConnectedUsers);	
				
			}catch(SocketTimeoutException e) {
				//System.out.println("ThreadReceiverUDP: No socket received ...");
				
			}catch (IOException e) {
				System.out.println("ThreadReceiverUDP: Error IOException thread");
				e.printStackTrace();
			}catch (Exception e) {
				
			}
			
		}
		this.receiver.close();
		System.out.println("ThreadReceiverUDP: Closing . . .");
		
		
	}
}