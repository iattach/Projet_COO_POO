package Model;

import java.sql.*;

public class Request1 extends Request {

	/**
	 * 
	 */
	public Request1() {

		super();
	}

	/**
	 * @param categorie
	 * @param datedebut
	 * @param datefin
	 * @throws Exception
	 */
	public void requete_un(String categorie, String datedebut, String datefin) throws Exception {
		// String readTimeout = "1000";
		// System.setProperty("oracle.jdbc.ReadTimeout", readTimeout);
		String url = "jdbc:oracle:thin:@charlemagne.iutnc.univ-lorraine.fr:1521:infodb";
		Connection con1;
							Statement state;
							
		String sql = ("SELECT no_imm, modele FROM vehicule WHERE   code_categ='" + categorie
				+ "' AND no_imm NOT IN (SELECT  no_imm FROM calendrier WHERE datejour>=to_date('" + datedebut
				+ "','DD-MM-YYYY') AND datejour <=to_date('" + datefin + "','DD-MM-YYYY') AND paslibre IS NOT NULL)");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (java.lang.ClassNotFoundException e) {
			super.resultat = e.getMessage();
		}
		try {
			con1 = DriverManager.getConnection(url, super.user, super.mdp);
			con1.setAutoCommit(false);
			state = con1.createStatement();
			ResultSet rs = state.executeQuery(sql);
			while (rs.next()) {
				String no_immat = rs.getString("no_imm");
				String modele = rs.getString("modele");
				super.resultat = super.resultat + "immatriculation :" + no_immat + "  ";
				super.resultat = super.resultat + "modele :" + modele + "\n";
			}
			con1.commit();
			state.close();
			con1.close();
		} catch (SQLException e) {
			super.resultat = super.resultat + e.getMessage();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see modele.Requete#getResultat()
	 */
	@Override
	public String getResultat() {

		super.resultat = "";
		try {
			this.requete_un(super.attribut, super.attribut1, super.attribut2);
		} catch (Exception e) {
			super.resultat = super.resultat + "connection time out\n" + e.getMessage();
		}

		return super.resultat;
	}

}
