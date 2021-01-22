package Model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;

public class Address {
	private InetAddress ip; //get the destination address
	private String nickname;
	private String username;
	private Timestamp time;
	
	public Address(InetAddress ip, String nickname, String username) {
		
		this.ip = ip;
		this.nickname = nickname;
		this.username = username;
		setTime(new Timestamp(System.currentTimeMillis()));
	}
	
	public Address(String nickname, String userTest) {
		try {
			this.ip = InetAddress.getByAddress(InstanceTool.getPcIP());
		} catch (UnknownHostException e) {
			this.ip = null;
		}
		setNickname(nickname);
		setUsername(userTest);
		setTime(new Timestamp(System.currentTimeMillis()));
	}
	 


	public Timestamp getTime() {
		return this.time;
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
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
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
	
	public void setIP(InetAddress iP) {
		this.ip = iP;
	}

	public InetAddress getIP() {
		return this.ip;
	}

	
}
