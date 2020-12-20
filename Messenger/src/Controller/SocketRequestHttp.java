package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.util.concurrent.ConcurrentHashMap;

import Model.Account;
import Model.Address;

public class SocketRequestHttp {
	protected static final String PresenceServer = "https://srv-gei-tomcat.insa-toulouse.fr/Messenger/PresenceServer";
	ConcurrentHashMap<String,Address> connectedUserList = new ConcurrentHashMap<String,Address>(); // Need to be synchronized
	
	protected final Account ;
	protected static final int MAX_CHAR = 300;
	
	ThreadReceiverUDP UDP_RCV_Thread;
	ThreadReceiverTCP TCP_RCV_Thread;
	protected DBLocal db;
	protected UserInterface UI;
	protected static Long ts;





}
