package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Timestamp;

public class ClientTCP {
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket sock = new Socket("127.0.0.1", 6666);
		PrintWriter out = new PrintWriter(sock.getOutputStream(),true);
	    out.print("Message\nAlice Username\nBob Username\n"+ (new Timestamp(System.currentTimeMillis())).toString() + "\nMessage IMPORTANTISSIME:\nHello World!");
	    out.close();
	    sock.close();
	}
}
