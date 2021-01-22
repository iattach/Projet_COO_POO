/*package Controller;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;

import Model.Account;
import Model.Address;
import Model.Message;

import java.net.InetAddress;
import java.net.UnknownHostException;
*/
/*ACCES VIA TERMINAL : mysql -h srv-bdens.insa-toulouse.fr -D tp_servlet_010 -u tp_servlet_010 -p
 *then nickname : aep5Ohba
 */
/*
public class DBCentral {
	private static String login = "tp_servlet_010";
	private static String password = "aep5Ohba";
	private static String url = "jdbc:mysql://srv-bdens.insa-toulouse.fr:3306/" + login;
	protected static Connection dbc = connectionDBCentral();
	private static DBRequestCreate drc = new DBRequestCreate(dbc);
	final static protected DBLocal dbl = new DBLocal();
	private String usernameLogged;
	public static boolean finPullDBC = false;
	private static Timestamp ts = new Timestamp(0L);

	public DBCentral(String usernameLogged) {
		this.usernameLogged = usernameLogged;
		drc.create();
	}

	public static Connection connectionDBCentral() {
		Connection co = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			co = DriverManager.getConnection(url, login, password);
			System.out.println("DBCentral : Database opened successfully");
		} catch (Exception e) {
			System.out.println("DBCentral : Error of connexion : " + e.getMessage());
			e.printStackTrace();
		}
		return co;
	}

	public void close() {
		try {
			DBCentral.dbc.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}


	//In the beginning of the application to get all the tables. 

	protected static void initPullAccount() {
		drc.createTableAccount(); 
		String sql = "SELECT * FROM account;";
		try {
			PreparedStatement ps = DBCentral.dbc.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dbl.setAccount(
						new Account(rs.getString("username"), rs.getString("password"), rs.getString("nickname"), null));
			}
			rs.close();
			ps.close();
			// DBCentral.dbc.close();
		} catch (SQLException e) {
			System.out.println("DBCentral: Error InitPullAccount");
			e.printStackTrace();
		}

	}
	
	protected void pullDBC() {
		// récupère la infos de la db central et les ajoutes dans la db locale vide.
		// Appelé apres connection de l'user dadns le constructeur.
		try {
			ts = new Timestamp(System.currentTimeMillis());
			// DBCentral.dbc = connectionDBCentral();
			Statement stmt = DBCentral.dbc.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM knownUsers where usernameLogged = '" + this.usernameLogged + "';");
			while (rs.next()) {
				dbl.setKnownUser(
						new Address(InetAddress.getByAddress(rs.getBytes("address")), rs.getString("nickname"),
								rs.getString("username")),
						usernameLogged, Timestamp.valueOf(rs.getString("timestamp")));
			}
			stmt.close();
			rs.close();

			String sql = "SELECT * FROM conversations WHERE (sender = ?) OR (receiver = ?) ;";
			PreparedStatement pstmt = DBCentral.dbc.prepareStatement(sql);
			pstmt.setString(1, this.usernameLogged);
			pstmt.setString(2, this.usernameLogged);
			ResultSet rs2 = pstmt.executeQuery();
			while (rs2.next()) {
				String sender = rs2.getString("sender");
				if (this.usernameLogged.equals(sender)) {
					dbl.setMessage(
							new Message(true, rs2.getString("message"), Timestamp.valueOf(rs2.getString("timestamp"))),
							sender, rs2.getString("receiver"));
				} else {
					dbl.setMessage(
							new Message(false, rs2.getString("message"), Timestamp.valueOf(rs2.getString("timestamp"))),
							sender, rs2.getString("receiver"));
				}

			}

			rs2.close();
			pstmt.close();
			DBCentral.finPullDBC = true;

		} catch (SQLException | UnknownHostException e) {
			System.out.println("DBCentral: Error PullDB");
			e.printStackTrace();
		}

	}
	
	protected void pushToDBC() {
		// récupère les infos de la db locale qui sont nouvelles depuis PullDBC() et les
		// ajoutes dans la db central. Appelée à la fermeture de l'app.
		// DBCentral.dbc = connectionDBCentral();

		try {
			ResultSet rs = dbl.getRSSpecificAccount(this.usernameLogged);
			rs.next();
			String nickname = rs.getString("nickname");
			String sql = "INSERT INTO account (username,password,nickname) VALUES (?,?,?) ON DUPLICATE KEY UPDATE nickname='"
					+ nickname + "';";
			PreparedStatement pstmt = DBCentral.dbc.prepareStatement(sql);
			pstmt.setString(1, this.usernameLogged);
			pstmt.setString(2, rs.getString("password"));
			pstmt.setString(3, nickname);
			pstmt.executeUpdate();
			pstmt.close();
			rs.close();

			rs = dbl.getRSAllKnownUsersAboveTS(this.usernameLogged, ts);
			while (rs.next()) {

				nickname = rs.getString("nickname");
				sql = "INSERT INTO knownUsers (username,nickname,address,usernameLogged,timestamp) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE nickname='"
						+ nickname + "';";
				pstmt = DBCentral.dbc.prepareStatement(sql);
				pstmt.setString(1, rs.getString("username"));
				pstmt.setString(2, nickname);
				pstmt.setBytes(3, rs.getBytes("address"));
				pstmt.setString(4, this.usernameLogged);
				pstmt.setString(5, rs.getTimestamp("timestamp").toString());
				// pstmt.setTimestamp(5,rs.getTimestamp("timestamp"));
				pstmt.executeUpdate();
				pstmt.close();
			}
			rs.close();

			rs = dbl.getRSAllMessageAboveTS(ts);
			while (rs.next()) {
				sql = "INSERT INTO conversations (sender,receiver, timestamp, message) VALUES (?,?,?,?)";
				pstmt = DBCentral.dbc.prepareStatement(sql);
				pstmt.setString(1, rs.getString("sender"));
				pstmt.setString(2, rs.getString("receiver"));
				pstmt.setString(3, rs.getTimestamp("timestamp").toString());
				// pstmt.setTimestamp(3, rs.getTimestamp("timestamp"));
				pstmt.setString(4, rs.getString("message"));
				pstmt.executeUpdate();
				pstmt.close();
			}
			rs.close();

		} catch (SQLException e) {
			System.out.println("DBCentral: Error PushToDBC");
			e.printStackTrace();
		}

	}

	protected void changeNickname(String Username, String newNickname) {

		String sql = "UPDATE account SET nickname = '" + newNickname + "' where username='" + Username + "';";
		try {
			Statement stmt = dbc.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();

		} catch (SQLException e) {
			System.out.println("DBLocal: Error changeNickname, create statement or execute");
			e.printStackTrace();
		}
		sql = "UPDATE knownUsers SET nickname = '" + newNickname + "' where username='" + Username + "';";
		try {
			Statement stmt = dbc.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error changeNickname, create statement or execute");
			e.printStackTrace();
		}

	}

	// return false when nickname is not available
	protected static boolean checkNickname(String nickname) {
		String sql = "SELECT * FROM account WHERE nickname = '" + nickname + "';";
		boolean res = false;
		try {
			Statement stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				res = false;
			} else {
				res = true;
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("DBLocal: Error checkNickname, create statement or execute");
			e.printStackTrace();
		}
		return res;
	}

	// return false when username is not available
	protected static boolean checkUsername(String username) {
		String sql = "SELECT * FROM account WHERE username = '" + username + "';";
		boolean res = false;
		try {
			Statement stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				res = false;
			} else {
				res = true;
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			System.out.println("DBLocal: Error checkUsername, create statement or execute");
			e.printStackTrace();
		}
		return res;
	}

	protected static void addAccount(Account acc) {
		String sql = "INSERT INTO account (username,password,nickname) VALUES (?,?,?)";
		try {
			PreparedStatement pstmt = dbc.prepareStatement(sql);
			pstmt.setString(1, acc.getUsername());
			pstmt.setString(2, acc.getPassword());
			pstmt.setString(3, acc.getNickname());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("DBLocal: Error addAccount");
			e.printStackTrace();
		}
	}

}*/