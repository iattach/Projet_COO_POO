package Controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBRequestCreate {
	Connection DB;
	
	public DBRequestCreate(Connection db) {
		this.DB=db;
	}
	public void create() {
		this.createTableKnownUsers();
		this.createTableConversations();
		this.createTableAccount();
	}
	protected synchronized void createTableKnownUsers() {
		String sql = "CREATE TABLE IF NOT EXISTS knownUsers (\n"
                + "    usernameLogged VARCHAR(255) NOT NULL,\n"
                + "    username VARCHAR(255) NOT NULL,\n"
                + "    pseudo VARCHAR(255) NOT NULL,\n"
                + "    address VARCHAR(255) NOT NULL,\n"
                + "    timestamp TIMESTAMP(3) NOT NULL,\n"
                + "    PRIMARY KEY(usernameLogged,username)"
                + ");";
		try {
			Statement stmt = DB.createStatement();
			stmt.execute(sql);
			System.out.println("DBLocal: table created successfully");
			stmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error createTableKnownUsers, create statement or execute");
			e.printStackTrace();
		}
		
	
	}
	
	protected synchronized void  createTableConversations() {
		String sql = "CREATE TABLE IF NOT EXISTS conversations (\n"
                + "    sender VARCHAR(255) NOT NULL,\n"
                + "    receiver VARCHAR(255) NOT NULL,\n"
                + "    timestamp TIMESTAMP(3) NOT NULL,\n"
                + "    message VARCHAR(255) NOT NULL\n,"
                + "    PRIMARY KEY(sender,receiver,timestamp,message)"
                + ");";
		try {
			Statement stmt = DB.createStatement();
			stmt.execute(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error createTableKnownUsers, create statement or execute");
			e.printStackTrace();
		}
		
	
	}
	
	protected synchronized void  createTableAccount() {
		String sql = "CREATE TABLE IF NOT EXISTS account (\n"
                + "    username VARCHAR(255) PRIMARY KEY,\n"
                + "    password VARCHAR(255) ,\n"
                + "    pseudo VARCHAR(255) NOT NULL\n"
                + ");";
		try {
			Statement stmt = DB.createStatement();
			stmt.execute(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error createTableKnownUsers, create statement or execute");
			e.printStackTrace();
		}
	}
}
