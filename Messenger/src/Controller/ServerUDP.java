package Controller;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class ServerUDP {

    public static void main(String[] args) throws IOException, SocketException{
        System.out.println("ServerUDP: Starting");
        int port = 1025;
        DatagramSocket dgramSocket= new DatagramSocket(port);
        System.out.println("ServerUDP: Server Created on port " + port );
        while(true){
            byte[] buffer = new byte[256];
            DatagramPacket inPacket= new DatagramPacket(buffer, buffer.length);
            dgramSocket.receive(inPacket);
            InetAddress clientAddress= inPacket.getAddress();
            System.out.println("ServerUDP: temp :" + clientAddress.toString());
            int clientPort= inPacket.getPort();
            System.out.println("ServerUDP: temp : " + clientPort);
            String message = new String(inPacket.getData(), 0, inPacket.getLength());
            System.out.println("ServerUDP: Message received \"" + message + "\"");
            
        }
        
    }
}
