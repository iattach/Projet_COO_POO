package Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTCP {
	public static void main(String[] args) throws IOException {
	    ServerSocket servSocket = new ServerSocket(6666);
	    System.out.println(" Waiting for connection");
	    Socket link = servSocket.accept();
	    boolean fin =false;
	    BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
	    while(!fin){
	        System.out.println("NewLine:");
	        String time = in.readLine();
	        if(time != null){
	    
	            System.out.println(time);           
	        }
	        
	    }

    
    link.close();
    servSocket.close();
}
}
