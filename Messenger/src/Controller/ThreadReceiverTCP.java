package Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;

import Model.Address;
import Model.InstanceTool;

public class ThreadReceiverTCP extends Thread {
	ServerSocket receiver;
	DBLocal db;
	ConcurrentHashMap<String,Address> coUsers;
	int n;
	String UsernameLogged;
	UserInterface UI;
	boolean termine = false;
	public ThreadReceiverTCP(DBLocal db_, String _UsernameLogged, ConcurrentHashMap<String,Address> _coUsers, UserInterface _UI) {
		super();
		this.coUsers = _coUsers;
		this.UsernameLogged = _UsernameLogged;
		this.db = db_;
		this.UI = _UI;
		try {
			receiver = new ServerSocket(InstanceTool.PortNumber.TCP_RCV_PORT.getValue());
		} catch (IOException e) {
			System.out.println("ThreadReceiverTCP: Error constructor ServSocket");
			e.printStackTrace();
		}
		this.n = 0;
		System.out.println("TCPThreadreceiver: Creation ServSocket");
		this.start();
		
	}
	
	
	public void setStop() {
		this.termine = true;
	}
	
	public void run() {
		try {
			receiver.setSoTimeout(1000);
		} catch (SocketException e1) {
			System.out.println("ThreadReceiverTCP: Error setTO");
			e1.printStackTrace();
		}
		while(!termine) {
			
				
				
				Socket clientSocket;
				try {
					clientSocket = receiver.accept();
					if (clientSocket != null) {
						n++;
						//System.out.println("ThreadReceiverTCP: Creation Socket fils en cours . . .");
						//new ThreadSocketFils(clientSocket, n, db,UsernameLogged, this.coUsers, this.UI);
					}
				} catch(SocketTimeoutException a) {
					
				}catch (IOException e) {
					// TODO Auto-generated catch block
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
	
}

