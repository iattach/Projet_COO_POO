package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

public class Address {
	private InetAddress ip; //get the destination address
	private String nikename;
	private String username;
	private Timestamp time;
	
	public Address(InetAddress ip, String nikename, String username) {
		
		this.ip = ip;
		this.nikename = nikename;
		this.username = username;
	}
	
	public Address(String nickname, String userTest) {
		try {
			this.ip = InetAddress.getByAddress(InstanceTool.getPcIP());
		} catch (UnknownHostException e) {
			this.ip = null;
		}
		setNikename(nickname);
		setUsername(userTest);
		setTime(new Timestamp(System.currentTimeMillis()));
	}
	 


	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public InetAddress getIp() {
		return ip;
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}
	public String getNikename() {
		return nikename;
	}
	public void setNikename(String nikename) {
		this.nikename = nikename;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String addrToString() {
		return(((int)this.ip.getAddress()[0])& 0xff) + "." + (((int)this.ip.getAddress()[1])& 0xff) + "." + (((int)this.ip.getAddress()[2])& 0xff) + "." + (((int)this.ip.getAddress()[3])& 0xff);
	}
	
	
	
	
}
