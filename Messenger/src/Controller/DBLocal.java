package Controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;

import Model.Account;
import Model.Address;
import Model.Conversation;
import Model.Message;


public class DBLocal {
	//https://www.tutorialspoint.com/sqlite/sqlite_java.htm
	//sqlite java
	
	private static final String PATH =  "./";
	final static protected Connection DB = connectionDB("DBMessenger.db");
	DBRequestCreate drc = new DBRequestCreate(DB);
	
	
	public DBLocal() {
		
		DBRequestCreate drc=new DBRequestCreate(DB);
		drc.create();
	}
	public  DBLocal(String nomDB) {
		
		DBRequestCreate drc=new DBRequestCreate(DB);
		drc.create();
		this.getSpecificKnownUser("users", "users");
	}
	
	private static synchronized Connection connectionDB(String nomDB) {
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + PATH +nomDB);
			System.out.println("DBLocal: Database opened successfully");
		} catch (ClassNotFoundException e) {
			System.out.println("DBLocal: Error connectionDB, class not Found");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error connectionDB, connection failed to db");
			e.printStackTrace();
		}
		return c;
	}
	
	protected synchronized Address getSpecificKnownUser(String usernameLogged, String userToSearch) {
		Address res = null;
		try {
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM knownUsers where usernameLogged = '" + usernameLogged + "' AND username = '" + userToSearch + "' ;");
			while (rs.next()) {
				res =  new Address(InetAddress.getByName(rs.getString("address")), rs.getString("nickname"), rs.getString("username"));
				System.out.println("DBlocal : usernickname found -> "+rs.getString("nickname"));
			}
			stmt.close();
			rs.close();
		}catch (SQLException e) {
			System.out.println("DBlocal: Error getSpecificKnownUser -> SQL ERROR");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("DBlocal: Error getSpecificKnownUser -> Unknown Host Error");
			e.printStackTrace();
		}
		return res;
	}
	protected synchronized Account getAccount(String username, String password) {
		System.out.println("BDLocal : account should be in research.");
		String sql = "SELECT * FROM account WHERE (username = ?)"; //AND (password = ?);"; //WHERE (username = ?) AND (password = ?) 
		ResultSet rs = null;
		String user;
		String display;
		String pw;
		String addr;
		Address temp;
		Account account = null;
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1,username);
			//pstmt.setString(2,password);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				System.out.println("BDLocal : account should be found.");
				user = rs.getString("username");
				display = rs.getString("nickname");
				pw = rs.getString("password");
				temp = new Address(null,display,user);
				account = new Account(user,pw,display,temp);	 
			}
			pstmt.close();
			rs.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error getAccount creation or execute query");
			e.printStackTrace();
		}
		return account;
		
	}
	protected synchronized void setAccount(Account acc){

		String sql = "INSERT INTO account (username,password,nickname) VALUES (?,?,?)";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, acc.getUsername());
			pstmt.setString(2, acc.getPassword());
			pstmt.setString(3, acc.getNickname());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error setAccount");
			e.printStackTrace();
		}
		
	}
	protected synchronized ArrayList<Address> getknownUsers(String usernameLogged){
		ArrayList<Address> temp = new ArrayList<Address>();
		Statement stmt;
		try {
			stmt = DB.createStatement();
			ResultSet rs;
			if (usernameLogged == null) {
				rs = stmt.executeQuery("SELECT * FROM knownUsers;");
			} //on récupère tout
			else{
				rs = stmt.executeQuery("SELECT * FROM knownUsers where usernameLogged = '" + usernameLogged +"';");
			}
			while(rs.next()) {
				String username = rs.getString("username");
				String nickname = rs.getString("nickname");
				String address = rs.getString("address");
				System.out.println(address);
				temp.add(new Address(InetAddress.getByName(address), nickname, username));
			}
			rs.close();
		    stmt.close();
		} catch (SQLException e) {
			System.out.println("DBlocal: Error getknownUsers, SQL ERROR");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("DBlocal: Error getknownUsers, Unknown Host Error");
			e.printStackTrace();
		}
		return temp;
		
	}
	
	
	private synchronized Address getUserAddress(String userLogged,String corres) throws IOException {
		Iterator<Address> iter = this.getknownUsers(userLogged).iterator();

		Address res = null;
		Boolean fin = false;
		while (iter.hasNext() && !fin) {
			res = iter.next();
			if (res.getUsername() == corres) {
				fin  =true;
			}
		}
		if(res != null) {
			return res;
		}else {
			throw new IOException("DBLocal: Error getUserAddress");
		}
	}
	
	protected synchronized Conversation getConversation(String userLogged, String corresp) {
		Conversation conv = null;
		try {
			conv = new Conversation(this.getUserAddress(userLogged,corresp));
		} catch (IOException e1) {
			System.out.println("DBLocal: Error getUserAddress");
			e1.printStackTrace();
		}
		try {
			String sql = "SELECT * FROM conversations WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) ORDER BY timestamp ASC;";
			PreparedStatement stmt =DB.prepareStatement(sql);
			stmt.setString(1, userLogged);
			stmt.setString(2, corresp);
			stmt.setString(3, corresp);
			stmt.setString(4, userLogged);
			
			ResultSet rs = stmt.executeQuery();
			if (rs.next() == false) {
				System.out.println("DBLocal: Error getConv return empty set");
			}else {
				 do {
					java.sql.Timestamp ts = rs.getTimestamp("timestamp");
					System.out.println(ts);
					String msg = rs.getString("message");
					if(rs.getString("sender").equals(userLogged)) {
						conv.addMessage(new Message(true,msg, ts));
					}else {
						conv.addMessage(new Message(false,msg, ts));
					}
					
				}while(rs.next());
				 
			}
			rs.close();
		    stmt.close();
			
		} catch (SQLException e) {
			System.out.println("DBlocal: Error getMessage");
			e.printStackTrace();
		}
		return conv;
	}
	
	// id corres isSender isNew ts msg 
	protected synchronized void setMessage(Message msg, String sender, String receiver) {
		try {
			String sql = "INSERT INTO conversations (sender,receiver, timestamp, message) VALUES (?,?,?,?)";
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, sender);
			pstmt.setString(2, receiver);
			pstmt.setTimestamp(3, msg.getTimestamp());
			pstmt.setString(4,msg.getMessage());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error setMessage SQL");
			e.printStackTrace();
		}
		
	}
	
	protected synchronized void setKnownUser(Address add, String usernameLogged) {
		String sql = "INSERT INTO knownUsers (username,nickname,address,usernameLogged,timestamp) VALUES (?,?,?,?,?)";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, add.getUsername());
			pstmt.setString(2, add.getNickname());
			pstmt.setString(3, add.getIP().getHostName());
			pstmt.setString(4,usernameLogged);
			pstmt.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error setKnownUser, creation pstmt or execute");
			e.printStackTrace();
		}
	}
	//return false when nickname is not available
	protected static boolean checkNickname(String nickname) {
		String sql = "SELECT * FROM account WHERE nickname = '" + nickname + "';";
		boolean res = true;
		try {
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				res = false;
			}
			
			rs.close();
			stmt.close();
			
			
			
		} catch (SQLException e) {
			System.out.println("DBLocale: Error checkPseudo, create statement or execute");
			e.printStackTrace();
		}
		return res;
	}
	//return false when username is not available
	protected static boolean checkUsername(String username) {
		String sql = "SELECT * FROM account WHERE username = '" + username + "';";
		boolean res = true;
		try {
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				res = false;
			}
			rs.close();
			stmt.close();
			
			
			
		} catch (SQLException e) {
			System.out.println("DBLocale: Error checkUsername, create statement or execute");
			e.printStackTrace();
		}
		return res;
	}
	//update nickname of the other user connected 
	protected synchronized void updateNickName(String newNickname, String username) {
		String sql = "UPDATE knownUsers SET nickname = '" + newNickname + "' where username='" + username + "';";
		try {
			Statement stmt = DB.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocale: Error createTableKnownUsers, create statement or execute");
			e.printStackTrace();
		}
	
	}
	/*
	protected synchronized void setKnownUser(Address add, String usernameLogged, Timestamp ts) {
		String sql = "INSERT INTO knownUsers (username,nickname,address,usernameLogged,timestamp) VALUES (?,?,?,?,?)";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, add.getUsername());
			pstmt.setString(2, add.getPseudo());
			pstmt.setBytes(3, add.getIP().getAddress());
			pstmt.setString(4,usernameLogged);
			pstmt.setTimestamp(5,ts);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error setKnownUser, creation pstmt or execute");
			e.printStackTrace();
		}
	}
	*/
	
	
	protected synchronized void updateNicknameAccount(String username, String new_nickname) {
		String sql = "UPDATE account SET nickname = '" + new_nickname + "' where username='" + username + "';";
		try {
			Statement stmt = DB.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error createTableKnownUsers, create statement or execute");
			e.printStackTrace();
		}
	}
	/*
	protected synchronized ArrayList<Address> getAllAccount(){
		ArrayList<Address> temp = new ArrayList<Address>();
		Statement stmt;
		try {
			stmt = DB.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM account;");
			while(rs.next()) {
				String username = rs.getString("username");
				String nickname = rs.getString("nickname");
				temp.add(new Address(InetAddress.getByAddress(Tools.getPcIP()), nickname, username));
			}
			rs.close();
		    stmt.close();
		} catch (SQLException e) {
			System.out.println("DBlocal: Error getAllAccount, SQL ERROR");
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("DBlocal: Error getAllAccount, Unknown Host Error");
			e.printStackTrace();
		}
		return temp;
		
	}
	

	//pour éviter l'erreur UnknownHostException (pour l'instant)//
	/*protected Account getAccount2(String username, String password) {
		String sql = "SELECT * FROM account WHERE (username = ?) AND (password = ?);"; //WHERE (username = ?) AND (password = ?) 
		ResultSet rs = null;
		String un;
		String ps;
		String pw;
		Address temp;
		Account tempA = null;
		try {
			PreparedStatement pstmt = this.DB.prepareStatement(sql);
			pstmt.setString(1,username);
			pstmt.setString(2,password);
			rs = pstmt.executeQuery();
			rs.next();
			if ( true) {
				System.out.println("YA");
				 un = rs.getString("username");
				 ps = rs.getString("nickname");
				 pw = rs.getString("password");
				 //temp = new Address(InetAddress.getByAddress(this.getPcIP()),ps,un);
				 //tempA = new Account(un,pw,ps,temp);
				 tempA = new Account(un,pw,ps,null);
			}
			
		} catch (SQLException | UnknownHostException e) {
			System.out.println("DBLocal: Error getAccount creation or execute query");
			e.printStackTrace();
		}
		return tempA;
		
	}*/
	/////
	
/*
	protected synchronized void setAccount(Account acc){

		String sql = "INSERT INTO account (username,password,nickname) VALUES (?,?,?)";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, acc.getUsername());
			pstmt.setString(2, acc.getPassword());
			pstmt.setString(3, acc.getPseudo());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error setAccount");
			e.printStackTrace();
		}
		
	}
	
	
	protected ResultSet getRSAllMessageAboveTS(Timestamp ts) {
		ResultSet rs = null;
		String sql = "SELECT * FROM conversations WHERE timestamp > ? ;";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setTimestamp(1, ts);
			rs = pstmt.executeQuery();
			
		} catch (SQLException e) {
			System.out.println("DBLocal: Error getRSAllMessageAboveTS, create statement or execute");
			e.printStackTrace();
		}
		return rs;
	}
	
	protected ResultSet getRSAllKnownUsersAboveTS(String Userlogged, Timestamp ts) {
		ResultSet rs = null;
		String sql = "SELECT * FROM knownUsers WHERE usernameLogged = ? AND timestamp > ?;";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, Userlogged);
			pstmt.setTimestamp(2, ts);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error getRSAllKnownUsersAboveTS, create statement or execute");
			e.printStackTrace();
		}
		return rs;
	}
	
	protected ResultSet getRSSpecificAccount(String Userlogged) {
		ResultSet rs = null;
		String sql = "SELECT * FROM account WHERE username = ?;";
		try {
			PreparedStatement pstmt = DB.prepareStatement(sql);
			pstmt.setString(1, Userlogged);
			rs = pstmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error getRSSpecificAccount, create statement or execute");
			e.printStackTrace();
		}
		return rs;
	}
	
	protected void printAllTable() {
		String sql;
		try {
			sql = "SELECT * FROM account;";
			Statement stmt = DB.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next() == false) {
				System.out.println("\nDBLocal: account is EMPTY");
			}else {
				System.out.println("\nDBLocal:Table account:\n");
				System.out.println("--------------------------------");
				System.out.println("| username | nickname | password |");
				System.out.println("--------------------------------\n");
				do {

					System.out.println("|" + rs.getString("username") + " | " + rs.getString("nickname") + " | " + rs.getString("password") + "|");
				}while(rs.next());
				 
			}
			rs.close();
		    stmt.close();
		    
		    sql = "SELECT * FROM conversations";
		    stmt = DB.createStatement();
		    rs = stmt.executeQuery(sql);
		    if (rs.next() == false) {
				System.out.println("\nDBLocal: conversations is EMPTY");
			}else {
				System.out.println("\nDBLocal:Table conversations:\n");
				System.out.println("-------------------------------------------");
				System.out.println("| sender | receiver | timestamp | message |");
				System.out.println("-------------------------------------------\n");
				do {
					System.out.println("|" + rs.getString("sender") + " | " + rs.getString("receiver") + " | "  + rs.getTimestamp("timestamp") + " | " + rs.getString("message") + " |");
				}while(rs.next());
				 
			}
			rs.close();
		    stmt.close();
		    
		    sql = "SELECT * FROM knownUsers";
		    stmt = DB.createStatement();
		    rs = stmt.executeQuery(sql);
		    if (rs.next() == false) {
				System.out.println("\nDBLocal: knownUsers is EMPTY");
			}else {
				System.out.println("\nDBLocal:Table knownUsers:\n");
				System.out.println("------------------------------------------------------------");
				System.out.println("| usernameLogged | username | nickname | address | timestamp |");
				System.out.println("------------------------------------------------------------\n");
				do {
					System.out.println("|" + rs.getString("usernameLogged") + " | " + rs.getString("username") + " | " + rs.getString("nickname") + " | " + rs.getBytes("address")[0] + "." + rs.getBytes("address")[1] + "." + rs.getBytes("address")[2] + "." + rs.getBytes("address")[3] + " | " + rs.getTimestamp("timestamp") + " |");
				}while(rs.next());
				 
			}
			rs.close();
		    stmt.close();
		    
			
		}catch (SQLException e) {
			System.out.println("DBLocal: Error getAccount creation or execute query");
			e.printStackTrace();
		}
		
	}
	*/
	protected void vanishDB() {
		String sql;
		Statement stmt;
		try {
			sql = "DROP TABLE account;";
			stmt = DB.createStatement();
			stmt.executeUpdate(sql);
			
			sql = "DROP TABLE conversations;";
			stmt = DB.createStatement();
			stmt.executeUpdate(sql);
			
			sql = "DROP TABLE knownUsers";
			stmt = DB.createStatement();
			stmt.executeUpdate(sql);
			
			drc.create();
			
			System.out.println("DBLocal: vanishDB successfully");
			//DB.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error vanishDB creation or execute query");
			e.printStackTrace();
		}
		
	}

}

